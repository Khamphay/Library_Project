package com.mycompany.library_project.Controller.Model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import com.mycompany.library_project.MyConnection;
import com.mycompany.library_project.Controller.ControllerDAOModel.DataAccessObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CategoryModel implements DataAccessObject {

    private PreparedStatement ps = null;
    private String query = "";
    private Connection con = MyConnection.getConnect();
    private ResultSet rs = null;
    private StringProperty catgId;
    private StringProperty catgName;

    public CategoryModel(String catgId) {
        this.catgId = new SimpleStringProperty(catgId);
    }

    public CategoryModel(String catgId, String catgName) {
        this.catgId = new SimpleStringProperty(catgId);
        this.catgName = new SimpleStringProperty(catgName);
    }

    public void setCatgId(String catgId) {
        this.catgId.set(catgId);
    }

    public void setCatgName(String catgName) {
        this.catgName.set(catgName);
    }

    public String getCatgId() {
        return catgId.get();
    }

    public String getCatgName() {
        return catgName.get();
    }

    public StringProperty catgIdProperty() {
        return catgId;
    }

    public StringProperty ctgNameProperty() {
        return catgName;
    }

    // Todo: Method management data
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
        query = "Insert Into tbcategory Values(?,?)";
        ps = con.prepareStatement(query);
        ps.setString(1, getCatgId());
        ps.setString(2, getCatgName());
        return ps.executeUpdate();
    }

    @Override
    public int updateData() throws SQLException {
        query = "Update tbcategory Set name=? Where catgid=?";
        ps = con.prepareStatement(query);
        ps.setString(2, getCatgId());
        ps.setString(1, getCatgName());
        return ps.executeUpdate();
    }

    @Override
    public int deleteData() throws SQLException {
        query = "Delete From tbcategory  Where catgid=?";
        ps = con.prepareStatement(query);
        ps.setString(1, getCatgId());
        return ps.executeUpdate();
    }
}
