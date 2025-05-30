package com.example.instance;
import com.example.util.sqlUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TypeList {
    boolean isSystem;
    String teamId;

    public ArrayList<Map<String, Object>> getTypeList() {
        return typeList;
    }

    ArrayList<Map<String,Object>> typeList;
    public TypeList(boolean isSystem,String teamId) throws  SQLException{
        this.isSystem = isSystem;
        typeList = new ArrayList<>();
        getSqlTypeInfo(teamId);
    }
    void getSqlTypeInfo(String teamId) throws SQLException{
        if(this.isSystem){
            String sql1 = "select id,name,sid from systype where id!=1;";
            ResultSet res = sqlUtil.query(sql1);
            while(res.next()){
                Map<String,Object> row = new HashMap<>();
                row.put("id",res.getString("id"));
                row.put("name",res.getString("name"));
                row.put("sid",res.getString("sid"));
                this.typeList.add(row);
            }
        }
    }
    public boolean insertType(String name){
        String sql1 = "Insert into sysType (name,sid) values (?,null)";
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
    public boolean updateType(String name,String id){
        String sql1 = "update sysType set name=? where id=?";
        try{
            int res = sqlUtil.update(sql1,name,id);
            if(res!=1) return false;
            else return true;
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }
    public TypeList update() throws SQLException {
        getSqlTypeInfo("");
        return this;
    }
}
