package com.mycompany.library_project.Controller.Model;

import javafx.beans.property.*;

public class BookLostModel {
    private StringProperty lostId;
    private StringProperty barcode;
    private IntegerProperty qty;
    private DoubleProperty fineCost;
    private StringProperty date;

    public BookLostModel(String lostId, String barcode, Integer qty, Double fineCost, String date) {
        this.lostId = new SimpleStringProperty(lostId);
        this.barcode = new SimpleStringProperty(barcode);
        this.qty = new SimpleIntegerProperty(qty);
        this.fineCost = new SimpleDoubleProperty(fineCost);
        this.date = new SimpleStringProperty(date);
    }

    public String getLostId() {
        return lostId.get();
    }

    public StringProperty lostIdProperty() {
        return lostId;
    }

    public void setLostId(String lostId) {
        this.lostId.set(lostId);
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

    public double getFineCost() {
        return fineCost.get();
    }

    public DoubleProperty fineCostProperty() {
        return fineCost;
    }

    public void setFineCost(double fineCost) {
        this.fineCost.set(fineCost);
    }

    public String getDate() {
        return date.get();
    }

    public StringProperty dateProperty() {
        return date;
    }

    public void setDate(String date) {
        this.date.set(date);
    }
}
