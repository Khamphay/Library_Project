package com.mycompany.library_project.Controller;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.*;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.net.URL;
import java.sql.*;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.mycompany.library_project.App;
import com.mycompany.library_project.Style;
import com.mycompany.library_project.ControllerDAOModel.*;
import com.mycompany.library_project.Model.*;
import com.mycompany.library_project.config.CreateLogFile;

import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

public class SendBookController implements Initializable {

    private ValidationSupport validRules = new ValidationSupport();
    private DecimalFormat dcFormat = new DecimalFormat("#,##0.00 ກີບ");
    private HomeController homeController = null;
    private AdjustmentModel adjustmentModel = null;
    private RentBookModel sendBook = new RentBookModel();
    private AlertMessage alertMessage = new AlertMessage();
    // private DialogMessage dialog = null;
    private BookDetailModel book = new BookDetailModel();
    private MyDate mydate = new MyDate();
    private ResultSet rs = null;
    // private JFXButton[] buttons = { buttonOK() };
    String rent_id = "", table = "", tableLog = "";
    int page = 0, qtyOutOfDate = 0;
    double price = 0.0, allPrice = 0.0;

    public void initConstructor(HomeController homeController) {
        this.homeController = homeController;
    }

    @FXML
    private StackPane stackPane;
    @FXML
    private TextField txtBarcode, txtBookName, txtCatg, txtType, txtMemberName, txtOutDate, txtPrice, txtAllPrice;

    @FXML
    private DatePicker dateRent, dateSend;

    @FXML
    private JFXButton btSave, btBookLost, btCancel, btClose;

    @FXML
    private TableView<RentBookModel> tableSendBooks;

    @FXML
    private TableColumn<RentBookModel, String> colRentId, colBarcode, colBookName, colPage, colCatg, colType, colTable,
            colTableLog, colMemberName, colPrice, colOutDate;

    @FXML
    private TableColumn<RentBookModel, Date> colDateSend, colDateRent;

