package com.mycompany.library_project.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;

import javax.swing.*;
import java.net.URL;
import java.util.ResourceBundle;

public class RentBookController implements Initializable {

    @FXML
    private DatePicker datePicker;

    @FXML
    private void ShowDate(ActionEvent event){
        JOptionPane.showMessageDialog(null,"Date: "+datePicker.getValue());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
