package com.mycompany.library_project.Controller;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import com.jfoenix.controls.*;
import com.jfoenix.controls.JFXDialog.DialogTransition;
import com.mycompany.library_project.Style;
import com.mycompany.library_project.ControllerDAOModel.*;
import com.mycompany.library_project.Model.AuthorModel;
import com.mycompany.library_project.config.CreateLogFile;

import org.controlsfx.validation.Severity;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.*;
import javafx.collections.*;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.*;
import javafx.fxml.*;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.util.Callback;

public class AuthorController implements Initializable {

    private ValidationSupport validRules = new ValidationSupport();
    private ManagePersonalCotroller personalCotroller = null;
    private AddBookController addBookController = null;
    private AuthorModel author = new AuthorModel();
    private ResultSet rs = null;
    private String gender = "";
    private AlertMessage alertMessage = new AlertMessage();
    private DialogMessage dialog = null;
    private ObservableList<AuthorModel> data = null;
    private CreateLogFile logfile = new CreateLogFile();
    final JFXButton[] buttons = { buttonOK() };
    double x, y;

    public void initConstructor(ManagePersonalCotroller managePersonalCotroller) {
        this.personalCotroller = managePersonalCotroller;

        btClose.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                personalCotroller.showMainMenuPerson();
            }

        });

    }

    public void initConstructor2(AddBookController addBookController) {
        this.addBookController = addBookController;

        btClose.setDisable(true);
        btClose.setVisible(false);
        // anchorPaneHeader.setOnMousePressed(event -> {
        // x = event.getSceneX();
        // y = event.getSceneY();
        // });

        // anchorPaneHeader.setOnMouseDragged(event -> {
        // addAuthor.setX(event.getScreenX() - x);
        // addAuthor.setY(event.getScreenY() - y);
        // });

        // btClose.setOnAction(new EventHandler<ActionEvent>() {

        // @Override
        // public void handle(ActionEvent event) {
        // Stage stage = (Stage) stackPane.getScene().getWindow();
        // stage.close();
        // }

        // });

    }

    @FXML
    private StackPane stackPane;

    @FXML
    private AnchorPane anchorPaneHeader;

    @FXML
    private JFXButton btSave, btEdit, btCancel, btClose;

    @FXML
    private TextField txtId, txtFname, txtLname, txtTel, txtEmail, txtSearch;

    @FXML
    private RadioButton rdbMale, rdbFemale;

    @FXML
    private TableView<AuthorModel> tableAuthor;

    @FXML
    private TableColumn<AuthorModel, String> colId, colFname, colSname, colGender, colTel, colEmail;

    private void initRules() {
        validRules.setErrorDecorationEnabled(false);
        validRules.redecorate();
        validRules.registerValidator(txtId, false, Validator.createEmptyValidator("ກະລຸນາປ້ອນລະຫັດນັກແຕ່ງປຶ້ມ"));
        validRules.registerValidator(txtFname, false, Validator.createEmptyValidator("ກະລຸນາປ້ອນຊື່ນັກແຕ່ງປຶ້ມ"));
        validRules.registerValidator(txtLname, false, Validator.createEmptyValidator("ກະລຸນາປ້ອນນາມສະກຸນນັກແຕ່ງປຶ້ມ"));
        validRules.registerValidator(txtTel, false, Validator.createEmptyValidator("ກະລຸນາປ້ອນເບີໂທ"));
        validRules.registerValidator(txtEmail, false,
                Validator.createEmptyValidator("ກະລຸນາປ້ອນ Email (ຖ້າມີ)", Severity.WARNING));
    }

    private void initTable() {
        colId.setCellValueFactory(new PropertyValueFactory<>("author_id"));
        colFname.setCellValueFactory(new PropertyValueFactory<>("full_name"));
        colSname.setCellValueFactory(new PropertyValueFactory<>("sur_name"));
        colGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        colTel.setCellValueFactory(new PropertyValueFactory<>("tel"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colId.setVisible(false);

        // Todo: Add column row number
        final TableColumn<AuthorModel, AuthorModel> colNumber = new TableColumn<AuthorModel, AuthorModel>("ລຳດັບ");
        colNumber.setMinWidth(50);
        colNumber.setMaxWidth(120);
        colNumber.setPrefWidth(60);
        colNumber.setCellValueFactory(
                new Callback<CellDataFeatures<AuthorModel, AuthorModel>, ObservableValue<AuthorModel>>() {

                    @Override
                    public ObservableValue<AuthorModel> call(CellDataFeatures<AuthorModel, AuthorModel> param) {
                        return new ReadOnlyObjectWrapper<AuthorModel>(param.getValue());
                    }

                });
        colNumber.setCellFactory(
                new Callback<TableColumn<AuthorModel, AuthorModel>, TableCell<AuthorModel, AuthorModel>>() {

                    @Override
                    public TableCell<AuthorModel, AuthorModel> call(TableColumn<AuthorModel, AuthorModel> param) {
                        return new TableCell<AuthorModel, AuthorModel>() {
                            @Override
                            protected void updateItem(AuthorModel item, boolean empty) {
                                if (empty)
                                    setText("");
                                else if (this.getTableRow() != null && item != null)
                                    setText(Integer.toString(this.getTableRow().getIndex() + 1));
                            }
                        };
                    }

                });

        colNumber.setSortable(false);
        tableAuthor.getColumns().add(0, colNumber);

        // Todo: Add column Button
        addButtonToTable();

    }

    private void clearText() {

        validRules.setErrorDecorationEnabled(false);

        txtId.clear();
        txtFname.clear();
        txtLname.clear();
        txtTel.clear();
        txtEmail.clear();
        txtId.setDisable(false);
        gender = "";

        if (btSave.isDisable())
            btSave.setDisable(false);
        if (!btEdit.isDisable())
            btEdit.setDisable(true);
        txtId.requestFocus();
    }

    private void initKeyEvents() {
        txtId.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER)
                txtFname.requestFocus();
        });
        txtFname.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER)
                txtLname.requestFocus();
        });
        txtLname.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER)
                txtTel.requestFocus();
        });
        txtTel.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER)
                txtEmail.requestFocus();
        });
        txtEmail.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER)
                txtId.requestFocus();
        });
    }

    private void initEvents() {
        btSave.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                try {
                    if (!txtId.getText().equals("") && !txtFname.getText().equals("") && !txtLname.getText().equals("")
                            && !txtTel.getText().equals("")) {
                        gender = (rdbMale.isSelected() ? rdbMale.getText() : rdbFemale.getText());
                        author = new AuthorModel(txtId.getText(), txtFname.getText(), txtLname.getText(), gender,
                                txtTel.getText(), txtEmail.getText());

                        if (txtTel.getText().length() < 7 || txtTel.getText().length() > 11) {
                            if (dialog != null)
                                dialog.closeDialog();
                            dialog = new DialogMessage(stackPane, "ຄຳເຕືອນ",
                                    "ເບີໂທລະສັບຕ້ອງຢູ່ລະຫວ່າງ 7 ຫາ 11 ຕົວເລກເທົ່ານັ້ນ.", DialogTransition.CENTER,
                                    buttons, false);
                            dialog.showDialog();
                            txtTel.requestFocus();
                            return;
                        }

                        if (author.saveData() > 0) {
                            showData();
                            clearText();
                            alertMessage.showCompletedMessage(stackPane, "Saved", "Saved data successfully.", 4,
                                    Pos.BOTTOM_RIGHT);

                            // Todo: Open From Add Book
                            if (addBookController != null)
                                addBookController.fillAuthor();
                        }
                    } else {
                        // Todo: Show warning message if text or combo box is empty
                        validRules.setErrorDecorationEnabled(true);
                        alertMessage.showWarningMessage(stackPane, "Save Warning",
                                "Please chack your information and try again.", 4, Pos.BOTTOM_RIGHT);
                    }
                } catch (Exception e) {
                    alertMessage.showErrorMessage(stackPane, "Save Error", "Error: " + e.getMessage(), 4,
                            Pos.BOTTOM_RIGHT);
                }
            }
        });

        btEdit.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                try {
                    if (!txtId.getText().equals("") && !txtFname.getText().equals("") && !txtLname.getText().equals("")
                            && !txtTel.getText().equals("")) {

                        if (txtTel.getText().length() < 7 || txtTel.getText().length() > 11) {
                            if (dialog != null)
                                dialog.closeDialog();
                            dialog = new DialogMessage(stackPane, "ຄຳເຕືອນ",
                                    "ເບີໂທລະສັບຕ້ອງຢູ່ລະຫວ່າງ 7 ຫາ 11 ຕົວເລກເທົ່ານັ້ນ.", DialogTransition.CENTER,
                                    buttons, false);
                            dialog.showDialog();
                            txtTel.requestFocus();
                            return;
                        }

                        gender = (rdbMale.isSelected() ? rdbMale.getText() : rdbFemale.getText());
                        author = new AuthorModel(txtId.getText(), txtFname.getText(), txtLname.getText(), gender,
                                txtTel.getText(), txtEmail.getText());
                        if (author.updateData() > 0) {
                            showData();
                            clearText();
                            alertMessage.showCompletedMessage(stackPane, "Edited", "Edit data successfully.", 4,
                                    Pos.BOTTOM_RIGHT);

                            // Todo: Open From Add Book
                            if (addBookController != null)
                                addBookController.fillAuthor();
                        }
                    } else {
                        // Todo: Show warning message
                        validRules.setErrorDecorationEnabled(true);
                        alertMessage.showWarningMessage(stackPane, "Edit Warning",
                                "Please chack your information and try again.", 4, Pos.BOTTOM_RIGHT);
                    }
                } catch (Exception e) {
                    alertMessage.showErrorMessage(stackPane, "Edit Error", "Error: " + e.getMessage(), 4,
                            Pos.BOTTOM_RIGHT);
                }
            }
        });

        btCancel.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                clearText();
            }

        });

        txtTel.textProperty().addListener(new ChangeListener<String>() {
            // Todo: set properties type only numeric
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    txtTel.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        tableAuthor.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() >= 2 && tableAuthor.getSelectionModel().getSelectedItem() != null) {

                    btEdit.setDisable(false);
                    btSave.setDisable(true);
                    txtId.setDisable(true);

                    txtId.setText(tableAuthor.getSelectionModel().getSelectedItem().getAuthor_id());
                    txtFname.setText(tableAuthor.getSelectionModel().getSelectedItem().getFull_name());
                    txtLname.setText(tableAuthor.getSelectionModel().getSelectedItem().getSur_name());
                    txtTel.setText(tableAuthor.getSelectionModel().getSelectedItem().getTel());
                    txtEmail.setText(tableAuthor.getSelectionModel().getSelectedItem().getEmail());
                    if (tableAuthor.getSelectionModel().getSelectedItem().getGender().equals("ຊາຍ")) {
                        rdbMale.setSelected(true);
                    } else {
                        rdbFemale.setSelected(true);
                    }
                }
            }
        });
    }

    private void showData() {
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                try {
                    data = FXCollections.observableArrayList();
                    // author = new AuthorModel();
                    rs = author.findAll();
                    while (rs.next()) {
                        data.add(new AuthorModel(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
                                rs.getString(5), rs.getString(6)));
                    }
                    // tableAuthor.setItems(data); //Todo: if you don't filter to Search data
                    // bellow:

                    // Todo: Search data
                    FilteredList<AuthorModel> filterEmployees = new FilteredList<AuthorModel>(data, emp -> true);
                    txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
                        filterEmployees.setPredicate(aut -> {
                            if (newValue.isEmpty())
                                return true;
                            if (aut.getAuthor_id().toLowerCase().indexOf(newValue.toLowerCase()) != -1
                                    || aut.getFull_name().toLowerCase().indexOf(newValue.toLowerCase()) != -1
                                    || aut.getSur_name().toLowerCase().indexOf(newValue.toLowerCase()) != -1
                                    || aut.getEmail().toLowerCase().indexOf(newValue.toLowerCase()) != -1
                                    || aut.getTel().toLowerCase().indexOf(newValue.toLowerCase()) != -1
                                    || aut.getGender().toLowerCase().indexOf(newValue.toLowerCase()) != -1)
                                return true;
                            else
                                return false;
                        });
                    });

                    SortedList<AuthorModel> sorted = new SortedList<>(filterEmployees);
                    sorted.comparatorProperty().bind(tableAuthor.comparatorProperty());
                    tableAuthor.setItems(sorted);
                } catch (Exception e) {
                    alertMessage.showErrorMessage("Load Data", "Error" + e.getMessage(), 4,
                            Pos.BOTTOM_RIGHT);
                }
            }
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        ToggleGroup group = new ToggleGroup();
        rdbMale.setToggleGroup(group);
        rdbFemale.setToggleGroup(group);
        rdbMale.setSelected(true);
        btEdit.setDisable(true);
        initTable();
        initRules();
        initEvents();
        initKeyEvents();
        showData();

    }

    private void addButtonToTable() {
        TableColumn<AuthorModel, Void> colAtion = new TableColumn<>("Action");
        Callback<TableColumn<AuthorModel, Void>, TableCell<AuthorModel, Void>> cellFactory = new Callback<TableColumn<AuthorModel, Void>, TableCell<AuthorModel, Void>>() {

            @Override
            public TableCell<AuthorModel, Void> call(TableColumn<AuthorModel, Void> param) {
                final TableCell<AuthorModel, Void> cell = new TableCell<AuthorModel, Void>() {
                    final JFXButton delete = new JFXButton("ລົບ");

                    {
                        final ImageView imgView = new ImageView();
                        imgView.setFitWidth(20);
                        imgView.setFitHeight(20);
                        imgView.setImage(new Image("/com/mycompany/library_project/Icon/bin.png"));
                        delete.setStyle(Style.buttonStyle);
                        delete.setGraphic(imgView);

                        delete.setOnAction(new EventHandler<ActionEvent>() {

                            @Override
                            public void handle(ActionEvent event) {
                                JFXButton[] buttons = {
                                        buttonYes(tableAuthor.getItems().get(getIndex()).getAuthor_id()), buttonNo(),
                                        buttonCancel() };
                                dialog = new DialogMessage(stackPane, "ຄຳເຕືອນ", "ຕ້ອງການລົບຂໍ້ມູນອອກບໍ?",
                                        JFXDialog.DialogTransition.CENTER, buttons, false);
                                dialog.showDialog();
                            }

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
        tableAuthor.getColumns().add(colAtion);

    }

    protected JFXButton buttonYes(String id) {
        JFXButton btyes = new JFXButton("ຕົກລົງ");
        btyes.setStyle(Style.buttonDialogStyle);
        btyes.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                // author = new AuthorModel();
                try {
                    if (author.deleteData(id) > 0) {
                        showData();
                        clearText();
                        alertMessage.showCompletedMessage(stackPane, "Deleted", "Delete data successfully.", 4,
                                Pos.BOTTOM_RIGHT);
                        dialog.closeDialog();

                        // Todo: Open From Add Book
                        if (addBookController != null)
                            addBookController.fillAuthor();
                    } else {
                        alertMessage.showWarningMessage(stackPane, "Deleted", "Can not delete data, Please try again.",
                                4, Pos.BOTTOM_RIGHT);
                    }
                } catch (SQLException e) {
                    alertMessage.showErrorMessage(stackPane, "Deleted", "Error: " + e.getMessage(), 4,
                            Pos.BOTTOM_RIGHT);
                    logfile.createLogFile("ມີບັນຫາໃນການລົບຂໍ້ມູນນັກແຕ່ງປຶ້ມ", e);
                }
            }
        });
        return btyes;
    }

    protected JFXButton buttonNo() {
        JFXButton btno = new JFXButton("  ບໍ່  ");
        btno.setStyle(Style.buttonDialogStyle);
        btno.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                dialog.closeDialog();
            }
        });
        return btno;
    }

    protected JFXButton buttonCancel() {
        JFXButton btcancel = new JFXButton("ຍົກເລີກ");
        btcancel.setStyle(Style.buttonDialogStyle);
        btcancel.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                dialog.closeDialog();
            }

        });
        return btcancel;
    }

    private JFXButton buttonOK() {
        JFXButton btOk = new JFXButton("OK");
        btOk.setStyle(Style.buttonDialogStyle);
        btOk.setOnAction(e -> {
            dialog.closeDialog();
        });
        return btOk;
    }

}
