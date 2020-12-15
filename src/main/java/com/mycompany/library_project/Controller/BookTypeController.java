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
import com.mycompany.library_project.Controller.Model.TypeModel;
import com.mycompany.library_project.MyConnection;

import javax.swing.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class BookTypeController implements Initializable {

    private Connection con = MyConnection.getConnect();
    private ResultSet rs;
    private TypeModel type = null;
    private ObservableList<TypeModel> data;
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
            query = "Select * From tbtype;";
            rs = con.createStatement().executeQuery(query);
            while (rs.next()) {
                data.add(new TypeModel(rs.getString(1), rs.getString(2)));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getSQLState());
        }
        colId.setCellValueFactory(new PropertyValueFactory<>("typeId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("typeName"));

        tableType.setItems(data);
    }

    private void ClearData() {
        txtTypeId.setText("");
        txtTypeName.setText("");
    }

    @FXML
    private void selectTableType(MouseEvent clickEvent) {
        if (clickEvent.getClickCount() > 1 && tableType.getSelectionModel().getSelectedItem() != null) {
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
        type = new TypeModel(txtTypeId.getText());
        if (type.deleteData() == 1) {
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
        ShowData();
    }
}
