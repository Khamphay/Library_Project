package com.mycompany.library_project.Controller;

import javafx.application.Platform;
import javafx.beans.value.*;
import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.*;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.image.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.mycompany.library_project.Style;
import com.mycompany.library_project.ControllerDAOModel.*;
import com.mycompany.library_project.Model.TableLogModel;

import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

public class TableLogController implements Initializable {

    private ValidationSupport validRules = new ValidationSupport();
    private TableLogModel tableLogModel = new TableLogModel();
    private ResultSet rs = null;
    private TreeItem<TableLogModel> subItem, root, node;
    private ObservableList<TreeItem<TableLogModel>> data = FXCollections.observableArrayList();
    private AlertMessage alertMessage = new AlertMessage();
    private DialogMessage dialog = new DialogMessage();
    private AddBookController addBookController = null;
    private ImportController importController = null;
    String maxLog = "";
    int qty = 0;
    double x, y;

    public void initConstructor(ManageBookController manageBookController) {
        btClose.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                manageBookController.showMainMenuBooks();
            }

        });
    }

    public void initConstructor2(AddBookController addBookController, Stage stage) {
        this.addBookController = addBookController;

        acHeaderPane.setOnMousePressed(mouseEvent -> {
            x = mouseEvent.getSceneX();
            y = mouseEvent.getSceneY();
        });
        // TODO: Set for move form
        acHeaderPane.setOnMouseDragged(mouseEvent -> {
            stage.setX(mouseEvent.getScreenX() - x);
            stage.setY(mouseEvent.getScreenY() - y);
        });

        btClose.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                stage.close();
            }

        });
    }

    public void initConstructor3(ImportController importController, Stage stage) {
        this.importController = importController;

        acHeaderPane.setOnMousePressed(mouseEvent -> {
            x = mouseEvent.getSceneX();
            y = mouseEvent.getSceneY();
        });
        // TODO: Set for move form
        acHeaderPane.setOnMouseDragged(mouseEvent -> {
            stage.setX(mouseEvent.getScreenX() - x);
            stage.setY(mouseEvent.getScreenY() - y);
        });

        btClose.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                stage.close();
            }

        });
    }

    @FXML
    private StackPane stackPane;

    @FXML
    private AnchorPane acHeaderPane;

    @FXML
    private TextField txtId, txtQty;

    @FXML
    private JFXButton btSave, btEdit, btCancel, btClose;

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
        colqty.setCellValueFactory(new TreeItemPropertyValueFactory<>("qty"));
        colaction.setCellValueFactory(new TreeItemPropertyValueFactory<>("action"));
    }

    private void showData() {
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                try {
                    // tableLogModel = new TableLogModel();
                    rs = tableLogModel.findAll();
                    node = new TreeItem<>();
                    while (rs.next()) {

                        root = new TreeItem<>(
                                new TableLogModel(rs.getString(1), rs.getString(2), getRootAction(rs.getString(1))));

                        // tableLogModel = new TableLogModel();
                        final ResultSet sublog = tableLogModel.findById(rs.getString(1));

                        int row = 0;
                        while (sublog.next()) {
                            subItem = new TreeItem<>(new TableLogModel(sublog.getString(2), "",
                                    getSubRootAction(sublog.getString(2), row)));
                            root.getChildren().add(subItem);
                            row++;
                        }

                        node.getChildren().add(root);

                    }
                    tableLog.setShowRoot(false);
                    tableLog.setRoot(node);

                } catch (Exception e) {
                    alertMessage.showErrorMessage("Load Data", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
                }
            }
        });
    }

    private void initKeyEvents() {
        txtId.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER)
                txtQty.requestFocus();
        });
        txtQty.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER)
                txtId.requestFocus();
        });
    }

    private void initEvents() {
        tableLog.setOnMouseClicked(e -> {

            if (e.getClickCount() >= 2 && tableLog.getSelectionModel().getSelectedItems() != null) {
                int rootindex = tableLog.getSelectionModel().getSelectedIndex();
                int allSubItem = tableLog.getSelectionModel().getModelItem(rootindex).getChildren().size();
                if (allSubItem <= 0)
                    return;

                txtLog.clear();
                for (int i = 0; i < allSubItem; i++) {
                    txtLog.appendText(tableLog.getSelectionModel().getModelItem(rootindex).getChildren().get(i)
                            .getValue().getTableId() + "\n");
                }

                btEdit.setDisable(false);
                btSave.setDisable(true);

                data = tableLog.getSelectionModel().getSelectedItems();
                txtId.setDisable(true);
                txtId.setText(data.get(0).getValue().getTableId());
                txtQty.setText(data.get(0).getValue().getQty());

                maxLog = tableLog.getSelectionModel().getModelItem(rootindex).getChildren().get(allSubItem - 1)
                        .getValue().getTableId();
                qty = Integer.parseInt(data.get(0).getValue().getQty());
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

            if (Integer.parseInt(txtQty.getText()) <= 0 || (Integer.parseInt(txtQty.getText()) + qty) > 6) {
                validRules.setErrorDecorationEnabled(true);
                dialog.showWarningDialog(null, "ຈຳນວນລ໋ອກຕູ້ທັງໝົດຕ້ອງຢູ່ລະຫວ່າງ 1 ຫາ 6 ລ໋ອກເທົ່ານັ້ນ.");
                return;
            }

            int newqty = Integer.parseInt(txtQty.getText());
            if (qty > 0 && maxLog != "") {
                for (int i = 1; i <= newqty; i++) {
                    txtLog.appendText(txtId.getText() + "000" + (i + qty) + "\n");
                }
            } else {
                for (int i = 1; i <= newqty; i++) {
                    txtLog.appendText(txtId.getText() + "000" + i + "\n");
                }
            }

        }
    }

    private void clearText() {
        validRules.setErrorDecorationEnabled(false);
        txtId.clear();
        txtLog.clear();
        txtQty.clear();
        txtId.setDisable(false);
        if (btSave.isDisable())
            btSave.setDisable(false);
        if (!btEdit.isDisable())
            btEdit.setDisable(true);
        txtId.requestFocus();

        maxLog = "";
        qty = 0;
    }

    @FXML
    private void btCancel(ActionEvent event) {
        clearText();
    }

    private void initRules() {
        validRules.setErrorDecorationEnabled(false);
        validRules.registerValidator(txtId, false, Validator.createEmptyValidator("ກະລຸນາປ້ອນເລກຕູ້"));
        validRules.registerValidator(txtQty, false,
                Validator.createEmptyValidator("ກະລຸນາປ້ອນຈຳນວນລ໋ອກຕູ້ ແລະ ຈຳນວນລ໋ອກຕູ້ຕ້ອງຫຼາຍກວ່າ 0 ລ໋ອກ"));
        validRules.registerValidator(txtLog, false, Validator.createEmptyValidator("ກະລຸນາປ້ອນຊື່ເລກລ໋ອກຕູ້"));
    }

    @FXML
    private void tbSaveData(ActionEvent event) {
        try {
            if (!txtId.getText().equals("") && !txtId.getText().equals("") && !txtQty.getText().equals("")) {
                // tableLogModel = new TableLogModel();
                if (tableLogModel.findTable(txtId.getText()).next()) {
                    dialog.showWarningDialog(null,
                            "ລະຫັດຕູ້ຊ້ຳກັບຂໍ້ມູນທີ່ມີຢູ່ໃນລະບົບກະລຸນາກວດສອບຂໍ້ມູນ ແລ້ວລອງບັນທຶກໃຫມ່ອີກຄັ້ງ");
                    return;
                }

                if (tableLogModel.saveTable(txtId.getText(), Integer.parseInt(txtQty.getText())) > 0) {
                    String line = txtLog.getText();
                    String[] lineCount = line.split("\n");

                    ArrayList<TableLogModel> list = new ArrayList<TableLogModel>();

                    for (int i = 0; i < lineCount.length; i++) {
                        /*
                         * try { // tableLogModel = new TableLogModel(txtId.getText(), lineCount[i]); //
                         * result = (tableLogModel.saveData() > 0) ? "Save Completed" : null; // Todo:
                         * // if...else // } catch (SQLException e) { //
                         * tableLogModel.deleteTable(txtId.getText()); //
                         * dialog.showExcectionDialog("Error", null, //
                         * "ເກີດບັນຫາໃນການບັນທືກຂໍ້ມູນລ໋ອກຕູ້", e); // return; }
                         */
                        list.add(new TableLogModel(txtId.getText(), lineCount[i]));
                    }

                    if (tableLogModel.saveTableLog(list, txtId.getText()) > 0) {
                        showData();
                        clearText();
                        alertMessage.showCompletedMessage("Saved", "Save data successfully.", 4, Pos.BOTTOM_RIGHT);
                        if (addBookController != null)
                            addBookController.fillTable();
                        if (importController != null)
                            importController.fillTable();
                    }
                }
            } else {
                validRules.setErrorDecorationEnabled(true);
                alertMessage.showWarningMessage("Save Warning", "Please chack your information and try again.", 4,
                        Pos.BOTTOM_RIGHT);
            }

        } catch (Exception e) {
            alertMessage.showErrorMessage("Save", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
        }
    }

    @FXML
    private void btUpdate(ActionEvent event) {
        try {

            if (!txtId.getText().equals("") && !txtId.getText().equals("") && !txtQty.getText().equals("")) {

                int result = 0;
                final String line = txtLog.getText();
                final String[] lineCount = line.split("\n");

                for (int i = 0; i < lineCount.length; i++) {
                    try {
                        tableLogModel = new TableLogModel(txtId.getText(), lineCount[i]);
                        result = tableLogModel.saveData();
                    } catch (Exception e) {
                        tableLogModel.deleteTable(txtId.getText());
                        dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການບັນທືກຂໍ້ມູນລ໋ອກຕູ້", e);
                        return;
                    }
                }

                // Todo: if result>0 have add new qty
                if (result > 0) {
                    tableLogModel = new TableLogModel(txtId.getText(), Integer.parseInt(txtQty.getText()));
                    if (tableLogModel.updateData() > 0) {
                        alertMessage.showCompletedMessage("Edited", "Edit data successfully.", 4, Pos.BOTTOM_RIGHT);
                        clearText();
                        showData();
                        if (addBookController != null)
                            addBookController.fillTable();
                        if (importController != null)
                            importController.fillTable();
                    } else {
                        alertMessage.showWarningMessage("Edit Warning", "Can not edit data", 4, Pos.BOTTOM_RIGHT);
                    }
                } else
                    alertMessage.showWarningMessage("Edit Warning", "No data had changed", 4, Pos.BOTTOM_RIGHT);

            } else {
                alertMessage.showWarningMessage("Edit Warning", "Please chack your information and try again.", 4,
                        Pos.BOTTOM_RIGHT);
            }
        } catch (Exception e) {
            alertMessage.showErrorMessage("Edit Error", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initTable();
        initRules();
        initEvents();
        initKeyEvents();
        txtQty.setText("1");
        btEdit.setDisable(true);
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
                Optional<ButtonType> result = dialog.showComfirmDialog("Comfirme", null, "ຕ້ອງການລົບຂໍ້ມູນອອກ ຫຼື ບໍ?");
                if (result.get() == ButtonType.YES)// Todo: Delete Data
                    // tableLogModel = new TableLogModel();
                    try {
                        if (tableLogModel.deleteTable(actionRoot.getId()) > 0) {
                            showData();
                            alertMessage.showCompletedMessage("Delete", "Delete data successfully.", 4,
                                    Pos.BOTTOM_RIGHT);
                            if (addBookController != null)
                                addBookController.fillTable();
                            if (importController != null)
                                importController.fillTable();
                        } else {
                            alertMessage.showWarningMessage("Delete", "Can not delete data.", 4, Pos.BOTTOM_RIGHT);
                        }
                    } catch (Exception ex) {
                        alertMessage.showErrorMessage("Delete", "Error: " + ex.getMessage(), 4, Pos.BOTTOM_RIGHT);
                    }

            });
        } catch (Exception e) {

        }
        return actionRoot;
    }

    public JFXButton getSubRootAction(String id, int rowIndex) {
        JFXButton actionSubRoot = new JFXButton("ລົບ");
        final Image img = new Image("com/mycompany/library_project/Icon/delete_subroot.png");
        ImageView imgView = new ImageView();
        try {
            imgView.setImage(img);
            imgView.setFitHeight(20);
            imgView.setFitWidth(20);
            actionSubRoot.setId(id);
            actionSubRoot.setGraphic(imgView);
            actionSubRoot.setStyle(" -fx-padding-left: 10px;    -fx-border-insets: 0px;    -fx-background-insets: 3px; "
                    + Style.buttonStyle);
            actionSubRoot.setOnAction(e -> {

                Optional<ButtonType> result = dialog.showComfirmDialog("Comfirme", null, "ຕ້ອງການລົບຂໍ້ມູນອອກ ຫຼື ບໍ?");
                if (result.get() == ButtonType.YES)
                    // Todo: Delete Data
                    // tableLogModel = new TableLogModel();
                    try {
                        rs = tableLogModel.findById(actionSubRoot.getId());
                        if (tableLogModel.deleteData(actionSubRoot.getId()) > 0) {
                            if (rs.next()) {
                                if (tableLogModel.updateTableQty(rs.getString(1)) > 0) {
                                    showData();
                                    alertMessage.showCompletedMessage("Delete", "Delete data successfully.", 4,
                                            Pos.BOTTOM_RIGHT);
                                }
                            }
                        }
                    } catch (SQLException ex) {
                        alertMessage.showErrorMessage("Delete", "Error: " + ex.getMessage(), 4, Pos.BOTTOM_RIGHT);
                    }

            });
        } catch (Exception e) {
        }
        return actionSubRoot;
    }
}
