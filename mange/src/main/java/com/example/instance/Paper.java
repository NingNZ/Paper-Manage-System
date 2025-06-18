package com.example.instance;

import com.example.util.ResultSetWrapper;
import com.example.util.paperUtil;
import com.example.util.sqlUtil;
import com.example.util.tool;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Paper {
    String id;
    String title;
    String journal;

    String authors;
    String type;
    String typeid;
    String time;

    public ArrayList<String> getAuthorIdList() {
        return authorIdList;
    }

    ArrayList<String> authorIdList;
    public Paper(String id) throws SQLException{
        String sql1 = "select sysPaper.id,title,time," +
                "sysjournal.name as journal,systype.id as typeid," +
                "systype.name as type " +
                "from syspaper,sysjournal,systype " +
                "where syspaper.id=? and " +
                "syspaper.typeId=systype.id and " +
                "syspaper.jourId=sysjournal.id;";
        ResultSetWrapper res = sqlUtil.query(sql1,id);
        if(res.isBeforeFirst()){
            res.next();
            this.id = res.getString("id");
            this.typeid = res.getString("typeid");
            this.type = res.getString("type");
            this.time = res.getString("time");
            this.journal = res.getString("journal");
            this.title = res.getString("title");
            this.authorIdList = new ArrayList<>();
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
            String time
    ){
        String id = "#p"+ tool.generateHash(title,10);
        System.out.println(id);
        String sql1 = "insert into syspaper (id,title,jourId,typeId,time) values (?,?,?,?,?)";
        try{
            int res = sqlUtil.update(sql1,id,title,jourId,typeid,time);
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }
        String sql2 = "insert into authors(paperId,userId,score) values(?,?,?)";
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
    private void sqlGetAuthors(){
        authors = "";
        if(!this.isNull()){
            String sql2 ="select users.name,users.id from users,authors " +
                    "where authors.userId = users.id and authors.paperId= ? ;";
            try{
                ResultSetWrapper resAuthor = sqlUtil.query(sql2,this.id);
                    resAuthor.next();
                    String tmp = resAuthor.getString("name");
                    String tmpId = resAuthor.getString("id");
                    this.authors = this.authors.concat(tmp);
                    this.authorIdList.add(tmpId);
                    while(resAuthor.next()){
                        tmp = resAuthor.getString("name");
                        tmpId = resAuthor.getString("id");
                        this.authors = this.authors.concat(", "+tmp);
                        this.authorIdList.add(tmpId);
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
    public  boolean fileLocalSave(File file) throws IOException {
        if (file==null) {
            System.out.println("File is empty!");
            return false;
        }
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
            Files.copy(file.toPath(),targetFile.toPath());
            System.out.println("File uploaded successfully: " + targetFile.getAbsolutePath());
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
            boolean delRes = this.getRelateFile().delete();
        }
        return  true;
    }
    public boolean fileSqlTypeUpdate(String typeid){
        String sql1 = "update syspaper set typeid=?  where id =?";
        try{
            int res = sqlUtil.update(sql1,typeid,this.id);
            if(res!=1) return false;
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
}

