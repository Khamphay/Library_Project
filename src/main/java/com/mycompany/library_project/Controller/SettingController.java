package com.mycompany.library_project.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class SettingController  implements Initializable {

    private double x, y;
    public static Stage settingStage = null; //Todo: Set object in when open Setting in class 'HomeController.java'

    @FXML
    private AnchorPane acSettings;

    private void setMoveForm(){
        acSettings.setOnMousePressed(mouseEvent -> {
            x=mouseEvent.getSceneX();
            y=mouseEvent.getSceneY();
        });
        acSettings.setOnMouseDragged(mouseEvent -> {
            settingStage.setX(mouseEvent.getScreenX()-x);
            settingStage.setY(mouseEvent.getScreenY()-y);
            settingStage.setOpacity(0.4f);
        });

        acSettings.setOnDragDone(dragEvent -> {
            settingStage.setOpacity(1.0f);
        });

        acSettings.setOnMouseReleased(mouseEvent -> {
            settingStage.setOpacity(1.0f);
        });
    }

    @FXML
    private void minimizeForm(ActionEvent event){
        settingStage.setIconified(true);
    }

    @FXML
    private void closeFormSetting(ActionEvent event){
        Stage stage= (Stage) acSettings.getScene().getWindow();
        stage.close();
        settingStage=null;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setMoveForm();
    }
}
