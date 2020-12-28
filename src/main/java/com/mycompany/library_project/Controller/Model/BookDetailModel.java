package com.mycompany.library_project.Controller.Model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;

import com.mycompany.library_project.MyConnection;
import com.mycompany.library_project.Controller.ControllerDAOModel.DataAccessObject;

public class BookDetailModel implements DataAccessObject {

    private Connection con = MyConnection.getConnect();
    private ResultSet rs = null;
    String sql = null;

    private StringProperty bookId;
    private StringProperty bookName;
    private IntegerProperty page;
    private StringProperty ISBN;
    private IntegerProperty qty;
    private IntegerProperty rentQty;
    private IntegerProperty reserQty;
    private StringProperty barcode;
    private StringProperty catgId;
    private StringProperty typeId;
    private StringProperty tableLogId;

    public BookDetailModel(String bookId, String barcode) {
        this.bookId = new SimpleStringProperty(bookId);
        this.barcode = new SimpleStringProperty(barcode);
    }

    public BookDetailModel(String bookId, String bookName, Integer page, String ISBN, Integer qty, Integer rentQty,
            Integer reserQty, String barcode, String catgId, String typeId, String tableLogId) {
        this.bookId = new SimpleStringProperty(bookId);
        this.bookName = new SimpleStringProperty(bookName);
        this.page = new SimpleIntegerProperty(page);
        this.ISBN = new SimpleStringProperty(ISBN);
        this.qty = new SimpleIntegerProperty(qty);
        this.rentQty = new SimpleIntegerProperty(rentQty);
        this.reserQty = new SimpleIntegerProperty(reserQty);
        this.barcode = new SimpleStringProperty(barcode);
        this.catgId = new SimpleStringProperty(catgId);
        this.typeId = new SimpleStringProperty(typeId);
        this.tableLogId = new SimpleStringProperty(tableLogId);
    }

    public String getBookId() {
        return bookId.get();
    }

    public StringProperty bookIdProperty() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId.set(bookId);
    }

    public String getBookName() {
        return bookName.get();
    }

    public StringProperty bookNameProperty() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName.set(bookName);
    }

    public int getPage() {
        return page.get();
    }

    public IntegerProperty pageProperty() {
        return page;
    }

    public void setPage(int page) {
        this.page.set(page);
    }

    public String getISBN() {
        return ISBN.get();
    }

    public StringProperty ISBNProperty() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN.set(ISBN);
    }

    public int getQty() {
        return qty.get();
    }

    public IntegerProperty qtyProperty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty.set(qty);
    }

    public int getRentQty() {
        return rentQty.get();
    }

    public IntegerProperty rentQtyProperty() {
        return rentQty;
    }

    public void setRentQty(int rentQty) {
        this.rentQty.set(rentQty);
    }

    public int getReserQty() {
        return reserQty.get();
    }

    public IntegerProperty reserQtyProperty() {
        return reserQty;
    }

    public void setReserQty(int reserQty) {
        this.reserQty.set(reserQty);
    }

    public String getBarcode() {
        return barcode.get();
    }

    public StringProperty barcodeProperty() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode.set(barcode);
    }

    public String getCatgId() {
        return catgId.get();
    }

    public StringProperty catgIdProperty() {
        return catgId;
    }

    public void setCatgId(String catgId) {
        this.catgId.set(catgId);
    }

    public String getTypeId() {
        return typeId.get();
    }

    public StringProperty typeIdProperty() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId.set(typeId);
    }

    public String getTableLogId() {
        return tableLogId.get();
    }

    public StringProperty tableLogIdProperty() {
        return tableLogId;
    }

    public void setTableLogId(String tableLogId) {
        this.tableLogId.set(tableLogId);
    }


    public String[] showBookType() throws SQLException {
        sql = "Select typename From tbtype";
        String[] type = null;
        rs = con.createStatement().executeQuery(sql);
        if (rs.getRow() > 0) {
            while (rs.next()) {
                type=new String[]{rs.getString(1)};
            }
        }
        return type;
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
