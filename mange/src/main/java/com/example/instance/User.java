package com.example.instance;

import com.example.util.ResultSetWrapper;
import com.example.util.sqlUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class User {
    public String getId() {
        return id;
    }

    private String id;
    private String pwd;

    public String getName() {
        return name;
    }

    private String name;
    private int permission;
    private String email;
    private  double score;
    public boolean isNull(){
        return this.id==null;
    }
    public User(String id) {
        String sql1 = "select id,pwd,name,email,score,permission from users where id = ?";
        try{
            ResultSetWrapper res = sqlUtil.query(sql1,id);
            if(!res.isBeforeFirst()) {
                this.id = null;
                this.name = null;
                this.pwd = null;
                this.email = null;
                this.score = 0;
                this.permission = -1;
            }
            else{
                res.next();
                this.id = id;
                this.pwd = res.getString("pwd");
                this.name = res.getString("name");
                this.email = res.getString("email");
                this.score =  Double.parseDouble(res.getString("score"));
                this.permission = Integer.parseInt(res.getString("permission"));
            }
        } catch (SQLException e) {
            this.id = null;
            this.name = null;
            this.pwd = null;
            this.email = null;
            this.score = 0;
            this.permission=-1;
        }

    }

    static public User insertDB(String id,String name,String pwd) throws SQLException {
        String sql1 = "insert into users (id,name,pwd) values(?,?,?)";
        int res = sqlUtil.update(sql1,id,name,pwd);
        if(res==1) return new User(id);
        else return null;
    }

    public int saveDB() throws SQLException {
        String sql1 = "update users set name=?, set pwd=? ,set email=?, set score=? where id = ?";
        return sqlUtil.update(sql1,name,pwd,email,score,id);
    }
    public Map<String,Object> tranferToMap(){
        if(this.id==null){
            return null;
        }else{
            Map<String,Object> row = new HashMap<>();
            row.put("id",this.id);
            row.put("name",this.name);
            row.put("email",this.email);
            row.put("score",this.score);
            return row;
        }
    }
    public boolean equalToUserId(String uid){
        return this.id.equals(uid);
    }

    public String getPwd() {
        return pwd;
    }

    public double getScore() {
        return score;
    }

    public boolean getPermission() {
        if(permission==1) return true;
        return false;
    }

    public ArrayList<Paper> getAllMyPapers() throws SQLException {
        String sql1 = "select paperId from authors where userId = ?";
        ArrayList<Paper> out = new ArrayList<>();
        ResultSetWrapper res =  sqlUtil.query(sql1,this.id);
        while(res.next()){
            String paperId = res.getString("paperId");
            if(paperId!=null) out.add(new Paper(paperId));
        }
        return out;
    }
    public ArrayList<Map<String,Object>> getAllMyPapersAndScores() throws SQLException {
        String sql1 = "select paperId,score from authors where userId = ?";
        ArrayList<Map<String,Object>> out = new ArrayList<>();
        ResultSetWrapper res =  sqlUtil.query(sql1,this.id);
        while(res.next()){
            String paperId = res.getString("paperId");
            if(paperId!=null){
                Map<String,Object> each= new Paper(paperId).transferToMap();
                double score = Double.parseDouble(res.getString("score"));
                each.put("score",String.format("%.2f",score));
                out.add(each);
            }
        }
        return out;
    }
    @Override
    public boolean equals(Object o){
        if (this == o) return true; // 同一个对象
        if (o == null || getClass() != o.getClass()) return false; // 判空和类型
        User user = (User) o;
        return this.getId().equals(user.getId());
    }
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
