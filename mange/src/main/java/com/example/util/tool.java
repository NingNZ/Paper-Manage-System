package com.example.util;

import java.util.HashMap;
import java.util.Map;

public class tool {
    public static Map<String,Object> msgCreate(int code, String msg){
        Map<String, Object> row = new HashMap<>();
        row.put("code",code);
        row.put("msg",msg);
        return row;
    }
}
