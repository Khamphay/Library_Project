package com.mycompany.library_project.Controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.mycompany.library_project.App;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class ReportController implements Initializable {

    private Parent root = null;

    @FXML
    private BorderPane bpReport, borderPane_Report;

    @FXML
    private AnchorPane acMenu;

    @FXML
    private JFXButton btReportBook, btReportBookLost, btReportMember, btReportRentBook, btReportSendBook,
            btReportImport;

    @FXML
    private ScrollPane scroll_panen;

    private void initEvents() {
        btReportBook.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                try {
                    root = FXMLLoader.load(App.class.getResource("frmReportBooks.fxml"));
                    Scene scen = new Scene(root);
                    Stage stage = new Stage();
                    stage.setScene(scen);
                    stage.setResizable(false);
                    stage.showAndWait();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initEvents();
    }

}
