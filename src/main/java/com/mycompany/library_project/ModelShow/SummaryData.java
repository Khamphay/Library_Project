package com.mycompany.library_project.ModelShow;

import java.sql.*;

import com.mycompany.library_project.MyConnection;
import com.mycompany.library_project.Controller.HomeController;

public class SummaryData extends Thread {
    private String sql;
    private ResultSet rs = null;
    private Connection con = MyConnection.getConnect();

    public String summaryValue = null;

    public SummaryData(String sql) {
        this.sql = sql;
    }

    @Override
    public void run() {
        try {
            rs = con.createStatement().executeQuery(sql);
            if (rs.next()) {
                HomeController.summaryValue = rs.getString(1);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
}
