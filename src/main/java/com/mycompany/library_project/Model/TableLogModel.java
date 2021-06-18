package com.mycompany.library_project.Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.jfoenix.controls.JFXButton;
import com.mycompany.library_project.MyConnection;
import com.mycompany.library_project.ControllerDAOModel.AlertMessage;
import com.mycompany.library_project.ControllerDAOModel.DataAccessObject;
import com.mycompany.library_project.config.CreateLogFile;

import javafx.geometry.Pos;

public class TableLogModel implements DataAccessObject {

    private AlertMessage alertMessage = new AlertMessage();
    private CreateLogFile logfile = new CreateLogFile();
    private PreparedStatement ps = null;
    private Connection con = MyConnection.getConnect();
    private ResultSet rs = null;
    private String query = "";

    private String tableId;
    private String tableLog;
    private int logQty;
    private String qty;
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

    public TableLogModel(String tableId, String qty, JFXButton action) {
        this.tableId = tableId;
        this.qty = qty;
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

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
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
        try {
            query = "call table_Show();";
            rs = con.createStatement().executeQuery(query);
            return rs;
        } catch (SQLException e) {
            alertMessage.showErrorMessage("Load Data Error", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
            logfile.createLogFile("Load Table Log Error", e);
            return null;
        } finally {
            // con.close();
        }

    }

    @Override
    public ResultSet findById(String id) throws SQLException {
        try {
            query = "call  tablelog_ShowById('" + id + "');";
            rs = con.createStatement().executeQuery(query);
            return rs;
        } catch (SQLException e) {
            alertMessage.showErrorMessage("Load Data Error", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
            logfile.createLogFile("Load Table Log By Table ID Error", e);
            return null;
        } finally {
            // con.close();
        }
    }

    public String findTableId(String tableLogId) throws SQLException {
        try {
            query = "Select tableid From tbtablelog Where tablelog='" + tableLogId + "'";
            rs = con.createStatement().executeQuery(query);
            if (rs.next()) {
                return rs.getString("tableid");
            } else {
                return "";
            }
        } catch (SQLException e) {
            alertMessage.showErrorMessage("Load Data Error", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
            logfile.createLogFile("Load Table Log By ID Error", e);
            return "";
        } finally {
            // con.close();
        }
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

    public int saveTable(String id, int qty) throws SQLException {
        try {
            query = "call  table_Insert(?, ?);";
            ps = con.prepareStatement(query);
            ps.setString(1, id);
            ps.setInt(2, qty);
            return ps.executeUpdate();
        } catch (SQLException e) {
            alertMessage.showErrorMessage("Save Error", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
            logfile.createLogFile("Save Table Error", e);
            return 0;
        } finally {
            ps.close();
            // con.close();
        }
    }

    @Override
    public int saveData() throws SQLException {
        // Todo: Don't use try...catch'
        query = "call  tablelog_Insert(?, ?) ";
        ps = con.prepareStatement(query);
        ps.setString(1, getTableId());
        ps.setString(2, getTableLog());
        int result = ps.executeUpdate();
        ps.close();
        // con.close();
        return result;
    }

    @Override
    public int updateData() throws SQLException {
        try {
            query = "call  table_Update(?, ?) ";
            ps = con.prepareStatement(query);
            ps.setString(1, getTableId());
            ps.setInt(2, getLogQty());
            return ps.executeUpdate();
        } catch (SQLException e) {
            alertMessage.showErrorMessage("Update Error", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
            logfile.createLogFile("Update Table Error", e);
            return 0;
        } finally {
            ps.close();
            // con.close();
        }
    }

    public int updateTableQty(String id) throws SQLException {
        try {
            query = "call table_UpdateQty(?);";
            ps = con.prepareStatement(query);
            ps.setString(1, id);
            return ps.executeUpdate();
        } catch (SQLException e) {
            alertMessage.showWarningMessage("Delete", "Can update table qty. Error: " + e.getMessage(), 4,
                    Pos.BOTTOM_RIGHT);
            logfile.createLogFile("Update Table Qty Error", e);
            return 0;
        } finally {
            ps.close();
            // con.close();
        }
    }

    @Override
    public int deleteData(String id) throws SQLException {
        try {
            query = "call tablelog_Delete(?);";
            ps = con.prepareStatement(query);
            ps.setString(1, id);
            return ps.executeUpdate();
        } catch (SQLException e) {
            alertMessage.showErrorMessage("Delete Error", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
            logfile.createLogFile("Delete Table Error", e);
            return 0;
        } finally {
            ps.close();
            // con.close();
        }
    }

    public int deleteTable(String id) throws SQLException {
        try {
            query = "call table_Delete(?);";
            ps = con.prepareStatement(query);
            ps.setString(1, id);
            return ps.executeUpdate();
        } catch (SQLException e) {
            alertMessage.showErrorMessage("Delete Error", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
            logfile.createLogFile("Delete Table Log Error", e);
            return 0;
        } finally {
            ps.close();
            // con.close();
        }
    }
}
