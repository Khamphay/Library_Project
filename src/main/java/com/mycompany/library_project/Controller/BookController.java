package com.mycompany.library_project.Controller;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.*;
import javafx.collections.transformation.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.*;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import com.jfoenix.controls.*;
import com.mycompany.library_project.*;
import com.mycompany.library_project.ControllerDAOModel.*;
import com.mycompany.library_project.Model.BookDetailModel;
import com.mycompany.library_project.config.CreateLogFile;

public class BookController implements Initializable {

    String[] values = { "001", "Data Analyst", "07-646712-21", "200", "20", "ຄອມພິວເຕີ", "ແບບຮຽນ", "ພາສາອັງກິດ" };
    Node node[] = new Node[1000];

    private ManageBookController manageBookController = null;
    private ResultSet rs = null;
    private BookDetailModel bookDetail = null;
    private ObservableList<BookDetailModel> data = null;
    private AlertMessage alertMessage = new AlertMessage();
    private CreateLogFile logfile = new CreateLogFile();
    private DialogMessage dialog = null;
    public static Stage addNewBook = null;
    public static Stage addBarcode = null;
    public static String _book_id = "";
    public static boolean add = false;

    public void initConstructor(ManageBookController manageBookController) {
        this.manageBookController = manageBookController;
    }

    @FXML
    private AddBookController addBookController;

    @FXML
    private BarcodeController barcodeController;

    @FXML
    private BorderPane borderPane;

    @FXML
    private StackPane stackPane;

    @FXML
    private JFXButton btAddNewBook, btRefresh, btClose;

    @FXML
    private MenuItem menuList, menuEdit;

    @FXML
    private TableView<BookDetailModel> tableBook;

    @FXML
    private TableColumn<BookDetailModel, String> bookid, bookname, bookisbn, bppkcategory, bppktype, tableid,
            bookdetail;

    @FXML
    private TableColumn<BookDetailModel, Integer> bookpage, bookqty;

    @FXML
    private TextField txtSearch;

    @FXML
    private VBox vbListBooks;

    private void showBarcode() {
        try {
            _book_id = tableBook.getSelectionModel().getSelectedItem().getBookId();
            final FXMLLoader loader = new FXMLLoader(App.class.getResource("frmBookBarcode.fxml"));
            final Parent root = loader.load();
            final Scene scene = new Scene(root);

            barcodeController = loader.getController();
            barcodeController.initConstructor(this);

            scene.setFill(Color.TRANSPARENT);
            addBarcode = new Stage();
            addBarcode.setScene(scene);
            addBarcode.initStyle(StageStyle.TRANSPARENT);
            addBarcode.show();
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    private void initEvents() {
        btRefresh.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                showData();
            }
        });
        menuList.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                showBarcode();
            }

        });
        menuEdit.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                if (tableBook.getSelectionModel().getSelectedItem() != null) {
                    try {
                        AddBookController.addBook = tableBook.getSelectionModel().getSelectedItem();
                        showAddBook();
                    } catch (Exception e) {
                        alertMessage.showErrorMessage(stackPane, "Open New Form", "Error: " + e.getMessage(), 4,
                                Pos.BOTTOM_RIGHT);
                        logfile.createLogFile("Open Form Edit Book Error", e);
                    }

                } else {
                    alertMessage.showWarningMessage("Edited", "Please select data and try again.", 4, Pos.BOTTOM_RIGHT);
                }
            }

        });
        btAddNewBook.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                showAddBook();
            }
        });

        btClose.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                manageBookController.showMainMenuBooks();
            }

        });
    }

    private void showAddBook() {
        try {
            add = true;
            FXMLLoader loader = new FXMLLoader(App.class.getResource("frmAddBooks.fxml"));
            final Parent root = loader.load();
            final Scene scene = new Scene(root);

            addBookController = loader.getController();
            addBookController.initConstructor2(this);

            addNewBook = new Stage();
            addNewBook.initStyle(StageStyle.UNDECORATED);
            addNewBook.setScene(scene);
            addNewBook.show();
        } catch (IOException e) {
            alertMessage.showErrorMessage(borderPane, "Open New Form", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
        }
    }

    public void showData() {
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                try {
                    data = FXCollections.observableArrayList();
                    bookDetail = new BookDetailModel();
                    rs = bookDetail.findAll();
                    while (rs.next()) {
                        data.add(new BookDetailModel(rs.getString(1), rs.getString(2), rs.getString(3), rs.getInt(4),
                                rs.getInt(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9)));
                    }

                    // tableBook.setItems(data); //Todo: if you don't filter to Search data bellow:

                    // Todo: Search data
                    FilteredList<BookDetailModel> filterBook = new FilteredList<BookDetailModel>(data, b -> true);
                    txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
                        filterBook.setPredicate(searchBook -> {
                            if (newValue.isEmpty())
                                return true;
                            if ((searchBook.getBookId().toLowerCase().indexOf(newValue.toLowerCase()) != -1
                                    || searchBook.getBookName().toLowerCase().indexOf(newValue.toLowerCase()) != -1
                                    || searchBook.getISBN().toLowerCase().indexOf(newValue.toLowerCase()) != -1
                                    || searchBook.getCatgId().toLowerCase().indexOf(newValue.toLowerCase()) != -1
                                    || searchBook.getTypeId().toLowerCase().indexOf(newValue.toLowerCase()) != -1
                                    || searchBook.getTableId().toLowerCase().indexOf(newValue.toLowerCase()) != -1
                                    || searchBook.getDetail().toLowerCase().indexOf(newValue.toLowerCase()) != -1))
                                return true;
                            else
                                return false;
                        });
                    });

                    SortedList<BookDetailModel> sorted = new SortedList<>(filterBook);
                    sorted.comparatorProperty().bind(tableBook.comparatorProperty());
                    tableBook.setItems(sorted);

                } catch (Exception e) {
                    alertMessage.showErrorMessage(borderPane, "Load data", "Error: " + e.getMessage(), 4,
                            Pos.BOTTOM_RIGHT);
                }
            }
        });
    }

    private void initColumn() {
        // Todo: Set properties to 'tableBook'
        bookid.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        bookname.setCellValueFactory(new PropertyValueFactory<>("bookName"));
        bookisbn.setCellValueFactory(new PropertyValueFactory<>("ISBN"));
        bookpage.setCellValueFactory(new PropertyValueFactory<>("page"));
        bookqty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        bppkcategory.setCellValueFactory(new PropertyValueFactory<>("catgId"));
        bppktype.setCellValueFactory(new PropertyValueFactory<>("typeId"));
        tableid.setCellValueFactory(new PropertyValueFactory<>("tableId"));
        bookdetail.setCellValueFactory(new PropertyValueFactory<>("detail"));
        bookid.setVisible(false);

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
        tableBook.getColumns().add(0, colNumber);

        // Todo: Add column Button
        addButtonToTable();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // showBooks();
        initEvents();
        initColumn();
        showData();
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
                                JFXButton[] buttons = { buttonYes(tableBook.getItems().get(getIndex()).getBookId()),
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
        tableBook.getColumns().add(colAtion);

    }

    private JFXButton buttonYes(String bookid) {
        JFXButton btyes = new JFXButton("ຕົກລົງ");
        btyes.setStyle(Style.buttonDialogStyle);
        btyes.setOnAction(e -> {
            // Todo: Delete Data
            try {
                bookDetail = new BookDetailModel();
                if (bookDetail.deleteData(bookid) > 0) {
                    dialog.closeDialog();
                    alertMessage.showCompletedMessage("Deleted", "Delete data successfully.", 4, Pos.BOTTOM_RIGHT);
                    showData();
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
