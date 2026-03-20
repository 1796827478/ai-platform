package com.nariit.oms.ai.admin;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class InitDb {
    public static void main(String[] args) {
        String url = "jdbc:mysql://127.0.0.1:3306/?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection conn = DriverManager.getConnection(url, "root", "123456");
                 Statement stmt = conn.createStatement()) {
                 
                stmt.execute("CREATE DATABASE IF NOT EXISTS ai_platform CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci");
                stmt.execute("USE ai_platform");
                
                String sqlContent = new String(Files.readAllBytes(Paths.get("D:/studyWorkSpace/ai-platform/db_init.sql")));
                String[] statements = sqlContent.split(";");
                for (String s : statements) {
                    if (!s.trim().isEmpty() && !s.trim().startsWith("--")) {
                        stmt.execute(s);
                    }
                }
                System.out.println("====== [DATABASE INITIALIZED SUCCESSFULLY] ======");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
