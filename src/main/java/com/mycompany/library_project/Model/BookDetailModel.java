package com.mycompany.library_project.Model;

import java.sql.*;
import java.text.ParseException;

import com.mycompany.library_project.MyConnection;
import com.mycompany.library_project.ControllerDAOModel.DataAccessObject;
import com.mycompany.library_project.config.CreateLogFile;

public class BookDetailModel implements DataAccessObject {

    private Connection con = MyConnection.getConnect();
    private ResultSet rs = null;
    private PreparedStatement ps = null;
    private CreateLogFile logfile = new CreateLogFile();

    private String sql = null;
    private String bookId;
    private String bookName;
    private Integer page;
    private String ISBN;
    private Integer qty;
    private Integer rentQty;
    private Integer reserQty;
    private String barcode;
    private String catgId;
    private String typeId;
    private String tableLogId;
    private String tableId;
    private String detail;
    private String status;

    public BookDetailModel() {
    }

    public BookDetailModel(String barcode, String tableLogId, String status) {
        this.barcode = barcode;
        this.tableLogId = tableLogId;
        this.status = status;
    }

    public BookDetailModel(String bookId, String bookName, String ISBN, Integer page, Integer qty, String catgId,
            String typeId, String tableId, String detail) {

        this.bookId = bookId;
        this.bookName = bookName;
        this.ISBN = ISBN;
        this.page = page;
        this.qty = qty;
        this.catgId = catgId;
        this.typeId = typeId;
        this.tableId = tableId;
        this.detail = detail;

    }

