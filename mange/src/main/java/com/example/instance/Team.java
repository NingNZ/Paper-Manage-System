package com.example.instance;

import com.example.util.Pair;
import com.example.util.sqlUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Team {
    String id;
    String name;
    String leaderId;
    int count;
    ArrayList<Pair<User,String>> member;
    public Team(String id){
        String sql1 = "select id,name,leaderId,count from team where id=?";
        try(ResultSet res = sqlUtil.query(sql1,id)){
            if(!res.isBeforeFirst()){
                this.id = null;
                this.name = null;
                this.leaderId = null;
                this.count = 0;
                this.member = null;
                return;
            }
            res.next();
            this.id = id;
            this.name = res.getString("name");
            this.leaderId = res.getString("leaderId");
            this.count = Integer.parseInt(res.getString("count"));
            member = new ArrayList<>();
        }catch(SQLException e){
            this.id = null;
            this.name = null;
            this.leaderId = null;
            this.count = 0;
            this.member = null;
            return;
        }
        String sql2 = "select userId,identity from teammember where teamId=?";
        try(ResultSet res = sqlUtil.query(sql2,id)){
            while (res.next()){
                String userId = res.getString("userId");
                User oneMember = new User(userId);
                String identity = res.getString("identity");
                if(oneMember.isNull()){
                    continue;
                }else{
                    this.member.add(new Pair<User,String>(oneMember,identity));
                }
            }
        }catch (SQLException e){
            this.member = null;
        }
    }
    public boolean isNull(){
        return this.id==null;
    }
    public List<Map<String,Object>> getMemberList(){
        if(this.isNull()){
            return null;
        }else{
            ArrayList<Map<String,Object>> res = new ArrayList<>();
            for(Pair<User,String> oneMember : member){
                Map<String,Object> row = oneMember.getKey().tranferToMap();
                if(row==null) continue;
                row.put("identity",oneMember.getValue());
                res.add(row);
            }
            return res;
        }
    }
}
