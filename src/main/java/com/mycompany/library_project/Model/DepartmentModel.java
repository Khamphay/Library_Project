package com.mycompany.library_project.Model;

import java.sql.*;
import java.text.ParseException;

import com.mycompany.library_project.MyConnection;
import com.mycompany.library_project.ControllerDAOModel.DataAccessObject;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class DepartmentModel implements DataAccessObject {

    private ResultSet rs = null;
    private PreparedStatement ps = null;
    private Connection con = MyConnection.getConnect();
    private String sql = null;

    private StringProperty depId;
    private StringProperty depName;

    public DepartmentModel(String depId, String depName) {
        this.depId = new SimpleStringProperty(depId);
        this.depName = new SimpleStringProperty(depName);
    }

    public String getDepId() {
        return depId.get();
    }

    public StringProperty depIdProperty() {
        return depId;
    }

    public void setDepId(String depId) {
        this.depId.set(depId);
    }

    public String getDepName() {
        return depName.get();
    }

    public StringProperty depNameProperty() {
        return depName;
    }

    public void setDepName(String depName) {
        this.depName.set(depName);
    }

    @Override
    public ResultSet findAll() throws SQLException {
        sql = "call department_ShowAll();";
        rs = con.createStatement().executeQuery(sql);
        return rs;
    }

    @Override
    public ResultSet findById(String id) throws SQLException {
        sql = "call department_ShowById(?);";
        ps = con.prepareStatement(sql);
        ps.setString(1, id);
        rs = ps.executeQuery();
        return rs;
    }

    @Override
    public ResultSet findByName(String name) throws SQLException {
        sql = "call department_ShowByName(?);";
        ps = con.prepareStatement(sql);
        ps.setString(1, name);
        rs = ps.executeQuery();
        return rs;
    }

    @Override
    public ResultSet searchData(String values) throws SQLException {
        sql = "call department_Search(?);";
        ps = con.prepareStatement(sql);
        ps.setString(1, values);
        rs = ps.executeQuery();
        return rs;
    }

    @Override
    public int saveData() throws SQLException, ParseException {
        sql = "call  department_Insert(?, ?);";
        ps = con.prepareStatement(sql);
        ps.setString(1, getDepId());
        ps.setString(2, getDepName());
        return ps.executeUpdate();
    }

    @Override
    public int updateData() throws SQLException, ParseException {
        sql = "call  department_Update(?, ?);";
        ps = con.prepareStatement(sql);
        ps.setString(1, getDepId());
        ps.setString(2, getDepName());
        return ps.executeUpdate();
    }

    @Override
    public int deleteData(String id) throws SQLException {
        sql = "call  department_Delete(?);";
        ps = con.prepareStatement(sql);
        ps.setString(1, id);
        return ps.executeUpdate();
    }
}
