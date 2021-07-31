package com.mycompany.library_project;

import java.sql.*;

import com.mycompany.library_project.ControllerDAOModel.DialogMessage;

public class MyConnection {

    public static String driver = "", dbtype = "", host = "", userName = "", password = "";

    public static Connection getConnect() {
        try {
            if (driver == null || dbtype == "" || dbtype == null || dbtype == "")
                return null;

            Class.forName(driver);
            return DriverManager.getConnection("jdbc:" + dbtype + "://" + host
                    + "/dblibrary?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&autoReconnectForPools=true",
                    userName, password);
        } catch (ClassNotFoundException | SQLException e) {
            DialogMessage dialog = new DialogMessage();
            dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການເຊື່ອມຕໍ່ຖານຂໍ້ມູນ", e);
            return null;
        }
    }
}
