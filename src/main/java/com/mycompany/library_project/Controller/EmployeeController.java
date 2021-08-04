package com.mycompany.library_project.Controller;

import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.jfoenix.controls.*;
import com.mycompany.library_project.App;
import com.mycompany.library_project.Style;
import com.mycompany.library_project.ControllerDAOModel.*;
import com.mycompany.library_project.Model.EmployeeModel;

import org.controlsfx.validation.Severity;
import org.controlsfx.validation.ValidationResult;
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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

public class EmployeeController implements Initializable {

    private ValidationSupport validRules = new ValidationSupport();
    private ValidationSupport validRulePass = new ValidationSupport();
    private ValidationSupport validPassLengt = new ValidationSupport();
    ManageBookController manageBookController = null;
    private EmployeeModel employee = new EmployeeModel();
    private ResultSet rs = null;
    private String gender = "";
    private AlertMessage alertMessage = new AlertMessage();
    private DialogMessage dialog = new DialogMessage();
    private ArrayList<EmployeeModel> user = null;
    private ObservableList<EmployeeModel> data = null;

    public void initConstructor(ManageBookController manageBookController) {
        this.manageBookController = manageBookController;
    }

    @FXML
    private StackPane stackPane;

    @FXML
    private JFXButton btSave, btEdit, btCancel, btClose;

    @FXML
    private TextField txtId, txtFname, txtLname, txtTel, txtEmail, txtSearch;

    @FXML
    private PasswordField txtPassword, txtRepassword;

    @FXML
    private RadioButton rdbMale, rdbFemale;

    @FXML
    private MenuItem menuAddUser, menuEditUser;

    @FXML
    private TableView<EmployeeModel> tableEmployee;

    @FXML
    private TableColumn<EmployeeModel, String> colId, colFname, colSname, colGender, colTel, colEmail;

