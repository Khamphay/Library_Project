package com.mycompany.library_project.Model;

import com.jfoenix.controls.JFXButton;
import com.mycompany.library_project.MyConnection;
import com.mycompany.library_project.ControllerDAOModel.DataAccessObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TypeModel implements DataAccessObject {

    private PreparedStatement ps = null;
    private ResultSet rs = null;
    private Connection con = MyConnection.getConnect();

    private String sql = "";

    private String typeId;
    private String typeName;

    public TypeModel() {

    }

    public TypeModel(String typeId) {
        this.typeId = typeId;
    }

    public TypeModel(String typeId, String typeName) {
        this.typeId = typeId;
        this.typeName = typeName;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    @Override
    public ResultSet findAll() throws SQLException {
        try {
            sql = "call type_ShowAll();";
            rs = con.createStatement().executeQuery(sql);
            return rs;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public ResultSet findById(String id) throws SQLException {
        try {
            sql = "call type_ShowById(?);";
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
            sql = "call type_ShowByName(?);";
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
            sql = "call type_Search(?);";
            ps = con.prepareStatement(sql);
            ps.setString(1, values);
            rs = ps.executeQuery();
            return rs;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public int saveData() throws SQLException {
        sql = "call type_Insert(?, ?); ";
        ps = con.prepareStatement(sql);
        ps.setString(1, getTypeId());
        ps.setString(2, getTypeName());
        return ps.executeUpdate();
    }

    @Override
    public int updateData() throws SQLException {
        sql = "call type_Update(?, ?);";
        ps = con.prepareStatement(sql);
        ps.setString(1, getTypeId());
        ps.setString(2, getTypeName());
        return ps.executeUpdate();
    }

    @Override
    public int deleteData(String id) throws SQLException {
        sql = "call type_Delete(?);";
        ps = con.prepareStatement(sql);
        ps.setString(1, id);
        return ps.executeUpdate();
    }

}
