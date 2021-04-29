package com.mycompany.library_project.Controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.mycompany.library_project.App;
import com.mycompany.library_project.ControllerDAOModel.AlertMessage;

import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

public class ManagePersonalCotroller implements Initializable {

    private Parent subForm = null;
    private AlertMessage alertMessage = new AlertMessage();

    @FXML
    private Text txtMember, txtEmployee, txtAuthor, txtDep;

    @FXML
    private BorderPane bpManagePerson;

    @FXML
    private ScrollPane scrollMenu;
    
    private void show_subForm(String form) {
        try {
            subForm = null;
            subForm = FXMLLoader.load(App.class.getResource(form));
            bpManagePerson.setCenter(subForm);
        } catch (Exception e) {
            alertMessage.showErrorMessage(bpManagePerson, "Open Form", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
        }
    }

    @FXML
    private void btEmployee_Click(ActionEvent event) {
        show_subForm("frmEmployee.fxml");
    }

    @FXML
    private void btMember_Click(ActionEvent event) {
        show_subForm("frmMember.fxml");
    }

    @FXML
    private void btAncthor_Click(ActionEvent event) {
        show_subForm("frmAuthor.fxml");
    }

    @FXML
    private void btDepartment_Click(ActionEvent event) {
        show_subForm("frmDepartment.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resources) {
        txtMember.setText(HomeController.summaryValue[6] + " ຄົນ");
        txtEmployee.setText(HomeController.summaryValue[7] + " ຄົນ");
        txtAuthor.setText(HomeController.summaryValue[8] + " ຄົນ");
        txtDep.setText(HomeController.summaryValue[9] + " ພາກວິຊາ");
    }

}
