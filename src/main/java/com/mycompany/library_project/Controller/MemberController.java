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
import com.mycompany.library_project.config.CreateLogFile;

import javafx.collections.*;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.*;
import javafx.fxml.*;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.util.Callback;

public class MemberController implements Initializable {

    private ObservableList<MemberModel> data = null;
    private AlertMessage alertMessage = new AlertMessage();
    private MemberModel memberModel = new MemberModel();
    private ResultSet rs = null;
    private DialogMessage dialog = null;
    private ArrayList<byte[]> byimg;
    public static Stage addMemberStage = null;
    public static boolean add = false;
    private CreateLogFile logfile = new CreateLogFile();

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
    private TableColumn<MemberModel, String> id, name, surname, gender, tel, village, district, province, depertment,
            studyYear;

    @FXML
    private TableColumn<MemberModel, Date> birthdate, date_register, date_exist;

    @FXML
    private TextField txtSearch;

    public void showData() {
        try {
            data = FXCollections.observableArrayList();
            rs = memberModel.findAll();
            byimg = new ArrayList<>();
            while (rs.next()) {
                data.add(new MemberModel(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
                        rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8),
                        Date.valueOf(rs.getString(10)), rs.getString(11), rs.getString(9),
                        Date.valueOf(rs.getString(12)), Date.valueOf(rs.getString(13)),
                        Date.valueOf(rs.getString(14))));
                byimg.add(rs.getBytes(15));
            }
            // tableMember.setItems(data); //Todo: if you don't filter to Search data
            // bellow:

            // Todo: Search data
            FilteredList<MemberModel> filterMembers = new FilteredList<MemberModel>(data, mb -> true);
            txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
                filterMembers.setPredicate(member -> {
                    if (newValue.isEmpty())
                        return true;
                    if (member.getMemberId().toLowerCase().indexOf(newValue.toLowerCase()) != -1
                            || member.getFirstName().toLowerCase().indexOf(newValue.toLowerCase()) != -1
                            || member.getSureName().toLowerCase().indexOf(newValue.toLowerCase()) != -1
                            || member.getGender().toLowerCase().indexOf(newValue.toLowerCase()) != -1
                            || member.getTel().toLowerCase().indexOf(newValue.toLowerCase()) != -1
                            || member.getVillage().toLowerCase().indexOf(newValue.toLowerCase()) != -1
                            || member.getDistrict().toLowerCase().indexOf(newValue.toLowerCase()) != -1
                            || member.getProvince().toLowerCase().indexOf(newValue.toLowerCase()) != -1
                            || member.getStudy_year().toLowerCase().indexOf(newValue.toLowerCase()) != -1
                            || member.getDetp().toLowerCase().indexOf(newValue.toLowerCase()) != -1)
                        return true;
                    else
                        return false;
                });
            });

            SortedList<MemberModel> sorted = new SortedList<>(filterMembers);
            sorted.comparatorProperty().bind(tableMember.comparatorProperty());
            tableMember.setItems(sorted);

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
        studyYear.setCellValueFactory(new PropertyValueFactory<>("study_year"));
        depertment.setCellValueFactory(new PropertyValueFactory<>("detp"));
        date_register.setCellValueFactory(new PropertyValueFactory<>("dateRegister"));
        date_exist.setCellValueFactory(new PropertyValueFactory<>("dateRegisterEnd"));

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
                    add = true;
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
                    logfile.createLogFile("ເກີດບັນຫາໃນການເປືດຟອມລົງທະບຽນ", e);
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
                        if (addMemberStage != null) {
                            addMemberStage.close();
                        }

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
                        logfile.createLogFile("ເກີດບັນຫາໃນການເປືດຟອມແກ້ໂຂຂໍ້ມູນ", e);
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
        addButtonToTable();
        showData();
    }

    private void addButtonToTable() {
        TableColumn<MemberModel, Void> colAtion = new TableColumn<>("Action");
        Callback<TableColumn<MemberModel, Void>, TableCell<MemberModel, Void>> cellFactory = new Callback<TableColumn<MemberModel, Void>, TableCell<MemberModel, Void>>() {

            @Override
            public TableCell<MemberModel, Void> call(TableColumn<MemberModel, Void> param) {
                final TableCell<MemberModel, Void> cell = new TableCell<MemberModel, Void>() {
                    final JFXButton delete = new JFXButton("ລົບ");

                    {
                        final Image img = new Image("/com/mycompany/library_project/Icon/bin.png");
                        final ImageView imgView = new ImageView();
                        imgView.setImage(img);
                        imgView.setFitWidth(20);
                        imgView.setFitHeight(20);
                        delete.setGraphic(imgView);
                        delete.setStyle(Style.buttonStyle);
                        delete.setOnAction(e -> {
                            JFXButton[] buttons = { buttonYes(tableMember.getItems().get(getIndex()).getMemberId()),
                                    buttonNo(), buttonCancel() };
                            dialog = new DialogMessage(stackPane, "ຄຳເຕືອນ", "ຕ້ອງການລົບຂໍ້ມູນອອກບໍ?",
                                    JFXDialog.DialogTransition.CENTER, buttons, false);
                            dialog.showDialog();
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty)
                            setGraphic(null);
                        else
                            setGraphic(delete);
                    }
                };
                return cell;
            }

        };
        colAtion.setCellFactory(cellFactory);
        tableMember.getColumns().add(colAtion);

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
                logfile.createLogFile("ມີບັນຫາໃນການລົບຂໍ້ມູນສະມາຊີກ", ex);
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
