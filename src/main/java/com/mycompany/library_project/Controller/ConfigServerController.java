package com.mycompany.library_project.Controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.mycompany.library_project.MyConnection;
import com.mycompany.library_project.ControllerDAOModel.DialogMessage;
import com.mycompany.library_project.config.*;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.*;
import javafx.fxml.*;
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

    private ObservableList<String> listDriver = FXCollections.observableArrayList("MariaDB", "MySQL");
    private ValidationSupport validRules = new ValidationSupport();
    private DialogMessage dialog = new DialogMessage();
    private CreateLogFile config = new CreateLogFile();
    private MaskerPane masker = new MaskerPane();
    private Task<Void> task = null;
    private Stage configStage = null;
    private String driver = null, dbtype = null;
    private Double x, y;

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
    private ComboBox<String> comboxDB;

    @FXML
    private JFXButton btSave, btCancel, btTest, btClose;

    private void initRules() {
        validRules.setErrorDecorationEnabled(false);
        validRules.registerValidator(comboxDB, false, Validator.createEmptyValidator("ກະລຸນາເລືອກລະບົບຖານຂໍ້ມູນ"));
        validRules.registerValidator(txtHost, false, Validator.createEmptyValidator("ກະລຸນາປ້ອນ Host Name"));
        validRules.registerValidator(txtPort, false, Validator.createEmptyValidator("ກະລຸນາປ້ອນ Port"));
        validRules.registerValidator(txtUserName, false, Validator.createEmptyValidator("ກະລຸນາປ້ອນ User Name"));
        validRules.registerValidator(txtPassword, false,
                Validator.createEmptyValidator("ກະລຸນາປ້ອນລະຫັດຜ່ານ (ຖ້າມີ)", Severity.WARNING));

    }

    private void saveData() {

        if (txtHost.getText().equals("") || txtPort.getText().equals("") || txtUserName.getText().equals("")) {
            validRules.setErrorDecorationEnabled(true);
            return;
        }
        masker.setText("ກຳລັງບັນທືກຂໍ້ມູນ, ກະລຸນາລໍຖ້າ...");
        task = new Task<Void>() {

            @Override
            protected Void call() throws Exception {
                masker.setVisible(true);
                masker.setProgressVisible(true);

                getDriver();

                if (!txtHost.getText().equals("") && !txtPort.getText().equals("")) {

                    MyConnection.driver = driver;
                    MyConnection.dbtype = dbtype;
                    MyConnection.host = txtHost.getText() + ":" + txtPort.getText();
                    MyConnection.userName = txtUserName.getText();
                    MyConnection.password = txtPassword.getText();

                    if (HomeController.con == null)
                        HomeController.con = MyConnection.getConnect();

                    if (HomeController.con != null) {
                        if (config.createFileConfig(driver, dbtype, txtHost.getText(), txtPort.getText(),
                                txtUserName.getText(), txtPassword.getText()) == true) {
                            textResualt.setFill(Color.GREEN);
                            textResualt.setText("ບັນທືກຂໍ້ມູນສຳເລ້ດແລ້ວ");
                        }
                    }
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
                // dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການບັນທຶກການຕັ້ງຄ່າ", task.getException());
            }

        };
        new Thread(task).start();
    }

    private void testConnection() {
        if (txtHost.getText().equals("") || txtPort.getText().equals("") || txtUserName.getText().equals("")) {
            validRules.setErrorDecorationEnabled(true);
            return;
        }
        masker.setText("ກຳລັງທົດສອບການເຊື່ອມຕໍ່, ກະລຸນາລໍຖ້າ...");
        task = new Task<Void>() {

            @Override
            protected Void call() throws Exception {
                masker.setVisible(true);
                masker.setProgressVisible(true);

                getDriver();

                MyConnection.driver = driver;
                MyConnection.dbtype = dbtype;
                MyConnection.host = txtHost.getText() + ":" + txtPort.getText();
                MyConnection.userName = txtUserName.getText();
                MyConnection.password = txtPassword.getText();
                HomeController.con = MyConnection.getConnect();

                return null;
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                masker.setVisible(false);
                masker.setProgressVisible(false);
                textResualt.setFill(Color.GREEN);
                textResualt.setText("ການເຊື່ອມຕໍ່ສຳເລັດແລ້ວ");
            }

            @Override
            protected void failed() {
                super.failed();
                masker.setVisible(false);
                masker.setProgressVisible(false);
                textResualt.setFill(Color.RED);
                textResualt.setText("ການເຊື່ອມຕໍ່ບໍ່ສຳເລັດແລ້ວ, ກະລຸນາກວດສອບຂໍ້ມຸນແລ້ວ ລອງໃຫມ່ອີກຄັ້ງ");
                // dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການທົດສອບການເຊື່ອມຕໍ່ຖານຂໍ້ມູນ",
                //         task.getException());
            }

        };
        new Thread(task).start();
    }

    public void getDriver() {
        if (comboxDB.getSelectionModel().getSelectedItem().equals("MariaDB")) {
            driver = "org.mariadb.jdbc.Driver";
            dbtype = "mariadb";
        } else {
            driver = "com.mysql.cj.jdbc.Driver";
            dbtype = "mysql";
        }
    }

    private void initEvents() {

        txtPort.textProperty().addListener(new ChangeListener<String>() {
            // Todo: set properties type only numeric
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    txtPort.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }

        });

        comboxDB.setOnAction(e -> {
            getDriver();
        });

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
                comboxDB.getSelectionModel().select(0);
                txtHost.clear();
                txtPort.clear();
                txtUserName.clear();
                txtPassword.clear();
                textResualt.setText("");
                driver = "";
                dbtype = "";
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
        comboxDB.getItems().addAll(listDriver);
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
                driver = server_infor[0];
                dbtype = server_infor[1];
                if (dbtype == "mariadb" || dbtype.equals("mariadb"))
                    comboxDB.getSelectionModel().select("MariaDB");
                else
                    comboxDB.getSelectionModel().select("MySQL");

                txtHost.setText(server_infor[2]);
                txtPort.setText(server_infor[3]);
                txtUserName.setText(server_infor[4]);
                txtPassword.setText(server_infor[5]);
            }
        }

        masker.setVisible(false);
        masker.setPrefWidth(50.0);
        masker.setPrefHeight(50.0);
        masker.setStyle("-fx-font-family: BoonBaan;");
        stackPane.getChildren().add(masker);

    }

}
