package com.example.instance;

import com.example.util.ResultSetWrapper;
import com.example.util.sqlUtil;
import com.example.util.tool;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class waitPaper {
    String id;

    public String getTitle() {
        return title;
    }

    String title;

    String jourId;
    String journal;
    String authors;
    ArrayList<String> authorsIdList;
    String type;
    String typeId;
    String time;

    String teamId;
    String teamName;
    public waitPaper(String id) throws SQLException {
        String sql1 = "select waitPaper.id,title,time,jourId,typeId," +
                "waitPaper.teamId,team.name as teamName," +
                "sysjournal.name as journal," +
                "systype.name as type " +
                "from waitPaper,sysjournal,systype,team " +
                "where waitPaper.id=? and " +
                "waitPaper.typeId=systype.id and " +
                "waitPaper.jourId=sysjournal.id and " +
                "waitPaper.teamId=team.id;";
        ResultSetWrapper res = sqlUtil.query(sql1,id);
        if(!res.isBeforeFirst()){
            this.id = null;
        }else{
            res.next();
            this.id = res.getString("id");
            this.title = res.getString("title");
            this.time = res.getString("time");
            this.teamId = res.getString("teamId");
            this.teamName = res.getString("teamName");
            this.journal = res.getString("journal");
            this.type = res.getString("type");
            this.jourId = res.getString("jourId");
            this.typeId = res.getString("typeId");
            this.sqlGetAuthors();
        }

    }
    private void sqlGetAuthors(){
        authors = "";
        if(!this.isNull()){
            String sql2 ="select users.name,waitauthors.userId as id from users,waitauthors " +
                    "where waitauthors.userId = users.id and waitauthors.paperId= ? ;";
            try{
                this.authorsIdList = new ArrayList<>();
                ResultSetWrapper resAuthor = sqlUtil.query(sql2,this.id);
                resAuthor.next();
                String tmp = resAuthor.getString("name");
                String tmpId = resAuthor.getString("id");
                this.authors = this.authors.concat(tmp);
                this.authorsIdList.add(tmpId);
                while(resAuthor.next()){
                    tmp = resAuthor.getString("name");
                    tmpId = resAuthor.getString("id");
                    this.authors = this.authors.concat(", "+tmp);
                    this.authorsIdList.add(tmpId);
                }
            }catch (SQLException e1){
                e1.printStackTrace();
                this.authors = "";
            }
        }
    }

    private boolean isNull() {
        return id==null;
    }
    public HashMap<String, Object> transferToMap(){
        HashMap<String, Object> row = new HashMap<>();
        row.put("id",id);
        row.put("title",title);
        row.put("authors",authors);
        row.put("time",time);
        row.put("journal",journal);
        row.put("type",type);
        row.put("team",teamName);
        return row;
    }

    public static String insertToWaitArea(
            String title,
            String jourId,
            String[] authorsId,
            String typeId,
            String teamId,
            String time
    ){
        String id = "#p"+ tool.generateHash(title,10);
        System.out.println(id);
        String sql1 = "insert into waitpaper (id,title,jourId,typeId,time,teamId) values (?,?,?,?,?,?)";
        try{
            int res = sqlUtil.update(sql1,id,title,jourId,typeId,time,teamId);
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }
        String sql2 = "insert into waitauthors(paperId,userId,score) values(?,?,?)";
        for(String author:authorsId){
            try{
                sqlUtil.update(sql2,id,author,tool.calculateScore(authorsId.length));
            }catch (SQLException e){
                e.printStackTrace();
                return null;
            }
        }
        return id;
    }

    private String saveToSysPaper() throws SQLException {
        System.out.println("tend to sys:"+id);
        String sql1 = "insert into syspaper (id,title,jourId,typeId,time) values (?,?,?,?,?)";
        int res = sqlUtil.update(sql1,id,title,jourId,typeId,time);
        String sql2 = "insert into authors(paperId,userId,score) values(?,?,?)";
        for(String author:this.authorsIdList){
            sqlUtil.update(sql2,id,author,tool.calculateScore(authorsIdList.size()));
        }
        return id;
    }

    public void dropFromWaitArea() throws SQLException {
        String sql1 = "delete from waitAuthors where paperId = ?";
        sqlUtil.update(sql1,id);
        String sql2 = "delete from waitpaper where id = ?";
        sqlUtil.update(sql2,id);
        this.fileLocalDelete();
        return;
    }

    public File getRelateFile(){
        if(this.isNull()) return null;
        String filepath = "D:\\code\\idea_java\\mange\\waitPapers";
        return new File(filepath,this.id+".pdf");
    }
    public  boolean fileLocalSave(MultipartFile file){
        if (file.isEmpty()) {
            System.out.println("File is empty!");
            return false;
        }
        try {
            // 设置文件存储路径
            String uploadFolder = "D:\\code\\idea_java\\mange\\waitPapers";
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
    private void fileLocalDelete() {
        String uploadFolder = "D:\\code\\idea_java\\mange\\waitPapers";
        File folder = new File(uploadFolder);
        String fileName = id + ".pdf";
        File targetFile = new File(folder, fileName);
        if (targetFile.exists()){
            targetFile.delete();
        }
    }
    public String tendToSysPaper() throws SQLException {
        this.saveToSysPaper();
        File waitFile = this.getRelateFile();
        Paper paper = new Paper(id);
        try {
            if(paper.fileLocalSave(waitFile)){
                //this.dropFromWaitArea();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("文件转移失败");
            return null;
        }
        return id;
    }
}
