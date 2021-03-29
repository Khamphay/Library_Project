package com.mycompany.library_project.Controller;

import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;

import com.mycompany.library_project.Model.MemberModel;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class MemberController implements Initializable {

    @FXML
    private TableView<MemberModel> tableMember;

    @FXML
    private TableColumn<MemberModel, String> id, name, surname, gender, tel, village, district, province, depertment;
    @FXML
    private TableColumn<MemberModel, Date> birthdate, date_register, date_exist;

    private void initTable() {
        id.setCellValueFactory(new PropertyValueFactory<>("memberId"));
        name.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        surname.setCellValueFactory(new PropertyValueFactory<>("sureName"));
        gender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        tel.setCellValueFactory(new PropertyValueFactory<>("tel"));
        village.setCellValueFactory(new PropertyValueFactory<>("village"));
        district.setCellValueFactory(new PropertyValueFactory<>("district"));
        province.setCellValueFactory(new PropertyValueFactory<>("province"));
        birthdate.setCellValueFactory(new PropertyValueFactory<>("birdate"));
        depertment.setCellValueFactory(new PropertyValueFactory<>("detp"));
        date_register.setCellValueFactory(new PropertyValueFactory<>("dateRegister"));
        date_exist.setCellValueFactory(new PropertyValueFactory<>("dateRegisterEnd"));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTable();

    }

}
