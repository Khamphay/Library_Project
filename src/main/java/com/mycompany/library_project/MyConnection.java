package com.mycompany.library_project;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;

public class MyConnection {
    public static Connection getConnect(){
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            return DriverManager.getConnection("jdbc:mariadb://localhost:3307/db_libary","root","1234");
        }catch (Exception e){
            JOptionPane.showMessageDialog(null,"Error: "+e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
