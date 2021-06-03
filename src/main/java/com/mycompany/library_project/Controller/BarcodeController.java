package com.mycompany.library_project.Controller;

import java.net.URL;
import java.sql.*;
import java.util.*;

import com.jfoenix.controls.*;
import com.mycompany.library_project.Style;
import com.mycompany.library_project.ControllerDAOModel.*;
import com.mycompany.library_project.Model.*;
import com.mycompany.library_project.Report.CreateReport;
import com.mycompany.library_project.config.CreateLogFile;

import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Callback;

public class BarcodeController implements Initializable {

    private ValidationSupport validRules = new ValidationSupport();
    private BookController bookController;
    private BookDetailModel addBarcode = null;
    private ResultSet rs = null;
    private TableLogModel table = new TableLogModel();
    private AlertMessage alertMessage = new AlertMessage();
    private DialogMessage dialog = null;
    private CreateLogFile logfile = new CreateLogFile();
    private ObservableList<BookDetailModel> data = null;
    private ObservableList<String> items = null;
    private ArrayList<String> book = null;
    private ObservableList<String> status = FXCollections.observableArrayList("ຫວ່າງ", "ກຳລັງຢືມ", "ເສຍ");
    private String barcode = "", bookid = "", tableid = "";
    private Double x, y;

    public void initConstructor(BookController bookController) {
        this.bookController = bookController;
    }

    @FXML
    private StackPane stackPane;

    @FXML
    private AnchorPane acHeaderPane;

    @FXML
    private TextField txtBarcode;

    @FXML
    private ComboBox<String> cmbStatus, cmbTabLog_id, cmbTable;

    @FXML
    private JFXButton btSave, btEdit, btCancel, btPrintBarcode, btClose;

    @FXML
    private TableView<BookDetailModel> tableBarcode;

    @FXML
    private TableColumn<BookDetailModel, String> colBarcode, colTableLog, colStatus;

    @FXML
    private TableColumn<BookDetailModel, JFXButton> colAction;

    private void initRules() {
        validRules.setErrorDecorationEnabled(false);
        validRules.redecorate();
        validRules.registerValidator(txtBarcode, false, Validator.createEmptyValidator("ກະລຸນາປ້ອນລະຫັດ Barcode ປຶ້ມ"));
        validRules.registerValidator(cmbTable, false, Validator.createEmptyValidator("ກະລຸນາເລືອກເລກຕູ້"));
        validRules.registerValidator(cmbTabLog_id, false, Validator.createEmptyValidator("ກະລຸນາເລືອກລ໋ອກຕູ້"));
        validRules.registerValidator(cmbStatus, false, Validator.createEmptyValidator("ກະລຸນາເລືອກສະຖານະຂອງປຶ້ມ"));
    }

