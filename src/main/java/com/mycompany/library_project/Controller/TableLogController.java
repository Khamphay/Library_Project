package com.mycompany.library_project.Controller;

import javafx.beans.value.*;
import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.image.*;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.mycompany.library_project.Model.TableLogModel;
import org.controlsfx.validation.ValidationSupport;

public class TableLogController implements Initializable {

    private TableLogModel tableLogModel = null;
    private ResultSet rs = null;
    private TreeItem<TableLogModel> subItem, root, node;
    private ValidationSupport validRules;
    private ObservableList<TreeItem<TableLogModel>> data = FXCollections.observableArrayList();

    @FXML
    private TextField txtId, txtQty;
    @FXML
    private TextArea txtLog;

    @FXML
    private TreeTableView<TableLogModel> tableLog;

    @FXML
    private TreeTableColumn<TableLogModel, String> colid;

    @FXML
    private TreeTableColumn<TableLogModel, String> colqty;

    @FXML
    private TreeTableColumn<TableLogModel, JFXButton> colaction;

    private void initTable() {
        colid.setCellValueFactory(new TreeItemPropertyValueFactory<>("tableId"));
        colqty.setCellValueFactory(new TreeItemPropertyValueFactory<>("logQty"));
        colaction.setCellValueFactory(new TreeItemPropertyValueFactory<>("action"));
    }

    private void showData() {
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
                            new TableLogModel(sublog.getString(1), 1, getSubRootAction(sublog.getString(1))));
                    root.getChildren().add(subItem);
                }

                node.getChildren().add(root);

            }
            tableLog.setShowRoot(false);
            tableLog.setRoot(node);

        } catch (Exception e) {
            System.out.println("Error Show Date: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void initEvents() {
        tableLog.setOnMouseClicked(e -> {
            if (e.getClickCount() > 0 && tableLog.getSelectionModel().getSelectedItem() != null) {
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
        if (txtId.getText() != "" && txtQty.getText() != "") {
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
    private void btCancel(ActionEvent event) {
        clearText();
    }

    @FXML
    private void tbSaveData(ActionEvent event) {
        String result = null;
        try {
            tableLogModel = new TableLogModel();
            if (tableLogModel.saveTable(txtId.getText(), Integer.parseInt(txtQty.getText())) > 0) {
                String line = txtLog.getText();
                String[] lineCount = line.split("\n");
                for (int i = 0; i < lineCount.length; i++) {
                    try {
                        tableLogModel = new TableLogModel(txtId.getText(), lineCount[i]);
                        result = (tableLogModel.saveData() > 0) ? "Save Completed" : null; // Todo: if...else
                    } catch (Exception e) {
                        System.out.println("Error Save Log: " + e.getMessage());
                        tableLogModel.deleteTable(txtId.getText());
                        return;
                    }
                }
            } else {
                System.out.println("Cannot Save");
            }

            if (result != null) {
                System.out.println(result);
                showData();
                clearText();
            } else {
                System.out.println("Save Fail");
            }

        } catch (Exception e) {
            System.out.println("Error Save Table: " + e.getMessage());
            // e.printStackTrace();
        }
    }

    @FXML
    private void btDelete(ActionEvent event) {
        try {
            tableLogModel = new TableLogModel();
            tableLogModel.deleteData(txtId.getText());
        } catch (Exception e) {
            // TODO: handle exception
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
            actionRoot.setOnAction(e -> {
                tableLogModel = new TableLogModel();
                try {
                    if (tableLogModel.deleteTable(actionRoot.getId()) > 0) {
                        System.out.println("Delete Completed");
                        showData();
                    } else {
                        System.out.println("Delete Fail");
                    }
                } catch (Exception ex) {
                    System.out.println("Delete error:" + ex.getMessage());
                    // ex.printStackTrace();
                }
            });
        } catch (Exception e) {
            System.out.println("Error on action root: " + e.getMessage());
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
            actionSubRoot.setOnAction(e -> {
                tableLogModel = new TableLogModel();
                try {
                    if (tableLogModel.deleteData(actionSubRoot.getId()) > 0) {
                        System.out.println("Delete Completed");
                        showData();
                    } else {
                        System.out.println("Delete Fail");
                    }
                } catch (SQLException ex) {
                    System.out.println("Delete error:" + ex.getMessage());
                }
            });
        } catch (Exception e) {
            System.out.println("Error on action subroot: " + e.getMessage());
        }
        return actionSubRoot;
    }
}
