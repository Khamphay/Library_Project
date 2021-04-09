package com.mycompany.library_project.Controller;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.mycompany.library_project.ControllerDAOModel.*;
import com.mycompany.library_project.Model.MemberModel;

import javafx.collections.*;
import javafx.fxml.*;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.*;
import javafx.scene.layout.*;

public class MemberController implements Initializable {

    private ObservableList<MemberModel> data = null;
    private AlertMessage alertMessage = new AlertMessage();
    private MemberModel memberModel = new MemberModel();
    private ResultSet rs = null;
    private DialogMessage dialog = null;

    @FXML
    private BorderPane boderPane;

    @FXML
    private StackPane stackPane;

    @FXML
    private TableView<MemberModel> tableMember;

    @FXML
    private TableColumn<MemberModel, String> id, name, surname, gender, tel, village, district, province, depertment;

    @FXML
    private TableColumn<MemberModel, Date> birthdate, date_register, date_exist;

    @FXML
    private TableColumn<MemberModel, JFXButton> colAction;

    private void showData() {
        try {
            data = FXCollections.observableArrayList();
            rs = memberModel.findAll();
            while (rs.next()) {
                data.add(new MemberModel(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
                        rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8),
                        Date.valueOf(rs.getString(10)), rs.getString(9), Date.valueOf(rs.getString(11)),
                        Date.valueOf(rs.getString(12)), Date.valueOf(rs.getString(13)), btDelete(rs.getString(1))));
            }
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
        colAction.setCellValueFactory(new PropertyValueFactory<>("action"));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTable();
        showData();
    }

    private JFXButton btDelete(String id) {
        JFXButton delete = new JFXButton("ລົບ");
        final Image img = new Image("/com/mycompany/library_project/Icon/bin.png");
        final ImageView imgView = new ImageView();
        imgView.setImage(img);
        imgView.setFitWidth(20);
        imgView.setFitHeight(20);
        delete.setId(id);
        delete.setGraphic(imgView);
        delete.setStyle("-fx-background-radius: 2em; -fx-background-color:#101D3D; -fx-text-fill:#FFF;");
        delete.setOnAction(e -> {
            JFXButton[] buttons = { buttonYes(delete.getId()), buttonNo(), buttonCancel() };
            dialog = new DialogMessage(stackPane, "ຄຳເຕືອນ", "ຕ້ອງການລົບຂໍ້ມູນອອກບໍ?",
                    JFXDialog.DialogTransition.CENTER, buttons, false);
            dialog.showDialog();
        });
        return delete;
    }

    private JFXButton buttonYes(String memberid) {
        JFXButton btyes = new JFXButton("ຕົກລົງ");
        btyes.setStyle(
                "-fx-border-radius: 0.5em; -fx-border-color: derive(#060621, 95%); -fx-background-radius: 0.5em;");
        btyes.setOnAction(e -> {
            // Todo: Delete Data
            try {
                if (memberModel.deleteData(memberid) > 0) {
                    showData();
                    dialog.closeDialog();
                    alertMessage.showCompletedMessage("Delete", "Delete data successfully.", 4, Pos.BOTTOM_RIGHT);
                }
            } catch (SQLException ex) {
                alertMessage.showErrorMessage(stackPane, "Delete", "Error: " + ex.getMessage(), 4, Pos.BOTTOM_RIGHT);
            }
        });
        return btyes;
    }

    private JFXButton buttonNo() {
        JFXButton btno = new JFXButton("  ບໍ່  ");
        btno.setStyle(
                "-fx-border-radius: 0.5em; -fx-border-color: derive(#060621, 95%); -fx-background-radius: 0.5em;");
        btno.setOnAction(e -> {
            dialog.closeDialog();
        });
        return btno;
    }

    private JFXButton buttonCancel() {
        JFXButton btcancel = new JFXButton("ຍົກເລີກ");
        btcancel.setStyle(
                "-fx-border-radius: 0.5em; -fx-border-color: derive(#060621, 95%); -fx-background-radius: 0.5em;");
        btcancel.setOnAction(e -> {
            dialog.closeDialog();
        });
        return btcancel;
    }

}
