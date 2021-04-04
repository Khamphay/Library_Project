package com.mycompany.library_project.Controller;

import javafx.application.Platform;
import javafx.collections.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.*;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.mycompany.library_project.App;
import com.mycompany.library_project.Controller.List.book_item;
import com.mycompany.library_project.Model.BookDetailModel;
import com.mycompany.library_project.ModelShow.MyArrayList;

public class BookController implements Initializable {

    String[] values = { "001", "Data Analyst", "07-646712-21", "200", "20", "ຄອມພິວເຕີ", "ແບບຮຽນ", "ພາສາອັງກິດ" };
    Node node[] = new Node[1000];

    private ResultSet rs = null;
    private BookDetailModel bookDetail = null;
    private ObservableList<BookDetailModel> data = null;
    private JFXButton btnAction = new JFXButton("Active");

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
                    e.printStackTrace();
                }
            }
        });
    }

    private void showData() {
        try {
            data = FXCollections.observableArrayList();
            bookDetail = new BookDetailModel();
            rs = bookDetail.findAll();
            while (rs.next()) {
                // data.add(new BookDetailModel(rs.getString(0), rs.getString(1),
                // rs.getString(2), rs.getInt(3),
                // rs.getInt(4), rs.getString(5), rs.getString(6), rs.getString(7), btnAction));
            }

            data.add(new BookDetailModel("rs.getString(0)", "rs.getString(1)", "rs.getString(2)", 309, 10,
                    "rs.getString(5)", "rs.getString(6)", "rs.getString(7)", btnAction));

            tableBook.setItems(data);

        } catch (Exception e) {
            e.printStackTrace();
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

    private void initButtons() {
        final ContextMenu contMenu = new ContextMenu();
        final ImageView imgshow = new ImageView();
        final ImageView imgedit = new ImageView();
        final ImageView imgdel = new ImageView();
        Image img = null;

        img = new Image("/com/mycompany/library_project/Icon/checklist.png");
        imgshow.setImage(img);
        imgshow.setFitHeight(20);
        imgshow.setFitWidth(20);
        MenuItem show = new MenuItem("ລາຍລະອຽດ");
        show.setGraphic(imgshow);

        img = new Image("/com/mycompany/library_project/Icon/icons8_edit_50px.png");
        imgedit.setImage(img);
        imgedit.setFitHeight(20);
        imgedit.setFitWidth(20);
        MenuItem edit = new MenuItem("ແກ້ໄຂ");
        edit.setGraphic(imgedit);

        img = new Image("/com/mycompany/library_project/Icon/delete.png");
        imgdel.setImage(img);
        imgdel.setFitHeight(20);
        imgdel.setFitWidth(20);
        MenuItem delete = new MenuItem("ລືບ");
        delete.setGraphic(imgdel);

        contMenu.getItems().addAll(show, edit, delete);

        btnAction.setId("btnAction");

        btnAction.setContextMenu(contMenu);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // showBooks();
        initColumn();
        initButtons();
        showData();
    }
}
