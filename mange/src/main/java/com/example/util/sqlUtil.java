package com.example.util;
import java.sql.*;

public class sqlUtil {
    private static final String URL = "jdbc:mysql://localhost:3306/manage";
    private static final String USERNAME = "soft";
    private static final String PASSWORD = "123456";

    public static ResultSet query(String sql, Object... params) throws SQLException {
        Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        PreparedStatement stmt = conn.prepareStatement(sql);

        // 设置参数
        for (int i = 0; i < params.length; i++) {
            stmt.setObject(i + 1, params[i]);
        }
        return stmt.executeQuery();
    }
    public static  int update (String sql, Object... params) throws SQLException {
        Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        PreparedStatement stmt = conn.prepareStatement(sql);

        // 设置参数
        for (int i = 0; i < params.length; i++) {
            stmt.setObject(i + 1, params[i]);
        }
        return stmt.executeUpdate();
    }
}
