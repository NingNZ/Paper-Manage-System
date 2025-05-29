package com.example.controller;

import com.example.instance.Paper;
import com.example.util.paperUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class sysPaperControl {
    @PostMapping("/sysPaper/new")
    public void solveNewSysPaper(
            @RequestParam("title") String title,
            @RequestParam("authors") String authorsJson, // 接收 JSON 字符串
            @RequestParam("type") String typeId,
            @RequestParam("journal") String journalId,
            @RequestParam("date") String date,
            @RequestParam("teamId") String teamId,
            @RequestParam("file") MultipartFile file
    ) {
        System.out.println("enter");
        System.out.println(authorsJson);
        ObjectMapper mapper = new ObjectMapper();
        String[] authors = new String[0];
        try {
            authors = mapper.readValue(authorsJson,String[].class);
            String id = Paper.insertToSysPaper(title, journalId, authors, typeId, teamId, date);
            paperUtil.fileLocalSave(id,file);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }




        return;
    }
}
