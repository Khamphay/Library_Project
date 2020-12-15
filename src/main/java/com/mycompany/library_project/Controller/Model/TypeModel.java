package com.mycompany.library_project.Controller.Model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import com.mycompany.library_project.MyConnection;
import com.mycompany.library_project.Controller.ControllerDAOModel.DataAccessObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TypeModel implements DataAccessObject {

    private PreparedStatement ps = null;
    private Connection con = MyConnection.getConnect();

    private String query = "";

    private StringProperty typeId;
    private StringProperty typeName;

    public TypeModel(String typeId) {
        this.typeId = new SimpleStringProperty(typeId);
    }

    public TypeModel(String typeId, String typeName) {
        this.typeId = new SimpleStringProperty(typeId);
        this.typeName = new SimpleStringProperty(typeName);
    }

    public String getTypeId() {
        return typeId.get();
    }

    public String getTypeName() {
        return typeName.get();
    }

    public void setTypeId(String values) {
        this.typeId.set(values);
    }

    public void setTypeName(String values) {
        this.typeName.set(values);
    }

    public StringProperty typeIdProperty() {
        return typeId;
    }

    public StringProperty typeNameProperty() {
        return typeName;
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
        query = "Insert Into tbtype Values(?,?)";
        ps = con.prepareStatement(query);
        ps.setString(1, getTypeId());
        ps.setString(2, getTypeName());
        return ps.executeUpdate();
    }

    @Override
    public int updateData() throws SQLException {
        query = "Update tbtype Set typename=? Where typeid=?";
        ps = con.prepareStatement(query);
        ps.setString(2, getTypeId());
        ps.setString(1, getTypeName());
        return ps.executeUpdate();
    }

    @Override
    public int deleteData() throws SQLException {
        query = "Delete From tbtype  Where typeid=?;";
        ps = con.prepareStatement(query);
        ps.setString(1, getTypeId());
        return ps.executeUpdate();
    }
}
