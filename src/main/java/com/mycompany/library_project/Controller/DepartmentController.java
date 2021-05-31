package com.mycompany.library_project.Controller;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import com.jfoenix.controls.*;
import com.mycompany.library_project.Style;
import com.mycompany.library_project.ControllerDAOModel.*;
import com.mycompany.library_project.Model.DepartmentModel;
import com.mycompany.library_project.config.CreateLogFile;

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
import javafx.scene.layout.StackPane;
import javafx.util.Callback;

public class DepartmentController implements Initializable {

    private ManagePersonalCotroller personalCotroller = null;
    private DepartmentModel depertment = null;
    private ResultSet rs = null;
    private ObservableList<DepartmentModel> data = null;
    private DialogMessage dialog = null;
    private AlertMessage alertMessage = new AlertMessage();
    private CreateLogFile logfile = new CreateLogFile();

    public void initConstructor(ManagePersonalCotroller managePersonalCotroller) {
        this.personalCotroller = managePersonalCotroller;
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

    private void initTable() {
        colId.setCellValueFactory(new PropertyValueFactory<>("depId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("depName"));

        // Todo: Add column number
        final TableColumn<DepartmentModel, DepartmentModel> colNumber = new TableColumn<DepartmentModel, DepartmentModel>(
                "ລຳດັບ");
        colNumber.setMinWidth(50);
        colNumber.setMaxWidth(100);
        colNumber.setPrefWidth(60);
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
                depertment = tableDepartment.getSelectionModel().getSelectedItem();
                txtId.setText(depertment.getDepId());
                txtName.setText(depertment.getDepName());
            }
        });
    }

    private void initEvents() {
        btSave.setOnAction(event -> {
            try {
                if (txtId.getText().equals("") && txtName.getText().equals("")) {
                    depertment = new DepartmentModel(txtId.getText(), txtName.getText());
                    if (depertment.saveData() > 0) {
                        showData();
                        ClearText();
                        alertMessage.showCompletedMessage(stackPane, "Saved", "Save data successfully.", 3,
                                Pos.BOTTOM_RIGHT);
                    } else {
                        alertMessage.showWarningMessage(stackPane, "Save Warning", "Can not save data.", 4,
                                Pos.BOTTOM_RIGHT);
                    }
                } else {
                    alertMessage.showWarningMessage(stackPane, "Save Warning",
                            "Please chack your information and try again.", 4, Pos.BOTTOM_RIGHT);
                }
            } catch (Exception ex) {
                alertMessage.showErrorMessage(stackPane, "Save Error", "Error: " + ex.getMessage(), 4,
                        Pos.BOTTOM_RIGHT);
                logfile.createLogFile("ມີບັນຫາໃນການບັນທືກຂໍ້ມູນພາກວິຊາ", ex);
            }
        });

        btEdit.setOnAction(event -> {
            try {
                if (txtId.getText().equals("") && txtName.getText().equals("")) {
                    depertment = new DepartmentModel(txtId.getText(), txtName.getText());
                    if (depertment.updateData() > 0) {
                        showData();
                        ClearText();
                        alertMessage.showCompletedMessage(stackPane, "Edited", "Edit data successfully.", 4,
                                Pos.BOTTOM_RIGHT);
                    } else {
                        alertMessage.showWarningMessage(stackPane, "Edit Warning", "Can not edit data.", 4,
                                Pos.BOTTOM_RIGHT);
                    }
                } else {
                    alertMessage.showWarningMessage(stackPane, "Edit Warning",
                            "Please chack your information and try again.", 4, Pos.BOTTOM_RIGHT);
                }
            } catch (Exception e) {
                alertMessage.showErrorMessage(stackPane, "Edit Error", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
                logfile.createLogFile("ມີບັນຫາໃນການແກ້ໄຂຂໍ້ມູນພາກວິຊາ", e);
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

    private void ClearText() {
        txtId.clear();
        txtName.clear();
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
                    alertMessage.showErrorMessage("ໂຫຼດຂໍ້ມູນ", e.getMessage(), 3, Pos.BOTTOM_RIGHT);
                }

            }
        });

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTable();
        initEvents();
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
                    alertMessage.showCompletedMessage(stackPane, "Delete", "Delete data successfully.", 4,
                            Pos.BOTTOM_RIGHT);
                } else {
                    alertMessage.showWarningMessage(stackPane, "Delete", "Can not delete data.", 4, Pos.BOTTOM_RIGHT);
                }
            } catch (Exception e) {
                alertMessage.showErrorMessage(stackPane, "Delete", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
                logfile.createLogFile("ມີບັນຫາໃນການລົບຂໍ້ມູນພາກວິຊາ", e);
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
