package com.mycompany.library_project.Controller;

import com.jfoenix.controls.*;

import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.*;
import javafx.stage.*;

import com.mycompany.library_project.Model.*;
import com.mycompany.library_project.MyConnection;

import javax.swing.*;

import java.io.File;
import java.net.URL;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

public class RegisterController implements Initializable {

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private Connection con = MyConnection.getConnect();
    private MemberModel memberModel = null;
    private DepartmentModel depertment = new DepartmentModel();
    private ObservableList<String> items = null;
    private ObservableList<String> years = FXCollections.observableArrayList("ປີ 1", "ປີ 1 ຕໍ່ເນື່ອງ", "ປີ 2",
            "ປີ 2 ຕໍ່ເນື່ອງ", "ປີ 3", "ປີ 4", "");
    private FileChooser chooser = null;
    private Image img = null;

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
    private JFXComboBox cmbYears;

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
    private ImageView imgPic;

    private void fillDep() {
        depertment = new DepartmentModel();
        try {
            ResultSet rs = depertment.findAll();
            items = FXCollections.observableArrayList();
            while (rs.next()) {
                items.add(rs.getString(2));
                System.out.println("==========> " + rs.getString(2));
            }
            cmbDept.setItems(items);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void setImage(ActionEvent event) {
        try {
            final Stage stage = new Stage();
            stage.setTitle("Choose image");
            chooser = new FileChooser();
            File file = chooser.showOpenDialog(stage);
            if (file != null) {
                img = new Image(file.toURI().toASCIIString());
                imgPic.setImage(img);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void Save(ActionEvent event) {
        try {
            // JOptionPane.showMessageDialog(null,"Date: "+birtDate.getValue());
            memberModel = new MemberModel(txtId.getText(), txtFName.getText(), txtLName.getText(), rdbMale.getText(),
                    txtTel.getText(), txtVill.getText(), txtDist.getText(), txtProv.getText(),
                    Date.valueOf(dateFormat.format("05/08/2000")), "Computer",
                    Date.valueOf(dateFormat.format("05/08/2000")),
                    Date.valueOf(dateFormat.format("05/08/2000")),
                    Date.valueOf(dateFormat.format("05/08/2000")), "Emty");
            if (memberModel.saveData() == 1) {
                JOptionPane.showMessageDialog(null, "Save Completed!!!");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Todo: Groud RadioButton
        ToggleGroup group = new ToggleGroup();
        rdbMale.setToggleGroup(group);
        rdbMale.setSelected(true);
        rdbFemale.setToggleGroup(group);

        cmbYears.setItems(years);
        fillDep();
    }
}
