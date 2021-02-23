package com.mycompany.library_project.Model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

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

    private StringProperty typeId;
    private StringProperty typeName;

    public TypeModel() {
        
    }

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
        sql = "call type_ShowAll();";
        rs = con.createStatement().executeQuery(sql);
        return rs;
    }

    @Override
    public ResultSet findById(String id) throws SQLException {
        sql = "call type_ShowById(?);";
        ps = con.prepareStatement(sql);
        ps.setString(1, id);
        rs = ps.executeQuery();
        return rs;
    }

    @Override
    public ResultSet findByName(String name) throws SQLException {
        sql = "call type_ShowByName(?);";
        ps = con.prepareStatement(sql);
        ps.setString(1, name);
        rs = ps.executeQuery();
        return rs;
    }

    @Override
    public ResultSet searchData(String values) throws SQLException {
        sql = "call type_Search(?);";
        ps = con.prepareStatement(sql);
        ps.setString(1, values);
        rs = ps.executeQuery();
        return rs;
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
