package com.mycompany.library_project.Controller;

import java.net.URL;
import java.sql.*;
import java.util.*;

import com.jfoenix.controls.*;
import com.mycompany.library_project.Style;
import com.mycompany.library_project.ControllerDAOModel.*;
import com.mycompany.library_project.Model.*;
import com.mycompany.library_project.Report.CreateReport;

import org.controlsfx.control.MaskerPane;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.*;
import javafx.concurrent.Task;
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
    private BookDetailModel addBarcode = new BookDetailModel();;
    private MaskerPane masker = new MaskerPane();
    private TableLogModel table = new TableLogModel();
    private AlertMessage alertMessage = new AlertMessage();
    private DialogMessage dialog = new DialogMessage();
    private ResultSet rs = null;
    private Task<Void> task = null;
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
                        // addBarcode = new BookDetailModel();
                        ArrayList<BookDetailModel> list = new ArrayList<>();
                        list.add(new BookDetailModel(txtBarcode.getText(), bookid,
                                cmbTabLog_id.getSelectionModel().getSelectedItem(),
                                cmbStatus.getSelectionModel().getSelectedItem()));
                        if (addBarcode.saveBookBarCode(list, null) > 0) {
                            if (addBarcode.updateBookQty(bookid, 1) > 0) {
                                alertMessage.showCompletedMessage("Saved", "Save data successfully", 4,
                                        Pos.BOTTOM_RIGHT);
                                clearText();
                                showBarcode(bookid);
                                bookController.showData();
                            }
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
                }
            }

        });

        btEdit.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                try {
                    if (!txtBarcode.getText().equals("")
                            && !cmbStatus.getSelectionModel().getSelectedItem().equals(null) && bookid != "") {
                        // addBarcode = new BookDetailModel();
                        if (addBarcode.updateData(bookid, txtBarcode.getText(), barcode,
                                cmbTabLog_id.getSelectionModel().getSelectedItem(),
                                cmbStatus.getSelectionModel().getSelectedItem()) > 0) {
                            alertMessage.showCompletedMessage("Edited", "Edit data completed", 4, Pos.BOTTOM_RIGHT);
                            clearText();
                            showBarcode(bookid);
                        }
                    } else {
                        // Todo: Show warning message if text or combo box is empty
                        validRules.setErrorDecorationEnabled(true);
                        alertMessage.showWarningMessage("Save Warning", "Please chack your information and try again.",
                                4, Pos.BOTTOM_RIGHT);
                    }
                } catch (Exception e) {
                    alertMessage.showErrorMessage("Edited", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
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
                task = new Task<Void>() {

                    @Override
                    protected Void call() throws Exception {
                        masker.setVisible(true);
                        masker.setProgressVisible(true);
                        CreateReport printBarcode = new CreateReport();
                        Map<String, Object> map = new HashMap<String, Object>();
                        if (!txtBarcode.getText().equals("")) {
                            map.put("bar_code", txtBarcode.getText());
                            map.put("bookid", "");
                        } else
                            map.put("bookid", bookid);
                        printBarcode.showReport(map, "printBarcodeByBookId.jrxml", "Print Barcode Error");
                        return null;
                    }

                    @Override
                    protected void succeeded() {
                        masker.setVisible(false);
                        masker.setProgressVisible(false);
                    }

                    @Override
                    protected void failed() {
                        masker.setVisible(false);
                        masker.setProgressVisible(false);
                        dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການພິມບາໂຄດ", task.getException());
                    }
                };
                new Thread(task).start();
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
        colNumber.setId("colCenter");
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

                                // Todo: Set row number
                                if (empty)
                                    setText("");
                                else if (this.getTableRow() != null && item != null)
                                    setText(Integer.toString(this.getTableRow().getIndex() + 1));

                                /*
                                 * // Todo: Set color to row that rent out date TableRow<BookDetailModel>
                                 * currentRow = getTableRow(); if (!empty) { if (item.getStatus().equals("ເສຍ"))
                                 * currentRow.setStyle("-fx-background-color:lightcoral"); }
                                 */
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

                    // table = new TableLogModel();
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
            // table = new TableLogModel();
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
                if (!cmbTable.getSelectionModel().getSelectedItem().isEmpty()) {
                    items = FXCollections.observableArrayList();
                    // table = new TableLogModel();
                    final ResultSet rsLog = table.findById(cmbTable.getSelectionModel().getSelectedItem().toString());
                    while (rsLog.next()) {
                        items.add(rsLog.getString(2));
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
                    // addBarcode = new BookDetailModel();
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

        masker.setVisible(false);
        masker.setText("ກຳລັງໂຫລດຂໍ້ມູນ, ກະລຸນາລໍຖ້າ...");
        masker.setStyle("-fx-font-family: BoonBaan;");
        stackPane.getChildren().add(masker);

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

                                Optional<ButtonType> result = dialog.showComfirmDialog("Comfirmed", null,
                                        "ຕ້ອງການລົບຂໍ້ມູນອອກບໍ ຫຼື ບໍ?");
                                if (result.get() == ButtonType.YES)
                                    // Todo: Delete Data
                                    try {
                                        // addBarcode = new BookDetailModel();
                                        if (addBarcode.deleteBarcode(bookid,
                                                tableBarcode.getItems().get(getIndex()).getBarcode()) > 0) {
                                            if (addBarcode.updateBookQty(bookid) > 0) {
                                                alertMessage.showCompletedMessage("Deleted",
                                                        "Delete data successfully.", 4, Pos.BOTTOM_RIGHT);
                                                showBarcode(bookid);
                                                clearText();
                                                bookController.showData();
                                            }
                                        }
                                    } catch (Exception ex) {
                                        dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການລົບຂໍ້ມູນ", ex);
                                        ex.printStackTrace();
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
        tableBarcode.getColumns().add(colAtion);

    }

}
