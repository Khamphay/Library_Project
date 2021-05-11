package com.mycompany.library_project.Controller;

import javafx.event.*;
import javafx.fxml.*;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;

import java.net.URL;
import java.sql.*;
import java.time.*;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog.DialogTransition;
import com.mycompany.library_project.Style;
import com.mycompany.library_project.ControllerDAOModel.*;
import com.mycompany.library_project.Model.*;

public class RentBookController implements Initializable {

    private MemberModel member = new MemberModel();
    private BookDetailModel book = new BookDetailModel();
    private RentBookModel rentBook = null;
    private ResultSet rs = null;
    private AlertMessage alertMessage = new AlertMessage();
    private DialogMessage dialog = null;
    private JFXButton[] buttons = { buttonOK() };
    private String rent_id = "", status = "";
    private int qty = 0;

    @FXML
    private StackPane stackPane;
    @FXML
    private DatePicker rentDate, sendDate;

    @FXML
    private JFXButton btAdd, btSave, btCancel;

    @FXML
    private TextField txtMemberId, txtMemberName, txtSurName, txtDep;

    @FXML
    private TextField txtBookId, txtBookName, txtCatg, txtType;

    @FXML
    private TableView<RentBookModel> tableRentBook;

    @FXML
    private TableColumn<RentBookModel, String> colId, colName, colCatg, colType;
    @FXML
    private TableColumn<RentBookModel, Date> colDateRent, colDateSend;

    @FXML
    private void ShowDate(ActionEvent event) {
    }

