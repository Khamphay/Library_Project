package com.mycompany.library_project.Controller.Model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import com.mycompany.library_project.Controller.ControllerDAOModel.DataAccessObject;

import java.sql.Connection;
import java.sql.SQLException;

public class BookDetailModel {
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

    public BookDetailModel(String bookId, String bookName, Integer page, String ISBN, Integer qty, Integer rentQty, Integer reserQty, String barcode, String catgId, String typeId, String tableLogId) {
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

}
