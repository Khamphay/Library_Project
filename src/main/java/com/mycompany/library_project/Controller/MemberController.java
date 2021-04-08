package com.mycompany.library_project.Controller;

import java.net.URL;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ResourceBundle;

import com.mycompany.library_project.ControllerDAOModel.*;
import com.mycompany.library_project.Model.MemberModel;

import javafx.collections.*;
import javafx.fxml.*;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;

public class MemberController implements Initializable {

    private ObservableList<MemberModel> data = null;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private AlertMessage alertMessage = new AlertMessage();

    @FXML
    private BorderPane boderPane;
    
    @FXML
    private TableView<MemberModel> tableMember;

    @FXML
    private TableColumn<MemberModel, String> id, name, surname, gender, tel, village, district, province, depertment;

    @FXML
    private TableColumn<MemberModel, Date> birthdate, date_register, date_exist;

    private void showData() {
        try {
            data = FXCollections.observableArrayList();
            data.add(new MemberModel("FNS0349.17", "ຄຳໄຟ", "ເສຍລີມົວ", "ຊາຍ", "02076736453", "ດົງໂດກ", "ໍໄຊທານີ",
                    "ນະຄອນຫຼວງວຽງຈັນ", Date.valueOf(LocalDate.of(2021, 8, 20)), "ວິທະຍາສາດຄອມພິວເຕິ",
                    Date.valueOf(LocalDate.of(2021, 8, 20)), Date.valueOf(LocalDate.of(2021, 8, 20))));
            tableMember.setItems(data);
        } catch (Exception e) {
            alertMessage.showErrorMessage(boderPane, "Show Data Error", "Error: " + e.getMessage(), 4,
                    Pos.BOTTOM_RIGHT);
        }
    }

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
        showData();
    }

}
