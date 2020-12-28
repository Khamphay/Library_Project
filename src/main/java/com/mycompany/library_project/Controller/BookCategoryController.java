package com.mycompany.library_project.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.mycompany.library_project.Controller.Model.CategoryModel;

public class BookCategoryController implements Initializable {
    
    private CategoryModel category = null;
    private ObservableList<CategoryModel> data = null;
    private ResultSet rs = null;
    @FXML
    private JFXTextField txtcatgId, txtcatgName;

    @FXML
    private JFXButton btSave, btEdite, btDelete, btCancel;

    @FXML
    private TableView<CategoryModel> tableCategory;

    @FXML
    private TableColumn<CategoryModel, String> colCatgId, colCatgName;
    
    private void showData() {
        try {
            data = FXCollections.observableArrayList();
            category = new CategoryModel();
            rs = category.findAll();
            while (rs.next()) {
                data.add(new CategoryModel(rs.getString(1), rs.getString(2)));
            }
            tableCategory.setItems(data);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    private void ClearData() {
        txtcatgId.setText("");
        txtcatgName.setText("");
    }

    @FXML
    private void selectTable(MouseEvent clickEvent) {
        if (clickEvent.getClickCount() > 0 && tableCategory.getSelectionModel().getSelectedItem() != null) {
            CategoryModel selectCatg = tableCategory.getSelectionModel().getSelectedItem();
            txtcatgId.setText(selectCatg.getCatgId());
            txtcatgName.setText(selectCatg.getCatgName());
        }
    }

    @FXML
    private void Clear(ActionEvent event) {
        ClearData();
    }

    @FXML
    private void Save(ActionEvent event) {
        try {
            category = new CategoryModel(txtcatgId.getText(), txtcatgName.getText());
            if (category.saveData() > 0) {
                showData();
                ClearData();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    @FXML
    private void Update(ActionEvent event) {
        try {
            category = new CategoryModel(txtcatgId.getText(), txtcatgName.getText());
            if (category.updateData() > 0) {
                showData();
                ClearData();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    @FXML
    private void Dalete() {
        try {
            category = new CategoryModel();
            if (category.deleteData(txtcatgId.getText()) > 0) {
                showData();
                ClearData();
            }
        } catch (Exception e) {
            //TODO: handle exception
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colCatgId.setCellValueFactory(new PropertyValueFactory<>("catgId"));
        colCatgName.setCellValueFactory(new PropertyValueFactory<>("catgName"));
        showData();
    }
}
