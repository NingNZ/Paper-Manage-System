package com.example.instance;

import com.example.util.ResultSetWrapper;
import com.example.util.sqlUtil;
import com.fasterxml.jackson.databind.util.ObjectBuffer;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class refPaperList {
    private String teamId;
    private HashMap<String,refPaper> refList;

    public refPaperList(String teamId) throws SQLException {
        this.teamId = teamId;
        this.refList = new HashMap<>();
        String sql1 = "select id from teamRefPaper where teamId = ?";
        ResultSetWrapper res = sqlUtil.query(sql1,teamId);
        while(res.next()){
            String id = res.getString("id");
            refPaper paper = refPaper.getRefPaperByID(id,teamId);
            if(paper==null){
                System.out.println("论文为空 in refPaperList\n");
            }else{
                refList.put(id,paper);
            }
        }
    }
    public ArrayList<Map<String, Object>> export(){
        ArrayList<Map<String, Object>> res = new ArrayList<>();
        for(Map.Entry<String,refPaper> paper : this.refList.entrySet()){
            res.add(paper.getValue().tranformat());
        }
        return res;
    }

    public boolean checkPaperExistById(String id){
        return refList.containsKey(id);
    }
}
