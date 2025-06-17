package com.example.util;

import java.sql.SQLException;
import java.util.ArrayList;
import com.example.instance.userMsg;

public class userMsgList {
    public static ArrayList<userMsg> getPersonalMsgList(String userId,int type) throws SQLException {
        ArrayList<userMsg> out = new ArrayList<>();
        String sql1 = "select id from member_takein_msg where userId = ? and type = ? order by state asc,result asc,time";
        ResultSetWrapper res = sqlUtil.query(sql1,userId,type);
        while(res.next()){
            String id = res.getString("id");
            out.add(new userMsg(id));
        }
        return out;
    }
    public static ArrayList<userMsg> getTeamMsgList(String teamId,int type) throws SQLException {
        ArrayList<userMsg> out = new ArrayList<>();
        String sql1 = "select id from member_takein_msg where teamId = ? and type = ? order by state asc,result asc,time";
        ResultSetWrapper res = sqlUtil.query(sql1,teamId,type);
        while(res.next()){
            String id = res.getString("id");
            out.add(new userMsg(id));
        }
        return out;
    }
}
