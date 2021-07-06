package com.mycompany.library_project.Controller;

import java.net.URL;
import java.sql.Date;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.mycompany.library_project.ControllerDAOModel.*;
import com.mycompany.library_project.Model.AdjustmentModel;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.*;
import javafx.fxml.*;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class PayController implements Initializable {

    private AlertMessage alertMessage = new AlertMessage();
    private DialogMessage dialog = new DialogMessage();
    private AdjustmentModel adjustmentModel = null;
    private DecimalFormat dcFormat = new DecimalFormat("#,##0.00 ກີບ");
    private SendBookController sendBookController = null;
    private Stage stage = null;
    String rent_id = null;
    int qtyOutOfDate = 0;
    double pricePay = 0.0;
    double x, y;

    @FXML
    private AnchorPane acHeaderPane;

    @FXML
    private TextField txtCostAll, txtRc, txtWd;

    @FXML
    private JFXButton btComfirme, btClose;

    public void initSendConstutor(SendBookController sendBookController, Stage stage, String rent_id, int qtyOutOfDate,
            double pricePay) {

        this.sendBookController = sendBookController;
        this.stage = stage;
        this.rent_id = rent_id;
        this.pricePay = pricePay;

        acHeaderPane.setOnMousePressed(mouseEvent -> {
            x = mouseEvent.getSceneX();
            y = mouseEvent.getSceneY();
        });

        // TODO: Set for move form
        acHeaderPane.setOnMouseDragged(mouseEvent -> {
            stage.setX(mouseEvent.getScreenX() - x);
            stage.setY(mouseEvent.getScreenY() - y);
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btComfirme.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                try {
                    sendBookController.sendBook();

                    adjustmentModel = new AdjustmentModel(rent_id, qtyOutOfDate, pricePay,
                            Date.valueOf(LocalDate.now()), "ຢືມປຶ້ມກາຍກຳນົດ");
                    if (adjustmentModel.saveData() > 0) {
                        alertMessage.showCompletedMessage("Saved", "Send Book successfully", 4, Pos.BOTTOM_RIGHT);
                        sendBookController.tableSendBooks.getItems().clear();
                        sendBookController.refreshRentOutOfDate();
                        stage.close();
                    } else
                        alertMessage.showWarningMessage("Warning",
                                "Can not save, Please chack your information and try again", 4, Pos.TOP_CENTER);
                } catch (Exception e) {
                    dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການບັນທືກຂໍ້ມູນການປັບໃຫມ", e);
                }
            }

        });

        btClose.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                stage.close();
            }

        });

        txtCostAll.setText(dcFormat.format(pricePay));

        txtRc.textProperty().addListener(new ChangeListener<String>() {
            // Todo: set properties type only numeric
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    txtRc.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }

        });

        txtRc.setOnKeyTyped(e -> {
            if (!txtRc.getText().equals("")) {
                txtWd.setText(dcFormat.format(Double.parseDouble(txtRc.getText()) - pricePay));
            }
        });
    }
}
