package com.mycompany.library_project.Model;

import java.sql.*;
import java.text.ParseException;

import com.jfoenix.controls.JFXButton;
import com.mycompany.library_project.MyConnection;
import com.mycompany.library_project.ControllerDAOModel.DataAccessObject;

public class EmployeeModel implements DataAccessObject {

    private PreparedStatement ps = null;
    private ResultSet rs = null;
    private Connection con = MyConnection.getConnect();
    private String sql = null;

    private String employeeId;
    private String firstName;
    private String lastName;
    private String gender;
    private String tel;
    private String email;
    private String user;
    private String password;

    public EmployeeModel() {

    }

    public EmployeeModel(String employeeId, String user, String password) {
        this.employeeId = employeeId;
        this.user = user;
        this.password = password;
    }

    public EmployeeModel(String employeeId, String firstName, String lastName, String gender, String tel,
            String email) {
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.tel = tel;
        this.email = email;
    }


    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public ResultSet findAll() throws SQLException {
        try {
            sql = "call employee_Show()";
            rs = con.createStatement().executeQuery(sql);
            return rs;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ResultSet findById(String id) throws SQLException {
        try {
            sql = "call employee_ShowById(" + getEmployeeId() + ")";
            rs = con.createStatement().executeQuery(sql);
            return rs;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ResultSet findByName(String name) throws SQLException {
        try {
            sql = "call employee_ShowByName(" + getFirstName() + ")";
            rs = con.createStatement().executeQuery(sql);
            return rs;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ResultSet searchData(String values) throws SQLException {
        try {
            sql = "call employee_Search(" + values + ")";
            rs = con.createStatement().executeQuery(sql);
            return rs;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int saveData() throws SQLException, ParseException {
        sql = "call employee_Insert(?, ?, ?, ?, ?, ?)";
        ps = con.prepareStatement(sql);
        ps.setString(1, getEmployeeId());
        ps.setString(2, getFirstName());
        ps.setString(3, getLastName());
        ps.setString(4, getGender());
        ps.setString(5, getTel());
        ps.setString(6, getEmail());
        return ps.executeUpdate();
    }

    @Override
    public int updateData() throws SQLException, ParseException {
        sql = "call employee_Update(?, ?, ?, ?, ?, ?)";
        ps = con.prepareStatement(sql);
        ps.setString(1, getEmployeeId());
        ps.setString(2, getFirstName());
        ps.setString(3, getLastName());
        ps.setString(4, getGender());
        ps.setString(5, getTel());
        ps.setString(6, getEmail());
        return ps.executeUpdate();
    }

    @Override
    public int deleteData(String id) throws SQLException {
        sql = "call employee_Delete(?)";
        ps = con.prepareStatement(sql);
        ps.setString(1, id);
        return ps.executeUpdate();
    }
}
