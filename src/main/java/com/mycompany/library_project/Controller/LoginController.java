package com.mycompany.library_project.Controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextField;
import com.mycompany.library_project.DesktopController;
import com.mycompany.library_project.ControllResize.Resizehelper;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    private double x, y;
    public static Stage loginSatge = null;//Seted object when open form 'Login' in class 'DesktopController.java'

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
    private JFXTextField txtUsername;
    @FXML
    private JFXPasswordField txtPassword;
    @FXML
    private JFXSpinner spinner;

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
        spinner.setVisible(false);
        
        
        loginSatge.close();

        //Todo:  Or use 
        Stage loginStg=(Stage) acPaneLogin.getScene().getWindow();
        loginStg.close();        
    }

    @FXML
    private void Login(ActionEvent event) throws Exception{
        spinner.setVisible(true);
        Parent root= FXMLLoader.load(getClass().getResource("/com/mycompany/library_project/MyProjectFrom/frmHome.fxml"));
        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        HomeController.homeStage = new Stage(StageStyle.TRANSPARENT);
        HomeController.homeStage.setTitle("FNS Library MS");
        HomeController.homeStage.setScene(scene);
        Resizehelper.addResizeListener(HomeController.homeStage);// Todo: Set to windows form able Resize
        HomeController.homeStage.show();

        closeThisForm();
        closeDesktopForm();
    }

    @FXML
    private void Cancel(ActionEvent event){
        txtUsername.setText("");
        spinner.setVisible(true);
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

    private void MyProgress(){
        spinner.setVisible(false);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        opacityFromMove();
        MyProgress();
    }

}