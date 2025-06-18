package com.example.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class test {
    @RequestMapping("/test")
    public String linkTest(){
<<<<<<< HEAD
        return "hello";
=======
        return "hello world";
>>>>>>> a1161c0 (第二次迭代完成)
    }
}
