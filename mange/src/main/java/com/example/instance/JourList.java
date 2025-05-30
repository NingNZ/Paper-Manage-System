package com.example.instance;
import com.example.util.sqlUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class JourList {
    public ArrayList<Map<String, Object>> getJourList() {
        return jourList;
    }

    ArrayList<Map<String,Object>> jourList;
    public JourList() throws  SQLException{
        jourList = new ArrayList<>();
        getSqlJourInfo();
    }
    void getSqlJourInfo() throws SQLException{
        String sql1 = "select id,name from sysjournal where id!=1;";
        ResultSet res = sqlUtil.query(sql1);
        while(res.next()){
            Map<String,Object> row = new HashMap<>();
            row.put("id",res.getString("id"));
            row.put("name",res.getString("name"));
            this.jourList.add(row);
        }
    }
    public boolean insertJournal(String name){
        String sql1 = "Insert into sysJournal (name) values (?)";
        try{
            int res = sqlUtil.update(sql1,name);
            if(res!=1){
                return false;
            }
            return true;
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }
    public boolean updateJournal(String name,String id){
        String sql1 = "update sysJournal set name=? where id=?";
        try{
            int res = sqlUtil.update(sql1,name,id);
            if(res!=1) return false;
            else return true;
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }
}
