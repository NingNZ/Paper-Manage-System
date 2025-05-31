package com.example.instance;

import com.example.util.sqlUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class User {
    private String id;
    private String name;
    private String email;
    public boolean isNull(){
        return this.id==null;
    }
    public User(String id) {
        String sql1 = "select id,name,email from users where id = ?";
        try(ResultSet res = sqlUtil.query(sql1,id)) {
            if(!res.isBeforeFirst()) {
                this.id = null;
                this.name = null;
                this.email = null;
            }
            else{
                res.next();
                this.id = id;
                this.name = res.getString("name");
                this.email = res.getString("email");
            }
        } catch (SQLException e) {
            this.id = null;
            this.name = null;
            this.email = null;
        }

    }
    public Map<String,Object> tranferToMap(){
        if(this.id==null){
            return null;
        }else{
            Map<String,Object> row = new HashMap<>();
            row.put("id",this.id);
            row.put("name",this.name);
            row.put("email",this.email);
            return row;
        }
    }
    public boolean equalToUserId(String uid){
        return this.id.equals(uid);
    }
}
