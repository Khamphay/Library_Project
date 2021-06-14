package com.mycompany.library_project.Controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.*;
import javafx.fxml.*;
import javafx.geometry.Pos;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.mycompany.library_project.ControllerDAOModel.AlertMessage;
import com.mycompany.library_project.ControllerDAOModel.StaticCostPrice;
import com.mycompany.library_project.Model.CostModel;

import org.controlsfx.validation.*;

public class SettingController implements Initializable {

    private AlertMessage alertMessage = new AlertMessage();
    private ValidationSupport valiType = new ValidationSupport();
    private CostModel costPrice = new CostModel();
    private double x, y;
    public static Stage settingStage = null; // Todo: Set object in when open Setting in class 'HomeController.java'
    double r, d, b;
    // private ObservableList<String> fonts = FXCollections
    // .observableArrayList(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());

    @FXML
    private JFXButton btSave;

    @FXML
    private TextField txtCostRegister, txtCostPerDay, txtCostPerBook;

    @FXML
    private AnchorPane acSettings;

    private void setMoveForm() {
        acSettings.setOnMousePressed(mouseEvent -> {
            x = mouseEvent.getSceneX();
            y = mouseEvent.getSceneY();
        });
        acSettings.setOnMouseDragged(mouseEvent -> {
            settingStage.setX(mouseEvent.getScreenX() - x);
            settingStage.setY(mouseEvent.getScreenY() - y);
            settingStage.setOpacity(0.4f);
        });

        acSettings.setOnDragDone(dragEvent -> {
            settingStage.setOpacity(1.0f);
        });

        acSettings.setOnMouseReleased(mouseEvent -> {
            settingStage.setOpacity(1.0f);
        });
    }

    private void initRules() {
        // validRules.setErrorDecorationEnabled(false);
        // validRules.registerValidator(txtCostRegister, false,
        // Validator.createEmptyValidator("ກະລຸນາປ້ອນຄ່າລົງທະບຽນ"));
        // validRules.registerValidator(txtCostPerDay, false,
        // Validator.createEmptyValidator("ກະລຸນາປ້ອນຄ່າ"));
        // validRules.registerValidator(txtCostPerBook, false,
        // Validator.createEmptyValidator("ກະລຸນາປ້ອນ Port"));

        valiType = new ValidationSupport();
        valiType.registerValidator(txtCostRegister, false, (Control c, String newValue) -> ValidationResult
                .fromErrorIf(c, "ປ້ອນໄດ້ສະເພາະຕົວເລກເທົ່ານັ້ນ", !newValue.matches("\\d*") || newValue.equals("")));
        valiType = new ValidationSupport();
        valiType.registerValidator(txtCostPerDay, false, (Control c, String newValue) -> ValidationResult.fromErrorIf(c,
                "ປ້ອນໄດ້ສະເພາະຕົວເລກເທົ່ານັ້ນ", !newValue.matches("\\d*") || newValue.equals("")));
        valiType = new ValidationSupport();
        valiType.registerValidator(txtCostPerBook, false, (Control c, String newValue) -> ValidationResult
                .fromErrorIf(c, "ປ້ອນໄດ້ສະເພາະຕົວເລກເທົ່ານັ້ນ", !newValue.matches("\\d*") || newValue.equals("")));

    }

    private void initEvents() {
        txtCostRegister.textProperty().addListener(new ChangeListener<String>() {
            // Todo: set properties type only numeric
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                // if (!newValue.matches("\\d*")) {
                // txtCostRegister.setText(newValue.replaceAll("[^\\d]", ""));
                // }

                valiType.setErrorDecorationEnabled(true);
            }

        });

        txtCostPerDay.textProperty().addListener(new ChangeListener<String>() {
            // Todo: set properties type only numeric
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                // if (!newValue.matches("\\d*")) {
                // txtCostPerDay.setText(newValue.replaceAll("[^\\d]", ""));
                // }

                valiType.setErrorDecorationEnabled(true);
            }

        });

        txtCostPerBook.textProperty().addListener(new ChangeListener<String>() {
            // Todo: set properties type only numeric
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                // if (!newValue.matches("\\d*")) {
                // txtCostPerBook.setText(newValue.replaceAll("[^\\d]", ""));
                // }

                valiType.setErrorDecorationEnabled(true);
            }

        });

        btSave.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                try {
                    if (!txtCostRegister.getText().equals("") && !txtCostPerBook.getText().equals("")
                            && !txtCostPerDay.getText().equals("")) {

                        double rg = (txtCostRegister.getText().equals("") ? 0
                                : Double.parseDouble(txtCostRegister.getText()));
                        double pd = (txtCostRegister.getText().equals("") ? 0
                                : Double.parseDouble(txtCostPerDay.getText()));
                        double pb = (txtCostRegister.getText().equals("") ? 0
                                : Double.parseDouble(txtCostPerBook.getText()));

                        SaveCost(rg, pd, pb);
                    } else {
                        alertMessage.showWarningMessage("Save", "Please chack your information and try again.", 4,
                                Pos.BOTTOM_RIGHT);
                    }
                } catch (Exception e) {
                    alertMessage.showWarningMessage("Save", "Please chack your information and try again.", 4,
                            Pos.BOTTOM_RIGHT);
                }
            }

        });
    }

    private void SaveCost(double costRG, double costPD, double costPB) {
        try {
            costPrice = new CostModel(costRG, costPD, costPB);
            final ResultSet rs = costPrice.findAll();
            if (!rs.next()) {
                if (costPrice.saveData() > 0) {
                    alertMessage.showCompletedMessage("Saved", "Saved data successfully.", 4, Pos.BOTTOM_RIGHT);
                }
            } else {
                if (costPrice.updateData() > 0) {
                    alertMessage.showCompletedMessage("Saved", "Saved data successfully.", 4, Pos.BOTTOM_RIGHT);
                }
            }

            StaticCostPrice.RegisterCost = costRG;
            StaticCostPrice.OutOfDateCost = costPD;
            StaticCostPrice.LostCost = costPB;

        } catch (Exception e) {
            alertMessage.showErrorMessage("Save Error", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
        }
    }

    @FXML
    private void minimizeForm(ActionEvent event) {
        settingStage.setIconified(true);
    }

    @FXML
    private void closeFormSetting(ActionEvent event) {
        settingStage.close();
        settingStage = null;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setMoveForm();
        // cmbFont.setItems(fonts);
        initRules();
        initEvents();

        txtCostRegister.setText(Double.toString(StaticCostPrice.RegisterCost).substring(0,
                Double.toString(StaticCostPrice.RegisterCost).indexOf('.')));
        txtCostPerDay.setText(Double.toString(StaticCostPrice.OutOfDateCost).substring(0,
                Double.toString(StaticCostPrice.OutOfDateCost).indexOf('.')));
        txtCostPerBook.setText(Double.toString(StaticCostPrice.LostCost).substring(0,
                Double.toString(StaticCostPrice.LostCost).indexOf('.')));
    }
}
