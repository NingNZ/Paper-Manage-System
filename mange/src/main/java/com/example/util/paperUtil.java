package com.example.util;

import com.mysql.cj.xdevapi.SqlResult;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class paperUtil {
    public static ArrayList<String> searchByTitleLike(String title) throws SQLException {
        StringBuilder keyBuild = new StringBuilder(title);
        for(int i=1;i<keyBuild.length();i++){
            if(keyBuild.charAt(i)==' ' && keyBuild.charAt(i-1)==' '){
                keyBuild.deleteCharAt(i-1);
                i--;
            }
            else if(keyBuild.charAt(i)!=' ' && keyBuild.charAt(i-1)==' '){
                keyBuild.setCharAt(i-1,'%');
            }
        }
        keyBuild.insert(0,'%').append('%');
        title = keyBuild.toString();
        String sql1 = "select id from syspaper where title like ?";
        ArrayList<String> paperIdList = new ArrayList<>();
        ResultSetWrapper res = sqlUtil.query(sql1,title);
        while(res.next()){
            paperIdList.add(res.getString("id"));
        }
        return paperIdList;
    }
    public static ArrayList<String> searchByTitleMatch(String title) throws SQLException{
        String sql1 = "select id from syspaper where title like ?";
        ArrayList<String> paperIdList = new ArrayList<>();
        ResultSetWrapper res = sqlUtil.query(sql1,title);
        while(res.next()){
            paperIdList.add(res.getString("id"));
        }
        return paperIdList;
    }
    public static ArrayList<String> searchByAuthors(String name) throws SQLException{
        String sql1 = "select paperId as id from authors,users where authors.userId=users.id and users.name like ?";
        ArrayList<String> paperIdList = new ArrayList<>();
        ResultSetWrapper res = sqlUtil.query(sql1,name);
        while(res.next()){
            paperIdList.add(res.getString("id"));
        }
        return paperIdList;
    }
    public static ArrayList<String> searchByJournal(String journal) throws SQLException{
        String sql1 = "select syspaper.id from syspaper,sysjournal " +
                "where syspaper.jourid = sysjournal.id and sysjournal.name like ? ";
        ArrayList<String> paperIdList = new ArrayList<>();
        ResultSetWrapper res = sqlUtil.query(sql1,journal);
        while(res.next()){
            paperIdList.add(res.getString("id"));
        }
        return paperIdList;
    }
    public static ArrayList<String> searchByType(String type) throws SQLException{
        String sql1 = "select syspaper.id from syspaper,systype " +
                "where syspaper.typeid = systype.id and systype.name like ? ";
        ArrayList<String> paperIdList = new ArrayList<>();
        ResultSetWrapper res = sqlUtil.query(sql1,type);
        while(res.next()){
            paperIdList.add(res.getString("id"));
        }
        return paperIdList;
    }
}
