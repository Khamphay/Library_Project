package com.mycompany.library_project.Controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.mycompany.library_project.App;
import com.mycompany.library_project.ControllerDAOModel.*;
import org.controlsfx.validation.*;

import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    private double x, y;
    public static Stage loginSatge = null;// Seted object when open form 'Login' in class 'DesktopController.java'
    private ValidationSupport validRules;
    private DialogMessage dialog = null;

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
        validRules = new ValidationSupport();
        validRules.registerValidator(txtUsername, false, Validator.createEmptyValidator("ກະລຸນາປ້ອນຊື່ຜູ້ໃຊ້"));

        validRules = new ValidationSupport();
        validRules.registerValidator(txtPassword, false, Validator.createEmptyValidator("ກະລຸນາປ້ອນລະຫັດຜ່ານ"));
    }

    @FXML
    private void Login(ActionEvent event) throws Exception {
        // spinner.setVisible(true);
        Parent root = FXMLLoader.load(App.class.getResource("frmHome.fxml"));
        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        HomeController.homeStage = new Stage();
        HomeController.homeStage.setTitle("FNS Library Management System");
        HomeController.homeStage.setScene(scene);
        // Resizehelper.addResizeListener(HomeController.homeStage);//! Todo: Set to
        // windows form able Resize
        HomeController.homeStage.show();

        loginSatge.close();
        // Todo: Or use
        // Stage loginStg = (Stage) acPaneLogin.getScene().getWindow();
        // loginStg.close();
    }

    @FXML
    private void Cancel(ActionEvent event) {
        txtUsername.setText("");
        // spinner.setVisible(true);
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
        // Todo: Or: dialog.setDialogProperty(stakePane, "ຄຳເຕືອນ",
        // "ຕ້ອງການອອກຈາກໂປຣແກຣມບໍ?", JFXDialog.DialogTransition.CENTER,
        // buttons);

        dialog.showDialog();
    }

    // private void MyProgress(){
    // spinner.setVisible(false);
    // }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        opacityFromMove();
        // MyProgress();
        textRules();

    }

    private JFXButton buttonYes() {
        JFXButton btyes = new JFXButton("ຕົກລົງ");
        btyes.setStyle(
                "-fx-border-radius: 0.5em; -fx-border-color: derive(#060621, 95%); -fx-background-radius: 0.5em;");
        btyes.setOnAction(e -> {
            dialog.closeDialog();
            loginSatge.close();
        });
        return btyes;
    }

    private JFXButton buttonNo() {
        JFXButton btno = new JFXButton("  ບໍ່  ");
        btno.setStyle(
                "-fx-border-radius: 0.5em; -fx-border-color: derive(#060621, 95%); -fx-background-radius: 0.5em;");
        btno.setOnAction(e -> {
            dialog.closeDialog();
        });
        return btno;
    }
}
