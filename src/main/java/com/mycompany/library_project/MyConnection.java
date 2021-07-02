package com.mycompany.library_project;

import java.sql.*;

import com.mycompany.library_project.ControllerDAOModel.DialogMessage;

public class MyConnection {

    public static String server = "", userName = "", password = "";

    public static Connection getConnect() {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            return DriverManager.getConnection(
                    "jdbc:mariadb://" + server
                            + "/dblibrary?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&autoReconnectForPools=true",
                    userName,
                    password);
        } catch (ClassNotFoundException | SQLException e) {
            DialogMessage dialog = new DialogMessage();
            dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການເຊື່ອມຕໍ່ຖານຂໍ້ມູນ", e);
            return null;
        }
    }
}
