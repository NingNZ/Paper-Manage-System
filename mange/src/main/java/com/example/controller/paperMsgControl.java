package com.example.controller;

import com.example.instance.Paper;
import com.example.instance.waitPaper;
import com.example.instance.paperMsg;
import com.example.util.paperMsgList;
import com.example.util.tool;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
public class paperMsgControl {
    @GetMapping("/notice/SysgetNoticesOP")
    public ArrayList<Map<String,Object>> getPaperMsgList(HttpSession session){
        ArrayList<Map<String,Object>> res = new ArrayList<>();
        try {
            ArrayList<Map<String,Object>> msgInfo = new ArrayList<>();
            for(paperMsg each: paperMsgList.getMsgList()){
                msgInfo.add(each.transformToMap());
            }
            res.add(tool.msgCreate(200,"success"));
            res.addAll(msgInfo);
        } catch (SQLException e) {
            e.printStackTrace();
            res.add(tool.msgCreate(400,"数据库搜索失败"));
        }
        return res;
    }

    @GetMapping("/notice/SysSolve")
    public Map<String,Object> solveSysMsg(@RequestParam String msgId,
                                          @RequestParam int select,
                                          HttpSession session){
        Object user = session.getAttribute("user");
        if(user==null){
            return tool.msgCreate(400,"未登录");
        }
        if(!user.toString().equals("root")){
            return tool.msgCreate(400,"你不是管理员");
        }
        System.out.println(msgId);
        System.out.println(select);
        paperMsg msg;
        try {
            msg = new paperMsg(msgId);
        } catch (SQLException e) {
            e.printStackTrace();
            return tool.msgCreate(400,"数据库操作错误");
        }
        if(msg.isNull()){
            return tool.msgCreate(400,"消息不存在");
        }
        try {
            msg.updatResult(select);
            if(select==1){
                waitPaper wpaper = new waitPaper(msg.getWaitPaperId());
                Paper paper = new Paper(msg.getWaitPaperId());
                if(!paper.isNull()){
                    paper.fileSqlDelete();
                }
                wpaper.tendToSysPaper();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return tool.msgCreate(400,"数据库操作错误");
        }
        return  tool.msgCreate(200,"success");
    }
}
