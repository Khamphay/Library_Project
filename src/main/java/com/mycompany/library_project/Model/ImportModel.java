package com.mycompany.library_project.Model;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;

import com.mycompany.library_project.Controller.HomeController;
import com.mycompany.library_project.ControllerDAOModel.DataAccessObject;
import com.mycompany.library_project.ControllerDAOModel.DialogMessage;

public class ImportModel implements DataAccessObject {

    // private Connection con = MyConnection.getConnect();
    private String sql = null;
    private PreparedStatement ps = null;
    private ResultSet rs = null;

    private DialogMessage dialog = new DialogMessage();

    private String importid;
    private String bookid;
    private String bookname;
    private String bookisbn;
    private int bookpage;
    private String bookcategory;
    private String booktype;
    private String writeYear;
    private String tableid;
    private String tbalelog;
    private int qty, totalQty;
    private double price, totalPrice;
    private Date importDate;

    public ImportModel() {
    }

    public ImportModel(String importid, String bookid, int qty, double price) {
        this.importid = importid;
        this.bookid = bookid;
        this.qty = qty;
        this.price = price;
    }

    public ImportModel(String importid, int totalQty, double totalPrice, Date importDate) {
        this.importid = importid;
        this.totalQty = totalQty;
        this.totalPrice = totalPrice;
        this.importDate = importDate;
    }

    public ImportModel(String bookid, String bookname, String bookisbn, int bookpage, String bookcategory,
            String booktype, String writeYear, String tableid, String tbalelog, int qty, double price,
            Double totalPrice) {
        this.bookid = bookid;
        this.bookname = bookname;
        this.bookisbn = bookisbn;
        this.bookpage = bookpage;
        this.bookcategory = bookcategory;
        this.booktype = booktype;
        this.writeYear = writeYear;
        this.tableid = tableid;
        this.tbalelog = tbalelog;
        this.qty = qty;
        this.price = price;
        this.totalPrice = totalPrice;
    }

    public String getImportid() {
        return importid;
    }

    public void setImportid(String importid) {
        this.importid = importid;
    }

    public String getBookid() {
        return bookid;
    }

    public void setBookid(String bookid) {
        this.bookid = bookid;
    }

    public String getBookname() {
        return bookname;
    }

    public void setBookname(String bookname) {
        this.bookname = bookname;
    }

    public String getBookisbn() {
        return bookisbn;
    }

    public void setBookisbn(String bookisbn) {
        this.bookisbn = bookisbn;
    }

    public int getBookpage() {
        return bookpage;
    }

    public void setBookpage(int bookpage) {
        this.bookpage = bookpage;
    }

    public String getBookcategory() {
        return bookcategory;
    }

    public void setBookcategory(String bookcategory) {
        this.bookcategory = bookcategory;
    }

    public String getBooktype() {
        return booktype;
    }

    public void setBooktype(String booktype) {
        this.booktype = booktype;
    }

    public String getWriteYear() {
        return writeYear;
    }

    public void setWriteYear(String writeYear) {
        this.writeYear = writeYear;
    }

    public String getTableid() {
        return tableid;
    }

    public void setTableid(String tableid) {
        this.tableid = tableid;
    }

    public String getTbalelog() {
        return tbalelog;
    }

    public void setTbalelog(String tbalelog) {
        this.tbalelog = tbalelog;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public int getTotalQty() {
        return totalQty;
    }

    public void setTotalQty(int totalQty) {
        this.totalQty = totalQty;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Date getImportDate() {
        return importDate;
    }

    public void setImportDate(Date importDate) {
        this.importDate = importDate;
    }

    @Override
    public ResultSet findAll() throws SQLException {
        // TODO Auto-generated method stub
        return null;
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
            sql = "call import_Insert(?, ?, ?, ?);";
            ps = HomeController.con.prepareStatement(sql);
            ps.setString(1, getImportid());
            ps.setInt(2, getTotalQty());
            ps.setDouble(3, getTotalPrice());
            ps.setDate(4, getImportDate());
            return ps.executeUpdate();
        } catch (SQLException e) {
            dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການບັນທືກຂໍ້ມູນນຳປຶ້ມເຂົ້າ", e);
            return 0;
        } finally {
            // ps.close();
        }
    }

    public int saveDataImportDetail(/* List<ImportModel> list */ String impID) throws SQLException, ParseException {
        try {

            sql = "call inertImport_Detail(?, ?, ?, ?);";
            ps = HomeController.con.prepareStatement(sql);
            ps.setString(1, getImportid());
            ps.setString(2, getBookid());
            ps.setInt(3, getQty());
            ps.setDouble(4, getPrice());
            return ps.executeUpdate();

            /*
             * sql = "Insert into tbimport_book_detail values(?, ?, ?, ?)"; // ps =
             * HomeController.con.prepareStatement(sql); int result = 0; for (ImportModel
             * importModel : list) { ps.setString(1, importModel.getImportid());
             * ps.setString(2, importModel.getBookid()); ps.setInt(3, importModel.getQty());
             * ps.setDouble(4, importModel.getPrice()); ps.addBatch(); if (result % 100 == 0
             * || result == list.size()) { ps.executeBatch(); result = 1; } } return result;
             */
        } catch (SQLException e) {
            dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການບັນທືກຂໍ້ມູນລາຍລະອຽດນຳປຶ້ມເຂົ້າ", e);
            this.deleteData(impID);
            return 0;
        } finally {
            // ps.close();
        }
    }

    @Override
    public int updateData() throws SQLException, ParseException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int deleteData(String id) throws SQLException {
        // TODO Auto-generated method stub
        return 0;
    }

    public int getMaxID() {
        try {
            sql = "select max(cast((substring(import_id,4,length(import_id))) as int)) as maxid from tbimport_book;";
            rs = HomeController.con.createStatement().executeQuery(sql);
            if (rs.next()) {
                return rs.getInt("maxid");
            } else {
                return 0;
            }
        } catch (SQLException e) {
            dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາຈັດການລະຫັດການນຳປຶ້ມເຂົ້າ", e);
            return 0;
        }
    }

}
