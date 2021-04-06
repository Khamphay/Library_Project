package com.mycompany.library_project.ModelShow;

import java.sql.*;

import com.mycompany.library_project.MyConnection;
import com.mycompany.library_project.Controller.HomeController;

public class SummaryData extends Thread {
    private String sql, name;
    private ResultSet rs = null;
    private Connection con = MyConnection.getConnect();

    public SummaryData(String sql, String name) {
        this.sql = sql;
        this.name = name;
    }

    @Override
    public void run() {
        try {
            rs = con.createStatement().executeQuery(sql);
            if (rs.next()) {
                switch (name) {

                case "ThreadingType":
                    HomeController.summaryValue[0] = rs.getString(1);
                    break;
                case "ThreadingCatg":
                    HomeController.summaryValue[1] = rs.getString(1);
                    break;
                case "ThreadingBook":
                    HomeController.summaryValue[2] = rs.getString(1);
                    break;
                case "ThreadingBookLost":
                    HomeController.summaryValue[3] = rs.getString(1);
                    break;
                case "ThreadingTableLog":
                    HomeController.summaryValue[4] = rs.getString(1);
                    HomeController.summaryValue[5] = rs.getString(2);
                    break;
                case "ThreadingMember":
                    HomeController.summaryValue[6] = rs.getString(1);
                    break;
                case "ThreadingEmployee":
                    HomeController.summaryValue[7] = rs.getString(1);
                    break;
                case "ThreadingAuthor":
                    HomeController.summaryValue[8] = rs.getString(1);
                    break;
                case "ThreadingDep":
                    HomeController.summaryValue[9] = rs.getString(1);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
