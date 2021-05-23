package com.mycompany.library_project.Model;

import com.jfoenix.controls.JFXButton;
import com.mycompany.library_project.MyConnection;
import com.mycompany.library_project.ControllerDAOModel.DataAccessObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TableLogModel implements DataAccessObject {

    private PreparedStatement ps = null;
    private Connection con = MyConnection.getConnect();
    private ResultSet rs = null;
    private String query = "";

    private String tableId;
    private String tableLog;
    private int logQty;
    private String newLog;
    private JFXButton action;

    public TableLogModel() {
    }

    public TableLogModel(String tableLog) {
        this.tableLog = tableLog;
    }

    public TableLogModel(String tableId, String tableLog) {
        this.tableId = tableId;
        this.tableLog = tableLog;
    }

    public TableLogModel(String tableId, int qty) {
        this.tableId = tableId;
        this.logQty = qty;
    }

    public TableLogModel(String tableId, int logQty, JFXButton action) {
        this.tableId = tableId;
        this.logQty = logQty;
        this.action = action;
    }

    public TableLogModel(String tableId, String tableLog, String newLog) {
        this.tableId = tableId;
        this.tableLog = tableLog;
        this.newLog = newLog;
    }

    public TableLogModel(String tableId, String tableLog, Integer qty) {
        this.tableId = tableId;
        this.tableLog = tableLog;
        this.logQty = qty;
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public String getTableLog() {
        return tableLog;
    }

    public void setTableLog(String tableLog) {
        this.tableLog = tableLog;
    }

    public int getLogQty() {
        return logQty;
    }

    public void setLogQty(int logQty) {
        this.logQty = logQty;
    }

    public String getNewLog() {
        return newLog;
    }

    public void setNewLog(String newLog) {
        this.newLog = newLog;
    }

    public JFXButton getAction() {
        return action;
    }

    public void setAction(JFXButton action) {
        this.action = action;
    }

    @Override
    public ResultSet findAll() throws SQLException {
        query = "call  table_Show();";
        rs = con.createStatement().executeQuery(query);
        return rs;
    }

    @Override
    public ResultSet findById(String id) throws SQLException {
        query = "call  tablelog_ShowById('" + id + "');";
        rs = con.createStatement().executeQuery(query);
        return rs;
    }

    @Override
    public ResultSet findByName(String name) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ResultSet searchData(String values) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    public int saveTable(String id, int qty) {
        try {
            query = "call  table_Insert(?, ?);";
            ps = con.prepareStatement(query);
            ps.setString(1, id);
            ps.setInt(2, qty);
            return ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public int saveData() throws SQLException {
        query = "call  tablelog_Insert(?, ?) ";
        ps = con.prepareStatement(query);
        ps.setString(1, getTableId());
        ps.setString(2, getTableLog());
        return ps.executeUpdate();
    }

    @Override
    public int updateData() throws SQLException {
        query = "call  table_Update(?, ?) ";
        ps = con.prepareStatement(query);
        ps.setString(1, getTableId());
        ps.setInt(2, getLogQty());
        return ps.executeUpdate();
    }

    public int updateTableQty(String id) throws SQLException {
        query = "call table_UpdateQty(?);";
        ps = con.prepareStatement(query);
        ps.setString(1, id);
        return ps.executeUpdate();
    }

    @Override
    public int deleteData(String id) throws SQLException {
        query = "call tablelog_Delete(?);";
        ps = con.prepareStatement(query);
        ps.setString(1, id);
        return ps.executeUpdate();
    }

    public int deleteTable(String id) throws SQLException {
        query = "call table_Delete(?);";
        ps = con.prepareStatement(query);
        ps.setString(1, id);
        return ps.executeUpdate();
    }
}
