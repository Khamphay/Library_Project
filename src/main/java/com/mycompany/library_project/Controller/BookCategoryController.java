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

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import com.jfoenix.controls.*;
import com.mycompany.library_project.Style;
import com.mycompany.library_project.ControllerDAOModel.*;
import com.mycompany.library_project.Model.CategoryModel;
import com.mycompany.library_project.config.CreateLogFile;

import org.controlsfx.control.textfield.TextFields;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

public class BookCategoryController implements Initializable {

    private ValidationSupport validRules = new ValidationSupport();
    private ManageBookController manageBookController = null;
    private AddBookController addBookController = null;
    private CategoryModel category = null;
    private ObservableList<CategoryModel> data = null;
    private ResultSet rs = null;
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
    private StackPane stakePane;

    @FXML
    private TextField txtcatgId, txtcatgName, txtSearch;

    @FXML
    private JFXButton btSave, btEdite, btCancel, btClose;

    @FXML
    private TableView<CategoryModel> tableCategory;

    @FXML
    private TableColumn<CategoryModel, String> colCatgId, colCatgName;

    private void initRules() {
        validRules.setErrorDecorationEnabled(false);
        validRules.redecorate();
        validRules.registerValidator(txtcatgId, false, Validator.createEmptyValidator("ກະລຸນາປ້ອນລະຫັດໝວດປຶ້ມ"));
        validRules.registerValidator(txtcatgName, false, Validator.createEmptyValidator("ກະລຸນາປ້ອນຊື່ໝວດປຶ້ມ"));
    }

    private void showData() {
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                try {
                    data = FXCollections.observableArrayList();
                    category = new CategoryModel();
                    rs = category.findAll();
                    while (rs.next()) {
                        data.add(new CategoryModel(rs.getString(1), rs.getString(2)));
                    }
                    // tableCategory.setItems(data); //Todo: if you don't filter to Search data
                    // bellow:

                    // Todo: Search Data
                    FilteredList<CategoryModel> filterCatg = new FilteredList<>(data, b -> true);
                    txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
                        filterCatg.setPredicate(searchCategory -> {
                            if (newValue.isEmpty()) {
                                return true;
                            }

                            if (searchCategory.getCatgName().toLowerCase().indexOf(newValue.toLowerCase()) != -1) {
                                return true;
                            } else
                                return false;

                        });
                    });

                    SortedList<CategoryModel> sorted = new SortedList<>(filterCatg);
                    sorted.comparatorProperty().bind(tableCategory.comparatorProperty());
                    tableCategory.setItems(sorted);
                    TextFields.bindAutoCompletion(txtSearch, sorted);
                } catch (Exception e) {
                    alertMessage.showErrorMessage(stakePane, "Load data", "Error: " + e.getMessage(), 4,
                            Pos.BOTTOM_RIGHT);
                }
            }
        });
    }

    private void ClearData() {
        validRules.setErrorDecorationEnabled(false);

        txtcatgId.setText("");
        txtcatgName.setText("");
        if (txtcatgId.isDisable())
            txtcatgId.setDisable(false);
        if (btSave.isDisable())
            btSave.setDisable(false);
        if (!btEdite.isDisable())
            btEdite.setDisable(true);
        txtcatgId.requestFocus();
    }

    @FXML
    private void closeForm() {
        manageBookController.showMainMenuBooks();
    }

    @FXML
    private void selectTable(MouseEvent clickEvent) {
        if (clickEvent.getClickCount() >= 2 && tableCategory.getSelectionModel().getSelectedItem() != null) {

            btEdite.setDisable(false);
            btSave.setDisable(true);
            txtcatgId.setDisable(true);

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
                    if (addBookController != null)
                        addBookController.fillCategory();
                }
            } else {
                // Todo: Show warning message if text or combo box is empty
                validRules.setErrorDecorationEnabled(true);
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
                    if (addBookController != null)
                        addBookController.fillCategory();
                }
            } else {
                // Todo: Show warning message if text or combo box is empty
                validRules.setErrorDecorationEnabled(true);
                alertMessage.showWarningMessage(stakePane, "Edit Warning",
                        "Please chack your information and try again.", 4, Pos.BOTTOM_RIGHT);
            }
        } catch (Exception e) {
            alertMessage.showErrorMessage(stakePane, "Edit", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
            logfile.createLogFile("ມີບັນຫາໃນການແກ້ໄຂຂໍ້ມູນໝວດປຶ້ມ", e);
        }
    }

    private void initTable() {
        colCatgId.setCellValueFactory(new PropertyValueFactory<>("catgId"));
        colCatgName.setCellValueFactory(new PropertyValueFactory<>("catgName"));

        // Todo: Add column number
        final TableColumn<CategoryModel, CategoryModel> colNumber = new TableColumn<CategoryModel, CategoryModel>(
                "ລຳດັບ");
        colNumber.setMinWidth(50);
        colNumber.setMaxWidth(100);
        colNumber.setPrefWidth(60);
        colNumber.setId("colCenter");
        colNumber.setCellValueFactory(
                new Callback<CellDataFeatures<CategoryModel, CategoryModel>, ObservableValue<CategoryModel>>() {

                    @Override
                    public ObservableValue<CategoryModel> call(CellDataFeatures<CategoryModel, CategoryModel> param) {
                        return new ReadOnlyObjectWrapper<CategoryModel>(param.getValue());
                    }

                });
        colNumber.setCellFactory(
                new Callback<TableColumn<CategoryModel, CategoryModel>, TableCell<CategoryModel, CategoryModel>>() {

                    @Override
                    public TableCell<CategoryModel, CategoryModel> call(
                            TableColumn<CategoryModel, CategoryModel> param) {
                        return new TableCell<CategoryModel, CategoryModel>() {
                            @Override
                            protected void updateItem(CategoryModel item, boolean empty) {
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
        tableCategory.getColumns().add(0, colNumber);

        // Todo: Add column Button
        addButtonToTable();
    }

    private void initKeyEvents() {
        txtcatgId.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER)
                txtcatgName.requestFocus();
        });
        txtcatgName.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER)
                txtcatgId.requestFocus();
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initTable();
        initRules();
        initKeyEvents();
        showData();
        btEdite.setDisable(true);
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
                    if (addBookController != null)
                        addBookController.fillCategory();
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
