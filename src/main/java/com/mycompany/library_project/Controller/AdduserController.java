package com.mycompany.library_project.Controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.mycompany.library_project.ControllerDAOModel.AlertMessage;
import com.mycompany.library_project.ControllerDAOModel.ProtectUserPassword;
import com.mycompany.library_project.Model.EmployeeModel;

import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.*;
import javafx.geometry.Pos;
import javafx.scene.control.Control;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AdduserController implements Initializable {

    private ValidationSupport validRules = new ValidationSupport();
    private ValidationSupport validRulePass = new ValidationSupport();
    private ValidationSupport validOldPass = new ValidationSupport();
    private AlertMessage alertMessage = new AlertMessage();
    private EmployeeModel user = null;
    private String employeeId = null;
    private Double x, y;
    private Stage stage = null;
    private final String salt = ProtectUserPassword.getSalt(40);
    private String oldUser = null, oldScrPassword = null, oldSalt = null;

    public void initConstructor(String empID, ArrayList<EmployeeModel> employee, Stage stage) {
        this.stage = stage;
        employeeId = empID;
        if (employee.size() > 0) {
            oldUser = employee.get(0).getUser();
            txtUserName.setText(oldUser);
            oldScrPassword = employee.get(0).getPassword();
            oldSalt = employee.get(0).getSaltKey();

            txtOldPassword.setDisable(false);
            chRenewPass.setDisable(false);
            lbNewPass.setText("ລະຫັດຜ່ານໃຫມ່");
            txtPassword.setDisable(true);
            txtRepassword.setDisable(true);

            validOldPass.setErrorDecorationEnabled(false);
            validOldPass.registerValidator(txtOldPassword, false,
                    (Control c, String newValue) -> ValidationResult.fromErrorIf(c, "ລະຫັດຜ່ານເກົ່າບໍ່ຖຶກຕ້ອງ",
                            !ProtectUserPassword.verifyPassword(newValue, oldScrPassword, oldSalt)
                                    || newValue.equals(null)));
        }
    }

    @FXML
    private AnchorPane achPane, achHeader;

    @FXML
    private JFXButton btSave;

    @FXML
    private TextField txtUserName;

    @FXML
    private PasswordField txtOldPassword, txtPassword, txtRepassword;

    @FXML
    private JFXCheckBox chRenewPass;

    @FXML
    private Text lbNewPass;

    private void clearTextField() {
        txtUserName.clear();
        txtOldPassword.clear();
        txtPassword.clear();
        txtRepassword.clear();
    }

    private void setMoveForm() {
        achHeader.setOnMousePressed(mouseEvent -> {
            x = mouseEvent.getSceneX();
            y = mouseEvent.getSceneY();
        });
        achHeader.setOnMouseDragged(mouseEvent -> {
            stage.setX(mouseEvent.getScreenX() - x);
            stage.setY(mouseEvent.getScreenY() - y);
            // stage.setOpacity(0.4f);
        });

        // achHeader.setOnDragDone(dragEvent -> {
        // stage.setOpacity(1.0f);
        // });
    }

    private void addNewUser() {
        try {
            if (!txtUserName.getText().equals("") && !txtPassword.getText().equals("")
                    && !txtRepassword.getText().equals("")) {
                if (txtPassword.getText().equals(txtRepassword.getText())) {

                    // Todo: Encrytp Password
                    final String myScrPassword = ProtectUserPassword.generateSecurePassword(txtPassword.getText(),
                            salt);

                    user = new EmployeeModel(employeeId, txtUserName.getText(), myScrPassword, salt);
                    if (user.addUser() > 0) {
                        alertMessage.showCompletedMessage("Save", "Add user successfully", 4, Pos.BOTTOM_RIGHT);
                        clearTextField();
                    }
                } else
                    alertMessage.showWarningMessage("Save", "Password not match", 4, Pos.BOTTOM_RIGHT);
            } else {
                validRules.setErrorDecorationEnabled(true);
                alertMessage.showWarningMessage("Save", "Please chack your information and try again.", 4,
                        Pos.BOTTOM_RIGHT);
            }
        } catch (Exception e) {
            alertMessage.showErrorMessage("Error Save Data", "Error" + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
        }
    }

    private void updateUser() {
        try {
            if (!txtUserName.getText().equals("") && !txtPassword.getText().equals("")
                    && !txtRepassword.getText().equals("")) {
                final boolean chackPass = ProtectUserPassword.verifyPassword(txtOldPassword.getText(), oldScrPassword,
                        oldSalt);
                if (chackPass) {
                    if (txtPassword.getText().equals(txtRepassword.getText())) {

                        // Todo: Encrytp Password
                        final String myScrPassword = ProtectUserPassword.generateSecurePassword(txtPassword.getText(),
                                salt);

                        user = new EmployeeModel(employeeId, txtUserName.getText(), myScrPassword, salt);
                        if (user.updateUser(employeeId, oldUser) > 0) {
                            alertMessage.showCompletedMessage("Save", "Edit user infor successfully.", 4,
                                    Pos.BOTTOM_RIGHT);
                            clearTextField();
                        }
                    } else {
                        validRules.setErrorDecorationEnabled(true);
                        alertMessage.showWarningMessage("Save", "Please chack your information and try again.", 4,
                                Pos.BOTTOM_RIGHT);
                    }
                } else
                    validOldPass.setErrorDecorationEnabled(true);
            }
        } catch (Exception e) {
            alertMessage.showErrorMessage("Error Save Data", "Error" + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
        }
    }

    private void updateUserName() {
        try {
            if (!txtUserName.getText().equals("")) {
                final boolean chackPass = ProtectUserPassword.verifyPassword(txtOldPassword.getText(), oldScrPassword,
                        oldSalt);
                if (chackPass) {
                    user = new EmployeeModel();
                    if (user.updateUserName(employeeId, oldUser, txtUserName.getText()) > 0) {
                        alertMessage.showCompletedMessage("Save", "Edit user name successfully.", 4, Pos.BOTTOM_RIGHT);
                        clearTextField();
                    } 
                } else
                    validOldPass.setErrorDecorationEnabled(true);
            }
        } catch (Exception e) {
            alertMessage.showErrorMessage("Error Save Data", "Error" + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
        }
    }

    private void initRules() {
        validRules.setErrorDecorationEnabled(false);
        validRules.registerValidator(txtUserName, false, Validator.createEmptyValidator("ກະລຸນາປ້ອນລະຫັດພະນັກງານ"));
        if (chRenewPass.isSelected()) {
            validRules.registerValidator(txtPassword, false, Validator.createEmptyValidator("ກະລຸນາປ້ອນຊື່"));
            validRulePass.registerValidator(txtRepassword, false, (Control c, String newValue) -> ValidationResult
                    .fromErrorIf(c, "ລະຫັດຜ່ານທີ່ປ້ອນບໍ່ຄືກັນ", !newValue.equals(txtPassword.getText())));
        }
    }

    private void initEvents() {
        btSave.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                if (txtOldPassword.isDisable() && chRenewPass.isDisable()) {
                    addNewUser();
                } else if (!chRenewPass.isDisable() && chRenewPass.isSelected()) {
                    updateUser();
                } else
                    updateUserName();
            }

        });

        txtOldPassword.setOnKeyTyped(e -> validOldPass.setErrorDecorationEnabled(false));

        chRenewPass.setOnAction(e -> {
            if (chRenewPass.isSelected()) {
                txtPassword.setDisable(false);
                txtRepassword.setDisable(false);
                initRules();
            } else {
                txtPassword.setDisable(true);
                txtRepassword.setDisable(true);
            }
        });

    }

    @FXML
    private void minimizeForm(ActionEvent event) {
        stage.setIconified(true);
    }

    @FXML
    private void closeForm(ActionEvent event) {
        stage.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setMoveForm();
        initRules();
        initEvents();
    }

}
