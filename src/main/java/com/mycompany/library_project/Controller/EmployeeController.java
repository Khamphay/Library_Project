package com.mycompany.library_project.Controller;

import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.jfoenix.controls.*;
import com.mycompany.library_project.Style;
import com.mycompany.library_project.ControllerDAOModel.*;
import com.mycompany.library_project.Model.EmployeeModel;

import javafx.beans.value.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

public class EmployeeController implements Initializable {

    private EmployeeModel employee = null;
    private ResultSet rs = null;
    private String gender = "";
    private AlertMessage alertMessage = new AlertMessage();
    private DialogMessage dialog = null;
    private ObservableList<EmployeeModel> data = null;
    private ArrayList<EmployeeModel> users = null;

    @FXML
    private StackPane stackPane;

    @FXML
    private JFXButton btSave, btEdit, btCancel;

    @FXML
    private TextField txtId, txtFname, txtLname, txtTel, txtEmail;

    @FXML
    private RadioButton rdbMale, rdbFemale;

    @FXML
    private TableView<EmployeeModel> tableEmployee;

    @FXML
    private TableColumn<EmployeeModel, String> colId, colFname, colSname, colGender, colTel, colEmail;

    @FXML
    private TableColumn<EmployeeModel, JFXButton> colAction;

    private void initTable() {
        colId.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        colFname.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colSname.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        colTel.setCellValueFactory(new PropertyValueFactory<>("tel"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("action"));
    }

    private void clearText() {
        txtId.clear();
        txtFname.clear();
        txtLname.clear();
        txtTel.clear();
        txtEmail.clear();
        gender = "";
    }

    private void initEvents() {
        btSave.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                try {
                    if (txtId.getText() != "" && txtFname.getText() != "" && txtLname.getText() != ""
                            && txtTel.getText() != "") {
                        gender = (rdbMale.isSelected() ? rdbMale.getText() : rdbFemale.getText());
                        employee = new EmployeeModel(txtId.getText(), txtFname.getText(), txtLname.getText(), gender,
                                txtTel.getText(), txtEmail.getText());
                        if (employee.saveData() > 0) {
                            showData();
                            clearText();
                            alertMessage.showCompletedMessage(stackPane, "Saved", "Saved data successfully.", 4,
                                    Pos.BOTTOM_RIGHT);
                        } else {
                            alertMessage.showWarningMessage(stackPane, "Saved", "Cannot save data.", 4,
                                    Pos.BOTTOM_RIGHT);
                        }
                    } else {
                        alertMessage.showWarningMessage(stackPane, "Save Warning",
                                "Please chack your information and try again.", 4, Pos.BOTTOM_RIGHT);
                    }
                } catch (Exception e) {
                    alertMessage.showErrorMessage(stackPane, "Save Error", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
                }
            }
        });

