package com.mycompany.library_project.Controller;

import com.jfoenix.controls.*;
import com.jfoenix.controls.JFXDialog.DialogTransition;
import com.mycompany.library_project.*;
import com.mycompany.library_project.ControllerDAOModel.*;
import com.mycompany.library_project.Model.EmployeeModel;
import com.mycompany.library_project.config.*;

import org.controlsfx.control.MaskerPane;
import org.controlsfx.validation.*;

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

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    private double x, y;
    public static Stage loginSatge = null;// Seted object when open form 'Login' in class 'DesktopController.java'
    private ValidationSupport validRules = new ValidationSupport();;
    private DialogMessage dialog = null;
    private Connection con = null;
    private CreateLogFile server = null;
    private EmployeeModel user = null;
    private AlertMessage alertMessage = new AlertMessage();
    private MaskerPane masker = new MaskerPane();

    @FXML
    private AnchorPane acPaneLogin;
    @FXML
    private StackPane stakePane;
    @FXML
    private JFXButton btLogin, btCancel, btMinimize, btClose;
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
            final JFXButton[] buttons = { buttonOK() };
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

                        } else {
                            if (dialog != null)
                                dialog.closeDialog();
                            dialog = new DialogMessage(stakePane, "ຄຳເຕືອນ",
                                    "ບໍ່ສາມາດເຂົ້າລະບົບໄດ້ ເນື່ອງຈາກລະຫັດຜ່ານບໍ່ຖຶກຕ້ອງ", DialogTransition.CENTER,
                                    buttons, false);
                            dialog.showDialog();
                        }

                    } else {
                        if (dialog != null)
                            dialog.closeDialog();
                        dialog = new DialogMessage(stakePane, "ຄຳເຕືອນ",
                                "ບໍ່ສາມາດເຂົ້າລະບົບໄດ້ ເນື່ອງຈາກຊື່ຜູ້ບໍ່ຖຶກຕ້ອງ", DialogTransition.CENTER, buttons,
                                false);
                        dialog.showDialog();
                    }
                } else {
                    if (dialog != null)
                        dialog.closeDialog();
                    dialog = new DialogMessage(stakePane, "ຄຳເຕືອນ",
                            "ບໍ່ສາມາດເຂົ້າລະບົບໄດ້ ເນື່ອງຈາກຊື່ຜູ້ໃຊ້ບໍ່ຖຶກຕ້ອງ", DialogTransition.CENTER, buttons,
                            false);
                    dialog.showDialog();
                }
            } else {
                validRules.setErrorDecorationEnabled(true);
                alertMessage.showWarningMessage("Login", "Please enter username, password and try again.", 4,
                        Pos.BOTTOM_RIGHT);
            }
        } catch (Exception e) {
            e.printStackTrace();
            alertMessage.showErrorMessage("Login Error", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
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
        // System.exit(0);
        final JFXButton[] buttons = { buttonYes(), buttonNo() };

        dialog = new DialogMessage(stakePane, "ຄຳເຕືອນ", "ຕ້ອງການອອກຈາກໂປຣແກຣມບໍ?", JFXDialog.DialogTransition.CENTER,
                buttons, false);
        dialog.showDialog();

        // Alert alert = new Alert(AlertType.CONFIRMATION);
        // DialogPane dialog = alert.getDialogPane();
        // dialog.getStylesheets().add(getClass().getResource("../Style/appstyle.css").toExternalForm());
        // alert.setTitle("Confirmation Dialog");
        // alert.setHeaderText("ຄຳເຕືອນ");
        // alert.setContentText("ຕ້ອງການອອກຈາກໂປຣແກຣມບໍ?");
        // alert.show();
    }

    // private void MyProgress(){
    // spinner.setVisible(false);
    // }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        masker.setVisible(false);
        masker.setPrefWidth(50.0);
        masker.setPrefHeight(50.0);
        masker.setText("ກຳລັງໂຫລດຂໍ້ມູນ, ກະລຸນາລໍຖ້າ...");
        masker.setStyle("-fx-font-family: BoonBaan;");
        stakePane.getChildren().add(masker);

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
                btLogin.requestFocus();
        });

        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                server = new CreateLogFile();
                if (server.chackFileConfig() == true) {
                    ConfigServerController.chack = true;
                    String[] infor = server.getServerInfor();
                    if (infor != null) {
                        MyConnection.server = infor[0] + ":" + infor[1];
                        MyConnection.userName = infor[2];
                        MyConnection.password = infor[3];
                        con = MyConnection.getConnect();
                        if (con == null) {
                            openConfigForm();
                        }
                    } else {
                        openConfigForm();
                    }
                } else {
                    openConfigForm();
                }
            }
        });
    }

    private JFXButton buttonYes() {
        JFXButton btyes = new JFXButton("ຕົກລົງ");
        btyes.setStyle(Style.buttonDialogStyle);
        btyes.setOnAction(e -> {
            dialog.closeDialog();
            loginSatge.close();
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

    private void openConfigForm() {
        try {
            final Parent root = FXMLLoader.load(App.class.getResource("frmConfig.fxml"));
            final Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            ConfigServerController.configStage = new Stage();
            ConfigServerController.configStage.setScene(scene);
            ConfigServerController.configStage.initStyle(StageStyle.TRANSPARENT);
            ConfigServerController.configStage.show();
        } catch (Exception e) {
            alertMessage.showErrorMessage("Open Config", "Error: " + e.getMessage(), 4, Pos.TOP_CENTER);
            server.createLogFile("ການເຂົ້າອອກລະບົບມິບັນຫາ", e);
        }
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
