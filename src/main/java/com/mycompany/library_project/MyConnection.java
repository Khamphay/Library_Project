package com.mycompany.library_project;

import java.sql.*;

public class MyConnection {

    public static String server = "", userName = "", password = "";

    public static Connection getConnect() {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            return DriverManager.getConnection("jdbc:mariadb://" + server + "/dblibrary", userName, password);
        } catch (Exception e) {
            return null;
        }
    }
}
