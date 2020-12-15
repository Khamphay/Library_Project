package com.mycompany.library_project.Controller.Model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class BooksModel {
    private StringProperty bookId;
    private StringProperty barcode;
    private StringProperty status;

    public BooksModel(String bookId, String barcode, String status) {
        this.bookId = new SimpleStringProperty(bookId);
        this.barcode = new SimpleStringProperty(barcode);
        this.status = new SimpleStringProperty(status);
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

    public String getBarcode() {
        return barcode.get();
    }

    public StringProperty barcodeProperty() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode.set(barcode);
    }

    public String getStatus() {
        return status.get();
    }

    public StringProperty statusProperty() {
        return status;
    }

    public void setStatus(String status) {
        this.status.set(status);
    }
}
