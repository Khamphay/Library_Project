package com.mycompany.library_project.Model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;

import com.jfoenix.controls.JFXButton;
import com.mycompany.library_project.MyConnection;
import com.mycompany.library_project.ControllerDAOModel.DataAccessObject;

public class BookDetailModel implements DataAccessObject {

    private Connection con = MyConnection.getConnect();
    private ResultSet rs = null;
    String sql = null;

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
    private String detail;
    private JFXButton action;

    public BookDetailModel() {
    }

    public BookDetailModel(String bookId, String barcode) {
        this.bookId = bookId;
        this.barcode = (barcode);
    }

    public BookDetailModel(String bookId, String bookName, String ISBN, Integer page, Integer qty, String catgId,
            String typeId, String detail, JFXButton action) {

        this.bookId = bookId;
        this.bookName = bookName;
        this.ISBN = ISBN;
        this.page = page;
        this.qty = qty;
        this.catgId = catgId;
        this.typeId = typeId;
        this.detail = detail;
        this.action = action;

    }

    public BookDetailModel(String bookId, String bookName, Integer page, String ISBN, Integer qty, Integer rentQty,
            Integer reserQty, String barcode, String catgId, String typeId, String tableLogId, String detail) {
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
        this.tableLogId = tableLogId;
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

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public JFXButton getAction() {
        return action;
    }

    public void setAction(JFXButton action) {
        this.action = action;
    }

    public String[] showBookType() throws SQLException {
        sql = "Select typename From tbtype";
        String[] type = null;
        rs = con.createStatement().executeQuery(sql);
        if (rs.getRow() > 0) {
            while (rs.next()) {
                type = new String[] { rs.getString(1) };
            }
        }
        return type;
    }

    @Override
    public ResultSet findAll() throws SQLException {
        try {
            sql = "call book_ShowAll();";
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
        // TODO Auto-generated method stub
        return 0;
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
}
