package com.mycompany.library_project.Controller;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import com.jfoenix.controls.*;
import com.mycompany.library_project.ControllerDAOModel.*;
import com.mycompany.library_project.Model.DepartmentModel;

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
            if (e.getClickCount() > 0 && tableDepartment.getSelectionModel().getSelectedItem() != null) {
                depertment = tableDepartment.getSelectionModel().getSelectedItem();
                txtId.setText(depertment.getDepId());
                txtName.setText(depertment.getDepName());
            }
        });
    }

    private void initEvents() {
        btSave.setOnAction(event -> {
            try {
                depertment = new DepartmentModel(txtId.getText(), txtName.getText());
                if (depertment.saveData() > 0) {
                    showData();
                    ClearText();
                    alertMessage.showCompletedMessage(stackPane, "Save", "Save data successfully.", 3,
                            Pos.BOTTOM_RIGHT);
                } else {
                    alertMessage.showWarningMessage(stackPane, "Save", "Can not save data.", 4, Pos.BOTTOM_RIGHT);
                }
            } catch (Exception ex) {
                alertMessage.showErrorMessage(stackPane, "Save", "Error: " + ex.getMessage(), 4, Pos.BOTTOM_RIGHT);
            }
        });

        btEdit.setOnAction(event -> {
            try {
                depertment = new DepartmentModel(txtId.getText(), txtName.getText());
                if (depertment.updateData() > 0) {
                    showData();
                    ClearText();
                    alertMessage.showCompletedMessage(stackPane, "Edit", "Edit data successfully.", 4,
                            Pos.BOTTOM_RIGHT);
                } else {
                    alertMessage.showWarningMessage(stackPane, "Edit", "Can not edit data.", 4, Pos.BOTTOM_RIGHT);
                }
            } catch (Exception e) {
                alertMessage.showErrorMessage(stackPane, "Edit", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
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
        delete.setStyle("-fx-background-radius: 2em; -fx-background-color:#101D3D; -fx-text-fill:#FFF;");
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
        btyes.setStyle(
                "-fx-border-radius: 0.5em; -fx-border-color: derive(#060621, 95%); -fx-background-radius: 0.5em;");
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
            }
        });
        return btyes;
    }

    private JFXButton buttonNo() {
        JFXButton btno = new JFXButton("  ບໍ່  ");
        btno.setStyle(
                "-fx-border-radius: 0.5em; -fx-border-color: derive(#060621, 95%); -fx-background-radius: 0.5em;");
        btno.setOnAction(e -> {
            dialog.closeDialog();
        });
        return btno;
    }

    private JFXButton buttonCancel() {
        JFXButton btcancel = new JFXButton("ຍົກເລີກ");
        btcancel.setStyle(
                "-fx-border-radius: 0.5em; -fx-border-color: derive(#060621, 95%); -fx-background-radius: 0.5em;");
        btcancel.setOnAction(e -> {
            dialog.closeDialog();
        });
        return btcancel;
    }

}
