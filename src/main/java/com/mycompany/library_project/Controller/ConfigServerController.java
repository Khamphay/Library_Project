package com.mycompany.library_project.Controller;

import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.mycompany.library_project.MyConnection;
import com.mycompany.library_project.ControllerDAOModel.*;
import com.mycompany.library_project.config.*;

import javafx.concurrent.Task;
import javafx.event.*;
import javafx.fxml.*;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import org.controlsfx.control.MaskerPane;
import org.controlsfx.validation.Severity;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

public class ConfigServerController implements Initializable {

    private ValidationSupport validRules = new ValidationSupport();
    private Double x, y;
    private AlertMessage alertMessage = new AlertMessage();
    private CreateLogFile config = new CreateLogFile();
    private MaskerPane masker = new MaskerPane();
    private Stage configStage = null;

    public void initConstructor(Stage stage) {
        this.configStage = stage;
        btClose.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                configStage.close();
            }

        });
    }

    @FXML
    private StackPane stackPane;

    @FXML
    private AnchorPane acTopBar;

    @FXML
    private TextField txtHost, txtPort, txtUserName, txtPassword;

    @FXML
    private Text textResualt;

    @FXML
    private JFXButton btSave, btCancel, btTest, btClose;

    private void initRules() {
        validRules.setErrorDecorationEnabled(false);
        validRules.registerValidator(txtHost, false, Validator.createEmptyValidator("ກະລຸນາປ້ອນ Host Name"));
        validRules.registerValidator(txtPort, false, Validator.createEmptyValidator("ກະລຸນາປ້ອນ Port"));
        validRules.registerValidator(txtUserName, false, Validator.createEmptyValidator("ກະລຸນາປ້ອນ User Name"));
        validRules.registerValidator(txtPassword, false,
                Validator.createEmptyValidator("ກະລຸນາປ້ອນລະຫັດຜ່ານ (ຖ້າມີ)", Severity.WARNING));

    }

    private void saveData() {

        masker.setText("ກຳລັງບັນທືກຂໍ້ມູນ, ກະລຸນາລໍຖ້າ...");
        Task<Void> task = new Task<Void>() {

            @Override
            protected Void call() throws Exception {
                masker.setVisible(true);
                masker.setProgressVisible(true);

                if (!txtHost.getText().equals("") && !txtPort.getText().equals("")) {
                    if (config.createFileConfig(txtHost.getText(), txtPort.getText(), txtUserName.getText(),
                            txtPassword.getText()) == true) {
                        MyConnection.server = txtHost.getText() + ":" + txtPort.getText();
                        MyConnection.userName = txtUserName.getText();
                        MyConnection.password = txtPassword.getText();
                        textResualt.setFill(Color.GREEN);
                        textResualt.setText("ບັນທືກຂໍ້ມູນສຳເລ້ດແລ້ວ");
                    }
                } else {
                    validRules.setErrorDecorationEnabled(true);
                    alertMessage.showWarningMessage("Save Configed", "Please input host name, port. and try again.", 4,
                            Pos.TOP_CENTER);
                }
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
                masker.setVisible(false);
                masker.setProgressVisible(false);
                textResualt.setFill(Color.RED);
                textResualt.setText("ບັນທືກຂໍ້ມູນບໍ່ສຳເລ້ດ, ກະລຸນາກວດສອບຂໍ້ມຸນແລ້ວ ລອງໃຫມ່ອີກຄັ້ງ.");
            }

        };
        new Thread(task).start();
    }

    private void testConnection() {
        masker.setText("ກຳລັງທົດສອບການເຊື່ອມຕໍ່, ກະລຸນາລໍຖ້າ...");
        Task<Void> task = new Task<Void>() {

            @Override
            protected Void call() throws Exception {
                masker.setVisible(true);
                masker.setProgressVisible(true);

                validRules.setErrorDecorationEnabled(true);
                MyConnection.server = txtHost.getText() + ":" + txtPort.getText();
                MyConnection.userName = txtUserName.getText();
                MyConnection.password = txtPassword.getText();
                Connection con = MyConnection.getConnect();
                if (con != null) {
                    textResualt.setFill(Color.GREEN);
                    textResualt.setText("ການເຊື່ອມຕໍ່ສຳເລັດແລ້ວ");
                }
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
                masker.setVisible(false);
                masker.setProgressVisible(false);
                textResualt.setFill(Color.RED);
                textResualt.setText("ການເຊື່ອມຕໍ່ບໍ່ສຳເລັດແລ້ວ, ກະລຸນາກວດສອບຂໍ້ມຸນແລ້ວ ລອງໃຫມ່ອີກຄັ້ງ");
            }

        };
        new Thread(task).start();
    }

    private void initEvents() {
        btSave.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                saveData();
            }
        });

        btTest.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                testConnection();
            }

        });

        btCancel.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                validRules.setErrorDecorationEnabled(false);
                txtHost.clear();
                txtPort.clear();
                txtUserName.clear();
                txtPassword.clear();
                textResualt.setText("");
            }

        });
    }

    private void initKeyEvents() {
        txtHost.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER)
                txtPort.requestFocus();
        });
        txtPort.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER)
                txtUserName.requestFocus();
        });
        txtUserName.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER)
                txtPassword.requestFocus();
        });
        txtPassword.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER)
                txtHost.requestFocus();

        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initEvents();
        initRules();
        initKeyEvents();
        // Todo: Move From
        acTopBar.setOnMousePressed(mouseEvent -> {
            x = mouseEvent.getSceneX();
            y = mouseEvent.getSceneY();
        });
        // TODO: Set for move form
        acTopBar.setOnMouseDragged(mouseEvent -> {
            configStage.setX(mouseEvent.getScreenX() - x);
            configStage.setY(mouseEvent.getScreenY() - y);
            configStage.setOpacity(0.4f);
        });

        acTopBar.setOnDragDone(mouseEvent -> {
            configStage.setOpacity(1.0f);
        });

        acTopBar.setOnMouseReleased(mouseEvent -> {
            configStage.setOpacity(1.0f);
        });

        if (config.chackFileConfig()) {
            String[] server_infor = config.getServerInfor();
            if (server_infor != null) {
            txtHost.setText(server_infor[0]);
            txtPort.setText(server_infor[1]);
            txtUserName.setText(server_infor[2]);
            txtPassword.setText(server_infor[3]);
        }
    }

        masker.setVisible(false);
        masker.setPrefWidth(50.0);
        masker.setPrefHeight(50.0);
        masker.setStyle("-fx-font-family: BoonBaan;");
        stackPane.getChildren().add(masker);

    }


}
