package com.mycompany.library_project.Controller;

import javafx.application.Platform;
import javafx.collections.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.stage.*;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import com.jfoenix.controls.*;
import com.mycompany.library_project.*;
import com.mycompany.library_project.Controller.List.book_item;
import com.mycompany.library_project.ControllerDAOModel.*;
import com.mycompany.library_project.Model.BookDetailModel;
import com.mycompany.library_project.ModelShow.MyArrayList;

public class BookController implements Initializable {

    String[] values = { "001", "Data Analyst", "07-646712-21", "200", "20", "ຄອມພິວເຕີ", "ແບບຮຽນ", "ພາສາອັງກິດ" };
    Node node[] = new Node[1000];

    private ResultSet rs = null;
    private BookDetailModel bookDetail = null;
    private ObservableList<BookDetailModel> data = null;
    private AlertMessage alertMessage = new AlertMessage();
    private DialogMessage dialog = null;
    public static Stage addNewBook = null;

    @FXML
    private BorderPane borderPane;

    @FXML
    private StackPane stackPane;

    @FXML
    private JFXButton btAddNewBook;

    @FXML
    private MenuItem menuList, menuEdit;

    @FXML
    private TableView<BookDetailModel> tableBook;

    @FXML
    private TableColumn<BookDetailModel, String> bookid, bookname, bookisbn, bppkcategory, bppktype, bookdetail;

    @FXML
    private TableColumn<BookDetailModel, Integer> bookpage, bookqty;

    @FXML
    private TableColumn<BookDetailModel, JFXButton> ation;

    @FXML
    private VBox vbListBooks;

    private void initEvents() {
        menuList.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                showAddBook();
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
    }

    private void showBooks() {
        vbListBooks.getChildren().clear();
        Platform.runLater(new Runnable() {
            int i = 0;

            @Override
            public void run() {
                try {
                    while (i < node.length) {
                        MyArrayList array_listnew = new MyArrayList();
                        book_item.book = array_listnew.bookList(values);
                        Parent list = FXMLLoader.load(App.class.getResource("bookList.fxml"));
                        node[i] = list;
                        vbListBooks.getChildren().addAll(node[i]);
                        i++;
                    }
                } catch (Exception e) {
                    alertMessage.showErrorMessage(borderPane, "Load data", "Error: " + e.getMessage(), 4,
                            Pos.BOTTOM_RIGHT);
                }
            }
        });
    }

    private void showAddBook() {
        try {
            final Parent root = FXMLLoader.load(App.class.getResource("frmAddBooks.fxml"));
            final Scene scene = new Scene(root);
            addNewBook = new Stage();
            addNewBook.initStyle(StageStyle.UNDECORATED);
            addNewBook.setScene(scene);
            addNewBook.show();
        } catch (IOException e) {
            alertMessage.showErrorMessage(borderPane, "Open New Form", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
        }
    }
    private void showData() {
        try {
            data = FXCollections.observableArrayList();
            bookDetail = new BookDetailModel();
            rs = bookDetail.findAll();
            while (rs.next()) {
                data.add(new BookDetailModel(rs.getString(1), rs.getString(2), rs.getString(3), rs.getInt(4),
                        rs.getInt(5), rs.getString(6), rs.getString(7), rs.getString(8), getAction(rs.getString(1))));
            }

            // data.add(new BookDetailModel("rs.getString(0)", "rs.getString(1)",
            // "rs.getString(2)", 309, 10,
            // "rs.getString(5)", "rs.getString(6)", "rs.getString(7)", btnAction));

            tableBook.setItems(data);

        } catch (Exception e) {
            alertMessage.showErrorMessage(borderPane, "Load data", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
        }
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
        bookdetail.setCellValueFactory(new PropertyValueFactory<>("detail"));
        ation.setCellValueFactory(new PropertyValueFactory<>("action"));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // showBooks();
        initEvents();
        initColumn();
        showData();
    }

    private JFXButton getAction(String id) {

        JFXButton delete = new JFXButton("ລົບ");
        final ImageView imageView = new ImageView();

        imageView.setImage(new Image("/com/mycompany/library_project/Icon/bin.png"));
        imageView.setFitHeight(20);
        imageView.setFitWidth(20);
        delete.setStyle(Style.buttonStyle);
        delete.setGraphic(imageView);
        delete.setId(id);
        delete.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                JFXButton[] buttons = { buttonYes(delete.getId()), buttonNo(), buttonCancel() };
                dialog = new DialogMessage(stackPane, "ຄຳເຕືອນ", "ຕ້ອງການລົບຂໍ້ມູນອອກບໍ?",
                        JFXDialog.DialogTransition.CENTER, buttons, false);
                dialog.showDialog();
            }
        });
        return delete;
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
