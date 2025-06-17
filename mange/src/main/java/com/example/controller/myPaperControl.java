package com.example.controller;

import com.example.instance.User;
import com.example.util.tool;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
public class myPaperControl {
    @GetMapping("/userPaper/Paper")
    public static ArrayList<Map<String,Object>> getMyAllPaper(HttpSession session){
        ArrayList<Map<String,Object>> res = new ArrayList<>();
        Object user = session.getAttribute("user");
        if(user==null){
            res.add(tool.msgCreate(400,"未登录"));
            return res;
        }
        String userId = user.toString();
        User yourUser = new User(userId);
        if(yourUser.isNull()){
            res.add(tool.msgCreate(400,"用户信息载入失败"));
            return res;
        }
        try {
            ArrayList<Map<String,Object>> info = yourUser.getAllMyPapersAndScores();
            res.add(tool.msgCreate(200,"success"));
            res.addAll(info);
        } catch (SQLException e) {
            res.add(tool.msgCreate(400,"数据库错误"));
            return res;
        }
        System.out.println(res);
        return res;
    }
}
