package com.mycompany.library_project.Controller;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import com.jfoenix.controls.*;
import com.mycompany.library_project.Style;
import com.mycompany.library_project.ControllerDAOModel.*;
import com.mycompany.library_project.Model.DepartmentModel;

import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.*;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.*;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;

public class DepartmentController implements Initializable {

    private ValidationSupport validRules = new ValidationSupport();
    private ManagePersonalCotroller personalCotroller = null;
    private DepartmentModel depertment = null;
    private ResultSet rs = null;
    private ObservableList<DepartmentModel> data = null;
    private DialogMessage dialog = null;
    private AlertMessage alertMessage = new AlertMessage();
    private RegisterController registerController = null;

    public void initConstructor(ManagePersonalCotroller managePersonalCotroller) {
        this.personalCotroller = managePersonalCotroller;
    }

    public void initConstructor2(RegisterController registerController) {
        this.registerController = registerController;
        btClose.setDisable(true);
        btClose.setVisible(false);
    }

    @FXML
    private StackPane stackPane;

    @FXML
    private JFXButton btSave, btEdit, btCancel, btClose;

    @FXML
    private TextField txtId, txtName, txtSearch;

    @FXML
    private TableView<DepartmentModel> tableDepartment;

    @FXML
    private TableColumn<DepartmentModel, String> colId, colName;

    private void initRules() {
        validRules.setErrorDecorationEnabled(false);
        validRules.registerValidator(txtId, false, Validator.createEmptyValidator("ກະລຸນາປ້ອນລະຫັດ ພາກວີຊາ"));
        validRules.registerValidator(txtName, false, Validator.createEmptyValidator("ກະລຸນາປ້ອນຊື່ ພາກວີຊາ"));
    }

