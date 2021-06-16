package com.mycompany.library_project;

import java.sql.*;

import com.mycompany.library_project.config.CreateLogFile;

public class MyConnection {

    public static String server = "", userName = "", password = "";

    public static Connection getConnect() {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            return DriverManager.getConnection(
                    "jdbc:mariadb://" + server + "/dblibrary?useUnicode=true&characterEncoding=UTF-8", userName,
                    password);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            CreateLogFile log = new CreateLogFile();
            log.createLogFile("Connection Error", e);
            return null;
        }
    }
}
