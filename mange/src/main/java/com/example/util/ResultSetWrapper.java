package com.example.util;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResultSetWrapper {
    List<Map<String,String>> dataList;
    List<String> columnLabelList;
    boolean isAvailable;
    int index;
    int length;
    public ResultSetWrapper(){
        dataList = new ArrayList<>();
        columnLabelList = new ArrayList<>();
        isAvailable = false;
        index = -1;
        length=0;
    }
    public ResultSetWrapper(ResultSet resultSet) throws SQLException {
        this();
        ResultSetMetaData metaData = resultSet.getMetaData();
        for(int i=1;i<=metaData.getColumnCount();i++){
            String columnLabel = metaData.getColumnLabel(i);
            if(columnLabel!=null) {
                columnLabelList.add(columnLabel);
            }
        }
        while(resultSet.next()){
            Map<String, String> row = new HashMap<>();
            for (String s : columnLabelList) {
                row.put(s, resultSet.getString(s));
            }
            dataList.add(row);
            length++;
        }
    }
    public boolean isBeforeFirst(){
        return length>0;
    }
    public boolean next(){
        index++;
        return index<length;
    }
<<<<<<< HEAD
=======

>>>>>>> 1e8193b (第二次迭代后端)
    public String getString(String label){
        if(index>=length) return null;
        return dataList.get(index).get(label);
    }
}
