package com.example.controller;

import com.example.instance.*;
import com.example.util.tool;
import jakarta.servlet.http.HttpSession;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;


@RestController
public class teamInfoPaperControl {
    @RequestMapping("/teamInfo/papers")
    public ArrayList<Map<String, Object>> getTeamRefPapers(@RequestParam String teamId, HttpSession session){
        ArrayList<Map<String,Object>> res = new ArrayList<>();
        Object userId = session.getAttribute("user");
        if(userId==null){
            res.add(tool.msgCreate(401,"你未登录"));
            return res;
        }
        Team team = new Team(teamId);
        if(team.isNull()){
            res.add(tool.msgCreate(400,"团队不存在"));
            return res;
        }
        if(!team.findMember(userId.toString())){
            res.add(tool.msgCreate(403,"你不是团队成员"));
            return res;
        }
        try {
            res.add(tool.msgCreate(200,"success"));
            refPaperList list = new refPaperList(teamId);
            res.addAll(list.export());
        } catch (SQLException e) {
            e.printStackTrace();
            res.add(tool.msgCreate(400,"处理失败"));
        }
        return res;
    }

    @PostMapping("/teamInfo/newPapers")
    public Map<String,Object> newTeamRefPapers(@RequestParam String title,
                                               @RequestParam String typeId,
                                               @RequestParam String teamId,
                                               @RequestParam MultipartFile file,
                                               HttpSession session){
        Object user = session.getAttribute("user");
        if(user==null) {
            return tool.msgCreate(400,"没有登录");
        }
        String userId = user.toString();
        Team team = new Team(teamId);
        if(team.isNull()){
            return tool.msgCreate(400,"团队不存在");
        }
        if(!team.findMember(userId)){
            return tool.msgCreate(400,"你不是团队成员");
        }
        refTypeTree tree = null;
        refPaperList paperList = null;
        try {
            tree = new refTypeTree(teamId);
            paperList = new refPaperList(teamId);
        } catch (SQLException e) {
            e.printStackTrace();
            return tool.msgCreate(400,"数据库构建错误");
        }
        if(!tree.checkTypeIdExists(typeId)){
            return tool.msgCreate(400,"不存在相应分类");
        }
        String paperId = "#p"+tool.generateHash(teamId+title,10);
        if(paperList.checkPaperExistById(paperId)){
            return  tool.msgCreate(400,"存在重复主键，建议重命名");
        }

        String time = tool.getTimeYMD();

        try {
            refPaper paper = refPaper.insetIntoDB(paperId,title,teamId,typeId,time,userId);
            if(!paper.fileLocalSave(file)){
                return tool.msgCreate(400,"文件保存失败");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return tool.msgCreate(400,"数据库添加错误");
        }
        return tool.msgCreate(200,"success");
    }

    @GetMapping("/teamInfo/deletePaper")

    public Map<String,Object> deleteTeamRefPaper(@RequestParam String teamId,
                                                 @RequestParam String paperId,
                                                 HttpSession session){
        Object user = session.getAttribute("user");
        if(user==null){
            return tool.msgCreate(400,"你未登录");
        }
        String uid = user.toString();
        Team team = new Team(teamId);
        if(team.isNull()){
            return tool.msgCreate(400,"团队不存在");
        }
        if(!team.getLeaderId().equals(uid)){
            return tool.msgCreate(400,"你不是团队组长");
        }
        try {
            if(refPaper.dropIntoDB(paperId,teamId)){
                return tool.msgCreate(200,"success");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return tool.msgCreate(400,"数据库操作失败");
        }
        return tool.msgCreate(400,"文件删除失败");
    }

    @GetMapping("/teamInfo/editPaper")

    public Map<String,Object> editTeamRefPaper(@RequestParam String teamId,
                                               @RequestParam String paperId,
                                               @RequestParam String typeId,
                                               HttpSession session){
        Object user = session.getAttribute("user");
        if(user==null){
            return tool.msgCreate(400,"你未登录");
        }
        String uid = user.toString();
        Team team = new Team(teamId);
        if(team.isNull()){
            return tool.msgCreate(400,"团队不存在");
        }
        if(!team.getLeaderId().equals(uid)){
            return tool.msgCreate(400,"你不是团队组长");
        }

        refTypeTree tree = null;
        try {
            tree = new refTypeTree(teamId);
        } catch (SQLException e) {
            e.printStackTrace();
            return tool.msgCreate(400,"数据库查询类别失败");
        }
        if(!tree.checkTypeIdExists(typeId)){
            return tool.msgCreate(400,"无效的类型");
        }
        try {
            refPaper paper = refPaper.getRefPaperByID(paperId,teamId);
            if(paper==null || !paper.isValid()){
                return tool.msgCreate(400,"该文件不存在");
            }
            if(!paper.getTypeId().equals(typeId)){
                paper.setTypeId(typeId);
                paper.updateOnDB();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return tool.msgCreate(400,"数据库操作失败");
        }
        return tool.msgCreate(200,"文件修改成功");
    }

    @GetMapping("/teamInfo/download")
    public ResponseEntity<Resource> downloadRefFile(@RequestParam String paperId,
                                                    @RequestParam String teamId,
                                                    HttpSession session) {
        System.out.println(paperId);
        Object user = session.getAttribute("user");
        if(user==null){
            return ResponseEntity
                    .status(401)
                    .body(new ByteArrayResource("你没有登录".getBytes(StandardCharsets.UTF_8)));
        }
        String userId = user.toString();
        Team team = new Team(teamId);
        if(team.isNull()){
            return ResponseEntity
                    .status(401)
                    .body(new ByteArrayResource("团队不存在".getBytes(StandardCharsets.UTF_8)));
        }
        if(!team.findMember(userId)){
            return ResponseEntity
                    .status(401)
                    .body(new ByteArrayResource("你不是团队成员".getBytes(StandardCharsets.UTF_8)));
        }
        refPaper paper = null;
        try {
            paper = refPaper.getRefPaperByID(paperId,teamId);
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(401)
                    .body(new ByteArrayResource("文件资源不存在".getBytes(StandardCharsets.UTF_8)));
        }
        if(paper==null || !paper.isValid()){
            return ResponseEntity
                    .status(401)
                    .body(new ByteArrayResource("文件资源不存在".getBytes(StandardCharsets.UTF_8)));
        }
        // 2. 加载文件资源
        File file = paper.getLocalFile();
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
        String encodedFileName = URLEncoder.encode(paper.getTitle(), StandardCharsets.UTF_8);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(MediaType.APPLICATION_PDF_VALUE)) // 动态类型
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + encodedFileName + "\""
                )
                .body(resource);
    }
}
