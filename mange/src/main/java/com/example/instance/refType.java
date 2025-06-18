package com.example.instance;

import java.util.ArrayList;

public class refType {
    private String id;
    private String name;

    public void setFid(String fid) {
        this.fid = fid;
    }

    private String fid;

    private int level;
    private boolean isBan;

    private ArrayList<String> sonIdList;

    public refType(String id,String name,String fid,int level,boolean isBan){
        this.id = id;
        this.name = name;
        this.fid = fid;
        this.level = level;
        this.isBan = isBan;
        sonIdList = new ArrayList<>();
    }

    public String getId() {
        return id;
    }
    public void sonIdListput(String sonId){
        this.sonIdList.add(sonId);
    }
    public ArrayList<String> getSonIdList(){
        return sonIdList;
    }

    public int getLevel() {
        return level;
    }

    public String getName() {
        return name;
    }

    public String getFid() {
        return fid;
    }

    public boolean isBan() {
        return isBan;
    }
}
