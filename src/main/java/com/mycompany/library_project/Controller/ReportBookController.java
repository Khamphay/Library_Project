package com.mycompany.library_project.Controller;

import java.net.URL;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import com.jfoenix.controls.*;
import com.mycompany.library_project.Style;
import com.mycompany.library_project.ControllerDAOModel.*;
import com.mycompany.library_project.Report.CreateReport;

import org.controlsfx.control.MaskerPane;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;

public class ReportBookController implements Initializable {

    private AlertMessage alertMessage = new AlertMessage();
    private DialogMessage dialog = null;
    private CreateReport report = null;
    Map<String, Object> map = null;
    private JFXButton[] buttons = { buttonOK() };
    private MaskerPane masker = new MaskerPane();
    String logo = Paths.get("bin/Logo.png").toAbsolutePath().toString();

    @FXML
    private StackPane stackPane;

    @FXML
    private JFXRadioButton rdbReportAll, rdbReportByBookId;

    @FXML
    private TextField txtBookId;

    @FXML
    private JFXButton btReport;

    private void initEvents() {

        rdbReportAll.setOnAction(e -> {
            txtBookId.setDisable(true);
            txtBookId.clear();
        });

        rdbReportByBookId.setOnAction(e -> {
            txtBookId.setDisable(false);
        });

        btReport.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                if (rdbReportByBookId.isSelected())
                    if (txtBookId.getText().equals("")) {
                        dialog = new DialogMessage(stackPane, "ຄຳເຕືອນ",
                                "ກະລຸນາປ້ອນລະຫັດປຶ້ມທີ່ຕ້ອງການລາຍງານແລ້ວ ລອງໃຫມ່ອີກຄັ້ງ.",
                                JFXDialog.DialogTransition.CENTER, buttons, false);
                        dialog.showDialog();
                        return;
                    }

                Task<Void> task = new Task<Void>() {

                    @Override
                    protected Void call() throws Exception {
                        masker.setVisible(true);
                        masker.setProgressVisible(true);
                        if (rdbReportByBookId.isSelected()) {
                            report = new CreateReport();
                            map = new HashMap<String, Object>();
                            map.put("logo", logo);
                            map.put("bookid", txtBookId.getText());
                            report.showReport(map, "reportBookById.jrxml", "Report Book By ID Error");
                        } else {
                            report = new CreateReport();
                            map = new HashMap<String, Object>();
                            map.put("logo", logo);
                            report.showReport(map, "reportAllBooks.jrxml", "Report All Book Error");
                        }
                        return null;
                    }

                    @Override
                    protected void succeeded() {
                        super.succeeded();
                        masker.setProgressVisible(false);
                        masker.setVisible(false);
                    }

                    @Override
                    protected void failed() {
                        super.failed();
                        masker.setProgressVisible(false);
                        masker.setVisible(false);
                        alertMessage.showErrorMessage("Report", "Report Failed", 4, Pos.BOTTOM_RIGHT);
                    }
                };
                new Thread(task).start();
            }

        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ToggleGroup group = new ToggleGroup();
        rdbReportAll.setToggleGroup(group);
        rdbReportByBookId.setToggleGroup(group);
        rdbReportAll.setSelected(true);
        txtBookId.setDisable(true);

        masker.setVisible(false);
        masker.setPrefWidth(50.0);
        masker.setPrefHeight(50.0);
        masker.setText("ກຳລັງໂຫຼດຂໍ້ມູນ, ກະລຸນາລໍຖ້າ...");
        masker.setStyle("-fx-font-family: BoonBaan;");
        stackPane.getChildren().add(masker);

        initEvents();
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
