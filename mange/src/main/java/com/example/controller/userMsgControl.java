package com.example.controller;

import com.example.instance.Team;
import com.example.instance.userMsg;
import com.example.instance.paperMsg;
import com.example.util.Pair;
import com.example.util.paperMsgList;
import com.example.util.tool;
import com.example.util.userMsgList;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
public class userMsgControl {
    @GetMapping("/notice/getNoticesOP")
    public ArrayList<Map<String,Object>> getUserNoticeWithOp(@RequestParam String selectId, HttpSession session){
        ArrayList<Map<String,Object>> res = new ArrayList<>();
        ArrayList<userMsg> msgList = new ArrayList<>();
        Object user = session.getAttribute("user");
        if(user==null){
            res.add(tool.msgCreate(400,"用户未登录"));
            return res;
        }
        if(selectId.equals("0#0#0#0")){
            try {
                msgList = userMsgList.getPersonalMsgList(user.toString(),1);
            } catch (SQLException e) {
                e.printStackTrace();
                res.add(tool.msgCreate(400,"信息获取错误"));
                return res;
            }
        }
        else{
            try {
                msgList = userMsgList.getTeamMsgList(selectId,0);
            } catch (SQLException e) {
                e.printStackTrace();
                res.add(tool.msgCreate(400,"信息获取错误"));
                return res;
            }
        }
        res.add(tool.msgCreate(200,"success"));
        for(userMsg msg : msgList){
            res.add(msg.transformToMap());
        }
        System.out.println(res);
        return res;
    }

    @GetMapping("/notice/getNoticesNoOP")
    public ArrayList<Map<String,Object>> getUserNoticeWithoutOp(@RequestParam String selectId, HttpSession session){
        ArrayList<Map<String,Object>> res = new ArrayList<>();
        ArrayList<userMsg> msgList = new ArrayList<>();
        Object user = session.getAttribute("user");
        if(user==null){
            res.add(tool.msgCreate(400,"用户未登录"));
            return res;
        }
        if(selectId.equals("0#0#0#0")){
            try {
                msgList = userMsgList.getPersonalMsgList(user.toString(),0);
            } catch (SQLException e) {
                e.printStackTrace();
                res.add(tool.msgCreate(400,"信息获取错误"));
                return res;
            }
            res.add(tool.msgCreate(200,"success"));
            for(userMsg msg : msgList){
                res.add(msg.transformToMap());
            }
        }
        else{
            //获取邀请反馈
            ArrayList<Map<String,Object>> inviteInfo = new ArrayList<>();
            try {
                msgList = userMsgList.getTeamMsgList(selectId,1);
            } catch (SQLException e) {
                e.printStackTrace();
                res.add(tool.msgCreate(400,"信息获取错误"));
                return res;
            }
            for(userMsg msg : msgList){
                inviteInfo.add(msg.transformToMap());
            }

            //获取论文反馈
            ArrayList<Map<String,Object>> paperInfo = new ArrayList<>();
            ArrayList<paperMsg> plist;
            try {
                plist = paperMsgList.getMsgList(selectId);
            } catch (SQLException e) {
                e.printStackTrace();
                res.add(tool.msgCreate(400,"信息获取错误"));
                return res;
            }
            for(paperMsg msg : plist){
                try {
                    paperInfo.add(msg.transformToMap());
                } catch (SQLException e) {
                    e.printStackTrace();
                    res.add(tool.msgCreate(400,"信息获取错误"));
                    return res;
                }
            }
            res.add(tool.msgCreate(200,"success"));
            HashMap<String,Object> expand = new HashMap<>();
            expand.put("invite",inviteInfo);
            expand.put("submit",paperInfo);
            res.add(expand);
        }

        System.out.println(res);
        return res;
    }

