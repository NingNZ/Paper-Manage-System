package com.example.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@Configuration
public class DatabaseInitializer {

    private static final String URL = "jdbc:mysql://localhost:3306/manage";
    private static final String USERNAME = "root";

    private static final String PASSWORD = "123520";
    @Bean
    public CommandLineRunner initializeDatabase() {
        return args -> {
            try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                 Statement stmt = conn.createStatement()) {
                // Execute the SQL statement
                stmt.execute("SET GLOBAL max_sp_recursion_depth = 30;");
                System.out.println("Global max_sp_recursion_depth set to 30.");
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException("Failed to initialize database settings", e);
            }
        };
    }
}