    public BookDetailModel(String bookId, String bookName, String ISBN, Integer page, Integer qty, Integer rentQty,
            Integer reserQty, String barcode, String catgId, String typeId, String tableId, String detail) {
        this.bookId = bookId;
        this.bookName = bookName;
        this.page = page;
        this.ISBN = ISBN;
        this.qty = qty;
        this.rentQty = rentQty;
        this.reserQty = reserQty;
        this.barcode = barcode;
        this.catgId = catgId;
        this.typeId = typeId;
        this.tableId = tableId;
        this.detail = detail;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String iSBN) {
        ISBN = iSBN;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public Integer getRentQty() {
        return rentQty;
    }

    public void setRentQty(Integer rentQty) {
        this.rentQty = rentQty;
    }

    public Integer getReserQty() {
        return reserQty;
    }

    public void setReserQty(Integer reserQty) {
        this.reserQty = reserQty;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getCatgId() {
        return catgId;
    }

    public void setCatgId(String catgId) {
        this.catgId = catgId;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getTableLogId() {
        return tableLogId;
    }

    public void setTableLogId(String tableLogId) {
        this.tableLogId = tableLogId;
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Todo: Book Detail
    @Override
    public ResultSet findAll() throws SQLException {
        try {
            sql = "call book_detail_ShowAll();";
            rs = con.createStatement().executeQuery(sql);
            return rs;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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

        sql = "call book_detail_Insert(?, ?, ?, ?, ?, ?, ?, ?, ?);";
        ps = con.prepareStatement(sql);
        ps.setString(1, getBookId());
        ps.setString(2, getBookName());
        ps.setString(3, getISBN());
        ps.setInt(4, getPage());
        ps.setInt(5, getQty());
        ps.setString(6, getDetail());
        ps.setString(7, getCatgId());
        ps.setString(8, getTypeId());
        ps.setString(9, getTableId());
        return ps.executeUpdate();
    }

    @Override
    public int updateData() throws SQLException, ParseException {

        sql = "call book_detail_Update(?, ?, ?, ?, ?, ?, ?, ?, ?);";
        ps = con.prepareStatement(sql);
        ps.setString(1, getBookId());
        ps.setString(2, getBookName());
        ps.setString(3, getISBN());
        ps.setInt(4, getPage());
        ps.setInt(5, getQty());
        ps.setString(6, getDetail());
        ps.setString(7, getCatgId());
        ps.setString(8, getTypeId());
        ps.setString(9, getTableId());
        return ps.executeUpdate();
    }

    public int updateBookQty(String book_id) throws SQLException {
        sql = "Update tbbooks_detail set qty=qty-1 Where book_id=?";
        ps = con.prepareStatement(sql);
        ps.setString(1, book_id);
        return ps.executeUpdate();
    }

    @Override
    public int deleteData(String id) throws SQLException {

        sql = "call book_detail_Delete(?);";
        ps = con.prepareStatement(sql);
        ps.setString(1, id);
        return ps.executeUpdate();
    }

    // Todo: Book (Barcode)
    public ResultSet findBookByBarcode(String barcode) throws SQLException {
        try {
            sql = "call book_detail_ShowByBarcode('" + barcode + "');";
            rs = con.createStatement().executeQuery(sql);
            return rs;
        } catch (Exception e) {
            return null;
        }
    }

    public ResultSet showBarcode(String book_id) {
        try {
            sql = "call book_ShowByBookId(?);";
            ps = con.prepareStatement(sql);
            ps.setString(1, book_id);
            rs = ps.executeQuery();
            return rs;
        } catch (Exception e) {
            logfile.createLogFile("Load data of barcode", e);
            return null;
        }
    }

    public int saveBookBarCode(String barcode, String bookid, String table_log_id, String status) throws SQLException {
        sql = "call book_Insert(?, ?, ?, ?);";
        ps = con.prepareStatement(sql);
        ps.setString(1, barcode);
        ps.setString(2, bookid);
        ps.setString(3, table_log_id);
        ps.setString(4, status);
        return ps.executeUpdate();

    }

    public int updateData(String book_id, String old_barcode, String new_book_barcode, String table_log_id,
            String book_status) throws SQLException, ParseException {
        sql = "call book_Update(?, ?, ?, ?, ?);";
        ps = con.prepareStatement(sql);
        ps.setString(1, old_barcode);
        ps.setString(2, new_book_barcode);
        ps.setString(3, book_id);
        ps.setString(4, table_log_id);
        ps.setString(5, book_status);
        return ps.executeUpdate();
    }

    public int deleteBarcode(String book_id, String barcode) throws SQLException {
        sql = "call book_Delete(?,?);";
        ps = con.prepareStatement(sql);
        ps.setString(1, book_id);
        ps.setString(2, barcode);
        return ps.executeUpdate();
    }

    // Todo: Write Book
    public ResultSet showWrite(String book_id) {
        try {
            sql = "call  write_Show('" + book_id + "');";
            rs = con.createStatement().executeQuery(sql);
            return rs;
        } catch (Exception e) {
            return null;
        }
    }

    public int saveWrite(String book_id, String author_id, String year) throws SQLException {
        sql = "call write_Insert(?, ?, ?);";
        ps = con.prepareStatement(sql);
        ps.setString(1, book_id);
        ps.setString(2, author_id);
        ps.setString(3, year);
        return ps.executeUpdate();
    }

    public int updateWrite(String book_id, String newauthor_id, String oldauthor_id, String year) throws SQLException {
        sql = "call write_Update(?, ?, ?, ?);";
        ps = con.prepareStatement(sql);
        ps.setString(1, book_id);
        ps.setString(2, newauthor_id);
        ps.setString(3, oldauthor_id);
        ps.setString(4, year);
        return ps.executeUpdate();
    }

    public int updateStatus(String barcode, String status) throws SQLException {
        sql = " call book_UpdateStatus(?, ?);";
        ps = con.prepareStatement(sql);
        ps.setString(1, barcode);
        ps.setString(2, status);
        return ps.executeUpdate();
    }

    public int deleteWrite(String book_id) throws SQLException {
        sql = "call write_Update(?, ?, ?, ?);";
        ps = con.prepareStatement(sql);
        ps.setString(1, book_id);
        return ps.executeUpdate();
    }
}