    private void openBookLost() {
        try {
            final FXMLLoader loader = new FXMLLoader(App.class.getResource("frmBookLost.fxml"));
            final Parent subForm = loader.load();
            BookLostController booklostController = loader.getController();
            booklostController.initConstructor(this);
            final Scene scene = new Scene(subForm);
            final Stage stage = new Stage();
            stage.setTitle("Ajustment");
            stage.setScene(scene);
            stage.showAndWait();

        } catch (Exception e) {
            alertMessage.showErrorMessage("Open Form", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
            CreateLogFile config = new CreateLogFile();
            config.createLogFile("ການເປີດຟອມຈັດການຂໍ້ມູນປຶ້ມມີບັນຫາ: Form Book Lost", e);
        }
    }

    private void initRules() {
        validRules.setErrorDecorationEnabled(false);
        // validRules.registerValidator(, false,
        // Validator.createEmptyValidator("ກະລຸນາປ້ອນລະຫັດສະມາຊິດ"));
        validRules.registerValidator(txtBarcode, false, Validator.createEmptyValidator("ກະລຸນາລະຫັດ Barcode"));
        validRules.registerValidator(txtBarcode, false, Validator.createEmptyValidator("ກະລຸນາປ້ອນຊື່ປຶ້ມ"));
        validRules.registerValidator(txtCatg, false, Validator.createEmptyValidator("ກະລຸນາປ້ອນໝວດປຶ້ມ"));
        validRules.registerValidator(txtType, false, Validator.createEmptyValidator("ກະລຸນາປ້ອນປະເພດປຶ້ມ"));
        validRules.registerValidator(txtMemberName, false,
                Validator.createEmptyValidator("ກະລຸນາປ້ອນຊື່ ແລະ ນາມສະກຸນສະມາຊິດ"));
    }

    private void clearTextField() {
        txtBarcode.clear();
        txtBookName.clear();
        txtCatg.clear();
        txtType.clear();
        txtMemberName.clear();
    }

    private void clearText() {
        validRules.setErrorDecorationEnabled(false);
        txtBarcode.clear();
        txtBookName.clear();
        txtCatg.clear();
        txtType.clear();
        txtMemberName.clear();
        txtAllPrice.clear();
        txtPrice.clear();
        txtOutDate.clear();
        rent_id = "";
        page = 0;
        table = "";
        tableLog = "";
        allPrice = 0.0;
        qtyOutOfDate = 0;
    }

    private void initTable() {
        colRentId.setCellValueFactory(new PropertyValueFactory<>("rentId"));
        colBarcode.setCellValueFactory(new PropertyValueFactory<>("barcode"));
        colBookName.setCellValueFactory(new PropertyValueFactory<>("bookName"));
        colPage.setCellValueFactory(new PropertyValueFactory<>("page"));
        colCatg.setCellValueFactory(new PropertyValueFactory<>("catg"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colTable.setCellValueFactory(new PropertyValueFactory<>("table"));
        colTableLog.setCellValueFactory(new PropertyValueFactory<>("tableLog"));
        colMemberName.setCellValueFactory(new PropertyValueFactory<>("member"));
        colDateRent.setCellValueFactory(new PropertyValueFactory<>("rentDate"));
        colDateSend.setCellValueFactory(new PropertyValueFactory<>("sendDate"));
        colOutDate.setCellValueFactory(new PropertyValueFactory<>("outDate"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("pricePerBook"));
        colRentId.setVisible(true);

        // Todo: Add column number
        final TableColumn<RentBookModel, RentBookModel> colNumber = new TableColumn<RentBookModel, RentBookModel>(
                "ລຳດັບ");
        colNumber.setMinWidth(50);
        colNumber.setMaxWidth(120);
        colNumber.setPrefWidth(60);
        colNumber.setCellValueFactory(
                new Callback<CellDataFeatures<RentBookModel, RentBookModel>, ObservableValue<RentBookModel>>() {

                    @Override
                    public ObservableValue<RentBookModel> call(CellDataFeatures<RentBookModel, RentBookModel> param) {
                        return new ReadOnlyObjectWrapper<RentBookModel>(param.getValue());
                    }
                });
        colNumber.setCellFactory(
                new Callback<TableColumn<RentBookModel, RentBookModel>, TableCell<RentBookModel, RentBookModel>>() {

                    @Override
                    public TableCell<RentBookModel, RentBookModel> call(
                            TableColumn<RentBookModel, RentBookModel> param) {
                        return new TableCell<RentBookModel, RentBookModel>() {
                            @Override
                            protected void updateItem(RentBookModel item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty)
                                    setText("");
                                else if (this.getTableRow() != null && item != null)
                                    setText(Integer.toString(this.getTableRow().getIndex() + 1));

                                // // Todo: Set color to row that rent out date
                                // TableRow<RentBookModel> currentRow = getTableRow();
                                // if (!empty && this.getTableRow() != null && item != null) {
                                // if (mydate.cancalarDate(item.getSendDate().toLocalDate()) > 0)
                                // currentRow.setStyle("-fx-background-color:lightcoral");
                                // }
                            }
                        };
                    }

                });
        colNumber.setSortable(true);
        tableSendBooks.getColumns().add(0, colNumber);

        // Todo: Add column Button
        addButtonToTable();
    }

    private void initEvents() {
        txtBarcode.setOnKeyPressed(key -> {
            if (key.getCode() == KeyCode.ENTER) {
                try {
                    if (!txtBarcode.getText().equals("")) {

                        rs = sendBook.getSendBook(txtBarcode.getText(), "ກຳລັງຢືມ");
                        if (rs.next()) {

                            rent_id = rs.getString("rent_id");
                            txtBookName.setText(rs.getString("book_name"));
                            page = rs.getInt("page");
                            txtCatg.setText(rs.getString("catg_name"));
                            txtType.setText(rs.getString("type_name"));
                            table = rs.getString("tableid");
                            tableLog = rs.getString("table_log_id");
                            txtMemberName.setText(rs.getString("full_name") + " " + rs.getString("sur_name"));
                            dateRent.setValue(rs.getDate("date_rent").toLocalDate());
                            dateSend.setValue(rs.getDate("date_send").toLocalDate());

                            // Todo: Cancalar Date
                            int dateout = mydate.cancalarDate(rs.getDate("date_send").toLocalDate());
                            if (dateout > 0) {
                                qtyOutOfDate += 1;
                            }
                            txtOutDate.setText(Integer.toString(dateout) + " ມື້");
                            allPrice += (price * dateout);

                            int index = 0;
                            if (tableSendBooks.getItems().size() > 0) {
                                for (RentBookModel row : tableSendBooks.getItems()) {
                                    if (row.getBarcode().equals(txtBarcode.getText())) {
                                        tableSendBooks.getItems().remove(index);
                                        allPrice -= (price * dateout);
                                        break;
                                    }
                                    index++;
                                }
                            }

                            txtAllPrice.setText(dcFormat.format(allPrice));
                            tableSendBooks.getItems()
                                    .add(new RentBookModel(rent_id, txtBarcode.getText(), txtBookName.getText(),
                                            (page + " ໜ້າ"), txtCatg.getText(), txtType.getText(), table, tableLog,
                                            txtMemberName.getText(), Date.valueOf(dateRent.getValue()),
                                            Date.valueOf(dateSend.getValue()), txtOutDate.getText(),
                                            dcFormat.format(price * dateout)));
                        }
                    } else {
                        validRules.setErrorDecorationEnabled(true);
                        alertMessage.showWarningMessage(stackPane, "Warning",
                                "Please chack your information and try again", 4, Pos.TOP_CENTER);
                        clearTextField();
                    }

                } catch (Exception e) {
                    alertMessage.showErrorMessage("Load Error", "Error" + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
                }

            }
        });
        btSave.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                try {
                    String msg = null;
                    for (RentBookModel row : tableSendBooks.getItems()) {
                        if (sendBook.sendBook(row.getRentId(), row.getBarcode(), "ສົ່ງແລ້ວ") > 0) {
                            msg = ((book.updateStatus(row.getBarcode(), "ຫວ່າງ") > 0) ? "Send books successfully"
                                    : null);
                        }
                    }

                    if (qtyOutOfDate > 0) {
                        adjustmentModel = new AdjustmentModel(rent_id, qtyOutOfDate, allPrice,
                                Date.valueOf(LocalDate.now()), "ຢືມປຶ້ມກາຍກຳນົດ");
                        adjustmentModel.saveData();
                    }
                    if (msg != null) {
                        alertMessage.showCompletedMessage(stackPane, "Send", msg, 4, Pos.TOP_CENTER);
                        tableSendBooks.getItems().clear();
                        clearText();
                    } else
                        alertMessage.showWarningMessage(stackPane, "Warning",
                                "Can not save, Please chack your information and try again", 4, Pos.TOP_CENTER);
                } catch (Exception e) {
                    alertMessage.showErrorMessage("Send Error", "Error" + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
                }
            }

        });
        btBookLost.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                openBookLost();
            }

        });
        btCancel.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                clearTextField();
            }
        });
        btClose.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                homeController.showMainMenuHome();
            }

        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dateRent = mydate.formateDatePicker(dateRent);
        dateSend = mydate.formateDatePicker(dateSend);
        initTable();
        initEvents();
        initRules();
        price = StaticCostPrice.OutOfDateCost;
        txtPrice.setText(dcFormat.format(price));
    }

    /*
     * private JFXButton buttonOK() { // JFXButton btOk = new JFXButton("OK"); //
     * btOk.setStyle(Style.buttonDialogStyle); // btOk.setOnAction(e -> { //
     * dialog.closeDialog(); // }); // return btOk; // }
     */

    private void addButtonToTable() {
        TableColumn<RentBookModel, Void> colAction = new TableColumn<>("Action");
        colAction.setPrefWidth(100);
        Callback<TableColumn<RentBookModel, Void>, TableCell<RentBookModel, Void>> cellFactory = new Callback<TableColumn<RentBookModel, Void>, TableCell<RentBookModel, Void>>() {

            @Override
            public TableCell<RentBookModel, Void> call(TableColumn<RentBookModel, Void> param) {
                final TableCell<RentBookModel, Void> cell = new TableCell<RentBookModel, Void>() {
                    final JFXButton delete = new JFXButton("ຍົກເລີກ");
                    {
                        final ImageView imgView = new ImageView();
                        imgView.setImage(new Image("/com/mycompany/library_project/Icon/bin.png"));
                        imgView.setFitWidth(20);
                        imgView.setFitHeight(20);
                        delete.setGraphic(imgView);
                        delete.setStyle(Style.buttonStyle);
                        delete.setOnAction(e -> {
                            tableSendBooks.getItems().remove(getIndex());
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

        colAction.setCellFactory(cellFactory);
        tableSendBooks.getColumns().add(colAction);
    }

    // private JFXButton buttonOK() {
    // JFXButton btOk = new JFXButton("OK");
    // btOk.setStyle(Style.buttonDialogStyle);
    // btOk.setOnAction(e -> {
    // dialog.closeDialog();
    // });
    // return btOk;
    // }

}
