package com.mycompany.library_project.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import com.jfoenix.controls.*;
import com.mycompany.library_project.Model.*;

public class AddBookController implements Initializable {

    private ObservableList<String> status = FXCollections.observableArrayList("ຫວ່າງ", "ກຳລົງຢືມ", "ເສຍ");;
    private ObservableList<String> items = null;
    private TypeModel type = null;
    private CategoryModel category = null;
    private TableLogModel tablle = null;

    @FXML
    private JFXButton btclose;

    @FXML
    private JFXTextArea txtBarcode;

    @FXML
    private JFXComboBox cmbType, cmbCagtegory, cmbTable, cmbtableLog, cmbStatus;

    @FXML
    private void coloseForm() {

    }

    private void fillType() {
        try {
            items = FXCollections.observableArrayList();
            type = new TypeModel();
            ResultSet rs = type.findAll();
            while (rs.next()) {
                items.add(rs.getString(2));
            }
            cmbType.setItems(items);
        } catch (Exception e) {
            System.out.println("Error fill value type: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void fillCategory() {
        try {
            items = FXCollections.observableArrayList();
            category = new CategoryModel();
            ResultSet rs = category.findAll();
            while (rs.next()) {
                items.add(rs.getString(2));
            }
            cmbCagtegory.setItems(items);
        } catch (Exception e) {
            System.out.println("Error fill value category: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void fillTable() {
        try {
            items = FXCollections.observableArrayList();
            tablle = new TableLogModel();
            // ResultSet rs = tablle.findAll();
            // while (rs.next()) {
            // items.add(rs.getString(2));
            // }
            // cmbTable.setItems(items);
        } catch (Exception e) {
            System.out.println("Error fill value tablel: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cmbStatus.setItems(status);
        fillType();
        fillCategory();
        fillTable();
    }
}
