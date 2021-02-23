package com.mycompany.library_project.Model;

import java.sql.*;
import java.text.ParseException;

import com.mycompany.library_project.MyConnection;
import com.mycompany.library_project.ControllerDAOModel.DataAccessObject;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class AuthorModel implements DataAccessObject {

    private ResultSet rs = null;
    private PreparedStatement ps = null;
    private Connection con = MyConnection.getConnect();
    private String sql = null;

    private StringProperty author_id;
    private StringProperty full_name;
    private StringProperty sur_name;
    private StringProperty tel;

    public AuthorModel(String author_id, String full_name, String sur_name, String tel) {
        this.author_id = new SimpleStringProperty(author_id);
        this.full_name = new SimpleStringProperty(full_name);
        this.sur_name = new SimpleStringProperty(sur_name);
        this.tel = new SimpleStringProperty(tel);
    }
    
    public void setAuthor_id(String author_id) {
        this.author_id.set(author_id);
    }
    
    public void setFull_name(String full_name) {
        this.full_name.set(full_name);
    }

    public void setSur_name(String sur_name) {
        this.sur_name.set(sur_name);
    }

    public void setTel(String tel) {
        this.tel.set(tel);
    }

    public String getAuthor_id() {
        return author_id.get();
    }

    public String getFull_name() {
        return full_name.get();
    }

    public String getSur_name() {
        return sur_name.get();
    }

    public String getTel() {
        return tel.get();
    }

    @Override
    public ResultSet findAll() throws SQLException {
        sql = "call author_ShowAll();";
        rs = con.createStatement().executeQuery(sql);
        return rs;
    }

    @Override
    public ResultSet findById(String id) throws SQLException {
        sql = "call author_ShowById(?);";
        ps = con.prepareStatement(sql);
        ps.setString(1, id);
        rs = ps.executeQuery();
        return rs;
    }

    @Override
    public ResultSet findByName(String name) throws SQLException {
        sql = "call author_ShowByName(?);";
        ps = con.prepareStatement(sql);
        ps.setString(1, name);
        rs = ps.executeQuery();
        return rs;
    }

    @Override
    public ResultSet searchData(String values) throws SQLException {
        sql = "call author_Search(?);";
        ps = con.prepareStatement(sql);
        ps.setString(1, values);
        rs = ps.executeQuery();
        return rs;
    }

    @Override
    public int saveData() throws SQLException, ParseException {
        sql = "call author_Insert(?, ?, ?, ?);";
        ps = con.prepareStatement(sql);
        ps.setString(1, getAuthor_id());
        ps.setString(2, getFull_name());
        ps.setString(3, getSur_name());
        ps.setString(4, getTel());
        return ps.executeUpdate();
    }

    @Override
    public int updateData() throws SQLException, ParseException {
        sql = "call author_Update(?, ?, ?, ?);";
        ps = con.prepareStatement(sql);
        ps.setString(1, getAuthor_id());
        ps.setString(2, getFull_name());
        ps.setString(3, getSur_name());
        ps.setString(4, getTel());
        return ps.executeUpdate();
    }

    @Override
    public int deleteData(String id) throws SQLException {
        sql = "call author_Delete(?);";
        ps = con.prepareStatement(sql);
        ps.setString(1, id);
        return ps.executeUpdate();
    }
}