    private void addToRentBook() {
        try {
            if (tableRentBook.getItems().size() < 3) {
                if (rentDate.getValue() == null || sendDate.getValue() == null) {
                    alertMessage.showWarningMessage("Warning",
                            "Please chack 'rent date' and 'send date' and try again.", 4, Pos.TOP_CENTER);
                    return;
                }
                if (!txtBookId.getText().equals("") && !txtBookName.getText().equals("")
                        && !txtMemberId.getText().equals("") && !txtMemberName.getText().equals("")) {
                    int index = 0;
                    if (tableRentBook.getItems().size() > 0) {
                        for (RentBookModel row : tableRentBook.getItems()) {
                            if (row.getBarcode().equals(txtBookId.getText())) {
                                tableRentBook.getItems().remove(index);
                                break;
                            }
                            index++;
                        }
                    }
                    tableRentBook.getItems()
                            .add(new RentBookModel(txtBookId.getText(), txtBookName.getText(), txtCatg.getText(),
                                    txtType.getText(), Date.valueOf(rentDate.getValue()),
                                    Date.valueOf(sendDate.getValue())));
                } else {
                    alertMessage.showWarningMessage("Warning", "Please chack your data and try again.", 4,
                            Pos.TOP_CENTER);
                }
            } else {
                alertMessage.showWarningMessage("Warning", "Can not rent more than 3 books.", 4, Pos.TOP_CENTER);
            }

        } catch (Exception e) {
            alertMessage.showErrorMessage("Add Book Error", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
        }
    }

    private void initTable() {
        colId.setCellValueFactory(new PropertyValueFactory<>("barcode"));
        colName.setCellValueFactory(new PropertyValueFactory<>("bookName"));
        colCatg.setCellValueFactory(new PropertyValueFactory<>("catg"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colDateRent.setCellValueFactory(new PropertyValueFactory<>("rentDate"));
        colDateSend.setCellValueFactory(new PropertyValueFactory<>("sendDate"));
    }

    private void initEvents() {
        txtMemberId.setOnKeyTyped(keytype -> {
            try {
                rs = member.findById(txtMemberId.getText());
                if (rs.next()) {
                    txtMemberName.setText(rs.getString("full_name"));
                    txtSurName.setText(rs.getString("sur_name"));
                    txtDep.setText(rs.getString("dep_name"));
                    Date dateMemberCarEnd = rs.getDate("date_end");
                    if (dateMemberCarEnd.compareTo(Date.valueOf(LocalDate.now())) < 0) {
                        dialog = new DialogMessage(stackPane, "ຄຳເຕືອນ",
                                "ບັດນີ້ໝົດອາຍຸແລ້ວບໍ່ສາມາດຢືມປຶ້ມ, ກະລຸນາຕໍ່ໃໝ່.", DialogTransition.CENTER, buttons,
                                false);
                        dialog.showDialog();
                    }
                } else {
                    dialog = new DialogMessage(stackPane, "ຄຳເຕືອນ",
                            "ໍບໍ່ມີຂໍ້ມູນບັດໃນລະບົບ, ກະລຸນາກວດສອບແລ້ວລອງໃຫມ່່ອິກຄັ້ງ", DialogTransition.CENTER, buttons,
                            false);
                    dialog.showDialog();
                }
            } catch (Exception e) {
                alertMessage.showErrorMessage("Load Member Error", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
            }
        });

        txtBookId.setOnKeyPressed(keyEnter -> {
            if (keyEnter.getCode() == KeyCode.ENTER) {
                if (status.equals("ຫວ່າງ")) {
                    addToRentBook();
                }
            } else {

            }
        });

        txtBookId.setOnKeyTyped(keytype -> {
            try {
                rs = book.findBookByBarcode(txtBookId.getText());
                if (rs.next()) {
                    txtBookName.setText(rs.getString("book_name"));
                    txtCatg.setText(rs.getString("catg_name"));
                    txtType.setText(rs.getString("type_name"));
                    status = rs.getString("status");
                } else {
                    dialog = new DialogMessage(stackPane, "ຄຳເຕືອນ",
                            "ບໍ່ມີຂໍ້ມູນບັດໃນລະບົບ, ກະລຸນາກວດສອບແລ້ວລອງໃຫມ່່ອິກຄັ້ງ", DialogTransition.CENTER, buttons,
                            false);
                    dialog.showDialog();
                }
            } catch (Exception e) {
                alertMessage.showErrorMessage("Load Member Error", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
            }
        });

        rentDate.setOnAction(e -> cancalarDate());

        btAdd.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                addToRentBook();
            }

        });

        btSave.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                try {
                    rentBook = new RentBookModel(rent_id, txtMemberId.getText(), txtBookId.getText(), qty,
                            Date.valueOf(rentDate.getValue()), Date.valueOf(sendDate.getValue()));
                    if (rentBook.saveData() > 0) {
                        for (RentBookModel row : tableRentBook.getItems()) {
                            rentBook.saveRentBook("", row.getBarcode(), "ກຳລັງຢືມ");
                            if (rentBook.saveRentBook(rent_id, row.getBarcode(), row.getStatus()) > 0) {

                            }
                        }
                    }
                } catch (Exception e) {
                    alertMessage.showErrorMessage("Rent Book Error", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
                }
            }
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initEvents();
        initTable();
        addButtonToTable();
        // cancalarDate();
        rentDate.setValue(LocalDate.now());
    }

    private JFXButton buttonOK() {
        JFXButton btOk = new JFXButton("OK");
        btOk.setStyle(Style.buttonDialogStyle);
        btOk.setOnAction(e -> {
            dialog.closeDialog();
        });
        return btOk;
    }

    private void addButtonToTable() {
        TableColumn<RentBookModel, Void> colAction = new TableColumn<>("Action");
        Callback<TableColumn<RentBookModel, Void>, TableCell<RentBookModel, Void>> cellFactory = new Callback<TableColumn<RentBookModel, Void>, TableCell<RentBookModel, Void>>() {

            @Override
            public TableCell<RentBookModel, Void> call(TableColumn<RentBookModel, Void> param) {
                final TableCell<RentBookModel, Void> cell = new TableCell<RentBookModel, Void>() {
                    final JFXButton delete = new JFXButton("ຍົກເລີກ");
                    {
                        final Image img = new Image("/com/mycompany/library_project/Icon/bin.png");
                        final ImageView imgView = new ImageView();
                        imgView.setImage(img);
                        imgView.setFitWidth(20);
                        imgView.setFitHeight(20);
                        delete.setGraphic(imgView);
                        delete.setStyle(Style.buttonStyle);
                        delete.setOnAction(e -> {
                            tableRentBook.getItems().remove(getIndex());
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
        tableRentBook.getColumns().add(colAction);
    }

    private void cancalarDate() {
        LocalDate dateRent = rentDate.getValue();
        DayOfWeek days = dateRent.getDayOfWeek();
        Locale locale = Locale.getDefault();
        LocalDate dateSend = dateRent;

        if (!days.getDisplayName(TextStyle.FULL, locale).equals("Sunday")
                && !days.getDisplayName(TextStyle.FULL, locale).equals("Saturday")) {
            for (int i = 1; i <= 5; i++) {
                if (days.getDisplayName(TextStyle.FULL, locale).equals("Sunday")
                        || days.getDisplayName(TextStyle.FULL, locale).equals("Saturday")) {
                    days = days.plus(1);
                    dateSend = dateSend.plusDays(1);
                }
                days = days.plus(1);
                dateSend = dateSend.plusDays(1);
            }
            sendDate.setValue(dateSend);
        } else {
            alertMessage.showWarningMessage("Warning", "Please Select only 'Monday' to 'Friday'.", 4, Pos.TOP_CENTER);
            rentDate.setValue(LocalDate.now());
        }
    }

    private JFXButton btDelete(String id) {
        JFXButton delete = new JFXButton("ຍົກເລີກ");
        final Image img = new Image("/com/mycompany/library_project/Icon/bin.png");
        final ImageView imgView = new ImageView();
        imgView.setImage(img);
        imgView.setFitWidth(20);
        imgView.setFitHeight(20);
        delete.setId(id);
        delete.setGraphic(imgView);
        delete.setStyle(Style.buttonStyle);
        delete.setOnAction(e -> {
        });
        return delete;
    }
}