    private void initEvents() {

        cmbStatus.setItems(status);

        btSave.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                try {
                    if (!txtBarcode.getText().equals("")
                            && !cmbStatus.getSelectionModel().getSelectedItem().equals(null) && bookid != "") {
                        addBarcode = new BookDetailModel();
                        if (addBarcode.saveBookBarCode(txtBarcode.getText(), bookid,
                                cmbTabLog_id.getSelectionModel().getSelectedItem(),
                                cmbStatus.getSelectionModel().getSelectedItem()) > 0) {
                            alertMessage.showCompletedMessage("Saved", "Save data completed", 4, Pos.BOTTOM_RIGHT);
                            clearText();
                            showBarcode(bookid);
                            bookController.showData();
                        } else {
                            alertMessage.showWarningMessage("Save Warning", "Save data fail", 4, Pos.BOTTOM_RIGHT);
                        }
                    } else {
                        // Todo: Show warning message if text or combo box is empty
                        validRules.setErrorDecorationEnabled(true);
                        alertMessage.showWarningMessage("Save Warning", "Please chack your information and try again.",
                                4, Pos.BOTTOM_RIGHT);
                    }
                } catch (Exception e) {
                    alertMessage.showErrorMessage("Save Error", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
                    logfile.createLogFile("ມີບັນຫາໃນການບັນທືກຂໍ້ມູນ Barcode ປຶ້ມ", e);
                }
            }

        });

        btEdit.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                try {
                    if (!txtBarcode.getText().equals("")
                            && !cmbStatus.getSelectionModel().getSelectedItem().equals(null) && bookid != "") {
                        addBarcode = new BookDetailModel();
                        if (addBarcode.updateData(bookid, txtBarcode.getText(), barcode,
                                cmbTabLog_id.getSelectionModel().getSelectedItem(),
                                cmbStatus.getSelectionModel().getSelectedItem()) > 0) {
                            alertMessage.showCompletedMessage("Edited", "Edit data completed", 4, Pos.BOTTOM_RIGHT);
                            clearText();
                            showBarcode(bookid);
                        } else {
                            alertMessage.showWarningMessage("Edited", "Edit data fail", 4, Pos.BOTTOM_RIGHT);
                        }
                    } else {
                        // Todo: Show warning message if text or combo box is empty
                        validRules.setErrorDecorationEnabled(true);
                        alertMessage.showWarningMessage("Save Warning", "Please chack your information and try again.",
                                4, Pos.BOTTOM_RIGHT);
                    }
                } catch (Exception e) {
                    alertMessage.showErrorMessage("Edited", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
                    logfile.createLogFile("ມີບັນຫາໃນການແກ້ໄຂຂໍ້ມູນ Barcode ປຶ້ມ", e);
                }
            }

        });

        btCancel.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                clearText();
            }

        });

        btPrintBarcode.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                CreateReport printBarcode = new CreateReport();
                Map<String, Object> map = new HashMap<String, Object>();
                if (!txtBarcode.getText().equals("")) {
                    map.put("bar_code", txtBarcode.getText());
                    map.put("bookid", "");
                } else
                    map.put("bookid", bookid);

                printBarcode.showReport(map, "printBarcodeByBookId.jrxml", "Print Barcode Error");
            }

        });

        btClose.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                Stage stage = (Stage) stackPane.getScene().getWindow();
                stage.close();
            }

        });
    }

    private void initTable() {
        colBarcode.setCellValueFactory(new PropertyValueFactory<>("barcode"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colTableLog.setCellValueFactory(new PropertyValueFactory<>("tableLogId"));

        // Todo: Add column number
        final TableColumn<BookDetailModel, BookDetailModel> colNumber = new TableColumn<BookDetailModel, BookDetailModel>(
                "ລຳດັບ");
        colNumber.setMinWidth(50);
        colNumber.setMaxWidth(120);
        colNumber.setPrefWidth(60);
        colNumber.setCellValueFactory(
                new Callback<CellDataFeatures<BookDetailModel, BookDetailModel>, ObservableValue<BookDetailModel>>() {

                    @Override
                    public ObservableValue<BookDetailModel> call(
                            CellDataFeatures<BookDetailModel, BookDetailModel> param) {
                        return new ReadOnlyObjectWrapper<BookDetailModel>(param.getValue());
                    }

                });
        colNumber.setCellFactory(
                new Callback<TableColumn<BookDetailModel, BookDetailModel>, TableCell<BookDetailModel, BookDetailModel>>() {

                    @Override
                    public TableCell<BookDetailModel, BookDetailModel> call(
                            TableColumn<BookDetailModel, BookDetailModel> param) {
                        return new TableCell<BookDetailModel, BookDetailModel>() {
                            @Override
                            protected void updateItem(BookDetailModel item, boolean empty) {
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
        tableBarcode.getColumns().add(0, colNumber);

        // Todo: Add column Button
        addButtonToTable();

        tableBarcode.setOnMouseClicked(event -> {
            if (event.getClickCount() >= 2 && tableBarcode.getSelectionModel().getSelectedItem() != null) {
                try {
                    btEdit.setDisable(false);
                    btSave.setDisable(true);

                    tableid = table.findTableId(tableBarcode.getSelectionModel().getSelectedItem().getTableLogId());
                    cmbTable.getSelectionModel().select(tableid);
                    barcode = tableBarcode.getSelectionModel().getSelectedItem().getBarcode();
                    txtBarcode.setText(tableBarcode.getSelectionModel().getSelectedItem().getBarcode());
                    cmbTabLog_id.getSelectionModel()
                            .select(tableBarcode.getSelectionModel().getSelectedItem().getTableLogId());
                    cmbStatus.getSelectionModel()
                            .select(tableBarcode.getSelectionModel().getSelectedItem().getStatus());
                    bookid = book.get(tableBarcode.getSelectionModel().getSelectedIndex());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void clearText() {
        validRules.setErrorDecorationEnabled(false);

        txtBarcode.clear();
        cmbStatus.getSelectionModel().clearSelection();
        cmbStatus.getSelectionModel().clearSelection();
        cmbTabLog_id.getSelectionModel().clearSelection();
        barcode = "";

        if (btSave.isDisable())
            btSave.setDisable(false);
        if (!btEdit.isDisable())
            btEdit.setDisable(true);
    }

    private void fillTable() {
        try {
            items = FXCollections.observableArrayList();
            table = new TableLogModel();
            ResultSet rs = table.findAll();
            while (rs.next()) {
                items.add(rs.getString(1));
            }
            cmbTable.setItems(items);
        } catch (Exception e) {
            alertMessage.showErrorMessage("Load table", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
        }

        cmbTable.setOnAction(e -> {
            try {
                // index_table = cmbTable.getSelectionModel().getSelectedIndex();
                if (cmbTable.getSelectionModel().getSelectedItem() != null) {
                    table = new TableLogModel();
                    items = FXCollections.observableArrayList();
                    ResultSet rs = table.findById(cmbTable.getSelectionModel().getSelectedItem().toString());
                    while (rs.next()) {
                        items.add(rs.getString(2));
                    }
                    cmbTabLog_id.setItems(items);
                }
            } catch (Exception ex) {
                alertMessage.showErrorMessage("Selete data", "Error: " + ex.getMessage(), 4, Pos.BOTTOM_RIGHT);
            }
        });
    }

    private void showBarcode(String book_id) {
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                try {
                    bookid = book_id;
                    data = FXCollections.observableArrayList();
                    book = new ArrayList<String>();
                    addBarcode = new BookDetailModel();
                    rs = addBarcode.showBarcode(book_id);
                    while (rs.next()) {
                        data.add(new BookDetailModel(rs.getString("barcode"), rs.getString("table_log_id"),
                                rs.getString("status")));
                        book.add(rs.getString("book_id"));
                    }
                    tableBarcode.setItems(data);
                } catch (Exception e) {
                    alertMessage.showErrorMessage("Load data", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
                }
            }
        });
    }

    private void moveForm() {
        acHeaderPane.setOnMousePressed(mouseEvent -> {
            x = mouseEvent.getSceneX();
            y = mouseEvent.getSceneY();
        });

        acHeaderPane.setOnMouseDragged(mouseEvent -> {
            BookController.addBarcode.setX(mouseEvent.getScreenX() - x);
            BookController.addBarcode.setY(mouseEvent.getScreenY() - y);
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        moveForm();
        fillTable();
        initTable();
        initRules();
        initEvents();
        btEdit.setDisable(true);
        showBarcode(BookController._book_id);
    }

    private void addButtonToTable() {
        TableColumn<BookDetailModel, Void> colAtion = new TableColumn<>("Action");
        Callback<TableColumn<BookDetailModel, Void>, TableCell<BookDetailModel, Void>> cellFactory = new Callback<TableColumn<BookDetailModel, Void>, TableCell<BookDetailModel, Void>>() {

            @Override
            public TableCell<BookDetailModel, Void> call(TableColumn<BookDetailModel, Void> param) {
                final TableCell<BookDetailModel, Void> cell = new TableCell<BookDetailModel, Void>() {
                    final JFXButton delete = new JFXButton("ລົບ");

                    {
                        final ImageView imageView = new ImageView();
                        imageView.setImage(new Image("/com/mycompany/library_project/Icon/bin.png"));
                        imageView.setFitHeight(20);
                        imageView.setFitWidth(20);
                        delete.setStyle(Style.buttonStyle);
                        delete.setGraphic(imageView);
                        delete.setOnAction(new EventHandler<ActionEvent>() {

                            @Override
                            public void handle(ActionEvent event) {
                                JFXButton[] buttons = { buttonYes(tableBarcode.getItems().get(getIndex()).getBarcode()),
                                        buttonNo(), buttonCancel() };
                                dialog = new DialogMessage(stackPane, "ຄຳເຕືອນ", "ຕ້ອງການລົບຂໍ້ມູນອອກບໍ?",
                                        JFXDialog.DialogTransition.CENTER, buttons, false);
                                dialog.showDialog();
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
        tableBarcode.getColumns().add(colAtion);

    }

    private JFXButton buttonYes(String barcodeid) {
        JFXButton btyes = new JFXButton("ຕົກລົງ");
        btyes.setStyle(Style.buttonDialogStyle);
        btyes.setOnAction(e -> {
            // Todo: Delete Data
            try {
                addBarcode = new BookDetailModel();
                if (addBarcode.deleteBarcode(bookid, barcodeid) > 0) {
                    dialog.closeDialog();
                    alertMessage.showCompletedMessage("Deleted", "Delete data successfully.", 4, Pos.BOTTOM_RIGHT);
                    showBarcode(bookid);
                    if (addBarcode.updateBookQty(bookid) > 0) {
                        clearText();
                        bookController.showData();
                    }
                } else {
                    alertMessage.showWarningMessage("Deleted", "Can not delete data.", 4, Pos.BOTTOM_RIGHT);
                }
            } catch (SQLException ex) {
                alertMessage.showErrorMessage(stackPane, "Delete", "Error: " + ex.getMessage(), 4, Pos.BOTTOM_RIGHT);
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
