package com.mycompany.library_project.Controller;

import javafx.beans.value.*;
import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.jfoenix.controls.*;
import com.mycompany.library_project.ControllerDAOModel.*;
import com.mycompany.library_project.Model.*;

public class AddBookController implements Initializable {

    private ObservableList<String> status = FXCollections.observableArrayList("ຫວ່າງ", "ກຳລົງຢືມ", "ເສຍ");;
    private ObservableList<String> items = null;
    private TypeModel type = null;
    private CategoryModel category = null;
    private TableLogModel table = null;
    private BookDetailModel bookDetail = null;
    private ArrayList<String> arr_type, arr_category, arr_table;
    private int index_type, index_category;
    private AlertMessage alertMessage = new AlertMessage();

    @FXML
    private AnchorPane acPane;
    @FXML
    private TextField txtId, txtName, txtPage, txtQty, txtISBN;

    @FXML
    private JFXButton btclose;

    @FXML
    private TextArea txtBarcode, txtDetail;

    @FXML
    private ComboBox<String> cmbType, cmbCagtegory, cmbTable, cmbtableLog, cmbStatus;

    @FXML
    private void coloseForm() {

    }

    private void generatedBarcode() {
        try {
            txtBarcode.clear();
            if (txtId.getText() != "" && txtQty.getText() != "") {
                for (int i = 1; i <= Integer.parseInt(txtQty.getText()); i++) {
                    txtBarcode.appendText(txtId.getText() + "000" + i + "\n");
                }
            }
        } catch (Exception e) {
            alertMessage.showErrorMessage("Gennerate barcode", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
        }
    }

    private void clearText() {
        txtId.clear();
        txtName.clear();
        txtISBN.clear();
        txtPage.clear();
        txtQty.clear();
        txtBarcode.clear();
        txtDetail.clear();
        // cmbCagtegory.getSelectionModel().clearSelection();
        // cmbType.getSelectionModel().clearSelection();
        // cmbTable.getSelectionModel().clearSelection();
        // cmbtableLog.getSelectionModel().clearSelection();
    }

    private void initEvents() {
        txtPage.textProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    txtPage.setText(newValue.replaceAll("[^\\d]", ""));
                }
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
            generatedBarcode();
        });
        txtQty.setOnKeyTyped(e -> {
            generatedBarcode();
        });

        cmbTable.setOnAction(e -> {
            try {
                // index_table = cmbTable.getSelectionModel().getSelectedIndex();
                if (cmbTable.getSelectionModel().getSelectedItem() != null) {
                    table = new TableLogModel();
                    items = FXCollections.observableArrayList();
                    ResultSet rs = table.findById(cmbTable.getSelectionModel().getSelectedItem().toString());
                    while (rs.next()) {
                        items.add(rs.getString(1));
                    }
                    cmbtableLog.setItems(items);
                }
            } catch (Exception ex) {
                alertMessage.showErrorMessage(acPane, "Selete data", "Error: " + ex.getMessage(), 4, Pos.BOTTOM_RIGHT);
            }
        });
    }

    private void fillType() {

        // Todo: set event to combo box
        cmbType.setOnAction(e -> {
            index_type = cmbType.getSelectionModel().getSelectedIndex();
        });

        // Todo: set value
        try {
            arr_type = new ArrayList<>();
            items = FXCollections.observableArrayList();
            type = new TypeModel();
            ResultSet rs = type.findAll();
            while (rs.next()) {
                items.add(rs.getString(2));
                arr_type.add(rs.getString(1));
            }
            cmbType.setItems(items);
        } catch (Exception e) {
            alertMessage.showErrorMessage(acPane, "Load type", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
        }
    }

    private void fillCategory() {
        // Todo: set event to combo box
        cmbCagtegory.setOnAction(e -> {
            index_category = cmbCagtegory.getSelectionModel().getSelectedIndex();
        });

        // Todo: set value
        try {
            arr_category = new ArrayList<>();
            items = FXCollections.observableArrayList();
            category = new CategoryModel();
            ResultSet rs = category.findAll();
            while (rs.next()) {
                arr_category.add(rs.getString(1));
                items.add(rs.getString(2));
            }
            cmbCagtegory.setItems(items);
        } catch (Exception e) {
            alertMessage.showErrorMessage(acPane, "Load category", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
        }
    }

    private void fillTable() {
        try {
            arr_table = new ArrayList<>();
            items = FXCollections.observableArrayList();
            table = new TableLogModel();
            ResultSet rs = table.findAll();
            while (rs.next()) {
                arr_table.add(rs.getString(1));
                items.add(rs.getString(1));
            }
            cmbTable.setItems(items);
        } catch (Exception e) {
            alertMessage.showErrorMessage(acPane, "Load table", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
        }
    }

    @FXML
    private void btSave(ActionEvent event) {
        // cmbType.g
        String msg = null;
        try {
            bookDetail = new BookDetailModel(txtId.getText(), txtName.getText(), txtISBN.getText(),
                    Integer.parseInt(txtPage.getText()), Integer.parseInt(txtQty.getText()),
                    arr_category.get(index_category), arr_type.get(index_type),
                    cmbtableLog.getSelectionModel().getSelectedItem().toString(), txtDetail.getText());
            if (bookDetail.saveData() > 0) {
                String line = txtBarcode.getText();
                String[] lineCount = line.split("\n");

                for (int i = 0; i < lineCount.length; i++) {
                    try {
                        int result = bookDetail.saveBookBarCode(lineCount[i], txtId.getText(),
                                cmbStatus.getSelectionModel().getSelectedItem().toString());
                        if (result > 0) {
                            msg = "Save Completed";
                        } else {
                            msg = null;
                        }
                    } catch (Exception e) {
                        // Todo:................................
                        return;
                    }
                }

            } else {
                msg = null;
            }

            if (msg != null) {
                alertMessage.showCompletedMessage(acPane, "Save", "Save data successfully.", 4, Pos.BOTTOM_RIGHT);
            } else {
                alertMessage.showWarningMessage("Save", "Can not save data.", 4, Pos.BOTTOM_RIGHT);
            }
        } catch (Exception e) {
            alertMessage.showErrorMessage(acPane, "Save", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
        }
    }

    @FXML
    private void btClearText() {
        clearText();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cmbStatus.setItems(status);
        initEvents();
        fillType();
        fillCategory();
        fillTable();
    }

}
