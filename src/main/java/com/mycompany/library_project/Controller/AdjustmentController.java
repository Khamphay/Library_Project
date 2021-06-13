package com.mycompany.library_project.Controller;

import java.net.URL;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.mycompany.library_project.Style;
import com.mycompany.library_project.ControllerDAOModel.DialogMessage;
import com.mycompany.library_project.Report.CreateReport;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.StackPane;

public class AdjustmentController implements Initializable {

    private CreateReport report = null;
    private Map<String, Object> map = null;
    private DialogMessage dialog = null;
    private JFXButton[] buttons = { buttonOK() };

    @FXML
    private StackPane stackPane;

    @FXML
    private JFXButton btReport;

    @FXML
    private DatePicker dateSatrt, dateEnd;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btReport.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                if (dateSatrt.getValue() != null && dateEnd.getValue() != null) {
                    report = new CreateReport();
                    map = new HashMap<String, Object>();
                    map.put("dateStart", Date.valueOf(dateSatrt.getValue()));
                    map.put("dateEnd", Date.valueOf(dateEnd.getValue()));
                    report.showReport(map, "reportAjustment.jrxml", "Report Adjustment Error");
                } else {
                    dialog = new DialogMessage(stackPane, "ຄຳເຕືອນ",
                            "ກະລຸນາປ້ອນລະຫັດ Barcode ປຶ້ມທີ່ຕ້ອງການລາຍງານແລ້ວ\nລອງໃຫມ່ອີກຄັ້ງ.",
                            JFXDialog.DialogTransition.CENTER, buttons, false);
                    dialog.showDialog();
                }

            }

        });
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
