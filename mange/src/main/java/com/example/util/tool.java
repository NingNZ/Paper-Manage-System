package com.example.util;

import org.springframework.util.DigestUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
<<<<<<< HEAD
=======
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
>>>>>>> 1e8193b (第二次迭代后端)
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
<<<<<<< HEAD
=======
    public static String generateHash(String Name,int length) {
        if (Name == null) {
            return null;
        }
        StringBuilder fileNameBuilder = new StringBuilder(Name);
        while(fileNameBuilder.length()<6){
            fileNameBuilder.append(new Random().nextInt(40, 123));
        }
        Name = fileNameBuilder.toString();
        byte[] bid = DigestUtils.md5Digest(Name.getBytes());
        String res = Arrays.toString(bid).substring(2,2+length);
        res = res.replace(' ','&');
        return res.replace('.','%');
    }

    public static String getTimeYMD(){
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return currentDate.format(formatter);
    }
    public static double calculateScore(int num){
        return 10.0/num;
    }
>>>>>>> 1e8193b (第二次迭代后端)
}
