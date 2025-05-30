package com.example.controller;

import com.example.instance.JourList;
import com.example.instance.TypeList;
import com.example.util.tool;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.Map;

@RestController
public class sysTypeJourControl {
    //SysType
    @GetMapping("/sysType/new")
    public Map<String,Object> sqlInsertType(@RequestParam String name){
        TypeList typeList = null;
        try {
            typeList = new TypeList(true,"");
            boolean insertRes = typeList.insertType(name);
            if(!insertRes){
                return tool.msgCreate(400,"添加错误");
            }
            return tool.msgCreate(200,"success");
        } catch (SQLException e) {
            e.printStackTrace();
            return tool.msgCreate(400,"系统错误");
        }
    }
    @GetMapping("/sysType/update")
    public Map<String,Object> sqlUpdateType(@RequestParam String name,@RequestParam String id){
        TypeList typeList = null;
        try {
            typeList = new TypeList(true,"");
            boolean updateRes = typeList.updateType(name,id);
            if(!updateRes){
                return tool.msgCreate(400,"修改错误");
            }
            return tool.msgCreate(200,"success");
        } catch (SQLException e) {
            e.printStackTrace();
            return tool.msgCreate(400,"系统错误");
        }
    }
    //Journal
    @GetMapping("/sysJournal/new")
    public Map<String,Object> sqlInsertJournal(@RequestParam String name){
        JourList jourList = null;
        try {
            jourList = new JourList();
            boolean insertRes = jourList.insertJournal(name);
            if(!insertRes){
                return tool.msgCreate(400,"添加错误");
            }
            return tool.msgCreate(200,"success");
        } catch (SQLException e) {
            e.printStackTrace();
            return tool.msgCreate(400,"系统错误");
        }
    }
    @GetMapping("/sysJournal/update")
    public Map<String,Object> sqlUpdateJournal(@RequestParam String name,@RequestParam String id){
        JourList jourList = null;
        try {
            jourList = new JourList();
            boolean updateRes = jourList.updateJournal(name,id);
            if(!updateRes){
                return tool.msgCreate(400,"修改错误");
            }
            return tool.msgCreate(200,"success");
        } catch (SQLException e) {
            e.printStackTrace();
            return tool.msgCreate(400,"系统错误");
        }
    }
}
