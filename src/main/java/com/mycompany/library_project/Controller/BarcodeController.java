package com.mycompany.library_project.Controller;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import com.jfoenix.controls.*;
import com.mycompany.library_project.Style;
import com.mycompany.library_project.ControllerDAOModel.*;
import com.mycompany.library_project.Model.*;
import com.mycompany.library_project.Report.CreateReport;
import com.mycompany.library_project.config.CreateLogFile;

import javafx.application.Platform;
import javafx.collections.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Callback;

public class BarcodeController implements Initializable {

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
    private ObservableList<String> status = FXCollections.observableArrayList("ຫວ່າງ", "ກຳລົງຢືມ", "ເສຍ");
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
                            showBarcode(bookid);
                            bookController.showData();
                        } else {
                            alertMessage.showWarningMessage("Save Warning", "Save data fail", 4, Pos.BOTTOM_RIGHT);
                        }
                    } else {
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
                    addBarcode = new BookDetailModel();
                    if (addBarcode.updateData(bookid, txtBarcode.getText(), barcode,
                            cmbTabLog_id.getSelectionModel().getSelectedItem(),
                            cmbStatus.getSelectionModel().getSelectedItem()) > 0) {
                        alertMessage.showCompletedMessage("Edited", "Edit data completed", 4, Pos.BOTTOM_RIGHT);
                        showBarcode(bookid);
                    } else {
                        alertMessage.showWarningMessage("Edited", "Edit data fail", 4, Pos.BOTTOM_RIGHT);
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
        addButtonToTable();

        tableBarcode.setOnMouseClicked(event -> {
            if (event.getClickCount() >= 2 && tableBarcode.getSelectionModel().getSelectedItem() != null) {
                try {
                    tableid = table.findTableId(tableBarcode.getSelectionModel().getSelectedItem().getTableLogId());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                cmbTable.getSelectionModel().select(tableid);
                barcode = tableBarcode.getSelectionModel().getSelectedItem().getBarcode();
                txtBarcode.setText(tableBarcode.getSelectionModel().getSelectedItem().getBarcode());
                cmbTabLog_id.getSelectionModel()
                        .select(tableBarcode.getSelectionModel().getSelectedItem().getTableLogId());
                cmbStatus.getSelectionModel().select(tableBarcode.getSelectionModel().getSelectedItem().getStatus());
                bookid = book.get(tableBarcode.getSelectionModel().getSelectedIndex());
            }
        });
    }

    private void clearText() {
        txtBarcode.clear();
        cmbStatus.getSelectionModel().select("");
        barcode = "";
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
        initEvents();
        initTable();
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
