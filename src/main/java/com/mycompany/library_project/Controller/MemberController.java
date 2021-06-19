package com.mycompany.library_project.Controller;

import java.io.InputStream;
import java.net.URL;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import com.jfoenix.controls.*;
import com.mycompany.library_project.App;
import com.mycompany.library_project.Style;
import com.mycompany.library_project.ControllerDAOModel.*;
import com.mycompany.library_project.Model.MemberModel;
import com.mycompany.library_project.Report.CreateReport;
import com.mycompany.library_project.config.CreateLogFile;

import org.controlsfx.control.MaskerPane;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.*;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Task;
import javafx.event.*;
import javafx.fxml.*;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.*;
import javafx.util.Callback;

public class MemberController implements Initializable {

    private ManagePersonalCotroller personalCotroller = null;
    private ObservableList<MemberModel> data = null;
    private AlertMessage alertMessage = new AlertMessage();
    private MemberModel memberModel = new MemberModel();
    private MaskerPane masker = new MaskerPane();
    private ResultSet rs = null;
    private DialogMessage dialog = null;
    private ArrayList<byte[]> byimg;
    public static Stage addMemberStage = null;
    public static boolean add = false;
    private CreateLogFile logfile = new CreateLogFile();

    public void initConstructor(ManagePersonalCotroller managePersonalCotroller) {
        this.personalCotroller = managePersonalCotroller;
    }

    @FXML
    private RegisterController registerController;

    @FXML
    private BorderPane boderPane;

    @FXML
    private StackPane stackPane;

    @FXML
    private JFXButton btAddUser, btClose;

    @FXML
    private MenuItem menuEdit, menuReport, menuDelete, menuPrintCard;

    @FXML
    private TableView<MemberModel> tableMember;

    @FXML
    private TableColumn<MemberModel, String> memberId, studentId, name, surname, gender, tel, village, district,
            province, depertment, studyYear;

    @FXML
    private TableColumn<MemberModel, Date> birthdate, date_register, date_exist;

    @FXML
    private TextField txtSearch;

