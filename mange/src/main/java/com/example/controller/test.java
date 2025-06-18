package com.example.controller;

import com.example.instance.paperMsg;
import com.example.instance.userMsg;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.HashMap;

@RestController
public class test {
    @RequestMapping("/test")
    public String linkTest(){
        return "hello world";
    }
}
