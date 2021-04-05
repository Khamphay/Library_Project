package com.mycompany.library_project.Controller;

import com.jfoenix.controls.JFXButton;
import com.mycompany.library_project.App;
import com.mycompany.library_project.DesktopController;
import com.mycompany.library_project.ModelShow.SummaryData;

import org.controlsfx.control.decoration.*;
import org.controlsfx.validation.*;

import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    private double x, y;
    public static Stage loginSatge = null;//Seted object when open form 'Login' in class 'DesktopController.java'
    private ValidationSupport validRules;
    private Decoration decoration;

    @FXML
    private AnchorPane acPaneLogin;
    @FXML
    private JFXButton btLogin;
    @FXML
    private  JFXButton btCancel;
    @FXML
    private JFXButton btMinimize;
    @FXML
    private JFXButton btClose;
    @FXML
    private TextField txtUsername;
    @FXML
    private PasswordField txtPassword;
//    @FXML
//    private JFXSpinner spinner;

    // TODO: Custom move form
    private void opacityFromMove() {
        acPaneLogin.setOnMousePressed(mouseEvent -> {
            x = mouseEvent.getSceneX();
            y = mouseEvent.getSceneY();
        });
        //TODO: Set for move form
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

    //Close Desktop Form
    private void closeDesktopForm() {
        Stage dststg = DesktopController.desktopStage;
        dststg.close();
    }

    //Close this form
    private void closeThisForm() {
        //Close Login Form
       // spinner.setVisible(false);

        loginSatge.close();

        //Todo:  Or use 
        Stage loginStg = (Stage) acPaneLogin.getScene().getWindow();
        loginStg.close();
    }
    
    private void textRules() {
        validRules = new ValidationSupport();
        validRules.registerValidator(txtUsername, false, Validator.createEmptyValidator("ກະລຸນາປ້ອນຊື່ຜູ້ໃຊ້"));

        validRules = new ValidationSupport();
        validRules.registerValidator(txtPassword, false, Validator.createEmptyValidator("ກະລຸນາປ້ອນລະຫັດຜ່ານ"));
    }

    @FXML
    private void Login(ActionEvent event) throws Exception{
      //  spinner.setVisible(true);
        Parent root = FXMLLoader.load(App.class.getResource("frmHome.fxml"));
        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        HomeController.homeStage = new Stage();
        HomeController.homeStage.setTitle("FNS Library Management System");
        HomeController.homeStage.setScene(scene);
        // Resizehelper.addResizeListener(HomeController.homeStage);//! Todo: Set to
        // windows form able Resize
        HomeController.homeStage.show();

        closeThisForm();
       // closeDesktopForm();
    }

    @FXML
    private void Cancel(ActionEvent event){
        txtUsername.setText("");
       // spinner.setVisible(true);
    }

    @FXML
    private void minimizeFrom(ActionEvent mouseEvent){
        loginSatge.setIconified(true);
    }

    //Exit Programe
    @FXML
    private void closeForm(ActionEvent mouseEvent) {
        // System.exit(0);
        closeThisForm();
    }

//    private void MyProgress(){
//        spinner.setVisible(false);
//    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        getSummaryData();
        opacityFromMove();
        //MyProgress();
        textRules();

    }

    public void getSummaryData() {
        SummaryData book = new SummaryData("call sumBook();");
        book.start();

        SummaryData member = new SummaryData("call sumMember();");
        member.start();
    }

}
