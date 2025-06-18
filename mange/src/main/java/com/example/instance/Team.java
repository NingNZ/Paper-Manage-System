package com.example.instance;

import com.example.util.*;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Team {
    String id;
    String name;

    public String getLeaderId() {
        return leaderId;
    }

    String leaderId;
    String leaderName;

    String defaultTypeId;
    int count;
    ArrayList<Pair<User,String>> member;
    public Team(String id){
        String sql1 = "select team.id,team.name,team.leaderId,team.count,users.name as leaderName " +
                "from users,(select id,name,leaderId,count from team where id=?) as team where team.leaderId=users.id;";
        try{
            ResultSetWrapper res = sqlUtil.query(sql1,id);
            if(!res.isBeforeFirst()){
                this.id = null;
                this.name = null;
                this.leaderId = null;
                this.leaderName = null;
                this.count = 0;
                this.member = null;
                this.defaultTypeId = null;
                return;
            }
            res.next();
            this.id = id;
            this.name = res.getString("name");
            this.leaderId = res.getString("leaderId");
            this.leaderName = res.getString("leaderName");
            this.count = Integer.parseInt(res.getString("count"));
            this.defaultTypeId = Team.getDefaultTypeIdDB(id);
            member = new ArrayList<>();
        }catch(SQLException e){
            this.id = null;
            this.name = null;
            this.leaderId = null;
            this.leaderName = null;
            this.count = 0;
            this.member = null;
            this.defaultTypeId = null;
            return;
        }
        String sql2 = "select userId,identity from teammember where teamId=? order by identity desc,userId ";
        try{
            ResultSetWrapper res = sqlUtil.query(sql2,id);
            while (res.next()){
                String userId = res.getString("userId");
                User oneMember = new User(userId);
                String identity = res.getString("identity");
                if(oneMember.isNull()){
                    continue;
                }else{
                    this.member.add(new Pair<User,String>(oneMember,identity));
                }
            }
        }catch (SQLException e){
            this.member = null;
        }
    }

    public static int checkTeamExist (String name){
        String sql1 = "select id,name from team where name=?";
        try{
            ResultSetWrapper res = sqlUtil.query(sql1,name);
            if(!res.isBeforeFirst()){
                return 400;
            }
            return 200;
        }catch(SQLException e){
            return 404;
        }
    }
    public static int checkTeamExistById (String id){
        String sql1 = "select id,name from team where id=?";
        try{
            ResultSetWrapper res = sqlUtil.query(sql1,id);
            if(!res.isBeforeFirst()){
                return 400;
            }
            return 200;
        }catch(SQLException e){
            return 404;
        }
    }
    public boolean isNull(){
        return this.id==null;
    }
    public List<Map<String,Object>> getMemberList(){
        if(this.isNull()){
            return null;
        }else{
            ArrayList<Map<String,Object>> res = new ArrayList<>();
            for(Pair<User,String> oneMember : member){
                Map<String,Object> row = oneMember.getKey().tranferToMap();
                if(row==null) continue;
                row.put("role",oneMember.getValue());
                res.add(row);
            }
            return res;
        }
    }
    public Map<String,Object> getInfo(){
        if(this.isNull()) return null;
        Map<String,Object> row = new HashMap<>();
        row.put("name",this.name);
        row.put("id",this.id);
        row.put("leaderName",this.leaderName);
        row.put("leaderId",this.leaderId);
        return row;
    }
    public static Team insertTeam(String name,String uid){
        String sql1 = "insert into team (id,name,leaderId,count) values(?,?,?,0)";
        String id = "#t"+ tool.generateHash(name,10);
        try {
            sqlUtil.update(sql1,id,name,uid);
            Team thisTeam = new Team(id);
            boolean addRes = thisTeam.addMember(uid,"组长");
            String sql2 = "insert into teamRefType(id,teamId,name,fid,level) values(?,?,?,?,?)";
            String typeId = tool.generateHash(id+"默认分类",12);
            sqlUtil.update(sql2,typeId,id,"默认分类",null,0);
            File folder = new File("D:/code/idea_java/mange/teamRefPapers/"+id);
            if(folder.mkdir()){
                System.out.println("创建文件夹:"+folder.getAbsolutePath());
                return thisTeam.update();
            }else{
                return null;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }
    public boolean addMember(String uid,String identity){
        String sql1 = "insert into teammember (teamId,userId,identity) values (?,?,?)";
        try{
            int res = sqlUtil.update(sql1,this.id,uid,identity);
            if(res==1) return true;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
    /**
    @return     390:删除组长<br>
                200:删除成功<br>
                400:删除失败
    */
    public int dropMember(String uid){
        if(this.leaderId.equals(uid)){
            return 390;
        }
        String sql1 = "delete from teammember where teamId=? and userId=?";
        try {
            int res = sqlUtil.update(sql1,this.id,uid);
            if(res==1) return 200;
            return 400;
        } catch (SQLException e) {
            e.printStackTrace();
            return 400;
        }
    }
    public boolean findMember(String uid){
        for(Pair<User,String> each:this.member){
            User oneUser = each.getKey();
            if(oneUser.equalToUserId(uid)) return true;
        }
        return false;
    }
    public static String getDefaultTypeIdDB(String teamId) throws SQLException {
        String sql1 = "select id from teamRefType where teamId = ? and level = 0";
        ResultSetWrapper res = sqlUtil.query(sql1,teamId);
        if(!res.next()) return null;
        return res.getString("id");
    }
    public Team update(){
        return new Team(this.id);
    }
}
