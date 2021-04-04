package com.mycompany.library_project.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.jfoenix.controls.*;
import com.mycompany.library_project.Model.*;

public class AddBookController implements Initializable {

    private ObservableList<String> status = FXCollections.observableArrayList("ຫວ່າງ", "ກຳລົງຢືມ", "ເສຍ");;
    private ObservableList<String> items = null;
    private TypeModel type = null;
    private CategoryModel category = null;
    private TableLogModel tablle = null;
    private BookDetailModel bookDetail = null;
    private ArrayList<String> arr_type, arr_category, arr_table;
    private int index_type, index_category, index_table;

    @FXML
    private TextField txtId, txtName, txtPage, txtQty, txtISBN;

    @FXML
    private JFXButton btclose;

    @FXML
    private TextArea txtBarcode;

    @FXML
    private ComboBox cmbType, cmbCagtegory, cmbTable, cmbtableLog, cmbStatus;

    @FXML
    private void coloseForm() {

    }

    private void fillType() {

        // Todo: set event to combo box
        cmbType.setOnAction(e -> {
            index_type = cmbType.getSelectionModel().getSelectedIndex();
        });

        // Todo: set value
        try {
            arr_type = new ArrayList<>();
            items = FXCollections.observableArrayList();
            type = new TypeModel();
            ResultSet rs = type.findAll();
            while (rs.next()) {
                items.add(rs.getString(2));
                arr_type.add(rs.getString(1));
            }
            cmbType.setItems(items);
        } catch (Exception e) {
            System.out.println("Error fill value type: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void fillCategory() {

        // Todo: set event to combo box
        cmbCagtegory.setOnAction(e -> {
            index_category = cmbCagtegory.getSelectionModel().getSelectedIndex();
        });

        // Todo: set value
        try {
            arr_category = new ArrayList<>();
            items = FXCollections.observableArrayList();
            category = new CategoryModel();
            ResultSet rs = category.findAll();
            while (rs.next()) {
                arr_category.add(rs.getString(1));
                items.add(rs.getString(2));
            }
            cmbCagtegory.setItems(items);
        } catch (Exception e) {
            System.out.println("Error fill value category: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void fillTable() {

        // Todo: set event to combo box
        cmbTable.setOnAction(e -> {
            index_table = cmbTable.getSelectionModel().getSelectedIndex();
        });

        // Todo: set value
        try {
            arr_table = new ArrayList<>();
            items = FXCollections.observableArrayList();
            tablle = new TableLogModel();
            // ResultSet rs = tablle.findAll();
            // while (rs.next()) {
            // arr_table.add(rs.getString(1));
            // items.add(rs.getString(2));
            // }
            // cmbTable.setItems(items);
        } catch (Exception e) {
            System.out.println("Error fill value tablel: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void btSave(ActionEvent event) {
        // cmbType.g
        try {
            bookDetail = new BookDetailModel(txtId.getText(), txtName.getText(), txtISBN.getText(),
                    Integer.parseInt(txtPage.getText()), Integer.parseInt(txtQty.getText()),
                    arr_category.get(index_category), arr_type.get(index_type), arr_table.get(index_table),
                    cmbStatus.getSelectionModel().getSelectedItem().toString());
            if (bookDetail.saveData() > 0) {
                System.out.println("Save Completed");
            }
        } catch (Exception e) {
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
