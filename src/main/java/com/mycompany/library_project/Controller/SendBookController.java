package com.mycompany.library_project.Controller;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.event.*;
import javafx.fxml.*;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.*;
import javafx.util.Callback;

import java.net.URL;
import java.sql.*;
import java.text.DecimalFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.mycompany.library_project.App;
import com.mycompany.library_project.MyConnection;
import com.mycompany.library_project.Style;
import com.mycompany.library_project.ControllerDAOModel.*;
import com.mycompany.library_project.Model.*;

import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

public class SendBookController implements Initializable {

    private Connection con = MyConnection.getConnect();
    private ValidationSupport validRules = new ValidationSupport();
    private DecimalFormat dcFormat = new DecimalFormat("#,##0.00 ກີບ");
    private HomeController homeController = null;
    private RentBookModel sendBook = new RentBookModel(con);
    private AlertMessage alertMessage = new AlertMessage();
    private DialogMessage dialog = new DialogMessage();
    private BookDetailModel book = new BookDetailModel(con);
    private MyDate mydate = new MyDate();
    private ResultSet rs = null;
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
    public TableView<RentBookModel> tableSendBooks;

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
            final Scene scene = new Scene(subForm);
            scene.setFill(Color.TRANSPARENT);
            final Stage stage = new Stage();
            stage.initStyle(StageStyle.TRANSPARENT);
            booklostController.initConstructor(this, stage);
            stage.setTitle("Ajustment");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();

        } catch (Exception e) {
            dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການເປີດຟອມ", e);
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
        dateRent.setDayCellFactory(new Callback<DatePicker, DateCell>() {

            @Override
            public DateCell call(DatePicker param) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        DayOfWeek day = DayOfWeek.from(item);
                        if (day == DayOfWeek.SUNDAY || day == DayOfWeek.SATURDAY)
                            this.setTextFill(Color.RED);
                    }
                };
            }

        });
        dateSend.setDayCellFactory(new Callback<DatePicker, DateCell>() {

            @Override
            public DateCell call(DatePicker param) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        DayOfWeek day = DayOfWeek.from(item);
                        if (day == DayOfWeek.SUNDAY || day == DayOfWeek.SATURDAY)
                            this.setTextFill(Color.RED);
                    }
                };
            }

        });
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
                        alertMessage.showWarningMessage("Warning", "Please chack your information and try again", 4,
                                Pos.TOP_CENTER);
                        clearTextField();
                    }

                } catch (Exception e) {
                    dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການກວດສອບຂໍ້ມູນ", e);
                }

            }
        });
        btSave.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                if (allPrice > 0 && qtyOutOfDate > 0)
                    showPay(rent_id, qtyOutOfDate, allPrice);
                else if (sendBook() > 0) {
                    alertMessage.showCompletedMessage("Saved", "Send Book successfully", 4, Pos.BOTTOM_RIGHT);
                } else {
                    alertMessage.showWarningMessage("Saved", "Can not send book", 4, Pos.BOTTOM_RIGHT);
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

    private void showPay(String rentid, int outofdate, double pricePay) {
        try {
            final FXMLLoader loader = new FXMLLoader(App.class.getResource("frmPay.fxml"));
            final Parent root = loader.load();
            final Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            final Stage stage = new Stage(StageStyle.TRANSPARENT);
            stage.setScene(scene);
            final PayController pay = loader.getController();
            pay.initSendConstutor(this, stage, rentid, outofdate, pricePay);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();

        } catch (Exception e) {
            dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການເປີດຟອມຈ່າຍຄ່າປັບໃຫມ", e);
        }
    }

    public int sendBook() {
        try {
            int result = 0;
            for (RentBookModel row : tableSendBooks.getItems()) {
                if (sendBook.sendBook(row.getRentId(), row.getBarcode(), "ສົ່ງແລ້ວ") > 0) {
                    result = book.updateStatus(row.getBarcode(), "ຫວ່າງ");
                }
            }
            clearText();
            return result;
        } catch (Exception e) {
            dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການບັນທຶກຂໍ້ມູນ", e);
            return 0;
        }
    }

    public void refreshRentOutOfDate() {
        homeController.showRentBookOutOfDate();
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
}
