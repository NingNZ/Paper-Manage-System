package com.example.controller;

import com.example.instance.User;
import com.example.util.tool;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

@RestController
public class loginControl {
    @PostMapping("/login")
    public Map<String,Object> solveLogin(@RequestParam String username, @RequestParam String password, HttpSession session){
        User loginUser = new User(username);
        if(loginUser.isNull()){
            return tool.msgCreate(404,"no find");
        }
        else if(loginUser.getPwd().equals(password)){
            session.setAttribute("user",loginUser.getId());
            session.setAttribute("permission",loginUser.getPermission());
            return tool.msgCreate(200,"Hello");
        }else{
            return tool.msgCreate(400,"error pwd");
        }
    }

    @PostMapping("/register")
    public Map<String,Object> solveRegister(@RequestParam String userId,@RequestParam String name,@RequestParam String password){
        //检查字符串是否合法
        System.out.println(userId);
        System.out.println(password);
        System.out.println(name);
        if(!(funcTool.checkId(userId) && funcTool.checkPwd(password) && funcTool.checkName(name))){
            return tool.msgCreate(400,"输入不合法");
        }
        User user = new User(userId);
        if(!user.isNull()){
            return tool.msgCreate(400,"账户名已经被占用");
        }
        try {
            User.insertDB(userId,name,password);
        } catch (SQLException e) {
            e.printStackTrace();
            return tool.msgCreate(400,"数据库操作错误");
        }
        return tool.msgCreate(200,"success");
    }
    @GetMapping("/session/userInfo")
    public ArrayList<Map<String,Object>> getSessionUserInfo(HttpSession session){
        ArrayList<Map<String,Object>> res = new ArrayList<>();
        Object id = session.getAttribute("user");
        System.out.println(id);
        if(id==null){
            res.add(tool.msgCreate(400,"游客"));
            return res;
        }
        if(id.toString().equals("root")){
            res.add(tool.msgCreate(300,"管理员"));
            return res;
        }
        User user = new User(id.toString());
        res.add(tool.msgCreate(200,"用户"));
        res.add(user.tranferToMap());
        return res;
    }
    @GetMapping("/session/check")
    public Map<String,Object> checkIdentity(HttpSession session){
        Object permission = session.getAttribute("permission");
        if(permission==null){
            return tool.msgCreate(404,"未登录");
        }
        boolean isRoot = Boolean.parseBoolean(permission.toString());
        if(isRoot) return tool.msgCreate(200,"管理员");
        return tool.msgCreate(300,"普通用户");
    }

    static class funcTool{
       static public boolean checkId(String id){
            String userIdRegex = "^[a-z\\d]{6,18}$"; // 6-18 characters, letters and numbers
            return id.matches(userIdRegex);
        }
       static public boolean checkPwd(String pwd){
            String passwordRegex = "^[A-Za-z0-9]{6,18}$";
            return pwd.matches(passwordRegex);
        }
       static public boolean checkName(String name){
            String usernameRegex = "^(?=.{1,18}$)([\\u4e00-\\u9fa5]+|[a-zA-Z]+([ ·][a-zA-Z]+)*)$"; // 1-18 characters, Chinese, English, spaces, or '·'
            return name.matches(usernameRegex);
        }
    }
}
