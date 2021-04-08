package com.mycompany.library_project.Controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.mycompany.library_project.Model.EmployeeModel;

import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class EmployeeController implements Initializable {

    @FXML
    private TableView<EmployeeModel> tableEmployee;

    @FXML
    private TableColumn<EmployeeModel, String> id, name, surname, gender, tel;

    private void initTable() {
        id.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        name.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        surname.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        gender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        tel.setCellValueFactory(new PropertyValueFactory<>("tel"));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTable();
    }

}
