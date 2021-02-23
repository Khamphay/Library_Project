package com.mycompany.library_project;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.mycompany.library_project.Controller.LoginController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class DesktopController implements Initializable {

    public static Stage desktopStage = null;//Todo: seted object from class 'Add.java' when you run project
    // public static Stage dstStage;
    private boolean max_res = false;
    private double x, y;
    private Rectangle2D bounds=null;

    @FXML
    private AnchorPane acpneHome;
    @FXML
    private AnchorPane acpneToolBar;
    @FXML
    private ScrollPane scrollDisplay;
    @FXML
    private BorderPane boderLayout;
    @FXML 
    private JFXButton btLogin;
    @FXML
    private JFXButton btMinimize;
    @FXML
    private JFXButton btMaximum;
    @FXML
    private JFXButton btCloseForm;

    private void moveFormAble() {
        acpneToolBar.setOnMousePressed(event -> {
            x = event.getSceneX();
            y = event.getSceneY();
        });

        acpneToolBar.setOnMouseDragged(event -> {
            desktopStage.setX(event.getScreenX() - x);
            desktopStage.setY(event.getScreenY() - y);
            desktopStage.setOpacity(0.4f);
        });
        acpneHome.setOnMouseReleased(event -> {
            desktopStage.setOpacity(1.0f);
        });
        acpneHome.setOnDragDone(event -> {
            desktopStage.setOpacity(1.0f);
        });
    }

    @FXML
    private void minimixeFrom(ActionEvent event) {
        desktopStage.setIconified(true);
    }

    @FXML
    private void maximunForm(ActionEvent event) {
        if (max_res == true) {
            //Todo: Restore win form
            desktopStage.setMaximized(false);
            max_res = false;
        } else {
            //TODO: Maximize able only the screen (not hide the taksbar
            bounds = Screen.getPrimary().getVisualBounds();
            desktopStage.setX(bounds.getMinX());
            desktopStage.setY(bounds.getMinY());
            desktopStage.setWidth(bounds.getWidth());
            desktopStage.setHeight(bounds.getHeight());

            //Todo: Maximize win form
            desktopStage.setMaximized(true);
            max_res = true;
        }
    }

    @FXML
    private void closePrograme(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    private void LoginMS(ActionEvent event)throws Exception {
        Parent rootLogin = FXMLLoader.load(App.class.getResource("Login.fxml"));
        Scene scene = new Scene(rootLogin);
        LoginController.loginSatge = new Stage(StageStyle.TRANSPARENT);
        scene.setFill(Color.TRANSPARENT);// Todo: set 'transparent' color to form, you must set 'transparent' to style in fxml
        LoginController.loginSatge.setScene(scene);
        LoginController.loginSatge.setTitle("Login");
        LoginController.loginSatge.initModality(Modality.APPLICATION_MODAL); //Todo: set dialog mode
        LoginController.loginSatge.showAndWait();// Todo: Show dialog mode
        
        // boderLayout.setCenter(rootLogin);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        moveFormAble();
    }
}
