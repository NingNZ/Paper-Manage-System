package com.example.util;

import org.springframework.util.DigestUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class tool {
    public static Map<String,Object> msgCreate(int code, String msg){
        Map<String, Object> row = new HashMap<>();
        row.put("code",code);
        row.put("msg",msg);
        return row;
    }
    public static boolean checkFileExist(String filepath){
        Path path = Paths.get(filepath);
        return Files.exists(path);
    }



}
