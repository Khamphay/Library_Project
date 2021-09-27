package com.mycompany.library_project.Controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.mycompany.library_project.ControllerDAOModel.*;
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
import javafx.stage.Stage;

public class RecoveryuserController implements Initializable {

    private ValidationSupport validRules = new ValidationSupport();
    private ValidationSupport validRulePass = new ValidationSupport();
    private ValidationSupport validPassLengt = new ValidationSupport();
    private AlertMessage alertMessage = new AlertMessage();
    private DialogMessage dialog = new DialogMessage();
    private EmployeeModel user = new EmployeeModel();
    // private Double x, y;
    private Stage stage = null;
    private final String salt = ProtectUserPassword.getSalt(40);

    @FXML
    private AnchorPane achPane, achHeader;

    @FXML
    private JFXButton btSave;

    @FXML
    private TextField txtUserName;

    @FXML
    private PasswordField txtPassword, txtRepassword;

    private void clearTextField() {
        txtUserName.clear();
        txtPassword.clear();
        txtRepassword.clear();
    }

    /*
     * private void setMoveForm() { achHeader.setOnMousePressed(mouseEvent -> { x =
     * mouseEvent.getSceneX(); y = mouseEvent.getSceneY(); });
     * achHeader.setOnMouseDragged(mouseEvent -> {
     * stage.setX(mouseEvent.getScreenX() - x); stage.setY(mouseEvent.getScreenY() -
     * y); // stage.setOpacity(0.4f); });
     * 
     * // achHeader.setOnDragDone(dragEvent -> { // stage.setOpacity(1.0f); // }); }
     */

    private void updateUser() {
        try {
            if (!txtUserName.getText().equals("") && !txtPassword.getText().equals("")
                    && !txtRepassword.getText().equals("")) {

                if (txtPassword.getText().length() < 6) {
                    dialog.showWarningDialog(null, "ລະຫັດຜ່ານຕ້ອງມີຕົວເລກ ຫຼື ຕົວອັກສອນຢ່າງໜ້ອຍ 6 ຕົວ");
                    txtRepassword.requestFocus();
                    return;
                }

                String empId = user.getEmpID(txtUserName.getText());
                if (empId == null || empId == "") {
                    dialog.showWarningDialog(null,
                            "ຊື່, ເບີໂທລະສັບ ຫຼື Email ບໍ່ຖຶກຕ້ອງ ກະລຸນາກວດສອບແລ້ວລອງໃຫມ່ອິກຄັ້ງ");
                    return;
                }

                if (txtPassword.getText().equals(txtRepassword.getText())) {

                    // Todo: Encrytp Password
                    final String myScrPassword = ProtectUserPassword.generateSecurePassword(txtPassword.getText(),
                            salt);
                    if (user.RecoveryPassword(myScrPassword, salt, txtUserName.getText(), empId) > 0) {
                        alertMessage.showCompletedMessage("Save", "Recovery Password successfully.", 4,
                                Pos.BOTTOM_RIGHT);
                        clearTextField();
                    } else {
                        dialog.showWarningDialog(null, "ບໍ່ສາມາດກູ້ຄືນລະຫັດຜ່ານ ເນື່ອງຈາກຂໍ້ມູນບໍ່ຖຶກຕ້ອງ");
                    }
                } else {
                    validRules.setErrorDecorationEnabled(true);
                    alertMessage.showWarningMessage("Save", "Please chack your information and try again.", 4,
                            Pos.BOTTOM_RIGHT);
                }
            }
        } catch (Exception e) {
            dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການປ່ຽນລະຫັດຜ່ານ", e);
        }
    }

    private void initRules() {
        validRules.setErrorDecorationEnabled(false);
        validRules.registerValidator(txtUserName, false, Validator.createEmptyValidator("ກະລຸນາປ້ອນລະຫັດພະນັກງານ"));
        validPassLengt.registerValidator(txtPassword, false, (Control c, String newValue) -> ValidationResult
                .fromErrorIf(c, "ລະຫັດຜ່ານຕ້ອງມີຕົວເລກ ຫຼື ຕົວອັກສອນຢ່າງໜ້ອຍ 6 ຕົວ", newValue.length() < 6));
        validRules.registerValidator(txtPassword, false, Validator.createEmptyValidator("ກະລຸນາປ້ອນຊື່"));
        validRulePass.registerValidator(txtRepassword, false, (Control c, String newValue) -> ValidationResult
                .fromErrorIf(c, "ລະຫັດຜ່ານທີ່ປ້ອນບໍ່ຄືກັນ", !newValue.equals(txtPassword.getText())));
    }

    private void initEvents() {
        btSave.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                updateUser();
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
        // setMoveForm();
        initRules();
        initEvents();
    }

}
