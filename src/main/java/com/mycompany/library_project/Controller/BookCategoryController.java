package com.mycompany.library_project.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.mycompany.library_project.ControllerDAOModel.DialogMessage;
import com.mycompany.library_project.Model.CategoryModel;

public class BookCategoryController implements Initializable {

    private CategoryModel category = null;
    private ObservableList<CategoryModel> data = null;
    private ResultSet rs = null;
    private DialogMessage dialog = null;

    @FXML
    private StackPane stakePane;

    @FXML
    private TextField txtcatgId, txtcatgName;

    @FXML
    private JFXButton btSave, btEdite, btCancel;

    @FXML
    private TableView<CategoryModel> tableCategory;

    @FXML
    private TableColumn<CategoryModel, String> colCatgId, colCatgName;
    @FXML
    private TableColumn<CategoryModel, JFXButton> colAction;

    private void showData() {
        try {
            data = FXCollections.observableArrayList();
            category = new CategoryModel();
            rs = category.findAll();
            while (rs.next()) {
                data.add(new CategoryModel(rs.getString(1), rs.getString(2), btDelete(rs.getString(1))));
            }
            tableCategory.setItems(data);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    private void ClearData() {
        txtcatgId.setText("");
        txtcatgName.setText("");
    }

    @FXML
    private void selectTable(MouseEvent clickEvent) {
        if (clickEvent.getClickCount() > 0 && tableCategory.getSelectionModel().getSelectedItem() != null) {
            CategoryModel selectCatg = tableCategory.getSelectionModel().getSelectedItem();
            txtcatgId.setText(selectCatg.getCatgId());
            txtcatgName.setText(selectCatg.getCatgName());
        }
    }

    @FXML
    private void Clear(ActionEvent event) {
        ClearData();
    }

    @FXML
    private void Save(ActionEvent event) {
        try {
            category = new CategoryModel(txtcatgId.getText(), txtcatgName.getText());
            if (category.saveData() > 0) {
                showData();
                ClearData();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    @FXML
    private void Update(ActionEvent event) {
        try {
            category = new CategoryModel(txtcatgId.getText(), txtcatgName.getText());
            if (category.updateData() > 0) {
                showData();
                ClearData();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colCatgId.setCellValueFactory(new PropertyValueFactory<>("catgId"));
        colCatgName.setCellValueFactory(new PropertyValueFactory<>("catgName"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("action"));
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
            dialog = new DialogMessage(stakePane, "ຄຳເຕືອນ", "ຕ້ອງການລົບຂໍ້ມູນອອກບໍ?",
                    JFXDialog.DialogTransition.CENTER, buttons, false);
            dialog.showDialog();
        });
        return delete;
    }

    private JFXButton buttonYes(String catgid) {
        JFXButton btyes = new JFXButton("ຕົກລົງ");
        btyes.setOnAction(e -> {
            // Todo: Delete Data
            try {
                category = new CategoryModel();
                if (category.deleteData(catgid) > 0) {
                    showData();
                    ClearData();
                    dialog.closeDialog();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
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

    private JFXButton buttonOK() {
        JFXButton btok = new JFXButton("OK");
        btok.setOnAction(e -> {
            dialog.closeDialog();
        });
        return btok;
    }

}
