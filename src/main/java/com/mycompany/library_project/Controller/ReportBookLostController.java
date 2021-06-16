package com.mycompany.library_project.Controller;

import java.net.URL;
import java.nio.file.Paths;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog.DialogTransition;
import com.mycompany.library_project.Style;
import com.mycompany.library_project.ControllerDAOModel.*;
import com.mycompany.library_project.Report.CreateReport;

import org.controlsfx.control.MaskerPane;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.*;
import javafx.geometry.Pos;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.StackPane;

public class ReportBookLostController implements Initializable {

    private CreateReport report = null;
    private Map<String, Object> map = null;
    private DialogMessage dialog = null;
    private AlertMessage alertMessage = new AlertMessage();
    private MaskerPane masker = new MaskerPane();
    private JFXButton[] buttons = { buttonOK() };

    @FXML
    private StackPane stackPane;

    @FXML
    private JFXButton btReport;

    @FXML
    private DatePicker dateSatrt, dateEnd;

    private void initEvents() {
        btReport.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                if (dateSatrt.getValue() == null && dateEnd.getValue() == null) {
                    dialog = new DialogMessage(stackPane, "ຄຳເຕືອນ",
                            "ກະລຸນາເລືອກວັນທີທີ່ຕ້ອງການລາຍງານ ລອງໃຫມ່ອີກຄັ້ງ.", DialogTransition.CENTER, buttons,
                            false);
                    dialog.showDialog();
                    return;
                }

                Task<Void> task = new Task<Void>() {

                    @Override
                    protected Void call() throws Exception {
                        masker.setVisible(true);
                        masker.setProgressVisible(true);

                        report = new CreateReport();
                        map = new HashMap<String, Object>();
                        map.put("dateStart", Date.valueOf(dateSatrt.getValue()));
                        map.put("dateEnd", Date.valueOf(dateEnd.getValue()));
                        map.put("logo", Paths.get("bin/Logo.png").toAbsolutePath().toString());
                        report.showReport(map, "reportBookLost.jrxml", "Report Book Lost Error");

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
