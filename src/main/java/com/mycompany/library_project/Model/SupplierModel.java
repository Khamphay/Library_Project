package com.mycompany.library_project.Model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;

import com.mycompany.library_project.Controller.HomeController;
import com.mycompany.library_project.ControllerDAOModel.*;

public class SupplierModel implements DataAccessObject {
    private DialogMessage dialog = new DialogMessage();

    private PreparedStatement ps = null;
    private ResultSet rs = null;
    private String sql = "";

    private String supid;
    private String supname;
    private String address;
    private String tel;
    private String email;

    public SupplierModel() {
    }

    public SupplierModel(String supid, String supname, String address, String tel, String email) {
        this.supid = supid;
        this.supname = supname;
        this.address = address;
        this.tel = tel;
        this.email = email;
    }

    public String getSupid() {
        return supid;
    }

    public void setSupid(String supid) {
        this.supid = supid;
    }

    public String getSupname() {
        return supname;
    }

    public void setSupname(String supname) {
        this.supname = supname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public ResultSet findAll() throws SQLException {
        try {
            sql = "call supplier_Show();";
            rs = HomeController.con.createStatement().executeQuery(sql);
            return rs;
        } catch (Exception e) {
            dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການສະແດງຂໍ້ມູນຜູ້ສະໜອງ", e);
            return null;
        }
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
    public int saveData() throws SQLException, ParseException {
        try {
            sql = "call supplier_Insert(?, ?, ?, ?, ?);";
            ps = HomeController.con.prepareStatement(sql);
            ps.setString(1, getSupid());
            ps.setString(2, getSupname());
            ps.setString(3, getAddress());
            ps.setString(4, getTel());
            ps.setString(5, getEmail());
            return ps.executeUpdate();
        } catch (Exception e) {
            dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການບັນທຶກຂໍ້ມູນຜູ້ສະໜອງ", e);
            return 0;
        }
    }

    @Override
    public int updateData() throws SQLException, ParseException {
        try {
            sql = "call supplier_Update(?, ?, ?, ?, ?);";
            ps = HomeController.con.prepareStatement(sql);
            ps.setString(1, getSupid());
            ps.setString(2, getSupname());
            ps.setString(3, getAddress());
            ps.setString(4, getTel());
            ps.setString(5, getEmail());
            return ps.executeUpdate();
        } catch (Exception e) {
            dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການແກ້ໄຂຂໍ້ມູນຜູ້ສະໜອງ", e);
            return 0;
        }
    }

    @Override
    public int deleteData(String id) throws SQLException {
        try {
            sql = "call supplier_Delete(?);";
            ps = HomeController.con.prepareStatement(sql);
            ps.setString(1, getSupid());
            return ps.executeUpdate();
        } catch (Exception e) {
            dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການລົບຂໍ້ມູນຜູ້ສະໜອງ", e);
            return 0;
        }
    }
}
