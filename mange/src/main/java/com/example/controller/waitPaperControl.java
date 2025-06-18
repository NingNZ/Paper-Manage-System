package com.example.controller;

import com.example.instance.Paper;
import com.example.instance.paperMsg;
import com.example.instance.waitPaper;
import com.example.util.tool;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Map;

@RestController
public class waitPaperControl {
    @PostMapping("/wait/newPaper")
    public Map<String,Object> solveNewSysPaper(
            @RequestParam("title") String title,
            @RequestParam("authors") String authorsJson, // 接收 JSON 字符串
            @RequestParam("type") String typeId,
            @RequestParam("journal") String journalId,
            @RequestParam("date") String date,
            @RequestParam("teamId") String teamId,
            @RequestParam("file") MultipartFile file
    ) {
        System.out.println("enterIn");
        System.out.println(authorsJson);
        ObjectMapper mapper = new ObjectMapper();
        String[] authors;
        paperMsg existMsg;

        //判断是否重复提交
        try {
            existMsg = paperMsg.checkMsgExist(title,teamId);
            if(existMsg!=null && !existMsg.isNull()){
                if(existMsg.getState()==0){
                    return tool.msgCreate(300,"已经提交过该申请,等待管理员审核");
                }else{
                    waitPaper waitpaper = new waitPaper(existMsg.getWaitPaperId());
                    existMsg.dropSelfFromDB();
                    waitpaper.dropFromWaitArea();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return tool.msgCreate(400,"数据库处理错误");
        }

        //提交
        try {
            authors = mapper.readValue(authorsJson,String[].class);
            String id = waitPaper.insertToWaitArea(title, journalId, authors,typeId,teamId, date);
            waitPaper paper = null;
            try {
                paper = new waitPaper(id);
                if(paper.fileLocalSave(file)){
                    paperMsg.insertToDB(id,teamId,tool.getTimeYMD());
                    return tool.msgCreate(200,"upload success");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return tool.msgCreate(400,"数据库处理错误");
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return tool.msgCreate(400,"file upload fail");
    }

    @GetMapping("/wait/download")
    public ResponseEntity<Resource> downloadFile(@RequestParam String paperId) {

        System.out.println(paperId);
        waitPaper target = null;
        try {
            target = new waitPaper (paperId);
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(401)
                    .body(new ByteArrayResource("文件不存在".getBytes(StandardCharsets.UTF_8)));
        }
        // 2. 加载文件资源
        File file = target.getRelateFile();
        if(file==null||!file.exists()){
            return ResponseEntity
                    .status(401)
                    .body(new ByteArrayResource("文件不存在".getBytes(StandardCharsets.UTF_8)));
        }
        Path path = Paths.get(file.getAbsolutePath());
        Resource resource = null;
        try {
            resource = new UrlResource(path.toUri());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(401)
                    .body(new ByteArrayResource("文件资源不存在".getBytes(StandardCharsets.UTF_8)));
        }
        // 3. 设置响应头（支持中文文件名）
        String encodedFileName = URLEncoder.encode(target.getTitle(), StandardCharsets.UTF_8);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(MediaType.APPLICATION_PDF_VALUE)) // 动态类型
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + encodedFileName + "\""
                )
                .body(resource);
    }

}
