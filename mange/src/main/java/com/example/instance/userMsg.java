package com.example.instance;

import com.example.util.ResultSetWrapper;
import com.example.util.sqlUtil;
import com.example.util.tool;

import java.sql.SQLException;
import java.util.HashMap;

public class userMsg {
    private String id;

    public String getTeamId() {
        return teamId;
    }

    private String teamId;
    private String teamName;

    public String getUserId() {
        return userId;
    }

    private String userId;
    private String userName;

    public int getType() {
        return type;
    }

    private int type ;
    private int result;

    public int getState() {
        return state;
    }

    private  int  state;
    private  String time;

    public userMsg(String id) throws SQLException {
        String sql = "select id,teamId,userId,type,result,state,time from member_takein_msg where id = ?";
        ResultSetWrapper res = sqlUtil.query(sql,id);
        if(res.next()){
            this.id = res.getString("id");
            this.teamId = res.getString("teamId");
            this.userId = res.getString("userId");
            this.type = Integer.parseInt(res.getString("type"));
            this.result = Integer.parseInt(res.getString("result"));
            this.state = Integer.parseInt(res.getString("state"));
            this.time = res.getString("time");
            String sql1 = "select name from users where id = ?";
            ResultSetWrapper res1 = sqlUtil.query(sql1,userId);
            if(res1.next()){
                this.userName = res1.getString("name");
            }
            String sql2 = "select name from team where id = ?";
            ResultSetWrapper res2 = sqlUtil.query(sql2,teamId);
            if(res2.next()){
                this.teamName = res2.getString("name");
            }
        }else{
            this.id = null;
        }
    }
    public boolean isNull(){
        return id==null;
    }
    public HashMap<String,Object> transformToMap(){
        HashMap<String,Object> map = new HashMap<>();
        map.put("id",id);
        map.put("teamId",teamId);
        map.put("team",teamName);
        map.put("userId",userId);
        map.put("userName",userName);
        map.put("type",type);
        map.put("result",result);
        map.put("state",state);
        map.put("time",time);
        return map;
    }

    public static String insertToDB(String teamId,String userId,int type,String time) throws SQLException {
        String id = "#mi"+tool.generateHash(teamId+userId+time+type,9);
        userMsg msg = new userMsg(id);
        if(!msg.isNull()){
            System.out.println("重复的消息id");
            return null;
        }
        String sql = "insert into member_takein_msg (id,teamId,userId,type,time) values(?,?,?,?,?)";
        sqlUtil.update(sql,id,teamId,userId,type,time);
        return id;
    }

    public static String searchExists(String userId,String teamId,int type) throws SQLException {
        String sql = "select id from member_takein_msg where userId = ? and teamId = ? and type=?";
        ResultSetWrapper res = sqlUtil.query(sql,userId,teamId,type);
        if(res.next()){
            return res.getString("id");
        }
        return null;
    }
    public void dropSelfFromDB() throws SQLException {
        String sql = "delete from member_takein_msg where id = ?";
        sqlUtil.update(sql,id);
    }

    public void updatResult(int result) throws SQLException {
        if(this.state==1) return;
        if(result!=0 && result!=1) return;
        String sql = "update member_takein_msg set state = 1, result = ? where id = ?";
        sqlUtil.update(sql,result,id);
    }
}
