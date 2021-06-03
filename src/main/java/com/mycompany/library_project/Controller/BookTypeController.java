package com.mycompany.library_project.Controller;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.*;
import javafx.collections.transformation.*;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;

import com.mycompany.library_project.Model.TypeModel;
import com.mycompany.library_project.config.CreateLogFile;

import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import com.jfoenix.controls.*;
import com.mycompany.library_project.Style;
import com.mycompany.library_project.ControllerDAOModel.*;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class BookTypeController implements Initializable {

    private ValidationSupport validRules = new ValidationSupport();
    private ManageBookController manageBookController = null;
    private AddBookController addBookController = null;
    private ResultSet rs;
    private TypeModel type = null;
    private ObservableList<TypeModel> data = null;
    private DialogMessage dialog = null;
    private AlertMessage alertMessage = new AlertMessage();
    private CreateLogFile logfile = new CreateLogFile();

    public void initConstructor(ManageBookController manageBookController) {
        this.manageBookController = manageBookController;
    }

    public void initConstructor2(AddBookController addBookController) {
        this.addBookController = addBookController;
        btClose.setDisable(true);
        btClose.setVisible(false);
    }

    @FXML
    private StackPane stackePane;

    @FXML
    TextField txtTypeId, txtTypeName, txtSearch;

    @FXML
    private JFXButton btSave, btEdit, btCancel, btClose;

    @FXML
    private TableView<TypeModel> tableType;

    @FXML
    private TableColumn<TypeModel, String> colId, colName;

    private void ShowData() {
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
               try {
                   data = FXCollections.observableArrayList();
                   type = new TypeModel();
                   rs = type.findAll();
                   while (rs.next()) {
                       data.add(new TypeModel(rs.getString(1), rs.getString(2)));
                   }
                   // tableType.setItems(data); //Todo: if you don't filter to Search data bellow:

                   // Todo: Search data
                   FilteredList<TypeModel> filterType = new FilteredList<>(data, t -> true);
                   txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
                       filterType.setPredicate(searchType -> {
                           if (newValue.isEmpty())
                               return true;

                           if (searchType.getTypeName().toLowerCase().indexOf(newValue.toLowerCase()) != -1)
                               return true;
                           else
                               return false;
                       });
                   });

                   SortedList<TypeModel> sorted = new SortedList<TypeModel>(filterType);
                   sorted.comparatorProperty().bind(tableType.comparatorProperty());
                   tableType.setItems(sorted);

               } catch (SQLException e) {
                   alertMessage.showErrorMessage(stackePane, "Load Data Error", "Error: " + e.getMessage(), 4,
                           Pos.BOTTOM_RIGHT);
               }
           }

       });
   }

   private void initRules() {
       validRules.setErrorDecorationEnabled(false);
       validRules.redecorate();
       validRules.registerValidator(txtTypeId, false, Validator.createEmptyValidator("ກະລຸນາປ້ອນລະຫັດປະເພດປຶ້ມ"));
       validRules.registerValidator(txtTypeName, false, Validator.createEmptyValidator("ກະລຸນາປ້ອນຊື່ປະເພດປຶ້ມ"));
   }

   private void ClearData() {

       validRules.setErrorDecorationEnabled(false);

        txtTypeId.setText("");
        txtTypeName.setText("");

        if (txtTypeId.isDisable())
            txtTypeId.setDisable(false);
        if (btSave.isDisable())
            btSave.setDisable(false);
        if (!btEdit.isDisable())
            btEdit.setDisable(true);
        txtTypeId.requestFocus();

    }

    private void initTable() {
        colId.setCellValueFactory(new PropertyValueFactory<>("typeId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("typeName"));

        // Todo: Add column number of row
        final TableColumn<TypeModel, TypeModel> colNumber = new TableColumn<TypeModel, TypeModel>("ລຳດັບ");
        colNumber.setMinWidth(50);
        colNumber.setPrefWidth(60);
        colNumber.setMaxWidth(100);
        colNumber.setCellValueFactory(
                new Callback<CellDataFeatures<TypeModel, TypeModel>, ObservableValue<TypeModel>>() {
                    @Override
                    public ObservableValue<TypeModel> call(CellDataFeatures<TypeModel, TypeModel> type) {
                        return new ReadOnlyObjectWrapper<TypeModel>(type.getValue());
                    }
                });

        colNumber.setCellFactory(new Callback<TableColumn<TypeModel, TypeModel>, TableCell<TypeModel, TypeModel>>() {
            @Override
            public TableCell<TypeModel, TypeModel> call(TableColumn<TypeModel, TypeModel> param) {
                return new TableCell<TypeModel, TypeModel>() {
                    @Override
                    protected void updateItem(TypeModel item, boolean empty) {
                        super.updateItem(item, empty);

                        if (empty)
                            setText("");
                        else if (this.getTableRow() != null && item != null)
                            setText(Integer.toString(this.getTableRow().getIndex() + 1));

                    }
                };
            }
        });
        colNumber.setSortable(false);
        tableType.getColumns().add(0, colNumber);

        // Todo: Add column Button
        addButtonToTable();
    }

    private void initKeyEvents() {
        txtTypeId.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER)
                txtTypeName.requestFocus();
        });
        txtTypeName.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER)
                txtTypeId.requestFocus();
        });
    }

    @FXML
    private void closeForm() {
        manageBookController.showMainMenuBooks();
    }

    @FXML
    private void selectTableType(MouseEvent clickEvent) {
        if (clickEvent.getClickCount() >= 2 && tableType.getSelectionModel().getSelectedItem() != null) {

            btEdit.setDisable(false);
            btSave.setDisable(true);
            txtTypeId.setDisable(true);

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
                    ShowData();
                    ClearData();
                    alertMessage.showCompletedMessage(stackePane, "Save", "Save data successfully.", 4,
                            Pos.BOTTOM_RIGHT);
                    if (addBookController != null)
                        addBookController.fillType();
                }
            } else {
                // Todo: Show warning message if text or combo box is empty
                validRules.setErrorDecorationEnabled(true);
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
                    ShowData();
                    ClearData();
                    alertMessage.showCompletedMessage(stackePane, "Edited", "Edit data successfully.", 4,
                            Pos.BOTTOM_RIGHT);
                    if (addBookController != null)
                        addBookController.fillType();
                }
            } else {
                // Todo: Show warning message if text or combo box is empty
                validRules.setErrorDecorationEnabled(true);
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
        initTable();
        initRules();
        initKeyEvents();
        ShowData();
        btEdit.setDisable(true);
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
                            JFXButton[] buttons = { buttonYes(tableType.getItems().get(getIndex()).getTypeId()),
                                    buttonNo(), buttonCancel() };
                            dialog = new DialogMessage(stackePane, "ຄຳເຕືອນ", "ຕ້ອງການລົບຂໍ້ມູນອອກບໍ?",
                                    JFXDialog.DialogTransition.CENTER, buttons, false);
                            dialog.showDialog();
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty)
                            setGraphic(null);
                        else
                            setGraphic(delete);
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
                    if (addBookController != null)
                        addBookController.fillType();
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
