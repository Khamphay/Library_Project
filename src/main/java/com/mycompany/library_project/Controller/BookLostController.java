package com.mycompany.library_project.Controller;

import java.net.URL;
import java.sql.Date;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.jfoenix.controls.*;
import com.jfoenix.controls.JFXDialog.DialogTransition;
import com.mycompany.library_project.Style;
import com.mycompany.library_project.ControllerDAOModel.*;
import com.mycompany.library_project.Model.*;

import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

public class BookLostController implements Initializable {

    private BookDetailModel bookDetail = new BookDetailModel();
    private BookLostModel booklostModel = new BookLostModel();
    private RentBookModel rentBook = new RentBookModel();
    private ObservableList<BookLostModel> data = null;
    private ValidationSupport validRules = new ValidationSupport();
    private AlertMessage alertMessage = new AlertMessage();
    private DialogMessage dialog = null;
    private MyDate mydate = new MyDate();
    private ArrayList<PriceList> priceList = null;;
    private DecimalFormat dcFormat = new DecimalFormat("#,##0.00 ກີບ");
    private ResultSet rs = null;
    private JFXButton[] buttons = { buttonOK() };
    double outPrice = 1000.0, lostPrice = 1000.0, sumOutPrice = 0.0, sumLostPrice = 0.0, totalPrice = 0.0;
    String rent_id = "", memberId = "";

