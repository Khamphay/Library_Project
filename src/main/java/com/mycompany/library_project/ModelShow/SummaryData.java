package com.mycompany.library_project.ModelShow;

import java.sql.*;

import com.mycompany.library_project.Controller.HomeController;

public class SummaryData extends Thread {
    private String sql;
    private ResultSet rs = null;
    // private Connection con = MyConnection.getConnect();

    public SummaryData() {
    }

    @Override
    public void run() {
        try {
            sql = "call getAllBookData();";
            rs = HomeController.con.createStatement().executeQuery(sql);
            if (rs.next()) {
                HomeController.summaryValue[0] = rs.getString("@type");
                HomeController.summaryValue[1] = rs.getString("@category");
                HomeController.summaryValue[2] = rs.getString("@books");
                HomeController.summaryValue[3] = rs.getString("@booklost");
                HomeController.summaryValue[4] = rs.getString("@tables");
                HomeController.summaryValue[5] = rs.getString("@tablelog");
            }
            sql = "call getAllPersonData();";
            rs = HomeController.con.createStatement().executeQuery(sql);
            if (rs.next()) {
                    HomeController.summaryValue[6] = rs.getString("@member");
                    HomeController.summaryValue[7] = rs.getString("@employee");
                    HomeController.summaryValue[8] = rs.getString("@author");
                    HomeController.summaryValue[9] = rs.getString("@department");
                    HomeController.summaryValue[10] = rs.getString("@supplier");
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
