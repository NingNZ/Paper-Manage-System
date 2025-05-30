package com.example.instance;

import com.example.util.paperUtil;
import com.example.util.sqlUtil;
import com.example.util.tool;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class Paper {
    String id;
    String title;
    String journal;
    String authors;
    String type;
    String typeid;
    String team;
    String time;
    public Paper(String id) throws SQLException{
        String sql1 = "select sysPaper.id,title,time," +
                "sysjournal.name as journal,systype.id as typeid," +
                "systype.name as type,team.name as team " +
                "from syspaper,sysjournal,systype,team " +
                "where syspaper.id=? and " +
                "syspaper.teamId=team.id and " +
                "syspaper.typeId=systype.id and " +
                "syspaper.jourId=sysjournal.id;";
        ResultSet res = sqlUtil.query(sql1,id);
        if(res.isBeforeFirst()){
            res.next();
            this.id = res.getString("id");
            this.team = res.getString("team");
            this.typeid = res.getString("typeid");
            this.type = res.getString("type");
            this.time = res.getString("time");
            this.journal = res.getString("journal");
            this.title = res.getString("title");
            sqlGetAuthors();
        }
    }

    public String getTitle() {
        return title+".pdf";
    }
    public boolean isNull(){
        return this.id==null;
    }
    public static String insertToSysPaper(
            String title,
            String jourId,
            String[] authorsId,
            String typeid,
            String teamId,
            String time
    ){
        String id = "#pid"+ paperUtil.generateHash(title);
        System.out.println(id);
        String sql1 = "insert into syspaper (id,title,jourId,typeId,teamId,time) values (?,?,?,?,?,?)";
        try{
            int res = sqlUtil.update(sql1,id,title,jourId,typeid,teamId,time);
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }
        String sql2 = "insert into authors(paperId,userId) values(?,?)";
        for(String author:authorsId){
            try{
                sqlUtil.update(sql2,id,author);
            }catch (SQLException e){
                return null;
            }
        }
        return id;
    }
    private void sqlGetAuthors(){
        authors = "";
        if(!this.isNull()){
            String sql2 ="select users.name  from users,authors " +
                    "where authors.userId = users.id and authors.paperId= ? ;";
            try(ResultSet resAuthor = sqlUtil.query(sql2,this.id)){
                if(resAuthor!=null) {
                    resAuthor.next();
                    String tmp = resAuthor.getString("name");
                    this.authors = this.authors.concat(tmp);
                    while(resAuthor.next()){
                        tmp = resAuthor.getString("name");
                        this.authors = this.authors.concat(", "+tmp);
                    }
                }
            }catch (SQLException e1){
                e1.printStackTrace();
                this.authors = "";
            }
        }
    }
    public Map<String, Object> transferToMap(){
        Map<String, Object> row = new HashMap<>();
        row.put("id",id);
        row.put("title",title);
        row.put("authors",authors);
        row.put("time",time);
        row.put("journal",journal);
        row.put("type",type);
        row.put("typeid",typeid);
        return row;
    }
    public File getRelateFile(){
        if(this.isNull()) return null;
        String filepath = "D:\\code\\idea_java\\mange\\uploadSysFiles";
        return new File(filepath,this.id+".pdf");
    }
    public  boolean fileLocalSave(MultipartFile file){
        if (file.isEmpty()) {
            System.out.println("File is empty!");
            return false;
        }
        try {
            // 设置文件存储路径
            String uploadFolder = "D:\\code\\idea_java\\mange\\uploadSysFiles";
            File folder = new File(uploadFolder);
            if (!folder.exists()) {
                folder.mkdirs();
            }
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
    public boolean fileSqlDelete(){
        String sql1 = "delete from syspaper where id =?";
        String sql2 = "delete from authors where paperId =?";
        if(this.isNull()) return false;
        try{
            int res2 = sqlUtil.update(sql2,this.id);
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
        try{
            int res1 = sqlUtil.update(sql1,this.id);
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
        if(this.getRelateFile().exists()){
            this.getRelateFile().delete();
        }
        return  true;
    }

}

