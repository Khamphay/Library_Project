package com.mycompany.library_project.Model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class RentBookModel {
    private StringProperty rentId;
    private StringProperty barcode;
    private IntegerProperty qty;
    private StringProperty status;

    public RentBookModel(String rentId, String barcode, Integer qty, String status) {
        this.rentId = new SimpleStringProperty(rentId);
        this.barcode = new SimpleStringProperty(barcode);
        this.qty = new SimpleIntegerProperty(qty);
        this.status = new SimpleStringProperty(status);
    }

    public String getRentId() {
        return rentId.get();
    }

    public StringProperty rentIdProperty() {
        return rentId;
    }

    public void setRentId(String rentId) {
        this.rentId.set(rentId);
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

    public int getQty() {
        return qty.get();
    }

    public IntegerProperty qtyProperty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty.set(qty);
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