    private void addNewUser() throws SQLException {
        try {
            // Todo: Encrytp Password
            final String salt = ProtectUserPassword.getSalt(40);
            final String myScrPassword = ProtectUserPassword.generateSecurePassword(txtPassword.getText(), salt);

            employee = new EmployeeModel(txtId.getText(), txtFname.getText(), myScrPassword, salt);
            if (employee.addUser() > 0) {
                alertMessage.showCompletedMessage("Save", "Add user successfully", 4, Pos.BOTTOM_RIGHT);
                showData();
                clearText();
            }
        } catch (Exception e) {
            alertMessage.showErrorMessage("Error Save Data", "Error" + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
            employee.deleteData(txtId.getText());
        }
    }

    private void initRules() {
        validRules.setErrorDecorationEnabled(false);
        validRules.registerValidator(txtId, false, Validator.createEmptyValidator("ກະລຸນາປ້ອນລະຫັດພະນັກງານ"));
        validRules.registerValidator(txtFname, false, Validator.createEmptyValidator("ກະລຸນາປ້ອນຊື່"));
        validRules.registerValidator(txtLname, false, Validator.createEmptyValidator("ກະລຸນາປ້ອນນາມສະກຸນ"));
        validRules.registerValidator(txtTel, false, Validator.createEmptyValidator("ກະລຸນາປ້ອນເບີໂທລະສັບ"));
        validRules.registerValidator(txtEmail, false,
                Validator.createEmptyValidator("ກະລຸນາປ້ອນ Email (ຖ້າມີ)", Severity.WARNING));
        validRules.registerValidator(txtPassword, false, Validator.createEmptyValidator("ກະລຸນາປ້ອນຊື່"));
        validRulePass.registerValidator(txtRepassword, false, (Control c, String newValue) -> ValidationResult
                .fromErrorIf(c, "ລະຫັດຜ່ານທີ່ປ້ອນບໍ່ຄືກັນ", !newValue.equals(txtPassword.getText())));
        validPassLengt.registerValidator(txtPassword, false, (Control c, String newValue) -> ValidationResult
                .fromErrorIf(c, "ລະຫັດຜ່ານຕ້ອງມີຕົວເລກ ຫຼື ຕົວອັກສອນຢ່າງໜ້ອຍ 6 ຕົວ", newValue.length() < 6));
    }

    private void initTable() {
        colId.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        colFname.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colSname.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        colTel.setCellValueFactory(new PropertyValueFactory<>("tel"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colId.setVisible(false);

        // Todo: Add column number
        final TableColumn<EmployeeModel, EmployeeModel> colNumber = new TableColumn<EmployeeModel, EmployeeModel>(
                "ລຳດັບ");
        colNumber.setMinWidth(50);
        colNumber.setMaxWidth(200);
        colNumber.setPrefWidth(100);
        colNumber.setId("colCenter");
        colNumber.setCellValueFactory(
                new Callback<CellDataFeatures<EmployeeModel, EmployeeModel>, ObservableValue<EmployeeModel>>() {

                    @Override
                    public ObservableValue<EmployeeModel> call(CellDataFeatures<EmployeeModel, EmployeeModel> param) {
                        return new ReadOnlyObjectWrapper<EmployeeModel>(param.getValue());
                    }

                });
        colNumber.setCellFactory(
                new Callback<TableColumn<EmployeeModel, EmployeeModel>, TableCell<EmployeeModel, EmployeeModel>>() {

                    @Override
                    public TableCell<EmployeeModel, EmployeeModel> call(
                            TableColumn<EmployeeModel, EmployeeModel> param) {
                        return new TableCell<EmployeeModel, EmployeeModel>() {
                            @Override
                            protected void updateItem(EmployeeModel item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty)
                                    setText("");
                                if (this.getTableRow() != null && item != null)
                                    setText(Integer.toString(this.getTableRow().getIndex() + 1));
                            }
                        };
                    }
                });

        colNumber.setSortable(false);
        tableEmployee.getColumns().add(0, colNumber);

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
        txtPassword.clear();
        txtRepassword.clear();
        rdbMale.setSelected(true);
        txtPassword.setDisable(false);
        txtRepassword.setDisable(false);

        if (txtId.isDisable())
            txtId.setDisable(false);
        if (btSave.isDisable())
            btSave.setDisable(false);
        if (!btSave.isDisable())
            btEdit.setDisable(true);

        gender = "";
        txtId.requestFocus();

    }

    private void editUser(String form) {
        try {
            user = new ArrayList<>();
            final String empID = tableEmployee.getSelectionModel().getSelectedItem().getEmployeeId();
            rs = employee.findUserByEmpId(empID);
            if (rs.next()) {
                user.add(new EmployeeModel(rs.getString("employee_id"), rs.getString("user_name"),
                        rs.getString("password"), rs.getString("salt")));
                final FXMLLoader loader = new FXMLLoader(App.class.getResource(form));
                final Parent root = loader.load();
                final Scene scene = new Scene(root);
                final Stage stage = new Stage(StageStyle.TRANSPARENT);
                scene.setFill(Color.TRANSPARENT);
                stage.setScene(scene);

                final AdduserController userController = loader.getController();
                userController.initConstructor(empID, user, stage);

                stage.initModality(Modality.APPLICATION_MODAL);
                stage.getIcons().add(new Image("/com/mycompany/library_project/Icon/icon.png"));
                stage.show();
            }

        } catch (Exception e) {
            // e.printStackTrace();
        }
    }

    private void initEvents() {
        btSave.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                try {
                    if (!txtId.getText().equals("") && !txtFname.getText().equals("") && !txtLname.getText().equals("")
                            && !txtTel.getText().equals("") && !txtPassword.getText().equals("")
                            && !txtRepassword.getText().equals("")) {

                        if (employee.findById(txtId.getText()).next()) {
                            dialog.showWarningDialog(null,
                                    "ລະຫັດພະນັກງານຊ້ຳກັບຂໍ້ມູນທີ່ມີຢູ່ໃນລະບົບກະລຸນາກວດສອບຂໍ້ມູນ ແລ້ວລອງບັນທຶກໃຫມ່ອີກຄັ້ງ");
                            return;
                        }

                        if (txtTel.getText().length() < 7 || txtTel.getText().length() > 11) {
                            dialog.showWarningDialog(null, "ເບີໂທລະສັບຕ້ອງຢູ່ລະຫວ່າງ 7 ຫາ 11 ຕົວເລກເທົ່ານັ້ນ.");
                            txtTel.requestFocus();
                            return;
                        }

                        if (txtPassword.getText().length() < 6) {
                            dialog.showWarningDialog(null, "ລະຫັດຜ່ານຕ້ອງມີຕົວເລກ ຫຼື ຕົວອັກສອນຢ່າງໜ້ອຍ 6 ຕົວ");
                            txtRepassword.requestFocus();
                            return;
                        }

                        if (!txtPassword.getText().equals(txtRepassword.getText())) {
                            dialog.showWarningDialog(null, "ລະຫັດຜ່ານບໍ່ຄືກັນ ກະລຸນາກວດສອອບແລ້ວລອງໃຫມ່ອິກຄັ້ງ.");
                            txtRepassword.requestFocus();
                            return;
                        }

                        gender = (rdbMale.isSelected() ? rdbMale.getText() : rdbFemale.getText());
                        employee = new EmployeeModel(txtId.getText(), txtFname.getText(), txtLname.getText(), gender,
                                txtTel.getText(), txtEmail.getText());
                        if (employee.saveData() > 0) {
                            addNewUser();
                        }

                    } else {
                        validRules.setErrorDecorationEnabled(true);
                        alertMessage.showWarningMessage("Save Warning",
                                "Please chack your information and try again.", 4, Pos.BOTTOM_RIGHT);
                    }
                } catch (Exception e) {
                    alertMessage.showErrorMessage("Save Error", "Error: " + e.getMessage(), 4,
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
                            dialog.showWarningDialog(null, "ເບີໂທລະສັບຕ້ອງຢູ່ລະຫວ່າງ 7 ຫາ 11 ຕົວເລກເທົ່ານັ້ນ.");
                            txtTel.requestFocus();
                            return;
                        }

                        gender = (rdbMale.isSelected() ? rdbMale.getText() : rdbFemale.getText());
                        employee = new EmployeeModel(txtId.getText(), txtFname.getText(), txtLname.getText(), gender,
                                txtTel.getText(), txtEmail.getText());
                        if (employee.updateData() > 0) {
                            showData();
                            clearText();
                            alertMessage.showCompletedMessage("Edited", "Edit data successfully.", 4,
                                    Pos.BOTTOM_RIGHT);
                        }
                    } else {
                        validRules.setErrorDecorationEnabled(true);
                        alertMessage.showWarningMessage("Edit Warning",
                                "Please chack your information and try again.", 4, Pos.BOTTOM_RIGHT);
                    }
                } catch (Exception e) {
                    alertMessage.showErrorMessage("Edit Error", "Error: " + e.getMessage(), 4,
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

        btClose.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                manageBookController.showMainMenuBooks();
            }

        });
        menuAddUser.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                editUser("frmAddUser.fxml");
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

        tableEmployee.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() >= 2 && tableEmployee.getSelectionModel().getSelectedItem() != null) {

                    btEdit.setDisable(false);
                    btSave.setDisable(true);
                    txtId.setDisable(true);
                    txtPassword.setDisable(true);
                    txtRepassword.setDisable(true);

                    txtId.setText(tableEmployee.getSelectionModel().getSelectedItem().getEmployeeId());
                    txtFname.setText(tableEmployee.getSelectionModel().getSelectedItem().getFirstName());
                    txtLname.setText(tableEmployee.getSelectionModel().getSelectedItem().getLastName());
                    txtTel.setText(tableEmployee.getSelectionModel().getSelectedItem().getTel());
                    txtEmail.setText(tableEmployee.getSelectionModel().getSelectedItem().getEmail());
                    if (tableEmployee.getSelectionModel().getSelectedItem().getGender().equals("ຊາຍ")) {
                        rdbMale.setSelected(true);
                    } else {
                        rdbFemale.setSelected(true);
                    }
                }
            }

        });
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

    private void showData() {
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                try {
                    data = FXCollections.observableArrayList();
                    // employee = new EmployeeModel();
                    rs = employee.findAll();
                    while (rs.next()) {
                        data.add(new EmployeeModel(rs.getString("employee_id"), rs.getString("full_name"),
                                rs.getString("sur_name"), rs.getString("gender"), rs.getString("tel"),
                                rs.getString("email")));
                    }
                    // tableEmployee.setItems(data); //Todo: if you don't filter to Search data
                    // bellow:

                    // Todo: Search data
                    FilteredList<EmployeeModel> filterEmployees = new FilteredList<EmployeeModel>(data, emp -> true);
                    txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
                        filterEmployees.setPredicate(emp -> {
                            if (newValue.isEmpty())
                                return true;
                            if (emp.getEmployeeId().toLowerCase().indexOf(newValue.toLowerCase()) != -1
                                    || emp.getFirstName().toLowerCase().indexOf(newValue.toLowerCase()) != -1
                                    || emp.getLastName().toLowerCase().indexOf(newValue.toLowerCase()) != -1
                                    || emp.getEmail().toLowerCase().indexOf(newValue.toLowerCase()) != -1
                                    || emp.getTel().toLowerCase().indexOf(newValue.toLowerCase()) != -1
                                    || emp.getGender().toLowerCase().indexOf(newValue.toLowerCase()) != -1)
                                return true;
                            else
                                return false;
                        });
                    });

                    SortedList<EmployeeModel> sorted = new SortedList<>(filterEmployees);
                    sorted.comparatorProperty().bind(tableEmployee.comparatorProperty());
                    tableEmployee.setItems(sorted);

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
        TableColumn<EmployeeModel, Void> colAtion = new TableColumn<>("Action");
        Callback<TableColumn<EmployeeModel, Void>, TableCell<EmployeeModel, Void>> cellFactory = new Callback<TableColumn<EmployeeModel, Void>, TableCell<EmployeeModel, Void>>() {

            @Override
            public TableCell<EmployeeModel, Void> call(TableColumn<EmployeeModel, Void> param) {
                final TableCell<EmployeeModel, Void> cell = new TableCell<EmployeeModel, Void>() {
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
                                dialog.showComfirmDialog("Comfirmed", null, "ຕ້ອງການລົບຂໍ້ມູນ ຫຼື ບໍ?");
                                try {
                                    if (employee
                                            .deleteData(tableEmployee.getItems().get(getIndex()).getEmployeeId()) > 0) {
                                        showData();
                                        clearText();
                                        alertMessage.showCompletedMessage("Deleted", "Delete data successfully.", 4,
                                                Pos.BOTTOM_RIGHT);
                                    }
                                } catch (Exception e) {
                                    alertMessage.showErrorMessage("Deleted", "Error: " + e.getMessage(), 4,
                                            Pos.BOTTOM_RIGHT);
                                }
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
        tableEmployee.getColumns().add(colAtion);

    }

}
