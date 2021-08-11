package com.mycompany.library_project.Controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.mycompany.library_project.App;
import com.mycompany.library_project.ControllerDAOModel.DialogMessage;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.*;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class RentSendController implements Initializable {

    private Parent rootRent = null, rootSend = null, rootHistory = null;
    private DialogMessage dialog = new DialogMessage();

    @FXML
    private TabPane tabPane;

    @FXML
    private Tab tabRent, tabSend, tabIinforHistory;

    @FXML
    private JFXButton btClose;

    public void initConstructor(HomeController homeController) {
        btClose.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                homeController.showMainMenuHome();
            }

        });
    }

    private void RentBooks() {
        try {
            if (rootRent == null) {
                final FXMLLoader loader = new FXMLLoader(App.class.getResource("frmRentBooks.fxml"));
                rootRent = loader.load();
                tabRent.setContent(rootRent);
            }
        } catch (Exception e) {
            dialog.showExcectionDialog("Error", null, "ເກິດບັນຫາໃນການເປີດຟອມຢືມປຶ້ມ", e);
        }
    }

    private void initEvents() {
        tabRent.setOnSelectionChanged(event -> {
            RentBooks();
        });

        tabSend.setOnSelectionChanged(event -> {
            try {
                if (rootSend == null) {
                    final FXMLLoader loader = new FXMLLoader(App.class.getResource("frmSendBook.fxml"));
                    rootSend = loader.load();
                    tabSend.setContent(rootSend);
                }
            } catch (Exception e) {
                dialog.showExcectionDialog("Error", null, "ເກິດບັນຫາໃນການເປີດຟອມສົ່ງປຶ້ມ", e);
            }
        });
        tabIinforHistory.setOnSelectionChanged(event -> {
            try {
                // if (rootHistory == null) {
                final FXMLLoader loader = new FXMLLoader(App.class.getResource("frmShowRent_SendBook.fxml"));
                rootHistory = loader.load();
                tabIinforHistory.setContent(rootHistory);
                // }
            } catch (Exception e) {
                dialog.showExcectionDialog("Error", null, "ເກິດບັນຫາໃນການເປີດຟອມຂໍ້ມູນຢືມ-ສົ່ງປຶ້ມ", e);
            }
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initEvents();
        RentBooks();
    }

}
