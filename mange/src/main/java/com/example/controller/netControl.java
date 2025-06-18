package com.example.controller;

import com.example.instance.Paper;
import com.example.instance.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.util.tool;

import java.sql.SQLException;
import java.util.*;

@RestController
public class netControl {
    @GetMapping("/net/checkId")
    public Map<String,Object> checkUserIdExist(@RequestParam String userId){
        User user = new User(userId);
        if(user.isNull()){
            return tool.msgCreate(400,"用户不存在");
        }else{
            if(user.getId().equals("root")){
                return tool.msgCreate(400,"不要查管理员账号");
            }
            return tool.msgCreate(200,"用户存在");
        }
    }

    @GetMapping("/net/getCoAuthor")
    public ArrayList<Map<String,Object>> getGraphData(@RequestParam String userId){
        ArrayList<Map<String,Object>> res = new ArrayList<>();
        HashMap<String,Object> data = new HashMap<>();
        HashMap<User,ArrayList<String>> CoAuthors = new HashMap<>();
        User user = new User(userId);
        if(user.isNull()){
            res.add(tool.msgCreate(400,"用户不存在"));
            return res;
        }
        ArrayList<Paper> paperList = null;
        try {
            paperList = user.getAllMyPapers();
        } catch (SQLException e) {
            e.printStackTrace();
            res.add(tool.msgCreate(400,"论文获取失败"));
            return res;
        }
        for(Paper each: paperList){
            for(String uid:each.getAuthorIdList()){
                if(!uid.equals(userId)){

                    User friend = new User(uid);
                    if(!CoAuthors.containsKey(friend)){
                        CoAuthors.put(friend,new ArrayList<String>());
                    }
                    CoAuthors.get(friend).add(each.getTitle());
                }
            }
        }
        int maxNum = 5;
        HashMap<String,Object> self = new HashMap<>();
        self.put("name",user.getId());
        self.put("displayName",user.getName());
        data.put("self",self);
        ArrayList<HashMap<String,Object>> coDataList = new ArrayList<>();
        while(!CoAuthors.isEmpty() && maxNum>0){
            HashMap<String,Object> each = new HashMap<>();

            Random random = new Random();
            List<Map.Entry<User, ArrayList<String>>> entryList = new ArrayList<>(CoAuthors.entrySet());
            Map.Entry<User, ArrayList<String>> randomEntry = entryList.get(random.nextInt(entryList.size()));

            each.put("name",randomEntry.getKey().getId());
            each.put("displayName",randomEntry.getKey().getName());
            each.put("papers",randomEntry.getValue());

            coDataList.add(each);
            CoAuthors.remove(randomEntry.getKey());
            maxNum--;
        }
        data.put("coAuthors",coDataList);

        res.add(tool.msgCreate(200,"success"));
        res.add(data);
        return res;
    }
}
