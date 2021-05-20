package com.mycompany.library_project.Model;

import java.sql.*;
import java.text.ParseException;

import com.mycompany.library_project.MyConnection;
import com.mycompany.library_project.ControllerDAOModel.DataAccessObject;

public class DepartmentModel implements DataAccessObject {

    private ResultSet rs = null;
    private PreparedStatement ps = null;
    private Connection con = MyConnection.getConnect();
    private String sql = null;

    private String depId;
    private String depName;

    public DepartmentModel() {
    }

    public DepartmentModel(String depId, String depName) {
        this.depId = depId;
        this.depName = depName;
    }

    public String getDepId() {
        return depId;
    }

    public void setDepId(String depId) {
        this.depId = depId;
    }

    public String getDepName() {
        return depName;
    }

    public void setDepName(String depName) {
        this.depName = depName;
    }

    @Override
    public ResultSet findAll() throws SQLException {
        try {
            sql = "call department_ShowAll();";
            rs = con.createStatement().executeQuery(sql);
            return rs;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public ResultSet findById(String id) throws SQLException {
        try {
            sql = "call department_ShowById(?);";
            ps = con.prepareStatement(sql);
            ps.setString(1, id);
            rs = ps.executeQuery();
            return rs;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public ResultSet findByName(String name) throws SQLException {
        try {
            sql = "call department_ShowByName(?);";
            ps = con.prepareStatement(sql);
            ps.setString(1, name);
            rs = ps.executeQuery();
            return rs;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public ResultSet searchData(String values) throws SQLException {
        try {
            sql = "call department_Search(?);";
            ps = con.prepareStatement(sql);
            ps.setString(1, values);
            rs = ps.executeQuery();
            return rs;
        } catch (Exception e) {
            return null;
        }
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
