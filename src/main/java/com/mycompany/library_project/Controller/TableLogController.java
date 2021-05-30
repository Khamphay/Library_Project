package com.mycompany.library_project.Controller;

import javafx.application.Platform;
import javafx.beans.value.*;
import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.image.*;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.mycompany.library_project.Style;
import com.mycompany.library_project.ControllerDAOModel.*;
import com.mycompany.library_project.Model.TableLogModel;
import com.mycompany.library_project.config.CreateLogFile;

public class TableLogController implements Initializable {

    private ManageBookController manageBookController = null;
    private TableLogModel tableLogModel = null;
    private ResultSet rs = null;
    private TreeItem<TableLogModel> subItem, root, node;
    // private ValidationSupport validRules;
    private ObservableList<TreeItem<TableLogModel>> data = FXCollections.observableArrayList();
    private AlertMessage alertMessage = new AlertMessage();
    private DialogMessage dialog = null;
    private CreateLogFile logfile = new CreateLogFile();

    public void initConstructor(ManageBookController manageBookController) {
        this.manageBookController = manageBookController;
    }

    @FXML
    private StackPane stackPane;

    @FXML
    private TextField txtId, txtQty;

    @FXML
    private TextArea txtLog;

    @FXML
    private TreeTableView<TableLogModel> tableLog;

    @FXML
    private TreeTableColumn<TableLogModel, String> colid, colqty;

    @FXML
    private TreeTableColumn<TableLogModel, JFXButton> colaction;

    private void initTable() {
        colid.setCellValueFactory(new TreeItemPropertyValueFactory<>("tableId"));
        colqty.setCellValueFactory(new TreeItemPropertyValueFactory<>("logQty"));
        colaction.setCellValueFactory(new TreeItemPropertyValueFactory<>("action"));
    }

