package com.mycompany.library_project.Controller;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
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

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.mycompany.library_project.Style;
import com.mycompany.library_project.ControllerDAOModel.*;
import com.mycompany.library_project.Model.*;

public class SendBookController implements Initializable {

    private HomeController homeController = null;
    private RentBookModel sendBook = new RentBookModel();
    private AlertMessage alertMessage = new AlertMessage();
    private BookDetailModel book = new BookDetailModel();
    private MyDate mydate = new MyDate();
    private ResultSet rs = null;
    // private DialogMessage dialog = null;
    // private JFXButton[] buttons = { buttonOK() };
    String rent_id = "";

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
    private TableColumn<RentBookModel, String> colBarcode, colBookName, colCatg, colType, colMemberName;

    @FXML
    private TableColumn<RentBookModel, Date> colDateSend, colDateRent;

    private void clearText() {
        txtBarcode.clear();
        txtBookName.clear();
        txtCatg.clear();
        txtType.clear();
        txtMemberName.clear();
        txtAllPrice.clear();
        txtPrice.clear();
        txtOutDate.clear();
        rent_id = "";
    }

    private void initTable() {
        colBarcode.setCellValueFactory(new PropertyValueFactory<>("barcode"));
        colBookName.setCellValueFactory(new PropertyValueFactory<>("bookName"));
        colCatg.setCellValueFactory(new PropertyValueFactory<>("catg"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colMemberName.setCellValueFactory(new PropertyValueFactory<>("member"));
        colDateRent.setCellValueFactory(new PropertyValueFactory<>("rentDate"));
        colDateSend.setCellValueFactory(new PropertyValueFactory<>("sendDate"));

        // Todo: Add column number
        final TableColumn<RentBookModel, RentBookModel> colNumber = new TableColumn<RentBookModel, RentBookModel>();
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
                    if (txtBarcode.getText().toString() != "") {

                        rs = sendBook.getSendBook(txtBarcode.getText(), "ກຳລັງຢືມ");
                        if (rs.next()) {
                            rent_id = rs.getString("rent_id");
                            txtBookName.setText(rs.getString("book_name"));
                            txtCatg.setText(rs.getString("catg_name"));
                            txtType.setText(rs.getString("type_name"));
                            txtMemberName.setText(rs.getString("full_name") + " " + rs.getString("sur_name"));
                            dateRent.setValue(rs.getDate("date_rent").toLocalDate());
                            dateSend.setValue(rs.getDate("date_send").toLocalDate());

                            // Todo: Cancalar Date
                            int outdate = mydate.cancalarDate(rs.getDate("date_send").toLocalDate());
                            txtOutDate.setText(Integer.toString(outdate) + " ມື້");
                            txtPrice.setText("0 ກີບ");
                            txtAllPrice.setText("0 ກີບ");
                            tableSendBooks.getItems()
                                    .add(new RentBookModel(txtBarcode.getText(), txtBookName.getText(),
                                            txtCatg.getText(), txtType.getText(), txtMemberName.getText(),
                                            Date.valueOf(dateRent.getValue()), Date.valueOf(dateSend.getValue())));
                        }
                    } else {
                        alertMessage.showWarningMessage(stackPane, "Warning",
                                "Please chack your information and try again", 4, Pos.TOP_CENTER);
                    }

                } catch (Exception e) {
                    alertMessage.showErrorMessage("Load Error", "Error" + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
                }

            }
        });

        btSave.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                String result = null;
                for (RentBookModel row : tableSendBooks.getItems()) {
                    try {
                        if (sendBook.sendBook(rent_id, row.getBarcode(), "ສົ່ງແລ້ວ") > 0) {
                            result = ((book.updateStatus(row.getBarcode(), "ຫວ່າງ") > 0) ? "Send books successfully"
                                    : null);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        alertMessage.showErrorMessage("Send Error", "Error" + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
                        result = null;
                        return;
                    }
                }
                if (result != null) {
                    alertMessage.showCompletedMessage(stackPane, "Send", result, 4, Pos.TOP_CENTER);
                    tableSendBooks.getItems().clear();
                    clearText();
                } else {
                    alertMessage.showWarningMessage(stackPane, "Warning",
                            "Can not save, Please chack your information and try again", 4, Pos.TOP_CENTER);
                }
            }

        });

        btCancel.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                clearText();
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
    }

    // private JFXButton buttonOK() {
    // JFXButton btOk = new JFXButton("OK");
    // btOk.setStyle(Style.buttonDialogStyle);
    // btOk.setOnAction(e -> {
    // dialog.closeDialog();
    // });
    // return btOk;
    // }

    private void addButtonToTable() {
        TableColumn<RentBookModel, Void> colAction = new TableColumn<>("Action");
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
