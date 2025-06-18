package com.example.instance;

import com.example.util.ResultSetWrapper;
import com.example.util.sqlUtil;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class refPaper {
    public String getId() {
        return id;
    }

    private String id;

    public String getTitle() {
        return title;
    }

    private String title;
    private String teamId;

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    private String typeId;

    private String typeName;

    private String time;

    private String userId;

    private String userName;

    private boolean valid;

    public refPaper(String id,String title,String teamId,String typeId,String time,String userId) throws SQLException {
        this.id = id;
        this.title = title;
        this.teamId = teamId;
        this.typeId = typeId;
        this.time = time;
        this.userId = userId;
        this.valid = true;
        String sql1 = "select name from teamRefType where id = ? and teamId = ?";
        ResultSetWrapper res = sqlUtil.query(sql1,typeId,teamId);
        if(res.isBeforeFirst()){
            res.next();
            this.typeName = res.getString("name");
        }else{
            this.valid = false;
        }

        String sql2 = "select name from users where id = ?";
        ResultSetWrapper res2 = sqlUtil.query(sql2,userId);
        if(res.isBeforeFirst()){
            res2.next();
            this.userName = res2.getString("name");
        }else{
            this.valid=false;
        }
    }
    public static refPaper getRefPaperByID(String id,String teamId) throws SQLException {
        String sql1 = "select title,teamId,typeId,time,userId from teamRefPaper where id = ? and teamId = ?";
        ResultSetWrapper res = sqlUtil.query(sql1,id,teamId);
        if(res.isBeforeFirst()){
            res.next();
            String title = res.getString("title");
            String typeId = res.getString("typeId");
            String time = res.getString("time");
            String userId = res.getString("userId");
            refPaper paper = new refPaper(id,title,teamId,typeId,time,userId);
            if(paper.isValid()) return paper;
        }
        return null;
    }

    public void updateOnDB() throws SQLException {
        String sql = "update teamRefPaper set typeId=? where id=? and teamId=?";
        sqlUtil.update(sql,typeId,id,teamId);
    }
    public static refPaper insetIntoDB(String id,String title,String teamId,String typeId,String time,String userId) throws SQLException {
        String sql = "insert into teamRefPaper (id,title,teamId,typeId,time,userId) values (?,?,?,?,?,?)";
        sqlUtil.update(sql,id,title,teamId,typeId,time,userId);
        return getRefPaperByID(id,teamId);
    }
    public static boolean dropIntoDB(String paperId,String teamId) throws SQLException {
        String sql = "delete from teamRefPaper where id = ? and teamId = ?";
        sqlUtil.update(sql,paperId,teamId);
        return fileLocalDelete(paperId,teamId);
    }
    public File getLocalFile(){
        String uploadFolder = "D:/code/idea_java/mange/teamRefPapers/"+teamId;
        File folder = new File(uploadFolder);
        // 获取文件名
        String fileName = id+".pdf";
        // 设置文件存储路径
        File targetFile = new File(folder, fileName);
        if(targetFile.exists()){
            return targetFile;
        }else{
            return null;
        }
    }
    public boolean fileLocalSave(MultipartFile file){
        if (file.isEmpty()) {
            System.out.println("File is empty!");
            return false;
        }
        try {
            // 设置文件存储路径
            String uploadFolder = "D:/code/idea_java/mange/teamRefPapers/"+teamId;
            File folder = new File(uploadFolder);
            // 获取文件名
            String fileName = id+".pdf";
            // 设置文件存储路径
            File targetFile = new File(folder, fileName);
            // 保存文件
            file.transferTo(targetFile);
            System.out.println("File uploaded successfully: " + targetFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("File upload failed: " + e.getMessage());
            return false;
        }
        return true;
    }

    public static boolean fileLocalDelete(String paperId,String teamId){
        String uploadFolder = "D:/code/idea_java/mange/teamRefPapers/"+teamId;
        File folder = new File(uploadFolder);
        // 获取文件名
        String fileName = paperId+".pdf";
        // 设置文件存储路径
        File targetFile = new File(folder, fileName);
        if(targetFile.exists()){
            return targetFile.delete();
        }
        return true;
    }
    public boolean isValid() {
        return valid;
    }

    public Map<String,Object> tranformat(){
        Map<String,Object> map = new HashMap<>();
        if(this.isValid()){
            map.put("id",this.id);
            map.put("title",this.title);
            map.put("userName",this.userName);
            map.put("typeId",this.typeId);
            map.put("typeName",this.typeName);
            map.put("time",this.time);
            return map;
        }
        return null;
    }

}
