package com.example.instance;

import com.example.util.paperUtil;
import com.example.util.sqlUtil;
import com.example.util.tool;

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
        if(this.id!=null){
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
}

