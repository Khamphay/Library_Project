package com.mycompany.library_project.Model;

import java.sql.*;
import java.text.ParseException;

import com.mycompany.library_project.ControllerDAOModel.*;

public class DepartmentModel implements DataAccessObject {

    private DialogMessage dialog = new DialogMessage();
    private ResultSet rs = null;
    private PreparedStatement ps = null;
    private Connection con = null;
    private String sql = null;

    private String depId;
    private String depName;

    public DepartmentModel(Connection con) {
        this.con = con;
    }

    public DepartmentModel(String depId, String depName) {
        this.depId = depId;
        this.depName = depName;
    }

    public String getDepId() {
        return depId;
    }

    public void setDepId(String depId) {
        this.depId = depId;
    }

    public String getDepName() {
        return depName;
    }

    public void setDepName(String depName) {
        this.depName = depName;
    }

    @Override
    public ResultSet findAll() throws SQLException {
        try {
            sql = "call department_ShowAll();";
            rs = con.createStatement().executeQuery(sql);
            return rs;
        } catch (SQLException e) {
            dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການໂຫຼດຂໍ້ມູນພາກວິຊາ", e);
            return null;
        } finally {
            //con.close();
        }
    }

    @Override
    public ResultSet findById(String id) throws SQLException {
        try {
            sql = "call department_ShowById(?);";
            ps = con.prepareStatement(sql);
            ps.setString(1, id);
            rs = ps.executeQuery();
            return rs;
        } catch (SQLException e) {
            dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການໂຫຼດຂໍ້ມູນພາກວິຊາ", e);
            return null;
        } finally {
            //con.close();
        }
    }

    @Override
    public ResultSet findByName(String name) throws SQLException {
        try {
            sql = "call department_ShowByName(?);";
            ps = con.prepareStatement(sql);
            ps.setString(1, name);
            rs = ps.executeQuery();
            return rs;
        } catch (SQLException e) {
            dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການໂຫຼດຂໍ້ມູນພາກວິຊາ", e);
            return null;
        } finally {
            //con.close();
        }
    }

    @Override
    public ResultSet searchData(String values) throws SQLException {
        try {
            sql = "call department_Search(?);";
            ps = con.prepareStatement(sql);
            ps.setString(1, values);
            rs = ps.executeQuery();
            return rs;
        } catch (SQLException e) {
            dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການຄົ້ນຂໍ້ມູນພາກວິຊາ", e);
            return null;
        } finally {
            //con.close();
        }
    }

    @Override
    public int saveData() throws SQLException, ParseException {
        try {
            sql = "call  department_Insert(?, ?);";
            ps = con.prepareStatement(sql);
            ps.setString(1, getDepId());
            ps.setString(2, getDepName());
            return ps.executeUpdate();
        } catch (SQLException e) {
            dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການບັນທຶກຂໍ້ມູນພາກວິຊາ", e);
            return 0;
        } finally {
            ps.close();
            //con.close();
        }
    }

    @Override
    public int updateData() throws SQLException, ParseException {
        try {
            sql = "call  department_Update(?, ?);";
            ps = con.prepareStatement(sql);
            ps.setString(1, getDepId());
            ps.setString(2, getDepName());
            return ps.executeUpdate();
        } catch (SQLException e) {
            dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການແກ້ໄຂ້ຂໍ້ມູນພາກວິຊາ", e);
            return 0;
        } finally {
            ps.close();
            //con.close();
        }

    }

    @Override
    public int deleteData(String id) throws SQLException {
        try {
            sql = "call  department_Delete(?);";
            ps = con.prepareStatement(sql);
            ps.setString(1, id);
            return ps.executeUpdate();
        } catch (SQLException e) {
            dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການລົບຂໍ້ມູນພາກວິຊາ", e);
            return 0;
        } finally {
            ps.close();
            //con.close();
        }

    }
}
