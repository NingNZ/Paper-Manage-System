package com.example.controller;

import com.example.instance.Paper;
import com.example.util.paperUtil;
import com.example.util.tool;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class sysPaperControl {
    @PostMapping("/sysPaper/new")
    public Map<String,Object> solveNewSysPaper(
            @RequestParam("title") String title,
            @RequestParam("authors") String authorsJson, // 接收 JSON 字符串
            @RequestParam("type") String typeId,
            @RequestParam("journal") String journalId,
            @RequestParam("date") String date,
            @RequestParam("teamId") String teamId,
            @RequestParam("file") MultipartFile file
    ) {
        System.out.println("enter");
        System.out.println(authorsJson);
        ObjectMapper mapper = new ObjectMapper();
        String[] authors = new String[0];
        try {
            authors = mapper.readValue(authorsJson,String[].class);
            String id = Paper.insertToSysPaper(title, journalId, authors, typeId, teamId, date);
            Paper paper = null;
            try {
                if(id==null) return tool.msgCreate(400,"文件主键重复,上传失败");
                paper = new Paper(id);
                if(paper.fileLocalSave(file)){
                    return tool.msgCreate(200,"upload success");
                }
            } catch (SQLException e) {
                e.printStackTrace();

            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return tool.msgCreate(400,"file upload fail");
    }
    @GetMapping("/sysPaper/download")
    public ResponseEntity<Resource> downloadFile(@RequestParam String paperId) {

        System.out.println(paperId);
        Paper target = null;
        try {
            target = new Paper(paperId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        // 2. 加载文件资源
        File file = target.getRelateFile();
        Path path = Paths.get(file.getAbsolutePath());
        Resource resource = null;
        try {
            resource = new UrlResource(path.toUri());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
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
    @DeleteMapping("/sysPaper/delete")
    public Map<String,Object> deleteFile(@RequestParam String paperId){
        try {
            Paper paper = new Paper(paperId);
            if(paper.fileSqlDelete()){
                return tool.msgCreate(200,"delete success");
            }else{
                return tool.msgCreate(400,"删除错误,系统故障");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return tool.msgCreate(400,"删除错误,检查文件是否存在");
        }
    }
    @GetMapping("/sysPaper/update")
    public Map<String,Object> updateFile(@RequestParam String paperId,@RequestParam String typeId){
        try {
            Paper paper = new Paper(paperId);
            if(paper.isNull()){
                return tool.msgCreate(400,"文件不存在");
            }else;
            if(paper.fileSqlTypeUpdate(typeId)){
                return tool.msgCreate(200,"更新成功");
            }else{
                return tool.msgCreate(400,"更新失败");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return tool.msgCreate(400,"系统错误");
        }
    }
}
