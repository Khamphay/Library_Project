package com.mycompany.library_project.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.mycompany.library_project.Style;
import com.mycompany.library_project.ControllerDAOModel.*;
import com.mycompany.library_project.Model.CategoryModel;
import com.mycompany.library_project.config.CreateLogFile;

public class BookCategoryController implements Initializable {

    private CategoryModel category = null;
    private ObservableList<CategoryModel> data = null;
    private ResultSet rs = null;
    private DialogMessage dialog = null;
    private AlertMessage alertMessage = new AlertMessage();
    private CreateLogFile logfile = new CreateLogFile();

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

    private void showData() {
        try {
            data = FXCollections.observableArrayList();
            category = new CategoryModel();
            rs = category.findAll();
            while (rs.next()) {
                data.add(new CategoryModel(rs.getString(1), rs.getString(2)));
            }
            tableCategory.setItems(data);
        } catch (Exception e) {
            alertMessage.showErrorMessage(stakePane, "Load data", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
        }
    }

    private void ClearData() {
        txtcatgId.setText("");
        txtcatgName.setText("");
    }

    @FXML
    private void selectTable(MouseEvent clickEvent) {
        if (clickEvent.getClickCount() >= 2 && tableCategory.getSelectionModel().getSelectedItem() != null) {
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
            if (!txtcatgId.getText().equals("") && !txtcatgName.getText().equals("")) {
                category = new CategoryModel(txtcatgId.getText(), txtcatgName.getText());
                if (category.saveData() > 0) {
                    showData();
                    ClearData();
                    alertMessage.showCompletedMessage(stakePane, "Save", "Save data successfully.", 4,
                            Pos.BOTTOM_RIGHT);
                }
            } else {
                alertMessage.showWarningMessage(stakePane, "Save Warning",
                        "Please chack your information and try again.", 4, Pos.BOTTOM_RIGHT);
            }

        } catch (Exception e) {
            alertMessage.showErrorMessage(stakePane, "Save", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
            logfile.createLogFile("ມີບັນຫາໃນການບັນທືກຂໍ້ມູນໝວດປຶ້ມ", e);
        }
    }

    @FXML
    private void Update(ActionEvent event) {
        try {
            if (!txtcatgId.getText().equals("") && !txtcatgName.getText().equals("")) {
                category = new CategoryModel(txtcatgId.getText(), txtcatgName.getText());
                if (category.updateData() > 0) {
                    showData();
                    ClearData();
                    alertMessage.showCompletedMessage(stakePane, "Edit", "Edit data successfully.", 4,
                            Pos.BOTTOM_RIGHT);
                }
            } else {
                alertMessage.showWarningMessage(stakePane, "Edit Warning",
                        "Please chack your information and try again.", 4, Pos.BOTTOM_RIGHT);
            }
        } catch (Exception e) {
            alertMessage.showErrorMessage(stakePane, "Edit", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
            logfile.createLogFile("ມີບັນຫາໃນການແກ້ໄຂຂໍ້ມູນໝວດປຶ້ມ", e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colCatgId.setCellValueFactory(new PropertyValueFactory<>("catgId"));
        colCatgName.setCellValueFactory(new PropertyValueFactory<>("catgName"));
        addButtonToTable();
        showData();
    }

    private void addButtonToTable() {
        TableColumn<CategoryModel, Void> colAtion = new TableColumn<>("Action");
        Callback<TableColumn<CategoryModel, Void>, TableCell<CategoryModel, Void>> cellFactory = new Callback<TableColumn<CategoryModel, Void>, TableCell<CategoryModel, Void>>() {

            @Override
            public TableCell<CategoryModel, Void> call(TableColumn<CategoryModel, Void> param) {
                final TableCell<CategoryModel, Void> cell = new TableCell<CategoryModel, Void>() {
                    final JFXButton delete = new JFXButton("ລົບ");

                    {
                        final ImageView imgView = new ImageView();
                        imgView.setImage(new Image("/com/mycompany/library_project/Icon/bin.png"));
                        imgView.setFitWidth(20);
                        imgView.setFitHeight(20);
                        delete.setGraphic(imgView);
                        delete.setStyle(Style.buttonStyle);
                        delete.setOnAction(e -> {
                            JFXButton[] buttons = { buttonYes(tableCategory.getItems().get(getIndex()).getCatgId()),
                                    buttonNo(), buttonCancel() };
                            dialog = new DialogMessage(stakePane, "ຄຳເຕືອນ", "ຕ້ອງການລົບຂໍ້ມູນອອກບໍ?",
                                    JFXDialog.DialogTransition.CENTER, buttons, false);
                            dialog.showDialog();
                        });
                    }

                };
                return cell;
            }

        };
        colAtion.setCellFactory(cellFactory);
        tableCategory.getColumns().add(colAtion);

    }

    private JFXButton buttonYes(String catgid) {
        JFXButton btyes = new JFXButton("ຕົກລົງ");
        btyes.setStyle(Style.buttonDialogStyle);
        btyes.setOnAction(e -> {
            // Todo: Delete Data
            try {
                category = new CategoryModel();
                if (category.deleteData(catgid) > 0) {
                    showData();
                    ClearData();
                    dialog.closeDialog();
                    alertMessage.showCompletedMessage(stakePane, "Delete", "Delete data successfully.", 4,
                            Pos.BOTTOM_RIGHT);
                }
            } catch (SQLException ex) {
                alertMessage.showErrorMessage(stakePane, "Delete", "Error: " + ex.getMessage(), 4, Pos.BOTTOM_RIGHT);
                logfile.createLogFile("ມີບັນຫາໃນການລົບຂໍ້ມູນໝວດປຶ້ມ", ex);
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

    // private JFXButton buttonOK() {
    // JFXButton btok = new JFXButton("OK");
    // btok.setStyle(Style.buttonDialogStyle);
    // btok.setOnAction(e -> {
    // dialog.closeDialog();
    // });
    // return btok;
    // }

}
