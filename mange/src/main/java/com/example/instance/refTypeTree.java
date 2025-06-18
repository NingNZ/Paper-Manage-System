package com.example.instance;

import java.sql.RowId;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import com.example.util.ResultSetWrapper;
import com.example.util.sqlUtil;
import com.example.util.tool;

public class refTypeTree {
    private String teamId;
    private HashMap<String,refType> typeMap;

    public String getDefaultId() {
        return defaultId;
    }

    private String defaultId;

    public refTypeTree(String teamId) throws SQLException {
        if(Team.checkTeamExistById(teamId)!=200){
            defaultId=null;
            return ;
        }
        this.teamId=teamId;
        String sql1 = "select id,name,fid,level,isBan from teamRefType where teamId = ? order by level,name";
        try {
            this.typeMap = new HashMap<>();
            typeMap.put(null,new refType(null,null,null,0,false));
            ResultSetWrapper res = sqlUtil.query(sql1,teamId);
            while (res.next()){
                String id = res.getString("id");
                String name = res.getString("name");
                String fid = res.getString("fid");
                int level = Integer.parseInt(res.getString("level"));
                boolean isBan = res.getString("isBan").equals("1");
                refType type = new refType(id,name,fid,level,isBan);
                typeMap.put(type.getId(),type);
                typeMap.get(fid).sonIdListput(id);
            }
            defaultId = Team.getDefaultTypeIdDB(teamId);
        } catch (SQLException e) {
            defaultId = null;
            throw new SQLException(e);
        }
    }

    public ArrayList<Map<String,Object>> formatToTree(){
        if(this.isNull()) return null;
        @SuppressWarnings("unchecked")
        ArrayList<Map<String,Object>> res =(ArrayList<Map<String,Object>>) DFSearch(typeMap.get(null)).get("children");
        return res;
    }
    public boolean isNull(){
        return defaultId==null;
    }

    public boolean checkTypeNameExists(String label){
        for(Map.Entry<String,refType> entry: this.typeMap.entrySet()){
            if(entry.getKey()==null) continue;
            if(entry.getValue().getName().equals(label)) return true;
        }
        return false;
        //String id = tool.generateHash(teamId+label,12);
        //return this.typeMap.containsKey(id);
    }
    public boolean checkTypeIdExists(String id){
        if(id==null) return  false;
        return typeMap.containsKey(id);
    }
    public void newType(String fid,String label) throws SQLException {
        if(fid!=null && fid.equals(defaultId)) return;
        String id = tool.generateHash(teamId+label,12);
        int farSeq = typeMap.get(fid).getLevel();
        String sql1 = "insert into teamRefType (id,teamId,name,fid,level) " +
                "values (?,?,?,?,?)";
        int res = sqlUtil.update(sql1,id,teamId,label,fid,farSeq+1);
        typeMap.put(id,new refType(id,label,fid,farSeq+1,false));
        typeMap.get(fid).sonIdListput(id);
        return;
    }

    public void deleteType(String id) throws SQLException {
        if(id.equals(defaultId)) return;
        String sql1 = "select id from teamRefType where fid = ?";
        ResultSetWrapper res = sqlUtil.query(sql1,id);
        while (res.next()){
            String thisId = res.getString("id");
            updateTypeFid(thisId,defaultId);
        }
        String sql2 = "update teamRefPaper set typeId = ? where typeId = ?";
        sqlUtil.update(sql2,defaultId,id);
        //还要修改参考论文的类别
        String sql3 = "delete from teamRefType where id = ?";
        sqlUtil.update(sql3,id);
        return;
    }
    public void updateTypeName(String id,String label) throws SQLException {
        if(id.equals(defaultId)) return;
        String sql1 = "update teamRefType set name=? where id=?";
        sqlUtil.update(sql1,label,id);
        return;
    }
    public void updateTypeFid(String id,String fid) throws SQLException {
        if(id.equals(defaultId)) return;
        int level = typeMap.get(fid).getLevel()+1;
        boolean isBan = false;
        if(fid!=null && fid.equals(defaultId)) isBan = true;
        String sql1 = "update teamRefType set fid=?, isBan= ?, level = ? where teamId=? and id=?";
        sqlUtil.update(sql1,fid,isBan,level,teamId,id);
        String sql2 = "{CALL update_all_children_level(?, ?)}";
        sqlUtil.excute(sql2,id,level);
        String sql3 = "{CALL sync_children_isBan(?,?)}";
        sqlUtil.excute(sql3,id,isBan);
        return;
    }
    private HashMap<String,Object> DFSearch(refType now){
        HashMap<String,Object> res = new HashMap<>();
        if(now.getId()!=null){
            if(now.isBan()){
                res.put("isBan","true");
            }
            if(now.getId().equals(defaultId)){
                res.put("isDefault","true");
            }
            res.put("id",now.getId());
            res.put("label",now.getName());
            res.put("level",now.getLevel());
        }
        if(!now.getSonIdList().isEmpty()){
            ArrayList<HashMap<String,Object>> children = new ArrayList<>();
            for(String cid : now.getSonIdList()){
                refType cType = typeMap.get(cid);
                children.add(DFSearch(cType));
            }
            res.put("children",children);
        }
        return res;
    }
    public refType getTypeById(String Id){
        return typeMap.get(Id);
    }
    public int getTypeHeight(String id){
        refType type = typeMap.get(id);
        int maxHeight = 1;
        for(String sid: type.getSonIdList()){
            maxHeight = Math.max(maxHeight,getTypeHeight(sid)+1);
        }
        return maxHeight;
    }
}
