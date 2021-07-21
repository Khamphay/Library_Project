package com.mycompany.library_project.Model;

import com.mycompany.library_project.MyConnection;
import com.mycompany.library_project.ControllerDAOModel.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TypeModel implements DataAccessObject {

    private DialogMessage dialog = new DialogMessage();
    private PreparedStatement ps = null;
    private ResultSet rs = null;
    private Connection con = MyConnection.getConnect();

    private String sql = "";

    private String typeId;
    private String typeName;

    public TypeModel() {
    }

    public TypeModel(String typeId) {
        this.typeId = typeId;
    }

    public TypeModel(String typeId, String typeName) {
        this.typeId = typeId;
        this.typeName = typeName;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    @Override
    public ResultSet findAll() throws SQLException {
        try {
            sql = "call type_ShowAll();";
            rs = con.createStatement().executeQuery(sql);
            return rs;
        } catch (SQLException e) {
            dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການໂຫຼດຂໍ້ມູນປະເພດປຶ້ມ", e);
            return null;
        } finally {
            //con.close();
        }
    }

    @Override
    public ResultSet findById(String id) throws SQLException {
        try {
            sql = "call type_ShowById(?);";
            ps = con.prepareStatement(sql);
            ps.setString(1, id);
            rs = ps.executeQuery();
            return rs;
        } catch (SQLException e) {
            dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການໂຫຼດຂໍ້ມູນປະເພດປຶ້ມ", e);
            return null;
        } finally {
            ps.close();
            //con.close();
        }
    }

    @Override
    public ResultSet findByName(String name) throws SQLException {
        try {
            sql = "call type_ShowByName(?);";
            ps = con.prepareStatement(sql);
            ps.setString(1, name);
            rs = ps.executeQuery();
            return rs;
        } catch (SQLException e) {
            dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການໂຫຼດຂໍ້ມູນປະເພດປຶ້ມ", e);
            return null;
        } finally {
            ps.close();
            //con.close();
        }
    }

    @Override
    public ResultSet searchData(String values) throws SQLException {
        try {
            sql = "call type_Search(?);";
            ps = con.prepareStatement(sql);
            ps.setString(1, values);
            rs = ps.executeQuery();
            return rs;
        } catch (SQLException e) {
            dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການຄົ້ນຂໍ້ມູນປະເພດປຶ້ມ", e);
            return null;
        } finally {
            ps.close();
            //con.close();
        }
    }

    @Override
    public int saveData() throws SQLException {

        try {
            sql = "call type_Insert(?, ?); ";
            ps = con.prepareStatement(sql);
            ps.setString(1, getTypeId());
            ps.setString(2, getTypeName());
            return ps.executeUpdate();
        } catch (SQLException e) {
            dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການບັນທຶກຂໍ້ມູນປະເພດປຶ້ມ", e);
            return 0;
        } finally {
            ps.close();
            //con.close();
        }
    }

    @Override
    public int updateData() throws SQLException {
        try {
            sql = "call type_Update(?, ?);";
            ps = con.prepareStatement(sql);
            ps.setString(1, getTypeId());
            ps.setString(2, getTypeName());
            return ps.executeUpdate();
        } catch (SQLException e) {
            dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການແກ້ໄຂ້ຂໍ້ມູນປະເພດປຶ້ມ", e);
            return 0;
        } finally {
            ps.close();
            //con.close();
        }
    }

    @Override
    public int deleteData(String id) throws SQLException {
        try {
            sql = "call type_Delete(?);";
        ps = con.prepareStatement(sql);
        ps.setString(1, id);
        return ps.executeUpdate();
    } catch (SQLException e) {
        dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການລົບຂໍ້ມູນປະເພດປຶ້ມ", e);
        return 0;
    } finally {
        ps.close();
        //con.close();
    }
    }

}