    private void initTable() {
        colId.setCellValueFactory(new PropertyValueFactory<>("depId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("depName"));

        // Todo: Add column number
        final TableColumn<DepartmentModel, DepartmentModel> colNumber = new TableColumn<DepartmentModel, DepartmentModel>(
                "ລຳດັບ");
        colNumber.setMinWidth(50);
        colNumber.setMaxWidth(100);
        colNumber.setPrefWidth(60);
        colNumber.setId("colCenter");
        colNumber.setCellValueFactory(
                new Callback<CellDataFeatures<DepartmentModel, DepartmentModel>, ObservableValue<DepartmentModel>>() {

                    @Override
                    public ObservableValue<DepartmentModel> call(
                            CellDataFeatures<DepartmentModel, DepartmentModel> param) {
                        return new ReadOnlyObjectWrapper<DepartmentModel>(param.getValue());
                    }

                });

        colNumber.setCellFactory(
                new Callback<TableColumn<DepartmentModel, DepartmentModel>, TableCell<DepartmentModel, DepartmentModel>>() {

                    @Override
                    public TableCell<DepartmentModel, DepartmentModel> call(
                            TableColumn<DepartmentModel, DepartmentModel> param) {
                        return new TableCell<DepartmentModel, DepartmentModel>() {
                            @Override
                            protected void updateItem(DepartmentModel item, boolean empty) {
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
        tableDepartment.getColumns().add(0, colNumber);

        // Todo: Add column Button
        addButtonToTable();

        tableDepartment.setOnMouseClicked(e -> {
            if (e.getClickCount() >= 2 && tableDepartment.getSelectionModel().getSelectedItem() != null) {

                btEdit.setDisable(false);
                txtId.setDisable(true);
                btSave.setDisable(true);

                depertment = tableDepartment.getSelectionModel().getSelectedItem();
                txtId.setText(depertment.getDepId());
                txtName.setText(depertment.getDepName());
            }
        });
    }

    private void initEvents() {
        btSave.setOnAction(event -> {
            try {
                if (!txtId.getText().equals("") && !txtName.getText().equals("")) {
                    depertment = new DepartmentModel(txtId.getText(), txtName.getText());
                    if (depertment.saveData() > 0) {
                        showData();
                        ClearText();
                        alertMessage.showCompletedMessage(stackPane, "Saved", "Save data successfully.", 3,
                                Pos.BOTTOM_RIGHT);
                        if (registerController != null)
                            registerController.fillDep();
                    }
                } else {
                    validRules.setErrorDecorationEnabled(true);
                    alertMessage.showWarningMessage(stackPane, "Save Warning",
                            "Please chack your information and try again.", 4, Pos.BOTTOM_RIGHT);
                }
            } catch (Exception ex) {
                alertMessage.showErrorMessage("Save Error", "Error: " + ex.getMessage(), 4,
                        Pos.BOTTOM_RIGHT);
            }
        });

        btEdit.setOnAction(event -> {
            try {
                if (!txtId.getText().equals("") && !txtName.getText().equals("")) {
                    depertment = new DepartmentModel(txtId.getText(), txtName.getText());
                    if (depertment.updateData() > 0) {
                        showData();
                        ClearText();
                        alertMessage.showCompletedMessage(stackPane, "Edited", "Edit data successfully.", 4,
                                Pos.BOTTOM_RIGHT);
                        if (registerController != null)
                            registerController.fillDep();
                    }
                } else {
                    validRules.setErrorDecorationEnabled(true);
                    alertMessage.showWarningMessage(stackPane, "Edit Warning",
                            "Please chack your information and try again.", 4, Pos.BOTTOM_RIGHT);
                }
            } catch (Exception e) {
                alertMessage.showErrorMessage("Edit Error", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
            }
        });

        btCancel.setOnAction(event -> {
            ClearText();
        });

        btClose.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                personalCotroller.showMainMenuPerson();
            }

        });
    }

    private void initKeyEvents() {
        txtId.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER)
                txtName.requestFocus();
        });
        txtName.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER)
                txtId.requestFocus();
        });
    }

    private void ClearText() {
        validRules.setErrorDecorationEnabled(false);
        txtId.clear();
        txtName.clear();
        if (txtId.isDisable())
            txtId.setDisable(false);
        if (btSave.isDisable())
            btSave.setDisable(false);
        if (!btEdit.isDisable())
            btEdit.setDisable(true);
        txtId.requestFocus();
    }

    private void showData() {

        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                try {
                    depertment = new DepartmentModel();
                    data = FXCollections.observableArrayList();
                    rs = depertment.findAll();
                    while (rs.next()) {
                        data.add(new DepartmentModel(rs.getString(1), rs.getString(2)));
                    }
                    // tableDepartment.setItems(data); //Todo: if you don't filter to Search data
                    // bellow:

                    // Todo: Search data
                    FilteredList<DepartmentModel> filterDep = new FilteredList<>(data, dep -> true);
                    txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
                        filterDep.setPredicate(searchDep -> {
                            if (newValue.isEmpty())
                                return true;

                            if (searchDep.getDepName().toLowerCase().indexOf(newValue.toLowerCase()) != -1)
                                return true;
                            else
                                return false;
                        });

                    });
                    SortedList<DepartmentModel> sorted = new SortedList<DepartmentModel>(filterDep);
                    sorted.comparatorProperty().bind(tableDepartment.comparatorProperty());
                    tableDepartment.setItems(sorted);

                } catch (Exception e) {
                    alertMessage.showErrorMessage("Load Data Error", e.getMessage(), 3, Pos.BOTTOM_RIGHT);
                }

            }
        });

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTable();
        initRules();
        initEvents();
        initKeyEvents();
        btEdit.setDisable(true);
        showData();

    }

    private void addButtonToTable() {
        TableColumn<DepartmentModel, Void> colAtion = new TableColumn<>("Action");
        Callback<TableColumn<DepartmentModel, Void>, TableCell<DepartmentModel, Void>> cellFactory = new Callback<TableColumn<DepartmentModel, Void>, TableCell<DepartmentModel, Void>>() {

            @Override
            public TableCell<DepartmentModel, Void> call(TableColumn<DepartmentModel, Void> param) {
                final TableCell<DepartmentModel, Void> cell = new TableCell<DepartmentModel, Void>() {
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
                            JFXButton[] buttons = { buttonYes(tableDepartment.getItems().get(getIndex()).getDepId()),
                                    buttonNo(), buttonCancel() };
                            dialog = new DialogMessage(stackPane, "ຄຳເຕືອນ", "ຕ້ອງການລົບຂໍ້ມູນອອກບໍ?",
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
        tableDepartment.getColumns().add(colAtion);

    }

    private JFXButton buttonYes(String depid) {
        JFXButton btyes = new JFXButton("ຕົກລົງ");
        btyes.setStyle(Style.buttonDialogStyle);
        btyes.setOnAction(event -> {
            // Todo: Delete Data
            try {
                depertment = new DepartmentModel();
                if (depertment.deleteData(depid) > 0) {
                    showData();
                    ClearText();
                    dialog.closeDialog();
                    alertMessage.showCompletedMessage("Delete", "Delete data successfully.", 4,
                            Pos.BOTTOM_RIGHT);
                    if (registerController != null)
                        registerController.fillDep();
                }
            } catch (Exception e) {
                alertMessage.showErrorMessage("Delete", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
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
