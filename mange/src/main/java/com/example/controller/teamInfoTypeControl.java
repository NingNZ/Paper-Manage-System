package com.example.controller;

import com.example.instance.Team;
import com.example.instance.refTypeTree;
import com.example.util.Pair;
import com.example.util.tool;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class teamInfoTypeControl {
    @GetMapping("/teamInfo/Category")
    public List<Map<String,Object>> getTeamCategory(@RequestParam String teamId, HttpSession session){
        ArrayList<Map<String,Object>> res = new ArrayList<>();
        Object userId = session.getAttribute("user");
        if(userId==null){
            res.add(tool.msgCreate(401,"你未登录"));
            return res;
        }
        Team team = new Team(teamId);
        if(team.isNull()){
            res.add(tool.msgCreate(400,"团队不存在"));
            return res;
        }
        if(!team.findMember(userId.toString())){
            res.add(tool.msgCreate(403,"你不是团队成员"));
            return res;
        }
        try {
            refTypeTree tree = new refTypeTree(teamId);
            res.add(tool.msgCreate(200,"success"));
            res.addAll(tree.formatToTree());
        } catch (SQLException e) {
            res.add(tool.msgCreate(400,"数据库错误"));
            e.printStackTrace();
        }
        return res;
    }

    @PostMapping("/teamInfo/CategoryAdd")
    public ArrayList<Map<String,Object>> addTeamCategory(@RequestParam String teamId,
                                              @RequestParam String label,
                                              @RequestParam String fid,
                                              HttpSession session){
        ArrayList<Map<String,Object>> res=new ArrayList<>();
        //权限管理
        Object userId = session.getAttribute("user");
        if(userId==null){
            res.add(tool.msgCreate(401,"你未登录"));
            return res;
        }
        Pair<Boolean,Team> check = checkUserRole(userId.toString(),teamId);
        if(!check.getKey()){
            res.add(tool.msgCreate(403,"你不是团队组长"));
            return res;
        }
        //
        refTypeTree tree = null;
        if(fid.equals("&_#$H~~H$#_&")) fid = null;
        if(label==null || label.trim().equals("")){
            res.add(tool.msgCreate(400,"类别名不能为空"));
            return res;
        }else if(label.length()>20){
            res.add(tool.msgCreate(400,"类别名太长"));
            return res;
        }
        try {
            tree = new refTypeTree(teamId);
            if(tree.getTypeById(fid).getLevel()==5){
                res.add(tool.msgCreate(400,"层数太深,拒绝操作"));
                return res;
            }
            if(tree.checkTypeNameExists(label)){
                res.add(tool.msgCreate(400,"重复的类别名"));
                return res;
            }
            if(fid!=null && fid.equals(tree.getDefaultId())){
                res.add(tool.msgCreate(400,"禁止向默认分类添加分类,拒绝操作"));
                return res;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            res.add(tool.msgCreate(400,"团队标识错误"));
            return res;
        }
        try {
            tree.newType(fid,label);
        } catch (SQLException e) {
            e.printStackTrace();
            res.add(tool.msgCreate(400,"数据库处理错误"));
            return res;
        }

        res.add(tool.msgCreate(200,"success"));
        res.addAll(tree.formatToTree());
        return res;
    }

    @GetMapping("/teamInfo/CategoryEdit")
    public Map<String,Object> editTeamCategory(@RequestParam String teamId,
                                              @RequestParam String label,
                                              @RequestParam String id,
                                              HttpSession session){
        //权限管理
        Object userId = session.getAttribute("user");
        if(userId==null){
            return tool.msgCreate(401,"你未登录");
        }
        Pair<Boolean,Team> check = checkUserRole(userId.toString(),teamId);
        if(!check.getKey()){
            return tool.msgCreate(403,"你不是团队组长");
        }
        //

        refTypeTree tree = null;
        if(label==null || label.trim().equals("")){
            return tool.msgCreate(400,"类别名不能为空");
        }else if(label.length()>20){
            return tool.msgCreate(400,"类别名太长");
        }
        try {
            tree = new refTypeTree(teamId);
            if(tree.checkTypeNameExists(label)){
                return tool.msgCreate(400,"重复的类别名");
            }
            if(!tree.checkTypeIdExists(id)){
                return tool.msgCreate(400,"不存在的条目");
            }
            if(id!=null && id.equals(tree.getDefaultId())){
                return tool.msgCreate(400,"禁止重命名默认分类");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return tool.msgCreate(400,"团队标识错误");
        }
        try {
            assert id != null;
            tree.updateTypeName(id,label);
        } catch (SQLException e) {
            e.printStackTrace();
            return tool.msgCreate(400,"数据库处理错误");
        }
        return tool.msgCreate(200,"success");
    }

    @GetMapping("/teamInfo/CategoryDelete")
    public ArrayList<Map<String,Object>> deleteTeamCategory(@RequestParam String teamId,
                                                 @RequestParam String id,
                                                 HttpSession session){
        ArrayList<Map<String,Object>> res=new ArrayList<>();
        //权限管理
        Object userId = session.getAttribute("user");
        if(userId==null){
            res.add(tool.msgCreate(401,"你未登录"));
            return res;
        }
        Pair<Boolean,Team> check = checkUserRole(userId.toString(),teamId);
        if(!check.getKey()){
            res.add(tool.msgCreate(403,"你不是团队组长"));
            return res;
        }
        //

        refTypeTree tree = null;
        try {
            tree = new refTypeTree(teamId);
            if(id!=null && id.equals(tree.getDefaultId())){
                res.add(tool.msgCreate(400,"禁止删除默认分类"));
                return res;
            }else if(id==null){
                res.add(tool.msgCreate(400,"无效ID"));
                return res;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            res.add(tool.msgCreate(400,"团队标识错误"));
            return res;
        }
        try {
            tree.deleteType(id);
        } catch (SQLException e) {
            e.printStackTrace();
            res.add(tool.msgCreate(400,"数据库处理错误"));
            return res;
        }
        res.add(tool.msgCreate(200,"success"));
        return res;
    }

    @GetMapping("/teamInfo/CategoryDrag")
    public Map<String,Object> dragTeamCategory(@RequestParam String fromId,
                                               @RequestParam String toId,
                                               @RequestParam String type,
                                               @RequestParam String teamId,
                                               HttpSession session){
        System.out.println(fromId);
        System.out.println(toId);
        System.out.println(type);
        //权限管理
        Object userId = session.getAttribute("user");
        if(userId==null){
            return tool.msgCreate(401,"你未登录");
        }
        Pair<Boolean,Team> check = checkUserRole(userId.toString(),teamId);
        if(!check.getKey()){
            return tool.msgCreate(403,"你不是团队组长");
        }
        //

        if(fromId==null || fromId.trim().length()==0){
            return tool.msgCreate(400,"无效参数");
        }
        if(toId==null || toId.trim().length()==0){
            return tool.msgCreate(400,"无效参数");
        }
        refTypeTree tree;
        try {
            tree = new refTypeTree(teamId);
        } catch (SQLException e) {
            e.printStackTrace();
            return tool.msgCreate(400,"团队标识错误");
        }

        if((type.equals("inner") || type.equals("prev")) && toId.equals(tree.getDefaultId())){
            return tool.msgCreate(400,"不能移动到默认分类的里面或前面，拒绝操作");
        }
        if(fromId.equals(tree.getDefaultId())){
            return tool.msgCreate(400,"不能移动默认分类，拒绝操作");
        }
        if(tree.getTypeById(toId).isBan()){
            return tool.msgCreate(400,"先处理默认分类内的分类，拒绝操作");
        }

        String fid = tree.getTypeById(toId).getFid();
        try {
            if(type.equals("prev") || type.equals("after")) {
                tree.updateTypeFid(fromId, fid);
            }else{
                int toLevel = tree.getTypeById(toId).getLevel();
                int fromHeight = tree.getTypeHeight(fromId);
                if(fromHeight+toLevel>5){
                    return tool.msgCreate(400,"层数太深，拒绝操作");
                }
                tree.updateTypeFid(fromId,toId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return tool.msgCreate(400,"处理失败");
        }
        return tool.msgCreate(200,"success");
    }

    @GetMapping("/teamMember")
    public List<Map<String,Object>> getTeamMember(@RequestParam String teamId,HttpSession session){
        Team oneTeam = new Team(teamId);
        List<Map<String,Object>> res = new ArrayList<>();
        if(oneTeam.isNull()){
            res.add(tool.msgCreate(400,"not find the team"));
            return res;
        }else{
            res = oneTeam.getMemberList();
            res.add(0,tool.msgCreate(200,"success"));
            return res;
        }
    }

    @GetMapping("/teamInfo/CheckRole")
    public Map<String,Object> checkTeamRole(@RequestParam String teamId,HttpSession session){
        Object user = session.getAttribute("user");
        if(user==null) return tool.msgCreate(400,"未登录");
        String userId = user.toString();
        Pair<Boolean,Team> check = checkUserRole(userId,teamId);
        if(check.getKey()) return tool.msgCreate(200,"你是团队组长");
        return tool.msgCreate(400,"你不是团队组长");
    }
    private final Pair<Boolean,Team> checkUserRole(String userId, String teamId){
        Team team = new Team(teamId);
        if(team.isNull()) return new Pair<>(false,null);
        String leaderId = team.getLeaderId();
        if(userId.equals(leaderId)) return new Pair<>(true,team);
        return new Pair<>(false,null);
    }
}
