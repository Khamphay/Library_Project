package com.mycompany.library_project.Model;

import java.sql.*;
import java.text.ParseException;

import com.mycompany.library_project.Controller.HomeController;
import com.mycompany.library_project.ControllerDAOModel.*;

public class AuthorModel implements DataAccessObject {

    private DialogMessage dialog = new DialogMessage();
    private ResultSet rs = null;
    private PreparedStatement ps = null;
    // private Connection con = MyConnection.getConnect();
    private String sql = null;

    private String author_id;
    private String full_name;
    private String sur_name;
    private String gender;
    private String tel;
    private String email;

    public AuthorModel() {
    }

    public AuthorModel(String author_id, String full_name) {
        this.author_id = author_id;
        this.full_name = full_name;
    }

    public AuthorModel(String author_id, String full_name, String sur_name, String gender, String tel, String email) {
        this.author_id = author_id;
        this.full_name = full_name;
        this.sur_name = sur_name;
        this.gender = gender;
        this.tel = tel;
        this.email = email;
    }

    public String getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(String author_id) {
        this.author_id = author_id;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getSur_name() {
        return sur_name;
    }

    public void setSur_name(String sur_name) {
        this.sur_name = sur_name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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
            sql = "call author_ShowAll();";
            rs = HomeController.con.createStatement().executeQuery(sql);
            return rs;
        } catch (SQLException e) {
            dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການໂຫຼດຂໍ້ມູນນັກແຕ່ງ", e);
            return null;
        } finally {
            // HomeController.con.close();
        }

    }

    @Override
    public ResultSet findById(String id) throws SQLException {
        try {
            sql = "call author_ShowById(?);";
            ps = HomeController.con.prepareStatement(sql);
            ps.setString(1, id);
            rs = ps.executeQuery();
            return rs;
        } catch (SQLException e) {
            dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການໂຫຼດຂໍ້ມູນນັກແຕ່ງ", e);
            return null;
        } finally {
            // ps.close();
        }
    }

    @Override
    public ResultSet findByName(String name) throws SQLException {
        try {
            sql = "call author_ShowByName(?);";
            ps = HomeController.con.prepareStatement(sql);
            ps.setString(1, name);
            rs = ps.executeQuery();
            return rs;
        } catch (SQLException e) {
            dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການໂຫຼດຂໍ້ມູນນັກແຕ່ງ", e);
            return null;
        } finally {
            // ps.close();
        }
    }

    public ResultSet findByBookID(String bookid) throws SQLException {
        try {
            sql = "call showAutorByBookID(?);";
            ps = HomeController.con.prepareStatement(sql);
            ps.setString(1, bookid);
            rs = ps.executeQuery();
            return rs;
        } catch (SQLException e) {
            dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການໂຫຼດຂໍ້ມູນນັກແຕ່ງ", e);
            return null;
        } finally {
            // ps.close();
        }
    }

    @Override
    public ResultSet searchData(String values) throws SQLException {
        try {
            sql = "call author_Search(?);";
            ps = HomeController.con.prepareStatement(sql);
            ps.setString(1, values);
            rs = ps.executeQuery();
            return rs;
        } catch (SQLException e) {
            dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການຄົ້ນຫາຂໍ້ມູນນັກແຕ່ງ", e);
            return null;
        } finally {
            // ps.close();
        }
    }

    @Override
    public int saveData() throws SQLException, ParseException {
        try {
            sql = "call author_Insert(?, ?, ?, ?, ?, ?);";
            ps = HomeController.con.prepareStatement(sql);
            ps.setString(1, getAuthor_id());
            ps.setString(2, getFull_name());
            ps.setString(3, getSur_name());
            ps.setString(4, getGender());
            ps.setString(5, getTel());
            ps.setString(6, getEmail());
            return ps.executeUpdate();
        } catch (SQLException e) {
            dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການບັນທືກຂໍ້ມູນນັກແຕ່ງ", e);
            return 0;
        } finally {
            // ps.close();
        }

    }

    @Override
    public int updateData() throws SQLException, ParseException {
        try {
            sql = "call author_Update(?, ?, ?, ?, ?, ?);";
            ps = HomeController.con.prepareStatement(sql);
            ps.setString(1, getAuthor_id());
            ps.setString(2, getFull_name());
            ps.setString(3, getSur_name());
            ps.setString(4, getGender());
            ps.setString(5, getTel());
            ps.setString(6, getEmail());
            return ps.executeUpdate();
        } catch (SQLException e) {
            dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການແກ້ໄຂ້ບຂໍ້ມູນນັກແຕ່ງ", e);
            return 0;
        } finally {
            // ps.close();
        }
    }

    @Override
    public int deleteData(String id) throws SQLException {
        try {
            sql = "call author_Delete(?);";
            ps = HomeController.con.prepareStatement(sql);
            ps.setString(1, id);
            return ps.executeUpdate();
        } catch (SQLException e) {
            dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການລົບຂໍ້ມູນນັກແຕ່ງ", e);
            return 0;
        } finally {
            // ps.close();
        }
    }
}
