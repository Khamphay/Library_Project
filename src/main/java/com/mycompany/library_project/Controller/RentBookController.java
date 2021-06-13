package com.mycompany.library_project.Controller;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.event.*;
import javafx.fxml.*;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;

import java.net.URL;
import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog.DialogTransition;
import com.mycompany.library_project.Style;
import com.mycompany.library_project.ControllerDAOModel.*;
import com.mycompany.library_project.Model.*;

import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

public class RentBookController implements Initializable {

    private ValidationSupport validRules = new ValidationSupport();
    private ValidationSupport dateRules = new ValidationSupport();
    private HomeController homeController = null;
    private MemberModel member = new MemberModel();
    private BookDetailModel book = new BookDetailModel();
    private RentBookModel rentBook = new RentBookModel();
    private MyDate formatDate = new MyDate();
    private ResultSet rs = null;
    private AlertMessage alertMessage = new AlertMessage();
    private DialogMessage dialog = null;
    private JFXButton[] buttons = { buttonOK() };
    private String rent_id = "", status = "", page = "", table = "", tableLog = "";
    private int qty_can_rent = 0;

    public void initConstructor(HomeController homeController) {
        this.homeController = homeController;
    }

    @FXML
    private BorderPane borderPane;

    @FXML
    private StackPane stackPane;

    @FXML
    private DatePicker rentDate, sendDate;

    @FXML
    private JFXButton btAdd, btSave, btCancel, btClose;

    @FXML
    private TextField txtMemberId, txtMemberName, txtSurName, txtDep;

    @FXML
    private TextField txtBookId, txtBookName, txtCatg, txtType;

    @FXML
    private TableView<RentBookModel> tableRentBook;

    @FXML
    private TableColumn<RentBookModel, String> colId, colName, colPage, colTable, colTableLog, colCatg, colType;
    @FXML
    private TableColumn<RentBookModel, Date> colDateRent, colDateSend;

    private void initRules() {
        validRules.setErrorDecorationEnabled(false);
        validRules.registerValidator(txtMemberId, false, Validator.createEmptyValidator("ກະລຸນາປ້ອນລະຫັດສະມາຊິດ"));
        validRules.registerValidator(txtMemberName, false, Validator.createEmptyValidator("ກະລຸນາປ້ອນຊື່ສະມາຊິດ"));
        validRules.registerValidator(txtSurName, false, Validator.createEmptyValidator("ກະລຸນາປ້ອນນາມສະກຸນ"));
        validRules.registerValidator(txtDep, false, Validator.createEmptyValidator("ກະລຸນາປ້ອນຊື່ພາກວີຊາ"));
        validRules.registerValidator(txtBookId, false, Validator.createEmptyValidator("ກະລຸນາລະຫັດ Barcode"));
        validRules.registerValidator(txtBookName, false, Validator.createEmptyValidator("ກະລຸນາປ້ອນຊື່ປຶ້ມ"));
        validRules.registerValidator(txtCatg, false, Validator.createEmptyValidator("ກະລຸນາປ້ອນໝວດປຶ້ມ"));
        validRules.registerValidator(txtType, false, Validator.createEmptyValidator("ກະລຸນາປ້ອນປະເພດປຶ້ມ"));
        validRules.registerValidator(rentDate, false, Validator.createEmptyValidator("ກະລຸນາປ້ອນວັນທີຢືມປຶ້ມ"));
        validRules.registerValidator(sendDate, false, Validator.createEmptyValidator("ກະລຸນາປ້ອນວັນທີສົ່ງປຶ້ມ"));

        dateRules.registerValidator(rentDate, false, (Control c, LocalDate newValue) -> ValidationResult
                .fromWarningIf(c, "ວັນທີຢືມປຶ້ມຄວນທີ່ຈະເປັນວັນທີປັດຈຸບັນ", !LocalDate.now().equals(newValue)));
    }

    private void clearText() {
        validRules.setErrorDecorationEnabled(false);
        txtMemberId.clear();
        txtMemberName.clear();
        txtSurName.clear();
        txtDep.clear();
        txtBookId.clear();
        txtBookName.clear();
        txtCatg.clear();
        txtType.clear();

        txtMemberId.requestFocus();
    }

