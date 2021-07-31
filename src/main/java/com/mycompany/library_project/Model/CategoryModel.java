package com.mycompany.library_project.Model;

import com.mycompany.library_project.Controller.HomeController;
import com.mycompany.library_project.ControllerDAOModel.DataAccessObject;
import com.mycompany.library_project.ControllerDAOModel.DialogMessage;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CategoryModel implements DataAccessObject {

    private DialogMessage dialog = new DialogMessage();
    private PreparedStatement ps = null;
    private String sql = "";
    // private Connection con = MyConnection.getConnect();
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
          rs = HomeController.con.createStatement().executeQuery(sql);
          return rs;
      } catch (SQLException e) {
          dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການໂຫຼດຂໍ້ມູນນໝວດປຶ້ມ", e);
          return null;
      } 

    }

    @Override
    public ResultSet findById(String id) throws SQLException {
        try {
            sql = "call category_ShowById(?);";
            ps = HomeController.con.prepareStatement(sql);
            ps.setString(1, id);
            rs = ps.executeQuery();
            return rs;
        } catch (SQLException e) {
            dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການໂຫຼດຂໍ້ມູນນໝວດປຶ້ມ", e);
            return null;
        } finally {
            //ps.close();
        }

    }

    @Override
    public ResultSet findByName(String name) throws SQLException {
        try {
            sql = "call category_ShowByName(?);";
            ps = HomeController.con.prepareStatement(sql);
            ps.setString(1, name);
            rs = ps.executeQuery();
            return rs;
        } catch (SQLException e) {
            dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການໂຫຼດຂໍ້ມູນນໝວດປຶ້ມ", e);
            return null;
        } finally {
            //ps.close();
        }
    }

    @Override
    public ResultSet searchData(String values) throws SQLException {
        try {
           sql = "call category_Search(?);";
           ps = HomeController.con.prepareStatement(sql);
           ps.setString(1, values);
           rs = ps.executeQuery();
           return rs;
       } catch (SQLException e) {
           dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການຄົ້ນຫາຂໍ້ມູນນໝວດປຶ້ມ", e);
           return null;
       } finally {
           //ps.close();
       }

    }

    @Override
    public int saveData() throws SQLException {
        try {
            sql = "call category_Insert(?, ?);";
            ps = HomeController.con.prepareStatement(sql);
            ps.setString(1, getCatgId());
            ps.setString(2, getCatgName());
            return ps.executeUpdate();
        } catch (SQLException e) {
            dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການບັນທືກຂໍ້ມູນນໝວດປຶ້ມ", e);
            return 0;
        } finally {
            //ps.close();
        }
    }

    @Override
    public int updateData() throws SQLException {
        try {
            sql = "call category_Update(?, ?);";
            ps = HomeController.con.prepareStatement(sql);
            ps.setString(1, getCatgId());
            ps.setString(2, getCatgName());
            return ps.executeUpdate();
        } catch (SQLException e) {
            dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການແກ້ໄຂ້ຂໍ້ມູນນໝວດປຶ້ມ", e);
            return 0;
        } finally {
            //ps.close();
        }

    }

    @Override
    public int deleteData(String id) throws SQLException {
        try {
            sql = "call category_Delete(?);";
            ps = HomeController.con.prepareStatement(sql);
            ps.setString(1, id);
            return ps.executeUpdate();
        } catch (SQLException e) {
            dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການລົບຂໍ້ມູນນໝວດປຶ້ມ", e);
            return 0;
        } finally {
            //ps.close();
        }
    }
}
