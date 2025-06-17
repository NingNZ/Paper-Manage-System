package com.example.util;

import com.example.instance.paperMsg;

import java.sql.SQLException;
import java.util.ArrayList;


public class paperMsgList {
    public static ArrayList<paperMsg> getMsgList() throws SQLException {
        String sql = "select id from checkPaperMsg order by state,result,time";
        ResultSetWrapper res = sqlUtil.query(sql);
        ArrayList<paperMsg> list = new ArrayList<>();
        while(res.next()){
            list.add(new paperMsg(res.getString("id")));
        }
        return list;
    }
    public static ArrayList<paperMsg> getMsgList(String teamId) throws SQLException {
        String sql = "select id from checkPaperMsg where teamId = ? order by state,result,time";
        ResultSetWrapper res = sqlUtil.query(sql,teamId);
        ArrayList<paperMsg> list = new ArrayList<>();
        while(res.next()){
            list.add(new paperMsg(res.getString("id")));
        }
        return list;
    }
}