    private void showData() {
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                try {
                    tableLogModel = new TableLogModel();
                    rs = tableLogModel.findAll();
                    node = new TreeItem<>();
                    while (rs.next()) {

                        root = new TreeItem<>(new TableLogModel(rs.getString(1), Integer.parseInt(rs.getString(2)),
                                getRootAction(rs.getString(1))));
                        final ResultSet sublog = tableLogModel.findById(rs.getString(1));

                        while (sublog.next()) {
                            subItem = new TreeItem<>(
                                    new TableLogModel(sublog.getString(2), 1, getSubRootAction(sublog.getString(2))));
                            root.getChildren().add(subItem);
                        }

                        node.getChildren().add(root);

                    }
                    tableLog.setShowRoot(false);
                    tableLog.setRoot(node);

                } catch (Exception e) {
                    alertMessage.showErrorMessage(stackPane, "Load Data", "Error: " + e.getMessage(), 4,
                            Pos.BOTTOM_RIGHT);
                }
            }
        });
    }

    private void initEvents() {
        tableLog.setOnMouseClicked(e -> {
            if (e.getClickCount() >= 2 && tableLog.getSelectionModel().getSelectedItem() != null) {
                data = tableLog.getSelectionModel().getSelectedItems();
                txtId.setText(data.get(0).getValue().getTableId());
                txtQty.setText("" + data.get(0).getValue().getLogQty());
            }
        });

        txtQty.textProperty().addListener(new ChangeListener<String>() {
            // Todo: set properties type only numeric
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    txtQty.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }

        });

        txtId.setOnKeyTyped(e -> {
            generatedLog();
        });

        txtQty.setOnKeyTyped(e -> {
            generatedLog();
        });
    }

    private void generatedLog() {
        if (!txtId.getText().equals("") && !txtQty.getText().equals("")) {
            txtLog.clear();
            for (int i = 1; i <= Integer.parseInt(txtQty.getText()); i++) {
                txtLog.appendText(txtId.getText() + "000" + i + "\n");
            }
        }
    }

    private void clearText() {
        txtId.clear();
        txtLog.clear();
        txtQty.clear();
    }

    @FXML
    private void closeForm() {
        manageBookController.showMainMenuBooks();
    }

    @FXML
    private void btCancel(ActionEvent event) {
        clearText();
    }

    @FXML
    private void tbSaveData(ActionEvent event) {
        String result = null;
        try {
            if (!txtId.getText().equals("") && !txtId.getText().equals("") && !txtQty.getText().equals("")) {
                tableLogModel = new TableLogModel();
                if (tableLogModel.saveTable(txtId.getText(), Integer.parseInt(txtQty.getText())) > 0) {
                    String line = txtLog.getText();
                    String[] lineCount = line.split("\n");
                    for (int i = 0; i < lineCount.length; i++) {
                        try {
                            tableLogModel = new TableLogModel(txtId.getText(), lineCount[i]);
                            result = (tableLogModel.saveData() > 0) ? "Save Completed" : null; // Todo: if...else
                        } catch (Exception e) {
                            tableLogModel.deleteTable(txtId.getText());
                            logfile.createLogFile("ມີບັນຫາໃນການບັນທືກຂໍ້ມູນລ໋ອກຕູ້", e);
                            return;
                        }
                    }
                } else {
                    alertMessage.showWarningMessage(stackPane, "Saved", "Warning: Can not save data.", 4,
                            Pos.BOTTOM_RIGHT);
                }

                if (result != null) {
                    showData();
                    clearText();
                    alertMessage.showCompletedMessage("Saved", "Save data successfully.", 4, Pos.BOTTOM_RIGHT);
                }
            } else {
                alertMessage.showWarningMessage(stackPane, "Save Warning",
                        "Please chack your information and try again.", 4, Pos.BOTTOM_RIGHT);
            }

        } catch (Exception e) {
            alertMessage.showErrorMessage(stackPane, "Save", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
            logfile.createLogFile("ມີບັນຫາໃນການບັນທືກຂໍ້ມູນຕູ້", e);
        }
    }

    @FXML
    private void btUpdate(ActionEvent event) {
        try {
            if (!txtId.getText().equals("") && !txtId.getText().equals("") && !txtQty.getText().equals("")) {
            tableLogModel = new TableLogModel(txtId.getText(), Integer.parseInt(txtQty.getText()));
            if (tableLogModel.updateData() > 0) {
                alertMessage.showCompletedMessage(stackPane, "Edited", "Edit data successfully.", 4, Pos.BOTTOM_RIGHT);
                showData();
            } else {
                alertMessage.showWarningMessage(stackPane, "Edit Warning", "Can not edit data", 4, Pos.BOTTOM_RIGHT);
            }
        } else {
            alertMessage.showWarningMessage(stackPane, "Edit Warning", "Please chack your information and try again.",
                    4, Pos.BOTTOM_RIGHT);
        }
        } catch (Exception e) {
            alertMessage.showErrorMessage(stackPane, "Edit Error", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
            logfile.createLogFile("ມີບັນຫາໃນການແກ້ໄຂຂໍ້ມູນລ໋ອກຕູ້", e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initTable();
        initEvents();
        showData();
    }

    public JFXButton getRootAction(String id) {
        JFXButton actionRoot = new JFXButton("ລົບ");
        final Image img = new Image("com/mycompany/library_project/Icon/delete_table.png");
        ImageView imgView = new ImageView();
        try {
            imgView.setImage(img);
            imgView.setFitHeight(20);
            imgView.setFitWidth(20);
            actionRoot.setGraphic(imgView);
            actionRoot.setId(id);
            actionRoot.setStyle(Style.buttonStyle);
            actionRoot.setOnAction(e -> {
                JFXButton[] buttons = { buttonYesTable(actionRoot.getId()), buttonNo(), buttonCancel() };
                dialog = new DialogMessage(stackPane, "ຄຳເຕືອນ", "ຕ້ອງການລົບຂໍ້ມູນອອກບໍ?",
                        JFXDialog.DialogTransition.CENTER, buttons, false);
                dialog.showDialog();

            });
        } catch (Exception e) {

        }
        return actionRoot;
    }

    public JFXButton getSubRootAction(String id) {
        JFXButton actionSubRoot = new JFXButton("ລົບ");
        final Image img = new Image("com/mycompany/library_project/Icon/delete_subroot.png");
        ImageView imgView = new ImageView();
        try {
            imgView.setImage(img);
            imgView.setFitHeight(20);
            imgView.setFitWidth(20);
            actionSubRoot.setId(id);
            actionSubRoot.setGraphic(imgView);
            actionSubRoot.setStyle(Style.buttonStyle);
            actionSubRoot.setOnAction(e -> {

                JFXButton[] buttons = { buttonYesTableLog(actionSubRoot.getId()), buttonNo(), buttonCancel() };
                dialog = new DialogMessage(stackPane, "ຄຳເຕືອນ", "ຕ້ອງການລົບຂໍ້ມູນອອກບໍ?",
                        JFXDialog.DialogTransition.CENTER, buttons, false);
                dialog.showDialog();

            });
        } catch (Exception e) {
        }
        return actionSubRoot;
    }

    private JFXButton buttonYesTable(String tableid) {
        JFXButton btyes = new JFXButton("ຕົກລົງ");
        btyes.setStyle(Style.buttonDialogStyle);
        btyes.setOnAction(e -> {
            // Todo: Delete Data
            tableLogModel = new TableLogModel();
            try {
                if (tableLogModel.deleteTable(tableid) > 0) {
                    showData();
                    dialog.closeDialog();
                    alertMessage.showCompletedMessage("Delete", "Delete data successfully.", 4, Pos.BOTTOM_RIGHT);
                } else {
                    alertMessage.showWarningMessage("Delete", "Can not delete data.", 4, Pos.BOTTOM_RIGHT);

                }
            } catch (Exception ex) {
                alertMessage.showErrorMessage(stackPane, "Delete", "Error: " + ex.getMessage(), 4, Pos.BOTTOM_RIGHT);
                logfile.createLogFile("ມີບັນຫາໃນການລົບຂໍ້ມູນຕູ້", ex);
            }
        });
        return btyes;
    }

    private JFXButton buttonYesTableLog(String tablelogid) {
        JFXButton btyes = new JFXButton("ຕົກລົງ");
        btyes.setStyle(Style.buttonDialogStyle);
        btyes.setOnAction(e -> {
            // Todo: Delete Data
            tableLogModel = new TableLogModel();
            try {
                
                rs = tableLogModel.findById(tablelogid);
                if (tableLogModel.deleteData(tablelogid) > 0) {
                    if (rs.next()) {
                        if (tableLogModel.updateTableQty(rs.getString(1)) > 0) {
                            showData();
                            alertMessage.showCompletedMessage("Delete", "Delete data successfully.", 4,
                                    Pos.BOTTOM_RIGHT);
                        } else {
                            alertMessage.showWarningMessage("Delete", "Can update table qty.", 4, Pos.BOTTOM_RIGHT);
                        }
                    }
                    dialog.closeDialog();
                } else {
                    alertMessage.showWarningMessage("Delete", "Can not delete data. ", 4, Pos.BOTTOM_RIGHT);
                }
            } catch (SQLException ex) {
                alertMessage.showErrorMessage(stackPane, "Delete", "Error: " + ex.getMessage(), 4, Pos.BOTTOM_RIGHT);
                logfile.createLogFile("ມີບັນຫາໃນການລົບຂໍ້ມູນລ໋ອກຕູ້", ex);
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
