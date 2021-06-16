package com.mycompany.library_project.Controller;

import java.net.URL;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.mycompany.library_project.App;
import com.mycompany.library_project.Report.CreateReport;


import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TitledPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MenuController1 implements Initializable {

    private CreateReport report = null;
    private Map<String, Object> map = null;

    @FXML
    private JFXButton btHome, btManageBook, btManagePerson, btImport, btSetting;

    @FXML
    private TitledPane titleReport;

    @FXML
    private JFXButton btReportBook, btReportRentBook, btReportImport, btReportBookLost, btReportMember,
            btReportAdjustment;

    private void initEvents() {
        btReportBook.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                try {
                    final Parent root = FXMLLoader.load(App.class.getResource("frmReportBooks.fxml"));
                    final Scene scen = new Scene(root);
                    final Stage stage = new Stage();
                    stage.setScene(scen);
                    stage.setResizable(false);
                    stage.setTitle("Report Books");
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });

        btReportBookLost.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                try {
                    final Parent root = FXMLLoader.load(App.class.getResource("frmReportBookLost.fxml"));
                    final Scene scene = new Scene(root);
                    final Stage stage = new Stage();
                    stage.setScene(scene);
                    stage.setResizable(false);
                    stage.setTitle("Report Books Lost");
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });

        btReportMember.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                report = new CreateReport();
                map = new HashMap<String, Object>();
                map.put("logo", Paths.get("bin/Logo.png").toAbsolutePath().toString());
                report.showReport(map, "reportMember.jrxml", "Report All Member Error");
            }

        });

        btReportAdjustment.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                try {
                    final Parent root = FXMLLoader.load(App.class.getResource("frmReportAbjustment.fxml"));
                    final Scene scene = new Scene(root);
                    final Stage stage = new Stage();
                    stage.setScene(scene);
                    stage.setResizable(false);
                    stage.setTitle("Report Adjustment");
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.show();
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
