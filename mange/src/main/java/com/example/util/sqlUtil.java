package com.example.util;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class sqlUtil {
    private static final String URL = "jdbc:mysql://localhost:3306/manage";
    private static final String USERNAME = "soft";
    private static final String PASSWORD = "123456";

    public static ResultSetWrapper query(String sql, Object... params) throws SQLException {
        ResultSetWrapper wapper = null;
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            // 设置参数
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }
            wapper = new ResultSetWrapper(stmt.executeQuery());
            return wapper;
        }
    }
        public static int update (String sql, Object...params) throws SQLException {
            int res = -1;
            try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                // 设置参数
                for (int i = 0; i < params.length; i++) {
                    stmt.setObject(i + 1, params[i]);
                }
                res = stmt.executeUpdate();
            }
            return res;
        }
    }
