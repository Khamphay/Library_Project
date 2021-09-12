package com.mycompany.library_project.Controller;

import java.net.URL;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.mycompany.library_project.Style;
import com.mycompany.library_project.ControllerDAOModel.*;
import com.mycompany.library_project.Model.SupplierModel;

import org.controlsfx.validation.*;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.*;
import javafx.collections.*;
import javafx.collections.transformation.*;
import javafx.event.*;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.fxml.*;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.util.Callback;

public class SupplierController implements Initializable {
    private DialogMessage dialog = new DialogMessage();
    private AlertMessage alertMessage = new AlertMessage();
    private SupplierModel supmodel = new SupplierModel();
    private ValidationSupport validRules = new ValidationSupport();
    private ManageBookController manageBookController = null;
    private ImportController importController = null;
    private ObservableList<SupplierModel> data = null;
    private ResultSet rs = null;
    private double x, y;

    public void initConstructor(ManageBookController manageBookController) {
        this.manageBookController = manageBookController;
    }

    public void initConstructor2(ImportController importController, Stage stage) {
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
    private AnchorPane acHeaderPane;

    @FXML
    public TextField txtId, txtName, txtTel, txtEmail, txtSearch;

    @FXML
    public TextArea txtAdress;

    @FXML
    public JFXButton btSave, btEdit, tbCancel, btClose;

    @FXML
    private TableView<SupplierModel> tableSupplier;

    @FXML
    private TableColumn<SupplierModel, String> colId, colName, colAddress, colTel, colEmail;

    private void initRules() {
        validRules.setErrorDecorationEnabled(false);
        validRules.registerValidator(txtId, false, Validator.createEmptyValidator("ກະລຸນາປ້ອນລະຫັດພະນັກງານ"));
        validRules.registerValidator(txtName, false, Validator.createEmptyValidator("ກະລຸນາປ້ອນຊື່"));
        validRules.registerValidator(txtAdress, false, Validator.createEmptyValidator("ກະລຸນາປ້ອນທີ່ຢູ່"));
        validRules.registerValidator(txtTel, false, Validator.createEmptyValidator("ກະລຸນາປ້ອນເບີໂທລະສັບ"));
        validRules.registerValidator(txtEmail, false,
                Validator.createEmptyValidator("ກະລຸນາປ້ອນ Email (ຖ້າມີ)", Severity.WARNING));
    }

    private void initTable() {
        colId.setCellValueFactory(new PropertyValueFactory<>("supid"));
        colName.setCellValueFactory(new PropertyValueFactory<>("supname"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colTel.setCellValueFactory(new PropertyValueFactory<>("tel"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colId.setVisible(false);

        // Todo: Add column number
        final TableColumn<SupplierModel, SupplierModel> colNumber = new TableColumn<SupplierModel, SupplierModel>(
                "ລຳດັບ");
        colNumber.setMinWidth(50);
        colNumber.setMaxWidth(200);
        colNumber.setPrefWidth(100);
        colNumber.setId("colCenter");
        colNumber.setCellValueFactory(
                new Callback<CellDataFeatures<SupplierModel, SupplierModel>, ObservableValue<SupplierModel>>() {

                    @Override
                    public ObservableValue<SupplierModel> call(CellDataFeatures<SupplierModel, SupplierModel> param) {
                        return new ReadOnlyObjectWrapper<SupplierModel>(param.getValue());
                    }

                });
        colNumber.setCellFactory(
                new Callback<TableColumn<SupplierModel, SupplierModel>, TableCell<SupplierModel, SupplierModel>>() {

                    @Override
                    public TableCell<SupplierModel, SupplierModel> call(
                            TableColumn<SupplierModel, SupplierModel> param) {
                        return new TableCell<SupplierModel, SupplierModel>() {
                            @Override
                            protected void updateItem(SupplierModel item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty)
                                    setText("");
                                if (this.getTableRow() != null && item != null)
                                    setText(Integer.toString(this.getTableRow().getIndex() + 1));
                            }
                        };
                    }
                });

        colNumber.setSortable(false);
        tableSupplier.getColumns().add(0, colNumber);

        // Todo: Add column Button
        addButtonToTable();

        tableSupplier.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() >= 2 && tableSupplier.getSelectionModel().getSelectedItem() != null) {

                    btEdit.setDisable(false);
                    btSave.setDisable(true);
                    txtId.setEditable(false);

                    txtId.setText(tableSupplier.getSelectionModel().getSelectedItem().getSupid());
                    txtName.setText(tableSupplier.getSelectionModel().getSelectedItem().getSupname());
                    txtAdress.setText(tableSupplier.getSelectionModel().getSelectedItem().getAddress());
                    txtTel.setText(tableSupplier.getSelectionModel().getSelectedItem().getTel());
                    txtEmail.setText(tableSupplier.getSelectionModel().getSelectedItem().getEmail());
                }
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
                txtAdress.requestFocus();
        });
        txtTel.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER)
                txtEmail.requestFocus();
        });

        txtTel.textProperty().addListener(new ChangeListener<String>() {
            // Todo: set properties type only numeric
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    txtTel.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
    }

    private void initEvents() {

        btSave.setOnAction(event -> {
            try {
                if (!txtId.getText().equals("") && !txtTel.getText().equals("") && !txtAdress.getText().equals("")
                        && !txtName.getText().equals("")) {

                    if (txtTel.getText().length() < 7 || txtTel.getText().length() > 11) {
                        dialog.showWarningDialog(null, "ເບີໂທລະສັບຕ້ອງຢູ່ລະຫວ່າງ 7 ຫາ 11 ຕົວເລກເທົ່ານັ້ນ.");
                        txtTel.requestFocus();
                        return;
                    }

                    supmodel = new SupplierModel(txtId.getText(), txtName.getText(), txtAdress.getText(),
                            txtTel.getText(), txtEmail.getText());
                    if (supmodel.saveData() > 0) {
                        alertMessage.showCompletedMessage("Saved", "Saved data successfully.", 4, Pos.BOTTOM_RIGHT);
                        showData();
                        clearText();
                        if (importController != null)
                            importController.fillSupplier();
                    }
                } else {
                    validRules.setErrorDecorationEnabled(true);
                }
            } catch (Exception e) {
                dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການບັນທຶກຂໍ້ມູນ", e);
                e.printStackTrace();
            }
        });

        btEdit.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                try {
                    if (!txtId.getText().equals("") && !txtTel.getText().equals("") && !txtAdress.getText().equals("")
                            && !txtName.getText().equals("")) {

                        if (txtTel.getText().length() < 7 || txtTel.getText().length() > 11) {
                            dialog.showWarningDialog(null, "ເບີໂທລະສັບຕ້ອງຢູ່ລະຫວ່າງ 7 ຫາ 11 ຕົວເລກເທົ່ານັ້ນ.");
                            txtTel.requestFocus();
                            return;
                        }

                        supmodel = new SupplierModel(txtId.getText(), txtName.getText(), txtAdress.getText(),
                                txtTel.getText(), txtEmail.getText());
                        if (supmodel.updateData() > 0) {
                            alertMessage.showCompletedMessage("Edit", "Edit data successfully.", 4, Pos.BOTTOM_RIGHT);
                            showData();
                            clearText();
                            if (importController != null)
                                importController.fillSupplier();
                        }
                    }
                } catch (Exception e) {
                    dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການແກ້ໄຂຂໍ້ມູນ", e);
                    e.printStackTrace();
                }
            }

        });

        tbCancel.setOnAction(event -> {
            clearText();
        });

        btClose.setOnAction(event -> {
            manageBookController.showMainMenuBooks();
        });
    }

    private void showData() {
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                try {
                    data = FXCollections.observableArrayList();
                    rs = supmodel.findAll();
                    while (rs.next()) {
                        data.add(new SupplierModel(rs.getString("sup_id"), rs.getString("sup_name"),
                                rs.getString("address"), rs.getString("tel"), rs.getString("email")));
                    }
                    // tableSupplier.setItems(data); //Todo: if you don't filter to Search data
                    // bellow:

                    // Todo: Search data
                    FilteredList<SupplierModel> filterSupplier = new FilteredList<SupplierModel>(data, supp -> true);
                    txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
                        filterSupplier.setPredicate(supp -> {
                            if (newValue.isEmpty())
                                return true;
                            if (supp.getSupid().toLowerCase().indexOf(newValue.toLowerCase()) != -1
                                    || supp.getSupname().toLowerCase().indexOf(newValue.toLowerCase()) != -1
                                    || supp.getAddress().toLowerCase().indexOf(newValue.toLowerCase()) != -1
                                    || supp.getEmail().toLowerCase().indexOf(newValue.toLowerCase()) != -1
                                    || supp.getTel().toLowerCase().indexOf(newValue.toLowerCase()) != -1)
                                return true;
                            else
                                return false;
                        });
                    });

                    SortedList<SupplierModel> sorted = new SortedList<>(filterSupplier);
                    sorted.comparatorProperty().bind(tableSupplier.comparatorProperty());
                    tableSupplier.setItems(sorted);

                } catch (Exception e) {
                    alertMessage.showErrorMessage("Load Data", "Error" + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
                }
            }

        });
    }

    private void clearText() {
        validRules.setErrorDecorationEnabled(false);
        txtId.clear();
        txtName.clear();
        txtAdress.clear();
        txtTel.clear();
        txtEmail.clear();
        txtId.setEditable(true);

        if (btSave.isDisable())
            btSave.setDisable(false);
        if (!btSave.isDisable())
            btEdit.setDisable(true);
        txtId.requestFocus();

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initEvents();
        initEvents();
        initRules();
        initTable();
        initKeyEvents();
        showData();
    }

    private void addButtonToTable() {
        TableColumn<SupplierModel, Void> colAtion = new TableColumn<>("Action");
        Callback<TableColumn<SupplierModel, Void>, TableCell<SupplierModel, Void>> cellFactory = new Callback<TableColumn<SupplierModel, Void>, TableCell<SupplierModel, Void>>() {

            @Override
            public TableCell<SupplierModel, Void> call(TableColumn<SupplierModel, Void> param) {
                final TableCell<SupplierModel, Void> cell = new TableCell<SupplierModel, Void>() {
                    final JFXButton delete = new JFXButton("ລົບ");

                    {
                        final ImageView imgView = new ImageView();
                        imgView.setFitWidth(20);
                        imgView.setFitHeight(20);
                        imgView.setImage(new Image("/com/mycompany/library_project/Icon/bin.png"));
                        delete.setStyle(Style.buttonStyle);
                        delete.setGraphic(imgView);

                        delete.setOnAction(new EventHandler<ActionEvent>() {

                            @Override
                            public void handle(ActionEvent event) {
                                dialog.showComfirmDialog("Comfirmed", null, "ຕ້ອງການລົບຂໍ້ມູນ ຫຼື ບໍ?");
                                try {
                                    if (supmodel.deleteData(supmodel.getSupid()) > 0) {
                                        showData();
                                        clearText();
                                        alertMessage.showCompletedMessage("Deleted", "Delete data successfully.", 4,
                                                Pos.BOTTOM_RIGHT);
                                        if (importController != null)
                                            importController.fillSupplier();
                                    }
                                } catch (Exception e) {
                                    dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການລົບຂໍ້ມູນ", e);
                                }
                            }

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
        tableSupplier.getColumns().add(colAtion);

    }
}
