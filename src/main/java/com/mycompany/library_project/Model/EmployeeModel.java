package com.mycompany.library_project.Model;

import java.sql.*;
import java.text.ParseException;

import com.mycompany.library_project.MyConnection;
import com.mycompany.library_project.ControllerDAOModel.AlertMessage;
import com.mycompany.library_project.ControllerDAOModel.DataAccessObject;
import com.mycompany.library_project.config.CreateLogFile;

import javafx.geometry.Pos;

public class EmployeeModel implements DataAccessObject {

    private AlertMessage alertMessage = new AlertMessage();
    private CreateLogFile logfile = new CreateLogFile();
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
    private String saltKey;

    public EmployeeModel() {

    }

    public EmployeeModel(String employeeId, String user, String password, String saltKey) {
        this.employeeId = employeeId;
        this.user = user;
        this.password = password;
        this.saltKey = saltKey;
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

    public String getSaltKey() {
        return saltKey;
    }

    public void setSaltKey(String saltKey) {
        this.saltKey = saltKey;
    }

    @Override
    public ResultSet findAll() throws SQLException {
        try {
            sql = "call employee_Show()";
            rs = con.createStatement().executeQuery(sql);
            return rs;
        } catch (Exception e) {
            alertMessage.showErrorMessage("Load Data", "Error" + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
            logfile.createLogFile("Load Employee Error", e);
            return null;
        } finally {
            //con.close();
        }
    }

    @Override
    public ResultSet findById(String id) throws SQLException {
        try {
            sql = "call employee_ShowById(" + getEmployeeId() + ")";
            rs = con.createStatement().executeQuery(sql);
            return rs;
        } catch (Exception e) {
            alertMessage.showErrorMessage("Load Data", "Error" + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
            return null;
        } finally {
            //con.close();
        }
    }

    @Override
    public ResultSet findByName(String name) throws SQLException {
        try {
            sql = "call employee_ShowByName(" + getFirstName() + ")";
            rs = con.createStatement().executeQuery(sql);
            return rs;
        } catch (Exception e) {
            alertMessage.showErrorMessage("Load Data", "Error" + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
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
        } finally {
            //con.close();
        }
    }

    @Override
    public int saveData() throws SQLException, ParseException {
        try {
            sql = "call employee_Insert(?, ?, ?, ?, ?, ?)";
            ps = con.prepareStatement(sql);
            ps.setString(1, getEmployeeId());
            ps.setString(2, getFirstName());
            ps.setString(3, getLastName());
            ps.setString(4, getGender());
            ps.setString(5, getTel());
            ps.setString(6, getEmail());
            return ps.executeUpdate();
        } catch (SQLException e) {
            alertMessage.showErrorMessage("Save Error", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
            logfile.createLogFile("Save Employee Error", e);
            return 0;
        } finally {
            ps.close();
            //con.close();
        }

    }

    @Override
    public int updateData() throws SQLException, ParseException {
        try {
            sql = "call employee_Update(?, ?, ?, ?, ?, ?)";
            ps = con.prepareStatement(sql);
            ps.setString(1, getEmployeeId());
            ps.setString(2, getFirstName());
            ps.setString(3, getLastName());
            ps.setString(4, getGender());
            ps.setString(5, getTel());
            ps.setString(6, getEmail());
            return ps.executeUpdate();
        } catch (SQLException e) {
            alertMessage.showErrorMessage("Edit Error", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
            logfile.createLogFile("Update Employee Error", e);
            return 0;
        } finally {
            ps.close();
            //con.close();
        }
    }

    @Override
    public int deleteData(String id) throws SQLException {
        try {
            sql = "call employee_Delete(?)";
            ps = con.prepareStatement(sql);
            ps.setString(1, id);
            return ps.executeUpdate();
        } catch (SQLException e) {
            alertMessage.showErrorMessage("Delete Error", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
            logfile.createLogFile("Delete Employee Error", e);
            return 0;
        } finally {
            ps.close();
            //con.close();
        }
    }

    public int addUser() throws SQLException {
        try {
            sql = "call addUser(?, ?, ?, ?);";
            ps = con.prepareStatement(sql);
            ps.setString(1, getEmployeeId());
            ps.setString(2, getUser());
            ps.setString(3, getPassword());
            ps.setString(4, getSaltKey());
            return ps.executeUpdate();
        } catch (SQLException e) {
            alertMessage.showErrorMessage("Add User Error", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
            logfile.createLogFile("Add User Error", e);
            return 0;
        } finally {
            ps.close();
            //con.close();
        }
    }

    public int updateUser(String empid, String oldUsername) throws SQLException {
        try {
            sql = "call updateUser(?, ?, ?, ?, ?);";
            ps = con.prepareStatement(sql);
            ps.setString(1, empid);
            ps.setString(2, oldUsername);
            ps.setString(3, getUser());
            ps.setString(4, getPassword());
            ps.setString(5, getSaltKey());
            return ps.executeUpdate();
        } catch (SQLException e) {
            alertMessage.showErrorMessage("Edit Error", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
            logfile.createLogFile("Update User Error", e);
            return 0;
        } finally {
            ps.close();
            //con.close();
        }
    }

    public int updateUserName(String empid, String oldUsername, String newUsername) throws SQLException {
        try {
            sql = "update tbuser set user_name =? where employee_id = ? and user_name = ?;";
            ps = con.prepareStatement(sql);
            ps.setString(1, newUsername);
            ps.setString(2, empid);
            ps.setString(3, oldUsername);
            return ps.executeUpdate();
        } catch (SQLException e) {
            alertMessage.showErrorMessage("Edit Error", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
            logfile.createLogFile("Update Username Error", e);
            return 0;
        } finally {
            ps.close();
            //con.close();
        }
    }

    public ResultSet Login(String user) throws SQLException {
        try {
            sql = "call Login(?);";
            ps = con.prepareStatement(sql);
            ps.setString(1, user);
            rs = ps.executeQuery();
            return rs;
        } catch (SQLException e) {
            alertMessage.showErrorMessage("Login Data Error", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
            logfile.createLogFile("Login Error", e);
            return null;
        } finally {
            //con.close();
        }

    }

    public ResultSet findUserByEmpId(String empId) throws SQLException {

        try {
            sql = "call findUserByEmpId(?);";
            ps = con.prepareStatement(sql);
            ps.setString(1, empId);
            rs = ps.executeQuery();
            return rs;
        } catch (SQLException e) {
            alertMessage.showErrorMessage("Load Data Error", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
            logfile.createLogFile("Load User Error", e);
            return null;
        } finally {
            //con.close();
        }
    }
}
