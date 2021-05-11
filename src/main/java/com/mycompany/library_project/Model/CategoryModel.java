package com.mycompany.library_project.Model;

import com.jfoenix.controls.JFXButton;
import com.mycompany.library_project.MyConnection;
import com.mycompany.library_project.ControllerDAOModel.DataAccessObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CategoryModel implements DataAccessObject {

    private PreparedStatement ps = null;
    private String sql = "";
    private Connection con = MyConnection.getConnect();
    private ResultSet rs = null;

    private String catgId;
    private String catgName;

    public CategoryModel() {
        
    }

    public CategoryModel(String catgId, String catgName) {
        this.catgId = catgId;
        this.catgName = catgName;
    }

    public String getCatgId() {
        return catgId;
    }

    public void setCatgId(String catgId) {
        this.catgId = catgId;
    }

    public String getCatgName() {
        return catgName;
    }

    public void setCatgName(String catgName) {
        this.catgName = catgName;
    }

    // Todo: Method management data
    @Override
    public ResultSet findAll() throws SQLException {
        try {
          sql = "call category_ShowAll();";
          rs = con.createStatement().executeQuery(sql);
          return rs;
      } catch (Exception e) {
          return null;
      }
    }

    @Override
    public ResultSet findById(String id) throws SQLException {
        try {
            sql = "call category_ShowById(?);";
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
            sql = "call category_ShowByName(?);";
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
           sql = "call category_Search(?);";
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
        sql = "call category_Insert(?, ?);";
        ps = con.prepareStatement(sql);
        ps.setString(1, getCatgId());
        ps.setString(2, getCatgName());
        return ps.executeUpdate();
    }

    @Override
    public int updateData() throws SQLException {
        sql = "call category_Update(?, ?);";
        ps = con.prepareStatement(sql);
        ps.setString(1, getCatgId());
        ps.setString(2, getCatgName());
        return ps.executeUpdate();
    }

    @Override
    public int deleteData(String id) throws SQLException {
        sql = "call category_Delete(?);";
        ps = con.prepareStatement(sql);
        ps.setString(1, id);
        return ps.executeUpdate();
    }

}