    private void addToRentBook() {
        if (!txtMemberId.getText().equals("") && !txtMemberName.getText().equals("") && !txtSurName.getText().equals("")
                && !txtBookId.getText().equals("") && !txtBookName.getText().equals("") && !txtCatg.getText().equals("")
                && !txtType.getText().equals("")) {
            try {
                if (rentDate.getValue() == null || sendDate.getValue() == null) {
                    if (dialog != null)
                        dialog.closeDialog();
                    dialog = new DialogMessage(stackPane, "ຄຳເຕືອນ", "ປື້ມຫົວນີ້ເສຍແລ້ວ, ດັັ່ງນັ້ນບໍ່ສາມາດຢືມໄດ້",
                            DialogTransition.CENTER, buttons, false);
                    dialog.showDialog();
                    return;
                }

                if (status.equals("ກຳລັງຢືມ")) {
                    if (dialog != null)
                        dialog.closeDialog();
                    dialog = new DialogMessage(stackPane, "ຄຳເຕືອນ", "ປື້ມຫົວນີ້ຖຶກຢືມໄປແລ້ວ, ດັັ່ງນັ້ນບໍ່ສາມາດຢືມໄດ້",
                            DialogTransition.CENTER, buttons, false);
                    dialog.showDialog();
                    return;
                } else if (status.equals("ເສຍ")) {
                    if (dialog != null)
                        dialog.closeDialog();
                    dialog = new DialogMessage(stackPane, "ຄຳເຕືອນ", "ປື້ມຫົວນີ້ເສຍແລ້ວ, ດັັ່ງນັ້ນບໍ່ສາມາດຢືມໄດ້",
                            DialogTransition.CENTER, buttons, false);
                    dialog.showDialog();
                    return;
                } else if (status.equals("ຫວ່າງ")) {
                    if (tableRentBook.getItems().size() < qty_can_rent) {

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
                                .add(new RentBookModel(txtBookId.getText(), txtBookName.getText(), page,
                                        txtCatg.getText(), txtType.getText(), table, tableLog,
                                        Date.valueOf(rentDate.getValue()), Date.valueOf(sendDate.getValue())));
                        txtBookId.clear();
                        txtBookName.clear();
                        txtCatg.clear();
                        txtType.clear();
                        page = "";
                        table = "";
                        tableLog = "";

                    } else {
                        alertMessage.showWarningMessage("Warning", "Can not rent more than " + qty_can_rent + " books.",
                                4, Pos.TOP_CENTER);
                    }

                } else {
                    if (dialog != null) {
                        dialog.closeDialog();
                    }
                    dialog = new DialogMessage(stackPane, "ຄຳເຕືອນ", "ປື້ມຫົວນີ້ບໍ່ອານຸຍາດໃຫ້ໄດ້",
                            DialogTransition.CENTER, buttons, false);
                    dialog.showDialog();

                }

            } catch (Exception e) {
                alertMessage.showErrorMessage("Add Book Error", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
                e.printStackTrace();
            }
        } else {
            validRules.setErrorDecorationEnabled(true);
            alertMessage.showWarningMessage(borderPane, "Warning", "Please chack your information and try again.", 4,
                    Pos.TOP_CENTER);
        }
    }

    private void initTable() {
        colId.setCellValueFactory(new PropertyValueFactory<>("barcode"));
        colName.setCellValueFactory(new PropertyValueFactory<>("bookName"));
        colPage.setCellValueFactory(new PropertyValueFactory<>("page"));
        colCatg.setCellValueFactory(new PropertyValueFactory<>("catg"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colTable.setCellValueFactory(new PropertyValueFactory<>("table"));
        colTableLog.setCellValueFactory(new PropertyValueFactory<>("tableLog"));
        colDateRent.setCellValueFactory(new PropertyValueFactory<>("rentDate"));
        colDateSend.setCellValueFactory(new PropertyValueFactory<>("sendDate"));

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

                            }
                        };
                    }

                });
        colNumber.setSortable(true);
        tableRentBook.getColumns().add(0, colNumber);

        // Todo: Add column Button
        addButtonToTable();
    }

    private void initEvents() {
        txtMemberId.setOnKeyPressed(keytype -> {
            if (keytype.getCode() == KeyCode.ENTER) {
                try {
                    rs = rentBook.chackMemberRentBook(txtMemberId.getText(), "ຍັງບໍ່ໄດ້ສົ່ງ");
                    if (rs.next() && rs.getDate("date_send") != null) {
                        // TODO: exite end of send
                        if (Date.valueOf(LocalDate.now()).compareTo(rs.getDate("date_send")) > 0) {
                            if (dialog != null) {
                                dialog.closeDialog();
                            }
                            dialog = new DialogMessage(stackPane, "ຄຳເຕືອນ",
                                    "ບັດນີ້ບໍ່ສາມາດຢືມປຶ້ມໄດ້ເນື່ອງຈາກຍັງມີປຶ້ມທີ່ຢືມກ່າຍກຳນົດແຕ່ບໍ່ໄດ້ສົ່ງ.",
                                    DialogTransition.CENTER, buttons, false);
                            dialog.showDialog();
                            return;
                        }

                        if (rs.getInt("count_book") >= 3) {
                            dialog = new DialogMessage(stackPane, "ຄຳເຕືອນ",
                                    "ບໍ່ສາມາດຢືມໄດ້ເນື່ອງຈາກບັດນີ້ໃຊ້ຢືມປຶ້ມຄົບຕາມຈຳນວນທີ່ກຳນົດແລ້ວ, ຖ້າຫາກຕ້ອງການຢືມໃຫມ່ຕ້ອງໄດ້ສົ່ງປຶ້ມທີ່ໄດ້ຢືມກ່ອນໜ້າ.",
                                    DialogTransition.CENTER, buttons, false);
                            dialog.showDialog();
                            return; // Todo: exit this 'method' or 'event'
                        } else if (rs.getInt("count_book") > 0 && rs.getInt("count_book") <= 2) {
                            qty_can_rent = 3 - rs.getInt("count_book");
                            if (dialog != null) {
                                dialog.closeDialog();
                            }
                            dialog = new DialogMessage(stackPane, "ຄຳເຕືອນ",
                                    "ບັດນີ້ສາມາດຢືມປຶ້ມໄດ້ຫຼາຍສຸດ " + qty_can_rent
                                            + " ຫົວເທົ່ານັ້ນ ເນື່ອງຈາກບັດນີ້ໃຊ້ຢືມປຶ້ມໄປແລ້ວ " + rs.getInt("count_book")
                                            + " ຫົວ.",
                                    DialogTransition.CENTER, buttons, false);
                            dialog.showDialog();
                        }
                    } else {
                        qty_can_rent = 3;
                    }

                    rs = member.findById(txtMemberId.getText());
                    if (rs.next()) {

                        Date dateMemberCarEnd = rs.getDate("date_end");
                        if (dateMemberCarEnd.compareTo(Date.valueOf(LocalDate.now())) < 0) {
                            if (dialog != null) {
                                dialog.closeDialog();
                            }
                            dialog = new DialogMessage(stackPane, "ຄຳເຕືອນ",
                                    "ບັດນີ້ໝົດອາຍຸແລ້ວບໍ່ສາມາດຢືມປຶ້ມໄດ້, ກະລຸນາຕໍ່ໃໝ່.", DialogTransition.CENTER,
                                    buttons, false);
                            dialog.showDialog();
                            return;
                        } else {
                            txtMemberId.setText(rs.getString("member_id"));
                            txtMemberName.setText(rs.getString("full_name"));
                            txtSurName.setText(rs.getString("sur_name"));
                            txtDep.setText(rs.getString("dep_name"));
                            txtBookId.requestFocus();
                        }
                    } else {
                        if (dialog != null) {
                            dialog.closeDialog();
                        }
                        dialog = new DialogMessage(stackPane, "ຄຳເຕືອນ",
                                "ໍບໍ່ມີຂໍ້ມູນບັດໃນລະບົບ, ກະລຸນາກວດສອບແລ້ວລອງໃຫມ່່ອິກຄັ້ງ", DialogTransition.CENTER,
                                buttons, false);
                        dialog.showDialog();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    alertMessage.showErrorMessage("Load Member Error", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
                }
            }
        });

        txtBookId.setOnKeyTyped(keytype -> {
            try {
                rs = book.findBookByBarcode(txtBookId.getText());
                if (rs.next()) {
                    txtBookName.setText(rs.getString("book_name"));
                    page = rs.getInt("page") + " ໜ້າ";
                    txtCatg.setText(rs.getString("catg_name"));
                    txtType.setText(rs.getString("type_name"));
                    table = rs.getString("tableid");
                    tableLog = rs.getString("table_log_id");
                    status = rs.getString("status");
                } else {
                    txtBookName.clear();
                    txtCatg.clear();
                    txtType.clear();
                    // dialog = new DialogMessage(stackPane, "ຄຳເຕືອນ",
                    // "ບໍ່ມີຂໍ້ມູນບັດໃນລະບົບ, ກະລຸນາກວດສອບແລ້ວລອງໃຫມ່່ອິກຄັ້ງ",
                    // DialogTransition.CENTER, buttons,
                    // false);
                    // dialog.showDialog();
                }
            } catch (Exception e) {
                alertMessage.showErrorMessage("Load Member Error", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
            }
        });

        txtBookId.setOnKeyPressed(keyEnter -> {
            if (keyEnter.getCode() == KeyCode.ENTER) {
                addToRentBook();

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
                String result = "";
                int qty = tableRentBook.getItems().size();
                rent_id = autoMaxID();
                try {
                    if (qty > 0) {
                        rentBook = new RentBookModel(rent_id, txtMemberId.getText(), txtBookId.getText(), qty,
                                Date.valueOf(rentDate.getValue()), Date.valueOf(sendDate.getValue()));
                        if (rentBook.saveData() > 0) {
                            for (RentBookModel row : tableRentBook.getItems()) {
                                try {
                                    rentBook.saveRentBook(rent_id, row.getBarcode(), "ກຳລັງຢືມ");
                                    if (book.updateStatus(row.getBarcode(), "ກຳລັງຢືມ") > 0) {
                                        result = "Rent book completed";
                                    }
                                } catch (Exception e) {
                                    rentBook.deleteData(rent_id);
                                    result = "";
                                    alertMessage.showErrorMessage("Rent Book Error", "Error: " + e.getMessage(), 4,
                                            Pos.BOTTOM_RIGHT);
                                    return;
                                }
                            }
                        } else {
                            result = "";
                        }
                    } else {
                        result = "";
                    }

                    if (result != "") {
                        alertMessage.showCompletedMessage("Rent Book Completed", result, 4, Pos.BOTTOM_RIGHT);
                        tableRentBook.getItems().clear();
                    } else {
                        alertMessage.showWarningMessage("Rent Book",
                                "Can't save. Please chack your information and try again.", 4, Pos.BOTTOM_RIGHT);
                    }
                } catch (Exception e) {
                    alertMessage.showErrorMessage("Rent Book Error", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
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

        rentDate = formatDate.formateDatePicker(rentDate);
        sendDate = formatDate.formateDatePicker(sendDate);
        // sendDate.setDisable(true);
        initTable();
        initRules();
        initEvents();
        // cancalarDate();
        rentDate.setValue(LocalDate.now());
        cancalarDate();
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
        LocalDate dateSend = dateRent;

        if (formatDate.cancalarDate(rentDate.getValue()) > 0) {
            rentDate.setValue(LocalDate.now());
            alertMessage.showWarningMessage(borderPane, "Warning", "Can not select the date after today. ເຮໂຣ", 4,
                    Pos.TOP_CENTER);
        }

        if (!days.getDisplayName(TextStyle.FULL, Locale.getDefault()).equals("Sunday")
                && !days.getDisplayName(TextStyle.FULL, Locale.getDefault()).equals("Saturday")) {
            for (int i = 1; i < 5; i++) {
                days = days.plus(1);
                dateSend = dateSend.plusDays(1);

                if (days.getDisplayName(TextStyle.FULL, Locale.getDefault()).equals("Sunday")
                        || days.getDisplayName(TextStyle.FULL, Locale.getDefault()).equals("Saturday")) {
                    days = days.plus(2);
                    dateSend = dateSend.plusDays(2);
                }
            }
            sendDate.setValue(dateSend);
        } else {
            alertMessage.showWarningMessage("Warning", "Please Select only 'Monday' to 'Friday'.", 4, Pos.TOP_CENTER);
            rentDate.setValue(LocalDate.now());
        }
    }

    private String autoMaxID() {
        try {
            String id = rentBook.getMaxID(), new_id = "";
            if (id != null) {
                int max_id = Integer.parseInt(id.substring(id.indexOf("T") + 1, id.indexOf("/")));
                max_id = max_id + 1;
                new_id = "RT" + max_id + "/" + LocalDate.now().format(DateTimeFormatter.ofPattern("dMyy"));
            } else {
                new_id = "RT" + 1 + "/" + LocalDate.now().format(DateTimeFormatter.ofPattern("dMY"));
            }
            return new_id;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

}
