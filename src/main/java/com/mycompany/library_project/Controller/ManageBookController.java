package com.mycompany.library_project.Controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.mycompany.library_project.App;

import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class ManageBookController implements Initializable {

    private Parent subForm = null;

    @FXML
    private BorderPane bpManageBook;

    @FXML
    private AnchorPane anchorMenu;

    private void show_subForm(String form) {
        try {
            subForm = null;
            subForm = FXMLLoader.load(App.class.getResource(form));
            bpManageBook.setCenter(subForm);
        } catch (Exception e) {
            e.printStackTrace();
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
        // TODO Auto-generated method stub

    }

}
