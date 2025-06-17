package com.example.instance;

import com.example.util.ResultSetWrapper;
import com.example.util.sqlUtil;
import com.example.util.tool;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class paperMsg {
    private String id;

    public String getWaitPaperId() {
        return waitPaperId;
    }

    public String getId() {
        return id;
    }

    private String waitPaperId;
    private String teamId;
    private String teamName;
    private String time;

    public int getState() {
        return state;
    }

    private int state;
    private int result;

    public paperMsg(String id) throws SQLException {
        String sql = "select id,wpid,teamId,state,time,result from checkPaperMsg where id = ?";
        ResultSetWrapper res = sqlUtil.query(sql,id);
        if(!res.isBeforeFirst()){
            this.id = null;
            return ;
        }
        res.next();
        this.id = res.getString("id");
        this.waitPaperId = res.getString("wpid");
        this.teamId = res.getString("teamId");
        this.time = res.getString("time");
        this.state = Integer.parseInt(res.getString("state"));
        this.result = Integer.parseInt(res.getString("result"));
        String sql1 = "select name from team where id = ?";
        ResultSetWrapper teamRes = sqlUtil.query(sql1,this.teamId);
        teamRes.next();
        this.teamName = teamRes.getString("name");
    }
    public boolean isNull(){
        return id==null;
    }
    public static void insertToDB(String waitpid,String teamId,String time) throws SQLException {
        String id = "#cm"+tool.generateHash(waitpid+teamId+time,9);
        String sql = "insert into checkPaperMsg (id,wpid,teamId,time) values (?,?,?,?)";
        sqlUtil.update(sql,id,waitpid,teamId,time);
    }
    public void dropSelfFromDB() throws SQLException {
        String sql = "delete from checkPaperMsg where id = ?";
        sqlUtil.update(sql,id);
    }
    public HashMap<String,Object> transformToMap() throws SQLException {
        waitPaper wpaper = new waitPaper(waitPaperId);
        HashMap<String,Object> paperMap = wpaper.transferToMap();
        HashMap<String,Object> outMap = new HashMap<>();
        outMap.put("msgId",id);
        outMap.put("teamId",teamId);
        outMap.put("teamName",teamName);
        outMap.put("time",time);
        outMap.put("state",state);
        outMap.put("result",result);
        outMap.put("paper",paperMap);
        return outMap;
    }

    public static paperMsg checkMsgExist(String title,String teamId) throws SQLException {
        String id = "#p"+tool.generateHash(title,10);
        String sql = "select id from checkPaperMsg where wpid = ? and teamId = ?";
        ResultSetWrapper res = sqlUtil.query(sql,id,teamId);
        if(!res.isBeforeFirst()){
            return null;
        }
        res.next();
        return new paperMsg(res.getString("id"));
    }
    public void updatResult(int result) throws SQLException {
        if(this.state==1) return;
        if(result!=0 && result!=1) return;
        String sql = "update checkPaperMsg set state = 1, result = ? where id = ?";
        sqlUtil.update(sql,result,id);
    }
}
