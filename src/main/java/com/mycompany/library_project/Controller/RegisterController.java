package com.mycompany.library_project.Controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import com.mycompany.library_project.Controller.Model.MemberModel;
import com.mycompany.library_project.MyConnection;

import javax.swing.*;
import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;

public class RegisterController implements Initializable {

    private Connection con = MyConnection.getConnect();
    private MemberModel memberModel = null;

    @FXML
    private JFXTextField txtId;
    @FXML
    private JFXTextField txtFName;
    @FXML
    private JFXTextField txtLName;
    @FXML
    private JFXTextField txtTel;
    @FXML
    private JFXTextField txtVill;
    @FXML
    private JFXTextField txtDist;
    @FXML
    private JFXTextField txtProv;
    @FXML
    private JFXComboBox cmbDept;

    @FXML
    private JFXRadioButton rdbMale;
    @FXML
    private JFXRadioButton rdbFemale;

    @FXML
    private JFXDatePicker birtDate;
    @FXML
    private JFXDatePicker registerDate;
    @FXML
    private JFXDatePicker endDate;
    @FXML
    private JFXDatePicker exitDate;

    @FXML
    private void Save(ActionEvent event) {
        try {
            // JOptionPane.showMessageDialog(null,"Date: "+birtDate.getValue());
            memberModel = new MemberModel(txtId.getText(), txtFName.getText(), txtLName.getText(), rdbMale.getText(),
                    txtTel.getText(), txtVill.getText(), txtDist.getText(), txtProv.getText(), "5/12/2020", "Computer",
                    "05/12/2020", "05/12/2020", "05/12/2020", "Emty");
            if (memberModel.saveData() == 1) {
                JOptionPane.showMessageDialog(null, "Save Completed!!!");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
