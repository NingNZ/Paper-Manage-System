package com.example.instance;

import com.example.util.ResultSetWrapper;
import com.example.util.sqlUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeamList {
    ArrayList<Team> teamList;

    public boolean isNull(){
        return this.teamList==null;
    }
    /**
     *根据用户ID得到团队列表
     * */
    public TeamList(String userId){
        String sql1 = "select teamId from teammember where binary userId=?";
        try {
            ResultSetWrapper resultSet = sqlUtil.query(sql1, userId);
            this.teamList=new ArrayList<>();
            while(resultSet.next()){
                String tid = resultSet.getString("teamId");
                teamList.add(new Team(tid));
            }
        }catch (SQLException e){
            e.printStackTrace();
            this.teamList=null;
        }
    }
    public ArrayList<Map<String,Object>> transfer(){
        if(this.isNull()) return null;
        ArrayList<Map<String,Object>> res = new ArrayList<>();
        for(Team each:this.teamList){
            Map<String,Object> row = each.getInfo();
            if(row!=null) res.add(row);
        }
        return res;
    }
    public ArrayList<Map<String,Object>> transferOnlyManage(String userId){
        if(this.isNull()) return null;
        ArrayList<Map<String,Object>> res = new ArrayList<>();
        for(Team each:this.teamList){
            if(each.getLeaderId().equals(userId)){
                Map<String,Object> row = each.getInfo();
                if(row!=null) res.add(row);
            }
        }
        return res;
    }
}
