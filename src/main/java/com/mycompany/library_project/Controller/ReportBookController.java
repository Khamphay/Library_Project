package com.mycompany.library_project.Controller;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import com.jfoenix.controls.*;
import com.mycompany.library_project.Style;
import com.mycompany.library_project.ControllerDAOModel.*;
import com.mycompany.library_project.Report.CreateReport;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;

public class ReportBookController implements Initializable {

    private DialogMessage dialog = null;
    private CreateReport report = null;
    Map<String, Object> map = null;
    private JFXButton[] buttons = { buttonOK() };

    @FXML
    private StackPane stackPane;

    @FXML
    private JFXRadioButton rdbReportAll, rdbReportByBookId, rdbReportByBarcode;

    @FXML
    private TextField txtBookId, txtBarcode;

    @FXML
    private JFXButton btReport;

    private void initEvents() {

        rdbReportAll.setOnAction(e -> {
            txtBookId.setDisable(true);
            txtBookId.clear();
            txtBarcode.setDisable(true);
            txtBarcode.clear();
        });

        rdbReportByBookId.setOnAction(e -> {
            txtBookId.setDisable(false);
            txtBarcode.setDisable(true);
            txtBarcode.clear();
        });

        rdbReportByBarcode.setOnAction(e -> {
            txtBarcode.setDisable(false);
            txtBookId.clear();
            txtBookId.setDisable(true);
        });

        btReport.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                if (rdbReportByBookId.isSelected()) {
                    if (!txtBookId.getText().equals("")) {
                        report = new CreateReport();
                        map = new HashMap<String, Object>();
                        InputStream logo = this.getClass().getResourceAsStream("bin/Logo_FNS.png");
                        map.put("logo", logo);
                        map.put("bookid", txtBookId.getText());
                        report.showReport(map, "reportBookById.jrxml", "Report Book By ID Error");
                    } else {
                        dialog = new DialogMessage(stackPane, "ຄຳເຕືອນ",
                                "ກະລຸນາປ້ອນລະຫັດປຶ້ມທີ່ຕ້ອງການລາຍງານແລ້ວ\nລອງໃຫມ່ອີກຄັ້ງ.",
                                JFXDialog.DialogTransition.CENTER, buttons, false);
                        dialog.showDialog();
                    }
                } else if (rdbReportByBarcode.isSelected()) {
                    if (!txtBookId.getText().equals("")) {

                    } else {
                        dialog = new DialogMessage(stackPane, "ຄຳເຕືອນ",
                                "ກະລຸນາປ້ອນລະຫັດ Barcode ປຶ້ມທີ່ຕ້ອງການລາຍງານແລ້ວ\nລອງໃຫມ່ອີກຄັ້ງ.",
                                JFXDialog.DialogTransition.CENTER, buttons, false);
                        dialog.showDialog();
                    }
                } else {
                    InputStream logos = this.getClass().getResourceAsStream("bin/Logo_FNS.png");
                    report = new CreateReport();
                    map = new HashMap<String, Object>();
                    map.put("logos", logos);
                    report.showReport(map, "reportAllBooks.jrxml", "Report All Book Error");
                }
            }

        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ToggleGroup group = new ToggleGroup();
        rdbReportAll.setToggleGroup(group);
        rdbReportByBookId.setToggleGroup(group);
        rdbReportByBarcode.setToggleGroup(group);
        rdbReportAll.setSelected(true);
        txtBookId.setDisable(true);
        txtBarcode.setDisable(true);
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
