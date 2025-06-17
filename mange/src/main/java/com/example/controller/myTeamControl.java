package com.example.controller;

import com.example.instance.Team;
import com.example.instance.TeamList;
import com.example.instance.User;
import com.example.util.tool;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class myTeamControl {
    @GetMapping("/myTeam/get")
    public List<Map<String,Object>> getMyTeamList(HttpSession session){
        Object userId = session.getAttribute("user");
        List<Map<String,Object>> res = new ArrayList<>();
        if(userId==null){
            res.add(tool.msgCreate(404,"未登录"));
            return res;
        }
        TeamList teamList = new TeamList(userId.toString());
        if(teamList.isNull()){
            res.add(tool.msgCreate(400,"系统错误"));
        }else{
            res=teamList.transfer();
            res.add(0,tool.msgCreate(200,"success"));
        }
        return res;
    }
    @GetMapping("/myTeam/check")
    public Map<String,Object> checkTeamExist(@RequestParam String name){
        int check = Team.checkTeamExist(name);
        switch (check){
            case 200 ->{
                return tool.msgCreate(200,"Exist");
            }
            case 400 ->{
                return tool.msgCreate(400,"None Exist");
            }
            case 404 ->{
                return tool.msgCreate(404,"系统错误");
            }
        }
        return tool.msgCreate(404,"系统错误");
    }
    @GetMapping("/myTeam/new")
    public Map<String,Object> createTeam(@RequestParam String name,HttpSession session){
        Object userId = session.getAttribute("user");
        if(userId==null) return tool.msgCreate(400,"未登录");
        if(Team.checkTeamExist(name)==200){
            return tool.msgCreate(400,"团队名已经被使用");
        }
        String leaderId = userId.toString();
        Team newTeam = Team.insertTeam(name,leaderId);
        if(newTeam==null){
            return tool.msgCreate(400,"系统错误,创建失败");
        }else{
            return tool.msgCreate(200,"创建成功");
        }
    }
    @GetMapping("/myTeam/dropMember")
    public Map<String,Object> dropTeamMember(@RequestParam String teamId,HttpSession session){
        Team thisTeam = new Team(teamId);
        if(thisTeam.isNull()){
            return tool.msgCreate(400,"系统错误,删除失败");
        }
        Object user = session.getAttribute("user");
        if(user==null){
            return tool.msgCreate(400,"你未登录");
        }
        String uid = user.toString();
        switch(thisTeam.dropMember(uid)){
            case 390->{
                return tool.msgCreate(390,"删除组长");
            }
            case 200->{
                return tool.msgCreate(200,"删除成功");
            }
            case 400->{
                return tool.msgCreate(400,"系统错误,删除失败");
            }
        }
        return tool.msgCreate(400,"系统错误,删除失败");
    }
    @GetMapping("/myTeam/searchOne")
    public List<Map<String,Object>> getTargetTeam(@RequestParam String teamId){
        Team team = new Team(teamId);
        List<Map<String,Object>> res = new ArrayList<>();
        if(team.isNull()){
            res.add(tool.msgCreate(400,"团队不存在"));
        }else{
            res.add(tool.msgCreate(200,"success"));
            res.add(team.getInfo());
        }
        return res;
    }
    /**
     * @return  200:添加成功<br>
     *          201:已经在团队中<br>
     *          400:系统错误
     * */
    @GetMapping("/myTeam/addMember")
    public Map<String,Object> addTeamMember(@RequestParam String teamId,HttpSession session){
        Object user = session.getAttribute("user");
        if(user==null) return tool.msgCreate(400,"未登录");
        String uid = user.toString();
        Team team = new Team(teamId);
        if(team.isNull()){
            return tool.msgCreate(400,"系统错误，团队不存在");
        }
        if(team.findMember(uid)){
            return tool.msgCreate(201,"你已经在团队中");
        }else{
            if(team.addMember(uid,"组员")){
                return tool.msgCreate(200,"添加成功");
            }else{
                return tool.msgCreate(400,"系统错误，添加成员失败");
            }
        }
    }
    @GetMapping("/myTeam/searchMember")
    public ArrayList<Map<String,Object>> searchTeamMember(@RequestParam String memberId,
                                                          @RequestParam String teamId,
                                                          HttpSession session){
        System.out.println(memberId);
        System.out.println(teamId);
        ArrayList<Map<String,Object>> res = new ArrayList<>();
        Object user = session.getAttribute("user");
        if(user==null){
            res.add(tool.msgCreate(400,"你未登录"));
            return res;
        }
        String uid = user.toString();
        Team team = new Team(teamId);
        if(team.isNull()){
            res.add(tool.msgCreate(400,"团队不存在"));
            return res;
        }
        if(!team.getLeaderId().equals(uid)){
            res.add(tool.msgCreate(400,"你不是团队组长"));
            return res;
        }
        if(memberId.equals("root")){
            res.add(tool.msgCreate(400,"禁止查管理员id"));
            return res;
        }
        User oneUser = new User(memberId);
        if(oneUser.isNull()){
            res.add(tool.msgCreate(400,"用户不存在"));
            return res;
        }
        if(team.findMember(memberId)){
            res.add(tool.msgCreate(300,"用户已经在团队中"));
            Map<String,Object> info = oneUser.tranferToMap();
            info.put("state",300);
            res.add(info);
        }else{
            res.add(tool.msgCreate(200,"查询成功"));
            Map<String,Object> info = oneUser.tranferToMap();
            info.put("state",200);
            res.add(info);
        }
        return res;
    }
    @GetMapping("/myTeam/getManagerTeam")
    public ArrayList<Map<String,Object>> solveManageTeam(HttpSession session){
        ArrayList<Map<String,Object>> res = new ArrayList<>();
        Object user = session.getAttribute("user");
        if(user==null){
            res.add(tool.msgCreate(400,"你未登录"));
            return res;
        }
        String uid = user.toString();
        TeamList list = new TeamList(uid);
        ArrayList<Map<String,Object>> data = list.transferOnlyManage(uid);
        res.add(tool.msgCreate(200,"success"));
        res.addAll(data);
        return res;
    }
}
