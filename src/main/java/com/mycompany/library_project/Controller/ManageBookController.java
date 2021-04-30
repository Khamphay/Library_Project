package com.mycompany.library_project.Controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.mycompany.library_project.App;
import com.mycompany.library_project.ControllerDAOModel.AlertMessage;
import com.mycompany.library_project.config.*;

import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

public class ManageBookController implements Initializable {

    private Parent subForm = null;
    private AlertMessage alertMessage = new AlertMessage();
    public static BorderPane mainBorder = null;

    @FXML
    private Text txtType, txtCategory, txtBook, txtBookLost, txtTableLog;

    @FXML
    private BorderPane bpManageBook;

    @FXML
    private ScrollPane scrollMenu;

    private void show_subForm(String form) {
        try {
            subForm = null;
            subForm = FXMLLoader.load(App.class.getResource(form));
            bpManageBook.setCenter(subForm);
        } catch (Exception e) {
            alertMessage.showErrorMessage(bpManageBook, "Open Form", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
            CreateLogFile config = new CreateLogFile();
            config.createLogFile("ການເປີດຟອມຈັດການຂໍ້ມູນປຶ້ມມີບັນຫາ: " + form, e);
        }
    }

    @FXML
    private void btBookType_Click(ActionEvent event) {
        show_subForm("frmbookType.fxml");
    }

    @FXML
    private void btBookCategory_Click(ActionEvent event) {
        show_subForm("frmBookCategory.fxml");
    }

    @FXML
    private void btBookTableLog_Click(ActionEvent event) {
        show_subForm("frmTableLogs.fxml");
    }

    @FXML
    private void btBooks_Click(ActionEvent event) {
        show_subForm("frmBooks.fxml");
    }

    @FXML
    private void btBookLost_Click(ActionEvent event) {
        // show_subForm("frmBooks.fxml");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        txtType.setText(HomeController.summaryValue[0] + " ປະເພດ");
        txtCategory.setText(HomeController.summaryValue[1] + " ຫວດໝູ່");
        txtBook.setText(
                "ຈຳນວນ " + ((HomeController.summaryValue[2] != null) ? HomeController.summaryValue[2] : "0") + " ຫົວ");
        txtBookLost.setText(
                "ຈຳນວນ " + ((HomeController.summaryValue[3] != null) ? HomeController.summaryValue[3] : "0") + " ຫົວ");
        txtTableLog.setText("ຈຳນວນ " + HomeController.summaryValue[4] + " ຕູ້ ແລະ "
                + ((HomeController.summaryValue[5] != null) ? HomeController.summaryValue[5] : "0") + " ລ໋ອກຕູ້");
    }

}