    // Todo: Call by ManageBookController Form
    public void initConstructor(ManageBookController manageBookController) {
        btClose.setOnAction((EventHandler<ActionEvent>) new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                manageBookController.showMainMenuBooks();
            }
        });
    }

    @FXML
    private StackPane stackPane;

    @FXML
    private TextField txtMemberId, txtFName, txtLname, txtTel, txtDep, txtOutPrice, txtSumOutPrice, txtLostPrice,
            txtQty, txtSumLostPrice, txtTotalPrice;

    @FXML
    private JFXButton btSave, btCancel, btClose;

    @FXML
    private TableView<BookLostModel> tableLost;

    @FXML
    private TableColumn<BookLostModel, String> colBookId, colBarcode, colBookName, colPage, colCatg, colType, colTable,
            colTableLog, colOutDate, colPrice;
    @FXML
    private TableColumn<BookLostModel, Date> colDateRent, colDateSend;

    private void clearText() {
        validRules.setErrorDecorationEnabled(false);
        txtMemberId.clear();
        txtFName.clear();
        txtLname.clear();
        txtTel.clear();
        txtDep.clear();
        txtOutPrice.setText(dcFormat.format(outPrice));
        txtSumOutPrice.setText(dcFormat.format(0));
        txtLostPrice.setText(dcFormat.format(lostPrice));
        txtQty.setText("0 ຫົວ");
        txtSumLostPrice.setText(dcFormat.format(0));
        txtTotalPrice.setText(dcFormat.format(0));
        rent_id = "";
        memberId = "";

        tableLost.getItems().clear();
    }

    private void initRules() {
        validRules.setErrorDecorationEnabled(false);
        validRules.registerValidator(txtMemberId, false, Validator.createEmptyValidator("ກະລຸນາປ້ອນລະຫັດສະມາຊິດ"));
        validRules.registerValidator(txtFName, false, Validator.createEmptyValidator("ກະລຸນາປ້ອນຊື່ສະມາຊິດ"));
        validRules.registerValidator(txtLname, false, Validator.createEmptyValidator("ກະລຸນາປ້ອນນາມສະກຸນ"));
        validRules.registerValidator(txtTel, false, Validator.createEmptyValidator("ກະລຸນາປ້ອນເບີໂທລະສັບ"));
        validRules.registerValidator(txtDep, false, Validator.createEmptyValidator("ກະລຸນາປ້ອນຊື່ພາກວີຊາ"));
    }

    private void initTable() {
        colBookId.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        colBarcode.setCellValueFactory(new PropertyValueFactory<>("barcode"));
        colBookName.setCellValueFactory(new PropertyValueFactory<>("bookName"));
        colPage.setCellValueFactory(new PropertyValueFactory<>("page"));
        colCatg.setCellValueFactory(new PropertyValueFactory<>("catg"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colTable.setCellValueFactory(new PropertyValueFactory<>("table"));
        colTableLog.setCellValueFactory(new PropertyValueFactory<>("tableLog"));
        colDateRent.setCellValueFactory(new PropertyValueFactory<>("rentDate"));
        colDateSend.setCellValueFactory(new PropertyValueFactory<>("sendDate"));
        colOutDate.setCellValueFactory(new PropertyValueFactory<>("outDate"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("pricePerBook"));

        // Todo: Add column number
        final TableColumn<BookLostModel, BookLostModel> colNumber = new TableColumn<BookLostModel, BookLostModel>(
                "ລຳດັບ");
        colNumber.setMinWidth(50);
        colNumber.setMaxWidth(120);
        colNumber.setPrefWidth(60);
        colNumber.setCellValueFactory(
                new Callback<CellDataFeatures<BookLostModel, BookLostModel>, ObservableValue<BookLostModel>>() {

                    @Override
                    public ObservableValue<BookLostModel> call(CellDataFeatures<BookLostModel, BookLostModel> param) {
                        return new ReadOnlyObjectWrapper<BookLostModel>(param.getValue());
                    }
                });

        colNumber.setCellFactory(
                new Callback<TableColumn<BookLostModel, BookLostModel>, TableCell<BookLostModel, BookLostModel>>() {

                    @Override
                    public TableCell<BookLostModel, BookLostModel> call(
                            TableColumn<BookLostModel, BookLostModel> param) {
                        return new TableCell<BookLostModel, BookLostModel>() {
                            @Override
                            protected void updateItem(BookLostModel item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty)
                                    setText("");
                                else if (this.getTableRow() != null && item != null)
                                    setText(Integer.toString(this.getTableRow().getIndex() + 1));
                            }
                        };
                    }

                });
        colNumber.setSortable(true);
        tableLost.getColumns().add(0, colNumber);

        // Todo: Add column Button
        addButtonToTable();
    }

    private void initEvents() {
        txtMemberId.setOnKeyPressed(key -> {
            if (key.getCode() == KeyCode.ENTER) {
                try {
                    if (!txtMemberId.getText().equals("")) {

                        sumOutPrice = 0.0;
                        sumLostPrice = 0.0;
                        totalPrice = 0.0;

                        rs = booklostModel.findRentBookByMemderID(txtMemberId.getText(), "ກຳລັງຢືມ");
                        data = FXCollections.observableArrayList();
                        priceList = new ArrayList<>();
                        while (rs.next()) {
                            rent_id = rs.getString("rent_id");
                            memberId = rs.getString("member_id");
                            txtFName.setText(rs.getString("full_name"));
                            txtLname.setText(rs.getString("sur_name"));
                            txtTel.setText(rs.getString("tel"));
                            txtDep.setText(rs.getString("dep_name"));

                            int outdate = mydate.cancalarDate(rs.getDate("date_send").toLocalDate());
                            int page = rs.getInt("page");

                            sumOutPrice += (outPrice * outdate);
                            sumLostPrice += (page * lostPrice);

                            priceList.add(new PriceList(outdate, page, (outPrice * outdate) + (page * lostPrice)));

                            data.add(new BookLostModel(rs.getString("book_id"), rs.getString("barcode"),
                                    rs.getString("book_name"), (rs.getInt("page") + " ໜ້າ"), rs.getString("catg_name"),
                                    rs.getString("type_name"), rs.getString("tableid"), rs.getString("table_log_id"),
                                    rs.getDate("date_rent"), rs.getDate("date_send"), outdate + " ມື້",
                                    dcFormat.format((outPrice * outdate) + (page * lostPrice))));
                        }

                        if (data.isEmpty()) {
                            if (dialog != null) {
                                dialog.closeDialog();
                            }
                            dialog = new DialogMessage(stackPane, "ຄຳເຕືອນ", "ບໍ່ພົບຂໍ້ມູນການຢືມປຶ້ມ",
                                    DialogTransition.CENTER, buttons, false);
                            dialog.showDialog();
                            return;
                        }

                        txtSumOutPrice.setText(dcFormat.format(sumOutPrice));
                        txtSumLostPrice.setText(dcFormat.format(sumLostPrice));
                        totalPrice = sumOutPrice + sumLostPrice;
                        txtTotalPrice.setText(dcFormat.format(totalPrice));
                        tableLost.setItems(data);

                        txtQty.setText(tableLost.getItems().size() + " ຫົວ");
                    } else {
                        validRules.setErrorDecorationEnabled(true);
                        alertMessage.showWarningMessage(stackPane, "Edit Warning",
                                "Please chack your information and try again.", 4, Pos.BOTTOM_RIGHT);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        btSave.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                String message = null;
                try {
                    booklostModel = new BookLostModel(txtMemberId.getText(), tableLost.getItems().size(), totalPrice,
                            Date.valueOf(LocalDate.now()));
                    int id = booklostModel.saveData();
                    System.out.println("Before:=========== " + id);
                    if (id > 0) {
                        int index = 0;
                        for (BookLostModel item : tableLost.getItems()) {
                            System.out.println("After:=========== " + tableLost.getItems().size());
                            try {
                                if (booklostModel.saveLostDetail(id, item.getBarcode(),
                                        priceList.get(index).getPricePerBook()) > 0)
                                    if (bookDetail.updateStatus(item.getBarcode(), "ເສຍ") > 0)
                                        if (rentBook.sendBook(rent_id, item.getBarcode(), "ສົ່ງແລ້ວ") > 0)
                                            message = "Save Successfully!.";

                            } catch (Exception e) {
                                e.printStackTrace();
                                alertMessage.showErrorMessage("Save Error", "Error: " + e.getMessage(), 4,
                                        Pos.BOTTOM_RIGHT);
                                message = null;
                                return;
                            }
                            index++;
                        }
                    }
                    if (message != null) {
                        alertMessage.showCompletedMessage("Saved", message, 4, Pos.BOTTOM_RIGHT);
                        clearText();
                    } else {
                        alertMessage.showWarningMessage("Save Warning", "Cannot save data", 4, Pos.BOTTOM_RIGHT);
                    }
                } catch (Exception e) {
                    alertMessage.showErrorMessage("Save Error", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
                    e.printStackTrace();
                }
            }
        });

        btCancel.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                clearText();
            }
        });

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTable();
        initEvents();
        initRules();
        txtOutPrice.setText(dcFormat.format(outPrice));
        txtSumOutPrice.setText(dcFormat.format(0));
        txtLostPrice.setText(dcFormat.format(lostPrice));
        txtQty.setText("0 ຫົວ");
        txtSumLostPrice.setText(dcFormat.format(0));
        txtTotalPrice.setText(dcFormat.format(0));
    }

    private void addButtonToTable() {
        TableColumn<BookLostModel, Void> colAction = new TableColumn<>("Action");
        colAction.setPrefWidth(100);
        Callback<TableColumn<BookLostModel, Void>, TableCell<BookLostModel, Void>> cellFactory = new Callback<TableColumn<BookLostModel, Void>, TableCell<BookLostModel, Void>>() {

            @Override
            public TableCell<BookLostModel, Void> call(TableColumn<BookLostModel, Void> param) {
                final TableCell<BookLostModel, Void> cell = new TableCell<BookLostModel, Void>() {
                    final JFXButton delete = new JFXButton("ຍົກເລີກ");
                    {
                        final ImageView imgView = new ImageView();
                        imgView.setImage(new Image("/com/mycompany/library_project/Icon/bin.png"));
                        imgView.setFitWidth(20);
                        imgView.setFitHeight(20);
                        delete.setGraphic(imgView);
                        delete.setStyle(Style.buttonStyle);
                        delete.setOnAction(e -> {
                            tableLost.getItems().remove(getIndex());
                            priceList.remove(getIndex());

                            sumOutPrice = 0.0;
                            sumLostPrice = 0.0;
                            totalPrice = 0.0;

                            priceList.forEach(item -> {
                                sumOutPrice += (outPrice * item.getOutDate());
                                sumLostPrice += (lostPrice * item.getPage());
                            });

                            txtSumOutPrice.setText(dcFormat.format(sumOutPrice));
                            txtSumLostPrice.setText(dcFormat.format(sumLostPrice));
                            totalPrice = sumOutPrice + sumLostPrice;
                            txtTotalPrice.setText(dcFormat.format(totalPrice));
                            txtQty.setText(tableLost.getItems().size() + " ຫົວ");
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
        tableLost.getColumns().add(colAction);
    }

    private JFXButton buttonOK() {
        JFXButton btOk = new JFXButton("OK");
        btOk.setStyle(Style.buttonDialogStyle);
        btOk.setOnAction(e -> {
            dialog.closeDialog();
        });
        return btOk;
    }

    class PriceList {
        int outDate;
        int page;
        double pricePerBook;

        public PriceList(int outDate, int page, double pricePerBook) {
            this.outDate = outDate;
            this.page = page;
            this.pricePerBook = pricePerBook;
        }

        public int getOutDate() {
            return outDate;
        }

        public void setOutDate(int outDate) {
            this.outDate = outDate;
        }

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public double getPricePerBook() {
            return pricePerBook;
        }

        public void setPricePerBook(double pricePerBook) {
            this.pricePerBook = pricePerBook;
        }
    }

}
