package com.mycompany.library_project.Controller;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import com.jfoenix.controls.*;
import com.mycompany.library_project.Style;
import com.mycompany.library_project.ControllerDAOModel.*;
import com.mycompany.library_project.Model.DepartmentModel;
import com.mycompany.library_project.config.CreateLogFile;

import javafx.collections.*;
import javafx.fxml.*;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.*;
import javafx.scene.layout.StackPane;

public class DepartmentController implements Initializable {

    private DepartmentModel depertment = null;
    private ResultSet rs = null;
    private ObservableList<DepartmentModel> data = null;
    private DialogMessage dialog = null;
    private AlertMessage alertMessage = new AlertMessage();
    private CreateLogFile logfile = new CreateLogFile();

    @FXML
    private StackPane stackPane;

    @FXML
    private JFXButton btSave, btEdit, btCancel;

    @FXML
    private TextField txtId, txtName;

    @FXML
    private TableView<DepartmentModel> tableDepartment;

    @FXML
    private TableColumn<DepartmentModel, String> colId, colName;

    @FXML
    private TableColumn<DepartmentModel, JFXButton> colAction;

    private void initTable() {
        colId.setCellValueFactory(new PropertyValueFactory<>("depId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("depName"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("action"));
        tableDepartment.setOnMouseClicked(e -> {
            if (e.getClickCount() >=2 && tableDepartment.getSelectionModel().getSelectedItem() != null) {
                depertment = tableDepartment.getSelectionModel().getSelectedItem();
                txtId.setText(depertment.getDepId());
                txtName.setText(depertment.getDepName());
            }
        });
    }

    private void initEvents() {
        btSave.setOnAction(event -> {
            try {
                if (txtId.getText().equals("") && txtName.getText().equals("")) {
                    depertment = new DepartmentModel(txtId.getText(), txtName.getText());
                    if (depertment.saveData() > 0) {
                        showData();
                        ClearText();
                        alertMessage.showCompletedMessage(stackPane, "Saved", "Save data successfully.", 3,
                                Pos.BOTTOM_RIGHT);
                    } else {
                        alertMessage.showWarningMessage(stackPane, "Save Warning", "Can not save data.", 4,
                                Pos.BOTTOM_RIGHT);
                    }
                } else {
                    alertMessage.showWarningMessage(stackPane, "Save Warning",
                            "Please chack your information and try again.", 4, Pos.BOTTOM_RIGHT);
                }
            } catch (Exception ex) {
                alertMessage.showErrorMessage(stackPane, "Save Error", "Error: " + ex.getMessage(), 4,
                        Pos.BOTTOM_RIGHT);
                logfile.createLogFile("ມີບັນຫາໃນການບັນທືກຂໍ້ມູນພາກວິຊາ", ex);
            }
        });

        btEdit.setOnAction(event -> {
            try {
                if (txtId.getText().equals("") && txtName.getText().equals("")) {
                    depertment = new DepartmentModel(txtId.getText(), txtName.getText());
                    if (depertment.updateData() > 0) {
                        showData();
                        ClearText();
                        alertMessage.showCompletedMessage(stackPane, "Edited", "Edit data successfully.", 4,
                                Pos.BOTTOM_RIGHT);
                    } else {
                        alertMessage.showWarningMessage(stackPane, "Edit Warning", "Can not edit data.", 4,
                                Pos.BOTTOM_RIGHT);
                    }
                } else {
                    alertMessage.showWarningMessage(stackPane, "Edit Warning",
                            "Please chack your information and try again.", 4, Pos.BOTTOM_RIGHT);
                }
            } catch (Exception e) {
                alertMessage.showErrorMessage(stackPane, "Edit Error", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
                logfile.createLogFile("ມີບັນຫາໃນການແກ້ໄຂຂໍ້ມູນພາກວິຊາ", e);
            }
        });

        btCancel.setOnAction(event -> {
            ClearText();
        });
    }

    private void ClearText() {
        txtId.clear();
        txtName.clear();
    }

    private void showData() {
        try {
            depertment = new DepartmentModel();
            data = FXCollections.observableArrayList();
            rs = depertment.findAll();
            while (rs.next()) {
                data.add(new DepartmentModel(rs.getString(1), rs.getString(2), btDelete(rs.getString(1))));
            }
            tableDepartment.setItems(data);
        } catch (Exception e) {
            alertMessage.showErrorMessage("ໂຫຼດຂໍ້ມູນ", e.getMessage(), 3, Pos.BOTTOM_RIGHT);
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTable();
        initEvents();
        showData();

    }

    private JFXButton btDelete(String id) {
        JFXButton delete = new JFXButton("ລົບ");
        final Image img = new Image("/com/mycompany/library_project/Icon/bin.png");
        final ImageView imgView = new ImageView();
        imgView.setImage(img);
        imgView.setFitWidth(20);
        imgView.setFitHeight(20);
        delete.setId(id);
        delete.setGraphic(imgView);
        delete.setStyle(Style.buttonStyle);
        delete.setOnAction(e -> {
            JFXButton[] buttons = { buttonYes(delete.getId()), buttonNo(), buttonCancel() };
            dialog = new DialogMessage(stackPane, "ຄຳເຕືອນ", "ຕ້ອງການລົບຂໍ້ມູນອອກບໍ?",
                    JFXDialog.DialogTransition.CENTER, buttons, false);
            dialog.showDialog();
        });
        return delete;
    }

    private JFXButton buttonYes(String depid) {
        JFXButton btyes = new JFXButton("ຕົກລົງ");
        btyes.setStyle(Style.buttonDialogStyle);
        btyes.setOnAction(event -> {
            // Todo: Delete Data
            try {
                depertment = new DepartmentModel();
                if (depertment.deleteData(depid) > 0) {
                    showData();
                    ClearText();
                    dialog.closeDialog();
                    alertMessage.showCompletedMessage(stackPane, "Delete", "Delete data successfully.", 4,
                            Pos.BOTTOM_RIGHT);
                } else {
                    alertMessage.showWarningMessage(stackPane, "Delete", "Can not delete data.", 4, Pos.BOTTOM_RIGHT);
                }
            } catch (Exception e) {
                alertMessage.showErrorMessage(stackPane, "Delete", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
                logfile.createLogFile("ມີບັນຫາໃນການລົບຂໍ້ມູນພາກວິຊາ", e);
            }
        });
        return btyes;
    }

    private JFXButton buttonNo() {
        JFXButton btno = new JFXButton("  ບໍ່  ");
        btno.setStyle(Style.buttonDialogStyle);
        btno.setOnAction(e -> {
            dialog.closeDialog();
        });
        return btno;
    }

    private JFXButton buttonCancel() {
        JFXButton btcancel = new JFXButton("ຍົກເລີກ");
        btcancel.setStyle(Style.buttonDialogStyle);
        btcancel.setOnAction(e -> {
            dialog.closeDialog();
        });
        return btcancel;
    }

}