        btEdit.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                try {
                    if (txtId.getText() != "" && txtFname.getText() != "" && txtLname.getText() != ""
                            && txtTel.getText() != "") {
                        gender = (rdbMale.isSelected() ? rdbMale.getText() : rdbFemale.getText());
                        employee = new EmployeeModel(txtId.getText(), txtFname.getText(), txtLname.getText(), gender,
                                txtTel.getText(), txtEmail.getText());
                        if (employee.updateData() > 0) {
                            showData();
                            clearText();
                            alertMessage.showCompletedMessage(stackPane, "Edited", "Edit data successfully.", 4,
                                    Pos.BOTTOM_RIGHT);
                        } else {
                            alertMessage.showWarningMessage(stackPane, "Edited", "Can not edit data.", 4,
                                    Pos.BOTTOM_RIGHT);
                        }
                    } else {
                        alertMessage.showWarningMessage(stackPane, "Edit Warning",
                                "Please chack your information and try again.", 4, Pos.BOTTOM_RIGHT);
                    }
                } catch (Exception e) {
                    alertMessage.showErrorMessage(stackPane, "Edit Error", "Error: " + e.getMessage(), 4,
                            Pos.BOTTOM_RIGHT);
                }
            }
        });

        btCancel.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                clearText();
            }

        });

        txtTel.textProperty().addListener(new ChangeListener<String>() {
            // Todo: set properties type only numeric
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    txtTel.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        tableEmployee.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() >= 2 && tableEmployee.getSelectionModel().getSelectedItem() != null) {
                    txtId.setText(tableEmployee.getSelectionModel().getSelectedItem().getEmployeeId());
                    txtFname.setText(tableEmployee.getSelectionModel().getSelectedItem().getFirstName());
                    txtLname.setText(tableEmployee.getSelectionModel().getSelectedItem().getLastName());
                    txtTel.setText(tableEmployee.getSelectionModel().getSelectedItem().getTel());
                    txtEmail.setText(tableEmployee.getSelectionModel().getSelectedItem().getEmail());
                    if (tableEmployee.getSelectionModel().getSelectedItem().getGender().equals("ຊາຍ")) {
                        rdbMale.setSelected(true);
                    } else {
                        rdbFemale.setSelected(true);
                    }
                }
            }

        });
    }

    private void showData() {
        try {
            data = FXCollections.observableArrayList();
            users = new ArrayList<>();
            employee = new EmployeeModel();
            rs = employee.findAll();
            while (rs.next()) {
                data.add(new EmployeeModel(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
                        rs.getString(5), rs.getString(6), buttonDelete(rs.getString(1))));
                users.add(new EmployeeModel(rs.getString(1), rs.getString(7), rs.getString(8)));
            }
            tableEmployee.setItems(data);
        } catch (Exception e) {
            alertMessage.showErrorMessage(stackPane, "Load Data", "Error" + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        ToggleGroup group = new ToggleGroup();
        rdbMale.setToggleGroup(group);
        rdbFemale.setToggleGroup(group);
        rdbMale.setSelected(true);

        initTable();
        showData();
        initEvents();
    }

    private JFXButton buttonDelete(String employee_id) {
        final JFXButton delete = new JFXButton("ລົບ");
        final ImageView imgView = new ImageView();
        imgView.setFitWidth(20);
        imgView.setFitHeight(20);
        imgView.setImage(new Image("/com/mycompany/library_project/Icon/bin.png"));
        delete.setStyle(Style.buttonStyle);
        delete.setId(employee_id);
        delete.setGraphic(imgView);

        delete.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                JFXButton[] buttons = { buttonYes(delete.getId()), buttonNo(), buttonCancel() };
                dialog = new DialogMessage(stackPane, "ຄຳເຕືອນ", "ຕ້ອງການລົບຂໍ້ມູນອອກບໍ?",
                        JFXDialog.DialogTransition.CENTER, buttons, false);
                dialog.showDialog();
            }

        });

        return delete;
    }

    protected JFXButton buttonYes(String id) {
        JFXButton btyes = new JFXButton("ຕົກລົງ");
        btyes.setStyle(Style.buttonDialogStyle);
        btyes.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                employee = new EmployeeModel();
                try {
                    if (employee.deleteData(id) > 0) {
                        showData();
                        alertMessage.showCompletedMessage(stackPane, "Deleted", "Delete data successfully.", 4,
                                Pos.BOTTOM_RIGHT);
                        dialog.closeDialog();
                    } else {
                        alertMessage.showWarningMessage(stackPane, "Deleted", "Can not delete data, Please try again.",
                                4, Pos.BOTTOM_RIGHT);
                    }
                } catch (SQLException e) {
                    alertMessage.showErrorMessage(stackPane, "Deleted", "Error: " + e.getMessage(), 4,
                            Pos.BOTTOM_RIGHT);
                }
            }
        });
        return btyes;
    }

    protected JFXButton buttonNo() {
        JFXButton btno = new JFXButton("  ບໍ່  ");
        btno.setStyle(Style.buttonDialogStyle);
        btno.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                dialog.closeDialog();
            }
        });
        return btno;
    }

    protected JFXButton buttonCancel() {
        JFXButton btcancel = new JFXButton("ຍົກເລີກ");
        btcancel.setStyle(Style.buttonDialogStyle);
        btcancel.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                dialog.closeDialog();
            }

        });
        return btcancel;
    }

}
