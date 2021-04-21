package com.mycompany.library_project;

import javafx.application.Application;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.paint.Color;
import javafx.stage.*;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.mycompany.library_project.Controller.LoginController;


//Todo:

/**
 * JavaFX App
 */
public class App extends Application implements Initializable {

    @Override
    public void start(Stage primaryStage) throws IOException {
       
        Parent root= FXMLLoader.load(App.class.getResource("Login.fxml"));
        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        primaryStage.setTitle("FNS Library Management System - Login");
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setScene(scene);
        //DesktopController.desktopStage = primaryStage;//Todo: set object
        // Resizehelper.addResizeListener(DesktopController.desktopStage);//Todo: Set to windows form able Resize 

        LoginController.loginSatge = primaryStage;
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}