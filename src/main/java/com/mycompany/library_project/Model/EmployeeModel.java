package com.mycompany.library_project.Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;

import com.mycompany.library_project.MyConnection;
import com.mycompany.library_project.ControllerDAOModel.DataAccessObject;

import javafx.beans.property.*;

public class EmployeeModel implements DataAccessObject {

    private PreparedStatement ps = null;
    private ResultSet rs = null;
    private Connection con = MyConnection.getConnect();
    private String sql = null;

    private StringProperty employeeId;
    private StringProperty firstName;
    private StringProperty lastName;
    private StringProperty gender;
    private StringProperty tel;

    public EmployeeModel(String employeeId, String firstName, String lastName, String gender, String tel) {
        this.employeeId = new SimpleStringProperty(employeeId);
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.gender = new SimpleStringProperty(gender);
        this.tel = new SimpleStringProperty(tel);
    }

    public String getEmployeeId() {
        return employeeId.get();
    }

    public StringProperty employeeIdProperty() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId.set(employeeId);
    }

    public String getFirstName() {
        return firstName.get();
    }

    public StringProperty firstNameProperty() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public String getLastName() {
        return lastName.get();
    }

    public StringProperty sureNameProperty() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public String getGender() {
        return gender.get();
    }

    public StringProperty genderProperty() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender.set(gender);
    }

    public String getTel() {
        return tel.get();
    }

    public StringProperty telProperty() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel.set(tel);
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
        try {
            sql = "call employee_Insert(?, ?, ?, ?, ?)";
            ps = con.prepareStatement(sql);
            ps.setString(1, getEmployeeId());
            ps.setString(2, getFirstName());
            ps.setString(3, getLastName());
            ps.setString(4, getGender());
            ps.setString(5, getTel());
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public int updateData() throws SQLException, ParseException {
        try {
            sql = "call employee_Update(?, ?, ?, ?, ?)";
            ps = con.prepareStatement(sql);
            ps.setString(1, getEmployeeId());
            ps.setString(2, getFirstName());
            ps.setString(3, getLastName());
            ps.setString(4, getGender());
            ps.setString(5, getTel());
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public int deleteData(String id) throws SQLException {
        try {
            sql = "call employee_Delete(?)";
            ps = con.prepareStatement(sql);
            ps.setString(1, getEmployeeId());
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

}
