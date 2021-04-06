package com.mycompany.library_project.Controller;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.mycompany.library_project.ControllerDAOModel.DialogMessage;
import com.mycompany.library_project.Model.DepartmentModel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class DepartmentController implements Initializable {

    private DepartmentModel depertment = null;
    private ResultSet rs = null;
    private ObservableList<DepartmentModel> data = null;
    private DialogMessage dialog = null;

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
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        btEdit.setOnAction(event -> {
            try {
                depertment = new DepartmentModel(txtId.getText(), txtName.getText());
                if (depertment.updateData() > 0) {
                    showData();
                    ClearText();
                }
            } catch (Exception e) {
                e.printStackTrace();
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
        btyes.setOnAction(event -> {
            // Todo: Delete Data
            try {
                depertment = new DepartmentModel();
                if (depertment.deleteData(depid) > 0) {
                    showData();
                    ClearText();
                    dialog.closeDialog();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return btyes;
    }

    private JFXButton buttonNo() {
        JFXButton btno = new JFXButton("  ບໍ່  ");
        btno.setOnAction(e -> {
            dialog.closeDialog();
        });
        return btno;
    }

    private JFXButton buttonCancel() {
        JFXButton btcancel = new JFXButton("ຍົກເລີກ");
        btcancel.setOnAction(e -> {
            dialog.closeDialog();
        });
        return btcancel;
    }

}
