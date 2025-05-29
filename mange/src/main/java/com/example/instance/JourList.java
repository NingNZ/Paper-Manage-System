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
}