    @GetMapping("/notice/teamApply")
    public Map<String,Object> applyToTeam(@RequestParam String teamId,HttpSession session){
        Object user = session.getAttribute("user");
        if(user==null) return tool.msgCreate(400,"未登录");
        String uid = user.toString();
        Team team = new Team(teamId);
        if(team.isNull()){
            return tool.msgCreate(400,"系统错误，团队不存在");
        }
        if(team.findMember(uid)){
            return tool.msgCreate(201,"你已经在团队中");
        }
        try {
            String existMsgId = userMsg.searchExists(uid,teamId,0);
            if(existMsgId!=null){
                userMsg msg = new userMsg(existMsgId);
                if(msg.getState()==0){
                    return tool.msgCreate(300,"你已经提交了申请，等待组长审核");
                }
                msg.dropSelfFromDB();
            }
            String time = tool.getTimeYMD();
            userMsg.insertToDB(teamId,uid,0,time);

        } catch (SQLException e) {
            e.printStackTrace();
            return tool.msgCreate(400,"数据库处理出错");
        }
        return tool.msgCreate(200,"success");
    }

    @GetMapping("/notice/memberInvite")
    public Map<String,Object> inviteUserMember(@RequestParam String teamId,
                                               @RequestParam String userId,
                                               HttpSession session){
        //权限管理
        Object logUser = session.getAttribute("user");
        if(logUser==null){
            return tool.msgCreate(400,"你未登录");
        }
        Pair<Boolean,Team> check = checkUserRole(logUser.toString(),teamId);
        if(!check.getKey()){
            return tool.msgCreate(400,"你不是团队组长");
        }
        Team team = check.getValue();
        if(team.findMember(userId)){
            return tool.msgCreate(300,"该用户已经在团队中");
        }
        try {
            String msgId = userMsg.searchExists(userId,teamId,1);
            if(msgId!=null){
                userMsg msg = new userMsg(msgId);
                if(msg.getState()==0){
                    return tool.msgCreate(300,"你已经邀请该用户,等待该用户同意");
                }
                msg.dropSelfFromDB();
            }
            userMsg.insertToDB(teamId,userId,1,tool.getTimeYMD());
        } catch (SQLException e) {
            e.printStackTrace();
            return tool.msgCreate(400,"数据库操作失败");
        }
        return tool.msgCreate(200,"success");
    }

    @GetMapping("/notice/userSolve")
    public Map<String,Object> solveUserMsg(@RequestParam String msgId,
                                           @RequestParam String select,
                                           HttpSession session){
        System.out.println(msgId);
        System.out.println(select);
        userMsg msg;
        try {
            msg = new userMsg(msgId);
            if(msg.isNull()){
                return tool.msgCreate(400,"不存在该消息");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return tool.msgCreate(400,"数据库查询错误");
        }
        Object logUser = session.getAttribute("user");
        if(logUser==null){
            return tool.msgCreate(400,"你未登录");
        }
        String logId = logUser.toString();

        String teamId = msg.getTeamId();
        Team team = new Team(teamId);
        int option = Integer.parseInt(select);
        if(team.isNull()){
            return tool.msgCreate(400,"不存在该团队");
        }
        if(msg.getType()==0){      //用户申请加入的消息，只有团队组长可以处理
            if(!team.getLeaderId().equals(logId)){
                return tool.msgCreate(400,"你不是团队组长");
            }
            try {
                if(team.findMember(msg.getUserId())) {
                    msg.dropSelfFromDB();
                    return tool.msgCreate(300,"该用户已经在团队中");
                }
                msg.updatResult(option);
                if(option==1){
                    team.addMember(msg.getUserId(),"组员");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return tool.msgCreate(400,"数据库操作失败");
            }
        }else{                     //团队邀请用户的消息，只有用户自己可以处理
            if(!logId.equals(msg.getUserId())){
                return tool.msgCreate(400,"这不是你的账户");
            }
            try {
                if(team.findMember(logId)) {
                    //msg.dropSelfFromDB();
                    return tool.msgCreate(300,"你已经在团队中");
                }
                msg.updatResult(option);
                if(option==1){
                    team.addMember(msg.getUserId(),"组员");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return tool.msgCreate(400,"数据库处理错误");
            }
        }
        return tool.msgCreate(200,"success");
    }
    private final Pair<Boolean,Team> checkUserRole(String userId, String teamId){
        Team team = new Team(teamId);
        if(team.isNull()) return new Pair<>(false,null);
        String leaderId = team.getLeaderId();
        if(userId.equals(leaderId)) return new Pair<>(true,team);
        return new Pair<>(false,null);
    }
}
