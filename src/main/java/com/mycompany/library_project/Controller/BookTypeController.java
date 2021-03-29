package com.mycompany.library_project.Controller;

import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import com.mycompany.library_project.Model.TypeModel;
import com.mycompany.library_project.MyConnection;

import javax.swing.*;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class BookTypeController implements Initializable {

    private Connection con = MyConnection.getConnect();
    private ResultSet rs;
    private TypeModel type = null;
    private ObservableList<TypeModel> data=null;
    private String query = "";

    @FXML
    JFXTextField txtTypeId;

    @FXML
    JFXTextField txtTypeName;

    @FXML
    private TableView<TypeModel> tableType;
    @FXML
    private TableColumn<TypeModel, String> colId;
    @FXML
    private TableColumn<TypeModel, String> colName;

    private void ShowData() {
        try {
            data = FXCollections.observableArrayList();
            type = new TypeModel();
            rs = type.findAll();
            while (rs.next()) {
                data.add(new TypeModel(rs.getString(1), rs.getString(2)));
            }
            tableType.setItems(data);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getSQLState());
        }
    }

    private void ClearData() {
        txtTypeId.setText("");
        txtTypeName.setText("");
    }

    @FXML
    private void selectTableType(MouseEvent clickEvent) {
        if (clickEvent.getClickCount() > 0 && tableType.getSelectionModel().getSelectedItem() != null) {
            TypeModel selectedType = tableType.getSelectionModel().getSelectedItem();
            txtTypeId.setText(selectedType.getTypeId());
            txtTypeName.setText(selectedType.getTypeName());
        }
    }

    @FXML
    private void Save(ActionEvent actionEvent) throws SQLException {
        type = new TypeModel(txtTypeId.getText(), txtTypeName.getText());
        if (type.saveData() == 1) {
            JOptionPane.showMessageDialog(null, "Save Type Completed!!!.", "Save", JOptionPane.INFORMATION_MESSAGE);
            ShowData();
            ClearData();
        }
    }

    @FXML
    private void Update(ActionEvent event) throws SQLException {
        type = new TypeModel(txtTypeId.getText(), txtTypeName.getText());
        if (type.updateData() == 1) {
            JOptionPane.showMessageDialog(null, "Update Type Completed!!!.", "Save", JOptionPane.INFORMATION_MESSAGE);
            ShowData();
            ClearData();
        }
    }

    @FXML
    private void Delete(ActionEvent event) throws SQLException {
        type = new TypeModel();
        if (type.deleteData(txtTypeId.getText()) == 1) {
            JOptionPane.showMessageDialog(null, "Delete Type Completed!!!.", "Save", JOptionPane.INFORMATION_MESSAGE);
            ShowData();
            ClearData();
        }
    }

    @FXML
    private void ClearText(ActionEvent event) {
        ClearData();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colId.setCellValueFactory(new PropertyValueFactory<>("typeId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("typeName"));
        ShowData();
    }
}
