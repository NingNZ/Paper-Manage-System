package com.example.controller;
import com.example.instance.*;
import com.example.util.paperUtil;
import com.example.util.tool;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.*;
import java.util.regex.Pattern;

@RestController
public class searchResultControl {
    @GetMapping("/search")
    public List<Map<String,Object>> searchResult(@RequestParam String type,
                                          @RequestParam String key) {
        List<Map<String, Object>> resultList = new ArrayList<>();
        boolean isError = false;
        ArrayList<String> paperIdList = new ArrayList<>();
        key = key.trim();
        if(type.equals("0")){
            try {
                paperIdList = paperUtil.searchByTitleLike(key);
            } catch (SQLException e) {
                isError = true;
                resultList.add(tool.msgCreate(400,"SQL Error"));
            }

        }
        else if(type.equals("1")){
            try {
                paperIdList = paperUtil.searchByTitleMatch(key);
            } catch (SQLException e) {
                isError = true;
                resultList.add(tool.msgCreate(400,"SQL Error"));
            }

        }
        else if(type.equals("2")){
            try {
                paperIdList = paperUtil.searchByAuthors(key);
            } catch (SQLException e) {
                isError = true;
                resultList.add(tool.msgCreate(400,"SQL Error"));
            }

        }
        else if(type.equals("3")){
            try {
                paperIdList = paperUtil.searchByJournal(key);
            } catch (SQLException e) {
                isError = true;
                resultList.add(tool.msgCreate(400,"SQL Error"));
            }
        }
        else if(type.equals("4")){
            try {
                paperIdList = paperUtil.searchByType(key);
            } catch (SQLException e) {
                isError = true;
                resultList.add(tool.msgCreate(400,"SQL Error"));
            }
        }
        else{
            resultList.add(tool.msgCreate(401,"Input Error"));
            System.err.println("wrong type");
            return null;
        }
        if(!isError){
            if(paperIdList.isEmpty()){
                resultList.add(tool.msgCreate(201,"Result is NULL"));
            } else {
                resultList.add(tool.msgCreate(200,"Query Success"));
            }
            while (!paperIdList.isEmpty()){
                try {
                    Paper papFirst = new Paper(paperIdList.get(0));
                    resultList.add(papFirst.transferToMap());
                    paperIdList.remove(0);
                }catch (SQLException e){
                    resultList.clear();
                    resultList.add(tool.msgCreate(400,"SQL Error"));
                    return resultList;
                }

            }
        }
        return resultList;
    }
    @GetMapping("/type")
    public List<Map<String,Object>> getType(@RequestParam Map<String, String> allParams){
        boolean isSystem = allParams.get("isSystem").equals("true");
        String teamId = (allParams.get("teamId").trim().length()==0)?null:allParams.get("teamId").trim();
        List<Map<String,Object>> res = null;
        if(!isSystem && teamId == null){
            res = new ArrayList<>();
            res.add(tool.msgCreate(401,"params wrong"));
            return res;
        }
        try{
            TypeList list = new TypeList(isSystem,teamId);
            res = new ArrayList<>(list.getTypeList());
            res.add(0,tool.msgCreate(200,"success"));
        }catch(SQLException e){
            e.printStackTrace();
            res = new ArrayList<>();
            res.add(tool.msgCreate(400,"Sql Wrong"));
        }
        return res;
    }
    @GetMapping("/journal")
    public List<Map<String,Object>> getJournal() {
        List<Map<String,Object>> res = null;
        try {
            JourList list = new JourList();
            res = new ArrayList<>(list.getJourList());
            res.add(0,tool.msgCreate(200,"success"));

        }catch (SQLException e){
            res = new ArrayList<>();
            res.add(0,tool.msgCreate(400,"Sql Wrong"));
        }
        return res;
    }
}
