package com.mycompany.library_project.Controller;

import com.jfoenix.controls.*;
import com.mycompany.library_project.*;
import com.mycompany.library_project.ControllerDAOModel.*;
import com.mycompany.library_project.Model.EmployeeModel;
import com.mycompany.library_project.config.*;

import org.controlsfx.control.MaskerPane;
import org.controlsfx.validation.*;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Optional;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    private double x, y;
    private Stage loginSatge = null;// Seted object when open form 'Login' in class 'DesktopController.java'
    private ValidationSupport validRules = new ValidationSupport();;
    private DialogMessage dialog = new DialogMessage();
    private Connection con = null;
    private CreateLogFile server = null;
    private EmployeeModel user = null;
    private AlertMessage alertMessage = new AlertMessage();
    private MaskerPane masker = new MaskerPane();
    private TranslateTransition slider = new TranslateTransition();

    public void initConstructor(Stage primaryStage) {
        this.loginSatge = primaryStage;
    }

    @FXML
    private AnchorPane acPaneLogin, layerSlider;
    @FXML
    private StackPane stakePane;
    @FXML
    private JFXButton btLogin, btCancel, btMinimize, btClose, btSearch, btSingin, btBack;
    @FXML
    private TextField txtUsername;
    @FXML
    private PasswordField txtPassword;

    // @FXML
    // private JFXSpinner spinner;

    // TODO: Custom move form
    private void opacityFromMove() {
        acPaneLogin.setOnMousePressed(mouseEvent -> {
            x = mouseEvent.getSceneX();
            y = mouseEvent.getSceneY();
        });
        // TODO: Set for move form
        acPaneLogin.setOnMouseDragged(mouseEvent -> {
            loginSatge.setX(mouseEvent.getScreenX() - x);
            loginSatge.setY(mouseEvent.getScreenY() - y);
            loginSatge.setOpacity(0.4f);
        });

        acPaneLogin.setOnDragDone(mouseEvent -> {
            loginSatge.setOpacity(1.0f);
        });

        acPaneLogin.setOnMouseReleased(mouseEvent -> {
            loginSatge.setOpacity(1.0f);
        });
    }

    private void textRules() {
        validRules.setErrorDecorationEnabled(false);
        validRules.redecorate();
        validRules.registerValidator(txtUsername, false, Validator.createEmptyValidator("ກະລຸນາປ້ອນຊື່ຜູ້ໃຊ້"));
        validRules.registerValidator(txtPassword, false, Validator.createEmptyValidator("ກະລຸນາປ້ອນລະຫັດຜ່ານ"));
    }

    private void loginMethod() {

        try {
            if (!txtUsername.getText().equals("") && !txtPassword.getText().equals("")) {
                user = new EmployeeModel();
                final ResultSet rs = user.Login(txtUsername.getText());
                if (rs.next()) {
                    if (txtUsername.getText().equals(rs.getString("user_name"))) {
                        final String salt = rs.getString("salt");
                        final String scrPassword = rs.getString("password");
                        final String providePassword = txtPassword.getText();
                        final boolean checkPassword = ProtectUserPassword.verifyPassword(providePassword, scrPassword,
                                salt);
                        if (checkPassword) {
                            final String[] userInfor = { rs.getString("full_name"), rs.getString("sur_name") };
                            final FXMLLoader loader = new FXMLLoader(App.class.getResource("frmHome.fxml"));
                            final Parent root = loader.load();
                            final Scene scene = new Scene(root);
                            final HomeController homeController = loader.getController();
                            scene.setFill(Color.TRANSPARENT);
                            homeController.homeStage = new Stage();
                            homeController.homeStage.setTitle("FNS Library Management System");
                            homeController.homeStage.setScene(scene);
                            homeController.initConstructor(userInfor);
                            homeController.homeStage.getIcons()
                                    .add(new Image("/com/mycompany/library_project/Icon/icon.png"));
                            homeController.homeStage.show();
                            loginSatge.close();

                        } else
                            dialog.showWarningDialog(null, "ບໍ່ສາມາດເຂົ້າລະບົບໄດ້ ເນື່ອງຈາກລະຫັດຜ່ານບໍ່ຖຶກຕ້ອງ");
                    } else
                        dialog.showWarningDialog(null, "ບໍ່ສາມາດເຂົ້າລະບົບໄດ້ ເນື່ອງຈາກຊື່ຜູ້ບໍ່ຖຶກຕ້ອງ");
                } else
                    dialog.showWarningDialog(null, "ບໍ່ສາມາດເຂົ້າລະບົບໄດ້ ເນື່ອງຈາກຊື່ຜູ້ໃຊ້ບໍ່ຖຶກຕ້ອງ");
            } else {
                validRules.setErrorDecorationEnabled(true);
                alertMessage.showWarningMessage("Login", "Please enter username, password and try again.", 4,
                        Pos.BOTTOM_RIGHT);
            }
        } catch (Exception e) {
            dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການເຂົ້າສູ່ລະບົບ", e);
        }
    }

    @FXML
    private void Login(ActionEvent event) throws Exception {

        loginMethod();

        /*
         * spinner.setVisible(true); //
         * Resizehelper.addResizeListener(HomeController.homeStage);//! Todo: Set to //
         * windows form able Resize // Todo: Or use // Stage loginStg = (Stage)
         * acPaneLogin.getScene().getWindow(); // loginStg.close();
         */
    }

    @FXML
    private void Cancel(ActionEvent event) {
        validRules.setErrorDecorationEnabled(false);
        txtUsername.setText("");
        // spinner.setVisible(true);
        txtPassword.setText("");
    }

    @FXML
    private void minimizeFrom(ActionEvent mouseEvent) {
        loginSatge.setIconified(true);
    }

    // Exit Programe
    @FXML
    private void closeForm(ActionEvent mouseEvent) {

        Optional<ButtonType> result = dialog.showComfirmDialog("Exit Programe", null, "ຕ້ອງການອອກຈາກໂປຣແກຣມບໍ?");
        if (result.get() == ButtonType.YES)
            loginSatge.close();
    }

    // private void MyProgress(){
    // spinner.setVisible(false);
    // }

    @FXML
    private void slideSingin(ActionEvent event) {
        btLogin.setDisable(false);
        btCancel.setDisable(false);
        btBack.setDisable(false);

        slider.setToX(344);
        slider.play();

        slider.setOnFinished(e -> {
            btSearch.setDisable(true);
            btSingin.setDisable(true);
        });
    }

    @FXML
    private void slideBack(ActionEvent event) {
        slider.setToX(0);
        btSearch.setDisable(false);
        btSingin.setDisable(false);

        slider.play();
        slider.setOnFinished(e -> {
            btLogin.setDisable(true);
            btCancel.setDisable(true);
            btBack.setDisable(true);
        });
    }

    @FXML
    private void SearchBookData(ActionEvent event) {
        try {
            final FXMLLoader loader = new FXMLLoader(App.class.getResource("frmSearch.fxml"));
            final Parent root = loader.load();
            final Scene scene = new Scene(root);
            final Stage stage = new Stage();
            stage.setTitle("FNS Library Management System - Search");
            final SearchController searchController = loader.getController();
            searchController.initConstructor2();
            stage.setScene(scene);
            stage.getIcons().add(new Image("/com/mycompany/library_project/Icon/icon.png"));
            stage.show();
            loginSatge.close();
        } catch (Exception e) {
            dialog.showExcectionDialog("Error", null, "ການເປີດຟອມຄົ້ນຫາປຶ້ມມີບັນຫາ", e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        masker.setVisible(false);
        masker.setPrefWidth(50.0);
        masker.setPrefHeight(50.0);
        masker.setText("ກຳລັງໂຫລດຂໍ້ມູນ, ກະລຸນາລໍຖ້າ...");
        masker.setStyle("-fx-font-family: BoonBaan;");
        stakePane.getChildren().add(masker);

        slider.setDuration(Duration.seconds(0.6));
        slider.setNode(layerSlider);

        btLogin.setDisable(true);
        btCancel.setDisable(true);
        btBack.setDisable(true);

        // Todo: Use FontAwesomeIcon
        // btLogin.setGraphic(new FontIcon());
        // btCancel.setGraphic(new FontIcon());

        opacityFromMove();
        // MyProgress();
        textRules();

        txtUsername.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER)
                txtPassword.requestFocus();
        });
        txtPassword.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER)
            {
                btLogin.requestFocus();
                loginMethod();
            }
        });

        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                server = new CreateLogFile();
                if (server.chackFileConfig() == true) {
                    String[] infor = server.getServerInfor();
                    if (infor != null) {
                        MyConnection.server = infor[0] + ":" + infor[1];
                        MyConnection.userName = infor[2];
                        MyConnection.password = infor[3];
                        con = MyConnection.getConnect();
                        if (con == null)
                            checkConnection();
                    } else
                        checkConnection();
                } else
                    checkConnection();
            }
        });
    }

    private void checkConnection() {
        try {
            final FXMLLoader loader = new FXMLLoader(App.class.getResource("frmConfig.fxml"));
            final Parent root = loader.load();
            final Scene scene = new Scene(root);
            final Stage stage = new Stage(StageStyle.TRANSPARENT);
            final ConfigServerController configServerController = loader.getController();
            configServerController.initConstructor(stage);
            scene.setFill(Color.TRANSPARENT);
            stage.setTitle("Config Database");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການເປີດຟອມ Config ຖານຂໍ້ມູນ", e);
        }
    }

}
