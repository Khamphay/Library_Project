package com.mycompany.library_project;

import javafx.application.Application;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.*;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.mycompany.library_project.Controller.LoginController;
import com.mycompany.library_project.ControllerDAOModel.DialogMessage;


//Todo:

/**
 * JavaFX App
 */
public class App extends Application implements Initializable {

    @Override
    public void start(Stage primaryStage) throws IOException {
        try {
            final FXMLLoader loader = new FXMLLoader(App.class.getResource("Login.fxml"));
            final Parent root = loader.load();
            Scene scene = new Scene(root);
            final LoginController loginController = loader.getController();
            scene.setFill(Color.TRANSPARENT);
            primaryStage.setTitle("FNS Library Management System - Login");
            primaryStage.initStyle(StageStyle.TRANSPARENT);
            primaryStage.setScene(scene);
            loginController.initConstructor(primaryStage);
            primaryStage.getIcons().add(new Image("/com/mycompany/library_project/Icon/icon.png"));
            primaryStage.show();

           // DesktopController.desktopStage = primaryStage;//Todo: set object
           // Resizehelper.addResizeListener(DesktopController.desktopStage);//Todo: Set to
           // windows form able Resize

       } catch (Exception e) {
           DialogMessage dialog = new DialogMessage();
           dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການເປີດໂປຣແກຣມ FNS Library", e);
       }
    }

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}