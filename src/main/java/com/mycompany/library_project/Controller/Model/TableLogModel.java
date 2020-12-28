package com.mycompany.library_project.Controller.Model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import com.mycompany.library_project.MyConnection;
import com.mycompany.library_project.Controller.ControllerDAOModel.DataAccessObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TableLogModel implements DataAccessObject {

    private PreparedStatement ps = null;
    private Connection con = MyConnection.getConnect();
    private ResultSet rs = null;
    private String query = "";

    private StringProperty tableId;
    private StringProperty tableLogNum;

    public TableLogModel(String tableId) {
        this.tableId = new SimpleStringProperty(tableId);
    }

    public TableLogModel(String tableId, String tableLogNum) {
        this.tableId = new SimpleStringProperty(tableId);
        this.tableLogNum = new SimpleStringProperty(tableLogNum);
    }

    public String getTableId() {
        return tableId.get();
    }

    public StringProperty tableIdProperty() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId.set(tableId);
    }

    public String getTableLogNum() {
        return tableLogNum.get();
    }

    public StringProperty tableLogNumProperty() {
        return tableLogNum;
    }

    public void setTableLogNum(String tableLogNum) {
        this.tableLogNum.set(tableLogNum);
    }

    @Override
    public ResultSet findAll() throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ResultSet findById(String id) throws SQLException {
        // TODO Auto-generated method stub
        return null;
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

    @Override
    public int saveData() throws SQLException {
        query = "Insert into tbtable Values(?,?);";
        ps = con.prepareStatement(query);
        ps.setString(1, getTableId());
        ps.setString(2, getTableLogNum());
        return ps.executeUpdate();
    }

    @Override
    public int updateData() throws SQLException {
        query = "Update tbtable Set tbdid=? Where tbid=?;";
        ps = con.prepareStatement(query);
        ps.setString(2, getTableId());
        ps.setString(1, getTableLogNum());
        return ps.executeUpdate();
    }

    @Override
    public int deleteData(String id) throws SQLException {
        query = "Delete From tbtable  Where tbid=?;";
        ps = con.prepareStatement(query);
        ps.setString(1, id);
        return ps.executeUpdate();
    }
}