    private void showAddRegister() {
        try {
            add = true;
            FXMLLoader loader = new FXMLLoader(App.class.getResource("frmRegister.fxml"));
            final Parent root = loader.load();
            final Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            registerController = loader.getController();
            addMemberStage = new Stage();
            addMemberStage.setScene(scene);
            addMemberStage.initStyle(StageStyle.TRANSPARENT);
            registerController.initConstructor2(this, addMemberStage);
            // addMemberStage.setAlwaysOnTop(true);
            addMemberStage.getIcons().add(new Image("/com/mycompany/library_project/Icon/icon.png"));
            addMemberStage.initModality(Modality.APPLICATION_MODAL);
            addMemberStage.show();

        } catch (Exception e) {
            alertMessage.showErrorMessage(stackPane, "Open New Form", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
            logfile.createLogFile("ເກີດບັນຫາໃນການເປືດຟອມລົງທະບຽນ", e);
        }
    }

    private void showEditeMembers() {
        try {
            if (addMemberStage != null) {
                addMemberStage.close();
            }

            final FXMLLoader loader = new FXMLLoader(App.class.getResource("frmRegister.fxml"));
            final Parent root = loader.load();
            final Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            registerController = loader.getController();
            registerController.memberModel = tableMember.getSelectionModel().getSelectedItem();
            registerController.memberModel.setByimg(byimg.get(tableMember.getSelectionModel().getSelectedIndex()));
            addMemberStage = new Stage();
            addMemberStage.setScene(scene);
            addMemberStage.initStyle(StageStyle.TRANSPARENT);
            registerController.initConstructor2(this, addMemberStage);
            addMemberStage.initModality(Modality.APPLICATION_MODAL);
            addMemberStage.show();

        } catch (Exception e) {
            alertMessage.showErrorMessage(stackPane, "Open New Form", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
            logfile.createLogFile("ເກີດບັນຫາໃນການເປືດຟອມແກ້ໂຂຂໍ້ມູນ", e);
        }
    }

    public void showData() {
        Task<Void> task = new Task<Void>() {

            @Override
            protected Void call() throws Exception {
                masker.setVisible(true);
                masker.setProgressVisible(true);
                // Platform.runLater(new Runnable() {

                // @Override
                // public void run() {
                try {
                    data = FXCollections.observableArrayList();
                    rs = memberModel.findAll();
                    byimg = new ArrayList<>();
                    while (rs.next()) {
                        data.add(new MemberModel(rs.getString("member_id"), rs.getString("student_id"),
                                rs.getString("full_name"), rs.getString("sur_name"), rs.getString("gender"),
                                rs.getString("tel"), rs.getString("village"), rs.getString("district"),
                                rs.getString("province"), Date.valueOf(rs.getString("birthdate")),
                                rs.getString("study_year"), rs.getString("dep_name"),
                                Date.valueOf(rs.getString("date_register")), Date.valueOf(rs.getString("date_end")),
                                Date.valueOf(rs.getString("date_exit"))));
                        byimg.add(rs.getBytes("img"));
                    }

                    /*
                     * tableMember.setItems(data); //Todo: if you don't filter to Search data
                     * bellow:
                     */
                    // Todo: Search data
                    FilteredList<MemberModel> filterMembers = new FilteredList<MemberModel>(data, mb -> true);
                    txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
                        filterMembers.setPredicate(member -> {
                            if (newValue.isEmpty())
                                return true;
                            if (member.getMemberId().toLowerCase().indexOf(newValue.toLowerCase()) != -1
                                    || member.getStudentId().toLowerCase().indexOf(newValue.toLowerCase()) != -1
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
                // }
                // });
                return null;
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                masker.setVisible(false);
                masker.setProgressVisible(false);
            }

            @Override
            protected void failed() {
                super.failed();
                masker.setProgressVisible(false);
                masker.setVisible(false);
                alertMessage.showErrorMessage("Load Data", "Load Data Failed", 4, Pos.BOTTOM_RIGHT);
            }

        };
        new Thread(task).start();
    }

    private void initTable() {
        memberId.setCellValueFactory(new PropertyValueFactory<>("memberId"));
        studentId.setCellValueFactory(new PropertyValueFactory<>("studentId"));
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
        memberId.setVisible(false);

        // Todo: Add column number
        final TableColumn<MemberModel, MemberModel> colNumber = new TableColumn<MemberModel, MemberModel>("ລຳດັບ");
        colNumber.setMinWidth(50);
        colNumber.setMaxWidth(120);
        colNumber.setPrefWidth(60);
        colNumber.setId("colCenter");
        colNumber.setCellValueFactory(
                new Callback<CellDataFeatures<MemberModel, MemberModel>, ObservableValue<MemberModel>>() {

                    @Override
                    public ObservableValue<MemberModel> call(CellDataFeatures<MemberModel, MemberModel> param) {
                        return new ReadOnlyObjectWrapper<MemberModel>(param.getValue());
                    }
                });
        colNumber.setCellFactory(
                new Callback<TableColumn<MemberModel, MemberModel>, TableCell<MemberModel, MemberModel>>() {

                    @Override
                    public TableCell<MemberModel, MemberModel> call(TableColumn<MemberModel, MemberModel> param) {
                        return new TableCell<MemberModel, MemberModel>() {
                            @Override
                            protected void updateItem(MemberModel item, boolean empty) {
                                super.updateItem(item, empty);

                                // Todo: Set row numder
                                if (empty)
                                    setText("");
                                else if (this.getTableRow() != null && item != null)
                                    setText(Integer.toString(this.getTableRow().getIndex() + 1));

                                /*
                                 * // Todo: Set color to row that rent out date TableRow<MemberModel> currentRow
                                 * = getTableRow(); if (!empty) { if
                                 * (mydate.cancalarDate(item.getDateRegisterEnd().toLocalDate()) > 0)
                                 * currentRow.setStyle("-fx-background-color:#DFC840"); if
                                 * (mydate.cancalarDate(item.getDateExit().toLocalDate()) > 0)
                                 * currentRow.setStyle("-fx-background-color:lightcoral"); }
                                 */
                            }
                        };
                    }

                });
        colNumber.setSortable(true);
        tableMember.getColumns().add(0, colNumber);

        // Todo: Add column Button
        addButtonToTable();

        tableMember.setOnMouseClicked(mevt -> {
            if (mevt.getClickCount() > 0 && tableMember.getSelectionModel().getSelectedItem() != null) {
                // memberModel = new MemberModel();
                memberModel = tableMember.getSelectionModel().getSelectedItem();
            }
        });
    }

    private void initEvents() {
        btAddUser.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                showAddRegister();
            }
        });

        menuPrintCard.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                Task<Void> task = new Task<Void>() {

                    @Override
                    protected Void call() throws Exception {
                        masker.setVisible(true);
                        masker.setProgressVisible(true);

                        InputStream imgInputStream = this.getClass().getResourceAsStream("bin/Logo.png");
                        CreateReport printCard = new CreateReport();
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("memberid", memberModel.getMemberId());
                        map.put("logo", imgInputStream);
                        printCard.showReport(map, "printCardById.jrxml", "Print Member Card Error");
                        return null;
                    }

                    @Override
                    protected void succeeded() {
                        super.succeeded();
                        masker.setProgressVisible(false);
                        masker.setVisible(false);
                    }

                    @Override
                    protected void failed() {
                        super.failed();
                        masker.setProgressVisible(false);
                        masker.setVisible(false);
                        alertMessage.showErrorMessage("Print", "Print Member Card Failed", 4, Pos.BOTTOM_RIGHT);
                    }
                };
                new Thread(task).start();

            }

        });

        menuReport.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                Task<Void> task = new Task<Void>() {

                    @Override
                    protected Void call() throws Exception {
                        masker.setVisible(true);
                        masker.setProgressVisible(true);

                        CreateReport createReport = new CreateReport();
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("logo", Paths.get("bin/Logo.png").toAbsolutePath().toString());
                        map.put("memberId", tableMember.getSelectionModel().getSelectedItem().getMemberId());
                        createReport.showReport(map, "reportMaemberById.jrxml", "Report Member By ID Error");

                        return null;
                    }

                    @Override
                    protected void succeeded() {
                        super.succeeded();
                        masker.setProgressVisible(false);
                        masker.setVisible(false);
                    }

                    @Override
                    protected void failed() {
                        super.failed();
                        masker.setProgressVisible(false);
                        masker.setVisible(false);
                        alertMessage.showErrorMessage("Report", "Report Member Failed", 4, Pos.BOTTOM_RIGHT);
                    }
                };
                new Thread(task).start();
            }

        });

        menuEdit.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                if (tableMember.getSelectionModel().getSelectedItem() != null) {
                    showEditeMembers();
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

        btClose.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                personalCotroller.showMainMenuPerson();
            }

        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        masker.setVisible(false);
        masker.setPrefWidth(50.0);
        masker.setPrefHeight(50.0);
        masker.setText("ກຳລັງໂຫລດຂໍ້ມູນ, ກະລຸນາລໍຖ້າ...");
        masker.setStyle("-fx-font-family: BoonBaan;");
        stackPane.getChildren().add(masker);

        initEvents();
        initTable();
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
                    // tableMember.getItems().remove(index);
                    dialog.closeDialog();
                    alertMessage.showCompletedMessage("Delete", "Delete data successfully.", 4, Pos.BOTTOM_RIGHT);
                }
            } catch (Exception ex) {
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
