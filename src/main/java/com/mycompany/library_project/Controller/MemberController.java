package com.mycompany.library_project.Controller;

import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.jfoenix.controls.*;
import com.mycompany.library_project.App;
import com.mycompany.library_project.Style;
import com.mycompany.library_project.ControllerDAOModel.*;
import com.mycompany.library_project.Model.MemberModel;

import javafx.collections.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.stage.*;

public class MemberController implements Initializable {

    private ObservableList<MemberModel> data = null;
    private AlertMessage alertMessage = new AlertMessage();
    private MemberModel memberModel = new MemberModel();
    private ResultSet rs = null;
    private DialogMessage dialog = null;
    private ArrayList<byte[]> byimg;
    public static Stage addMemberStage = null;

    @FXML
    private BorderPane boderPane;

    @FXML
    private StackPane stackPane;

    @FXML
    private JFXButton btAddUser;

    @FXML
    private MenuItem menuEdit, menuDelete;

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
            byimg = new ArrayList<>();
            while (rs.next()) {
                data.add(new MemberModel(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
                        rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8),
                        Date.valueOf(rs.getString(10)), rs.getString(9), Date.valueOf(rs.getString(11)),
                        Date.valueOf(rs.getString(12)), Date.valueOf(rs.getString(13)), btDelete(rs.getString(1))));
                byimg.add(rs.getBytes(14));
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

        tableMember.setOnMouseClicked(mevt -> {
            if (mevt.getClickCount() > 0 && tableMember.getSelectionModel().getSelectedItem() != null) {
                memberModel = new MemberModel();
                memberModel = tableMember.getSelectionModel().getSelectedItem();
            }
        });
    }

    private void initEvents() {
        btAddUser.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                try {
                    final Parent root = FXMLLoader.load(App.class.getResource("frmRegister.fxml"));
                    final Scene scene = new Scene(root);
                    addMemberStage = new Stage();
                    addMemberStage.setScene(scene);
                    addMemberStage.initStyle(StageStyle.UNDECORATED);
                    // addMemberStage.setAlwaysOnTop(true);
                    addMemberStage.show();

                } catch (Exception e) {
                    alertMessage.showErrorMessage(stackPane, "Open New Form", "Error: " + e.getMessage(), 4,
                            Pos.BOTTOM_RIGHT);
                }
            }
        });

        menuEdit.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                if (tableMember.getSelectionModel().getSelectedItem() != null) {

                    RegisterController.memberModel = tableMember.getSelectionModel().getSelectedItem();
                    RegisterController.memberModel
                            .setByimg(byimg.get(tableMember.getSelectionModel().getSelectedIndex()));
                    try {
                        final Parent root = FXMLLoader.load(App.class.getResource("frmRegister.fxml"));
                        final Scene scene = new Scene(root);
                        addMemberStage = new Stage();
                        addMemberStage.setScene(scene);
                        addMemberStage.initStyle(StageStyle.TRANSPARENT);
                        // addMemberStage.setAlwaysOnTop(true);
                        addMemberStage.show();
                    } catch (Exception e) {
                        alertMessage.showErrorMessage(stackPane, "Open New Form", "Error: " + e.getMessage(), 4,
                                Pos.BOTTOM_RIGHT);
                    }
                } else {
                    alertMessage.showWarningMessage(stackPane, "Edit", "Please selecte the data and try again", 4,
                            Pos.BOTTOM_RIGHT);
                }
            }
        });

        menuDelete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (tableMember.getSelectionModel().getSelectedItem() != null) {
                    JFXButton[] buttons = { buttonYes(tableMember.getSelectionModel().getSelectedItem().getMemberId()),
                            buttonNo(), buttonCancel() };
                    dialog = new DialogMessage(stackPane, "ຄຳເຕືອນ", "ຕ້ອງການລົບຂໍ້ມູນອອກບໍ?",
                            JFXDialog.DialogTransition.CENTER, buttons, false);
                    dialog.showDialog();
                } else {
                    alertMessage.showWarningMessage(stackPane, "Delete", "Please selecte the data and try again", 4,
                            Pos.BOTTOM_RIGHT);
                }
            }
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) { 
        initEvents();
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
        delete.setStyle(Style.buttonStyle);
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
        btyes.setStyle(Style.buttonDialogStyle);
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
        btno.setStyle(Style.buttonDialogStyle);
        btno.setOnAction(e -> {
            dialog.closeDialog();
        });
        return btno;
    }

    private JFXButton buttonCancel() {
        JFXButton btcancel = new JFXButton("ຍົກເລີກ");
        btcancel.setStyle(Style.buttonDialogStyle);
        btcancel.setOnAction(e -> {
            dialog.closeDialog();
        });
        return btcancel;
    }
}
