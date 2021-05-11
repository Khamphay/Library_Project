package com.mycompany.library_project.Controller;

import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;

import com.mycompany.library_project.Model.TypeModel;
import com.mycompany.library_project.config.CreateLogFile;
import com.jfoenix.controls.*;
import com.mycompany.library_project.Style;
import com.mycompany.library_project.ControllerDAOModel.*;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class BookTypeController implements Initializable {

    private ResultSet rs;
    private TypeModel type = null;
    private ObservableList<TypeModel> data = null;
    private DialogMessage dialog = null;
    private AlertMessage alertMessage = new AlertMessage();
    private CreateLogFile logfile = new CreateLogFile();

    @FXML
    private StackPane stackePane;

    @FXML
    TextField txtTypeId, txtTypeName;

    @FXML
    private TableView<TypeModel> tableType;
    @FXML
    private TableColumn<TypeModel, String> colId;
    @FXML
    private TableColumn<TypeModel, String> colName;

    private void ShowData() {
        try {
            data = FXCollections.observableArrayList();
            type = new TypeModel();
            rs = type.findAll();
            while (rs.next()) {
                data.add(new TypeModel(rs.getString(1), rs.getString(2)));
            }
            tableType.setItems(data);
        } catch (SQLException e) {
            alertMessage.showErrorMessage(stackePane, "Load Data Error", "Error: " + e.getMessage(), 4,
                    Pos.BOTTOM_RIGHT);
        }
    }

    private void ClearData() {
        txtTypeId.setText("");
        txtTypeName.setText("");
    }

    @FXML
    private void selectTableType(MouseEvent clickEvent) {
        if (clickEvent.getClickCount() >= 2 && tableType.getSelectionModel().getSelectedItem() != null) {
            type = tableType.getSelectionModel().getSelectedItem();
            txtTypeId.setText(type.getTypeId());
            txtTypeName.setText(type.getTypeName());
        }
    }

    @FXML
    private void Save(ActionEvent actionEvent) throws SQLException {
        try {
            if (!txtTypeId.getText().equals("") && !txtTypeName.getText().equals("")) {
                type = new TypeModel(txtTypeId.getText(), txtTypeName.getText());
                if (type.saveData() == 1) {
                    alertMessage.showCompletedMessage(stackePane, "Save", "Save data successfully.", 4,
                            Pos.BOTTOM_RIGHT);
                    ShowData();
                    ClearData();
                }
            } else {
                alertMessage.showWarningMessage(stackePane, "Save Warning",
                        "Please chack your information and try again.", 4, Pos.BOTTOM_RIGHT);
            }
        } catch (Exception e) {
            alertMessage.showErrorMessage(stackePane, "Save Error", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
            logfile.createLogFile("ມີບັນຫາໃນການບັນທືກຂໍ້ມູນປະເພດປຶ້ມ", e);
        }
    }

    @FXML
    private void Update(ActionEvent event) throws SQLException {
        try {
            if (!txtTypeId.getText().equals("") && !txtTypeName.getText().equals("")) {
                type = new TypeModel(txtTypeId.getText(), txtTypeName.getText());
                if (type.updateData() == 1) {
                    alertMessage.showCompletedMessage(stackePane, "Edited", "Edit data successfully.", 4,
                            Pos.BOTTOM_RIGHT);
                    ShowData();
                    ClearData();
                }
            } else {
                alertMessage.showWarningMessage(stackePane, "Edit Warning",
                        "Please chack your information and try again.", 4, Pos.BOTTOM_RIGHT);
            }
        } catch (Exception e) {
            alertMessage.showErrorMessage(stackePane, "Edit Error", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
            logfile.createLogFile("ມີບັນຫາໃນການແກ້ໄຂຂໍ້ມູນປະເພດປຶ້ມ", e);
        }
    }

    @FXML
    private void ClearText(ActionEvent event) {
        ClearData();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colId.setCellValueFactory(new PropertyValueFactory<>("typeId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("typeName"));
        addButtonToTable();
        ShowData();
    }

    private void addButtonToTable() {
        TableColumn<TypeModel, Void> colAtion = new TableColumn<>("Action");
        Callback<TableColumn<TypeModel, Void>, TableCell<TypeModel, Void>> cellFactory = new Callback<TableColumn<TypeModel, Void>, TableCell<TypeModel, Void>>() {

            @Override
            public TableCell<TypeModel, Void> call(TableColumn<TypeModel, Void> param) {
                final TableCell<TypeModel, Void> cell = new TableCell<TypeModel, Void>() {
                    final JFXButton delete = new JFXButton("ລົບ");

                    {
                        final Image img = new Image("/com/mycompany/library_project/Icon/bin.png");
                        final ImageView imgView = new ImageView();
                        imgView.setImage(img);
                        imgView.setFitWidth(20);
                        imgView.setFitHeight(20);
                        delete.setGraphic(imgView);
                        delete.setStyle(Style.buttonStyle);
                        delete.setOnAction(e -> {
                            JFXButton[] buttons = { buttonYes(tableType.getItems().get(getIndex()).getTypeId()), buttonNo(), buttonCancel() };
                            dialog = new DialogMessage(stackePane, "ຄຳເຕືອນ", "ຕ້ອງການລົບຂໍ້ມູນອອກບໍ?",
                                    JFXDialog.DialogTransition.CENTER, buttons, false);
                            dialog.showDialog();
                        });
                    }

                };
                return cell;
            }

        };
        colAtion.setCellFactory(cellFactory);
        tableType.getColumns().add(colAtion);

    }
    
    private JFXButton buttonYes(String typeid) {
        JFXButton btyes = new JFXButton("ຕົກລົງ");
        btyes.setStyle(Style.buttonDialogStyle);
        btyes.setOnAction(e -> {
            // Todo: Delete Data
            try {
                type = new TypeModel();
                if (type.deleteData(typeid) > 0) {
                    ShowData();
                    ClearData();
                    dialog.closeDialog();
                    alertMessage.showCompletedMessage(stackePane, "Delete", "Delete data successfully.", 4,
                            Pos.BOTTOM_RIGHT);
                }
            } catch (SQLException ex) {
                alertMessage.showErrorMessage(stackePane, "Delete", "Error: " + ex.getMessage(), 4, Pos.BOTTOM_RIGHT);
                logfile.createLogFile("ມີບັນຫາໃນການລົບຂໍ້ມູນປະເພດປຶ້ມ", ex);
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
