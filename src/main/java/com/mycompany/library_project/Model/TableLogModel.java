package com.mycompany.library_project.Model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.jfoenix.controls.JFXButton;
import com.mycompany.library_project.Controller.HomeController;
import com.mycompany.library_project.ControllerDAOModel.*;


public class TableLogModel implements DataAccessObject {

    private DialogMessage dialog = new DialogMessage();
    private PreparedStatement ps = null;
    // private Connection con = MyConnection.getConnect();
    private ResultSet rs = null;
    private String query = "";

    private String tableId;
    private String tableLog;
    private int logQty;
    private String qty;
    private String newLog;
    private JFXButton action;

    public TableLogModel() {
    }

    public TableLogModel(String tableLog) {
        this.tableLog = tableLog;
    }

    public TableLogModel(String tableId, String tableLog) {
        this.tableId = tableId;
        this.tableLog = tableLog;
    }

    public TableLogModel(String tableId, int qty) {
        this.tableId = tableId;
        this.logQty = qty;
    }

    public TableLogModel(String tableId, String qty, JFXButton action) {
        this.tableId = tableId;
        this.qty = qty;
        this.action = action;
    }

    public TableLogModel(String tableId, String tableLog, String newLog) {
        this.tableId = tableId;
        this.tableLog = tableLog;
        this.newLog = newLog;
    }

    public TableLogModel(String tableId, String tableLog, Integer qty) {
        this.tableId = tableId;
        this.tableLog = tableLog;
        this.logQty = qty;
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public String getTableLog() {
        return tableLog;
    }

    public void setTableLog(String tableLog) {
        this.tableLog = tableLog;
    }

    public int getLogQty() {
        return logQty;
    }

    public void setLogQty(int logQty) {
        this.logQty = logQty;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getNewLog() {
        return newLog;
    }

    public void setNewLog(String newLog) {
        this.newLog = newLog;
    }

    public JFXButton getAction() {
        return action;
    }

    public void setAction(JFXButton action) {
        this.action = action;
    }

    @Override
    public ResultSet findAll() throws SQLException {
        try {
            query = "call table_Show();";
            rs = HomeController.con.createStatement().executeQuery(query);
            return rs;
        } catch (SQLException e) {
            dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການໂຫຼດຂໍ້ມູນຕູ້", e);
            return null;
        }
    }

    public ResultSet findTable(String id) throws SQLException {
        try {
            query = "call table_ShowByID('" + id + "');";
            rs = HomeController.con.createStatement().executeQuery(query);
            return rs;
        } catch (SQLException e) {
            dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການໂຫຼດຂໍ້ມູນຕູ້", e);
            return null;
        }
    }

    @Override
    public ResultSet findById(String id) throws SQLException {
        try {
            query = "call  tablelog_ShowById('" + id + "');";
            rs = HomeController.con.createStatement().executeQuery(query);
            return rs;
        } catch (SQLException e) {
            dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການໂຫຼດຂໍ້ມູນລ໋ອກຕູ້", e);
            return null;
        }
    }

    public String findTableId(String tableLogId) throws SQLException {
        try {
            query = "Select tableid From tbtablelog Where tablelog='" + tableLogId + "'";
            rs = HomeController.con.createStatement().executeQuery(query);
            if (rs.next()) {
                return rs.getString("tableid");
            } else {
                return "";
            }
        } catch (SQLException e) {
            dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການໂຫຼດຂໍ້ມູນຕູ້", e);
            return "";
        }
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

    public int saveTable(String id, int qty) throws SQLException {
        try {
            query = "call  table_Insert(?, ?);";
            ps = HomeController.con.prepareStatement(query);
            ps.setString(1, id);
            ps.setInt(2, qty);
            return ps.executeUpdate();
        } catch (SQLException e) {
            dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການບັນທືກຂໍ້ມູນຕູ້", e);
            return 0;
        } finally {
            //ps.close();
        }
    }

    @Override
    public int saveData() throws SQLException {
        // Todo: Don't use try...catch'
        query = "call  tablelog_Insert(?, ?) ";
        ps = HomeController.con.prepareStatement(query);
        ps.setString(1, getTableId());
        ps.setString(2, getTableLog());
        int result = ps.executeUpdate();
        //ps.close();
        return result;
    }

    public int saveTableLog(List<TableLogModel> list, String id) throws SQLException {
        try {
            // query = "call tablelog_Insert(?,?)";
            query = "insert into tbtablelog values(?, ?)";
            ps = HomeController.con.prepareStatement(query);
            HomeController.con.setAutoCommit(false);
            int result = 0, count = 0;
            for (TableLogModel tableLog : list) {
                ps.setString(1, tableLog.getTableId());
                ps.setString(2, tableLog.getTableLog());
                ps.addBatch();
                count++;
                if (count % 100 == 0 || count == list.size()) {
                    ps.executeBatch();
                    HomeController.con.commit();
                    result = 1;
                }
            }
            return result;
        } catch (SQLException e) {
            try {
                this.deleteTable(id);
            } catch (SQLException ex) {
            }
            dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການບັນທືກຂໍ້ມູນລ໋ອກຕູ້", e);
            return 0;
        } finally {
            //ps.close();
        }
    }

    @Override
    public int updateData() throws SQLException {
        try {
            query = "call  table_Update(?, ?) ";
            ps = HomeController.con.prepareStatement(query);
            ps.setString(1, getTableId());
            ps.setInt(2, getLogQty());
            return ps.executeUpdate();
        } catch (SQLException e) {
            dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການແກ້ໄຂ້ຂໍ້ມູນລ໋ອກຕູ້", e);
            return 0;
        } finally {
            //ps.close();
        }
    }

    public int updateTableQty(String id) throws SQLException {
        try {
            query = "call table_UpdateQty(?);";
            ps = HomeController.con.prepareStatement(query);
            ps.setString(1, id);
            return ps.executeUpdate();
        } catch (SQLException e) {
            dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການແກ້ໄຂ້ຈຳນວນລ໋ອກຕູ້", e);
            return 0;
        } finally {
            //ps.close();
        }
    }

    @Override
    public int deleteData(String id) throws SQLException {
        try {
            query = "call tablelog_Delete(?);";
            ps = HomeController.con.prepareStatement(query);
            ps.setString(1, id);
            return ps.executeUpdate();
        } catch (SQLException e) {
            dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການລົບຂໍ້ມູນລ໋ອກຕູ້", e);
            return 0;
        } finally {
            //ps.close();
        }
    }

    public int deleteTable(String id) throws SQLException {
        try {
            query = "call table_Delete(?);";
            ps = HomeController.con.prepareStatement(query);
            ps.setString(1, id);
            return ps.executeUpdate();

        } catch (SQLException e) {
            dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການລົບຂໍ້ມູນຕູ້", e);
            return 0;
        } finally {
            //ps.close();
        }
    }
}
