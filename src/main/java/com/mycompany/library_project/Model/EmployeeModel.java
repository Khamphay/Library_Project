package com.mycompany.library_project.Model;

import java.sql.*;
import java.text.ParseException;

import com.mycompany.library_project.Controller.HomeController;
import com.mycompany.library_project.ControllerDAOModel.*;

public class EmployeeModel implements DataAccessObject {

    private DialogMessage dialog = new DialogMessage();
    private PreparedStatement ps = null;
    private ResultSet rs = null;
    // private Connection con = MyConnection.getConnect();
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
            rs = HomeController.con.createStatement().executeQuery(sql);
            return rs;
        } catch (SQLException e) {
            dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການໂຫຼດຂໍ້ມູນພະນັກງານ", e);
            return null;
        }
    }

    @Override
    public ResultSet findById(String id) throws SQLException {
        try {
            sql = "call employee_ShowById(" + getEmployeeId() + ")";
            rs = HomeController.con.createStatement().executeQuery(sql);
            return rs;
        } catch (SQLException e) {
            dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການໂຫຼດຂໍ້ມູນພະນັກງານ", e);
            return null;
        }
    }

    @Override
    public ResultSet findByName(String name) throws SQLException {
        try {
            sql = "call employee_ShowByName(" + getFirstName() + ")";
            rs = HomeController.con.createStatement().executeQuery(sql);
            return rs;
        } catch (SQLException e) {
            dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການໂຫຼດຂໍ້ມູນພະນັກງານ", e);
            return null;
        }
    }

    @Override
    public ResultSet searchData(String values) throws SQLException {
        try {
            sql = "call employee_Search(" + values + ")";
            rs = HomeController.con.createStatement().executeQuery(sql);
            return rs;
        } catch (SQLException e) {
            dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການຄົ້ນຂໍ້ມູນພະນັກງານ", e);
            return null;
        }
    }

    @Override
    public int saveData() throws SQLException, ParseException {
        try {
            sql = "call employee_Insert(?, ?, ?, ?, ?, ?)";
            ps = HomeController.con.prepareStatement(sql);
            ps.setString(1, getEmployeeId());
            ps.setString(2, getFirstName());
            ps.setString(3, getLastName());
            ps.setString(4, getGender());
            ps.setString(5, getTel());
            ps.setString(6, getEmail());
            return ps.executeUpdate();
        } catch (SQLException e) {
            dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການບັນທຶກຂໍ້ມູນພະນັກງານ", e);
            return 0;
        } finally {
            //// ps.close();
        }

    }

    @Override
    public int updateData() throws SQLException, ParseException {
        try {
            sql = "call employee_Update(?, ?, ?, ?, ?, ?)";
            ps = HomeController.con.prepareStatement(sql);
            ps.setString(1, getEmployeeId());
            ps.setString(2, getFirstName());
            ps.setString(3, getLastName());
            ps.setString(4, getGender());
            ps.setString(5, getTel());
            ps.setString(6, getEmail());
            return ps.executeUpdate();
        } catch (SQLException e) {
            dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການແກ້ໄຂ້ຂໍ້ມູນພະນັກງານ", e);
            return 0;
        } finally {
            //// ps.close();
        }
    }

    @Override
    public int deleteData(String id) throws SQLException {
        try {
            sql = "call employee_Delete(?)";
            ps = HomeController.con.prepareStatement(sql);
            ps.setString(1, id);
            return ps.executeUpdate();
        } catch (SQLException e) {
            dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການລົບຂໍ້ມູນພະນັກງານ", e);
            return 0;
        } finally {
            //// ps.close();
        }
    }

    public int addUser() throws SQLException {
        try {
            sql = "call addUser(?, ?, ?, ?);";
            ps = HomeController.con.prepareStatement(sql);
            ps.setString(1, getEmployeeId());
            ps.setString(2, getUser());
            ps.setString(3, getPassword());
            ps.setString(4, getSaltKey());
            return ps.executeUpdate();
        } catch (SQLException e) {
            dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການບັນທຶກຂໍ້ມູນຜູ້ໃຊ້", e);
            return 0;
        } finally {
            //// ps.close();
        }
    }

    public int updateUser(String empid, String oldUsername) throws SQLException {
        try {
            sql = "call updateUser(?, ?, ?, ?, ?);";
            ps = HomeController.con.prepareStatement(sql);
            ps.setString(1, empid);
            ps.setString(2, oldUsername);
            ps.setString(3, getUser());
            ps.setString(4, getPassword());
            ps.setString(5, getSaltKey());
            return ps.executeUpdate();
        } catch (SQLException e) {
            dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການແກ້ໄຂ້ຂໍ້ມູນຜູ້ໃຊ້", e);
            return 0;
        } finally {
            //// ps.close();
        }
    }

    public int updateUserName(String empid, String oldUsername, String newUsername) throws SQLException {
        try {
            sql = "update tbuser set user_name=? where employee_id = ? and user_name = ?;";
            ps = HomeController.con.prepareStatement(sql);
            ps.setString(1, newUsername);
            ps.setString(2, empid);
            ps.setString(3, oldUsername);
            return ps.executeUpdate();
        } catch (SQLException e) {
            dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການແກ້ໄຂ້ຊື່ຜູ້ໃຊ້", e);
            return 0;
        } finally {
            //// ps.close();
        }
    }

    public ResultSet Login(String user) throws SQLException {
        try {
            sql = "call Login(?);";
            ps = HomeController.con.prepareStatement(sql);
            ps.setString(1, user);
            rs = ps.executeQuery();
            return rs;
        } catch (SQLException e) {
            dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການເຂົ້າລະບົບ", e);
            return null;
        } finally {
            //// ps.close();
        }

    }

    public ResultSet findUserByEmpId(String empId) throws SQLException {

        try {
            sql = "call findUserByEmpId(?);";
            ps = HomeController.con.prepareStatement(sql);
            ps.setString(1, empId);
            rs = ps.executeQuery();
            return rs;
        } catch (SQLException e) {
            dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການໂຫຼດຂໍ້ມູນຜູ້້ໃຊ້", e);
            return null;
        } finally {
            //// ps.close();
        }
    }

    public String getEmpID(String userInfor) {
        try {
            sql = "SELECT tbuser.employee_id from tbemployee INNER join tbuser ON tbemployee.employee_id=tbuser.employee_id  WHERE  tbemployee.full_name=? OR tbemployee.tel=? OR tbemployee.email=? OR tbuser.user_name=?";
            ps = HomeController.con.prepareStatement(sql);
            ps.setString(1, userInfor);
            ps.setString(2, userInfor);
            ps.setString(3, userInfor);
            ps.setString(4, userInfor);
            rs = ps.executeQuery();
            if (rs.next())
                return rs.getString("employee_id");
            else
                return null;
        } catch (SQLException e) {
            dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການປ່ຽນລະຫັດຜ່ານ", e);
            return null;
        }
    }

    public int RecoveryPassword(String password, String salt, String username, String empId) throws SQLException {
        try {
            sql = "update tbuser set user_name=?, `password`=?, salt=? where employee_id=?";
            ps = HomeController.con.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, salt);
            ps.setString(4, empId);
            return ps.executeUpdate();
        } catch (SQLException e) {
            dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການປ່ຽນລະຫັດຜ່ານ", e);
            return 0;
        }
    }
}
