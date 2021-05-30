package com.mycompany.library_project.Controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.mycompany.library_project.App;
import com.mycompany.library_project.ControllerDAOModel.AlertMessage;
import com.mycompany.library_project.config.CreateLogFile;

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
    private CreateLogFile logfile = new CreateLogFile();

    @FXML
    private Text txtMember, txtEmployee, txtAuthor, txtDep;

    @FXML
    private BorderPane bpManagePerson;

    @FXML
    private ScrollPane scrollMenu;

    public void showMainMenuPerson() {
        bpManagePerson.setCenter(scrollMenu);
    }

    @FXML
    private void btEmployee_Click(ActionEvent event) {
        try {
            final FXMLLoader loader = new FXMLLoader(App.class.getResource("frmEmployee.fxml"));
            subForm = loader.load();
            EmployeeController employeeController = loader.getController();
            employeeController.initConstructor(this);
            bpManagePerson.setCenter(subForm);
        } catch (Exception e) {
            alertMessage.showErrorMessage(bpManagePerson, "Open Form", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
            logfile.createLogFile("ການເປີດຟອມຈັດການຂໍ້ມູນສ່ວນບຸກຄົນມີບັນຫາ: " + "Form Employee", e);
        }
    }

    @FXML
    private void btMember_Click(ActionEvent event) {
        try {
            final FXMLLoader loader = new FXMLLoader(App.class.getResource("frmMember.fxml"));
            subForm = loader.load();
            MemberController memberController = loader.getController();
            memberController.initConstructor(this);
            bpManagePerson.setCenter(subForm);
        } catch (Exception e) {
            alertMessage.showErrorMessage(bpManagePerson, "Open Form", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
            logfile.createLogFile("ການເປີດຟອມຈັດການຂໍ້ມູນສ່ວນບຸກຄົນມີບັນຫາ: " + "Form Member", e);
        }
    }

    @FXML
    private void btAncthor_Click(ActionEvent event) {
        try {
            final FXMLLoader loader = new FXMLLoader(App.class.getResource("frmAuthor.fxml"));
            subForm = loader.load();
            AuthorController authorController = loader.getController();
            authorController.initConstructor(this);
            bpManagePerson.setCenter(subForm);
        } catch (Exception e) {
            alertMessage.showErrorMessage(bpManagePerson, "Open Form", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
            logfile.createLogFile("ການເປີດຟອມຈັດການຂໍ້ມູນສ່ວນບຸກຄົນມີບັນຫາ: " + "Form Author", e);
        }
    }

    @FXML
    private void btDepartment_Click(ActionEvent event) {
        try {
            final FXMLLoader loader = new FXMLLoader(App.class.getResource("frmDepartment.fxml"));
            subForm = loader.load();
            DepartmentController departmentController = loader.getController();
            departmentController.initConstructor(this);
            bpManagePerson.setCenter(subForm);
        } catch (Exception e) {
            alertMessage.showErrorMessage(bpManagePerson, "Open Form", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
            logfile.createLogFile("ການເປີດຟອມຈັດການຂໍ້ມູນສ່ວນບຸກຄົນມີບັນຫາ: " + "Form Department", e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resources) {
        txtMember.setText(HomeController.summaryValue[6] + " ຄົນ");
        txtEmployee.setText(HomeController.summaryValue[7] + " ຄົນ");
        txtAuthor.setText(HomeController.summaryValue[8] + " ຄົນ");
        txtDep.setText(HomeController.summaryValue[9] + " ພາກວິຊາ");
    }

}
