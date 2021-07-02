package com.mycompany.library_project.Controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.mycompany.library_project.MyConnection;
import com.mycompany.library_project.ControllerDAOModel.*;
import com.mycompany.library_project.Model.*;

import org.controlsfx.control.MaskerPane;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.*;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.StackPane;

public class SearchController implements Initializable {

    private Connection con = MyConnection.getConnect();
    private TreeItem<String> subItemBarcode, subItemAuthor, root, node;
    private BookDetailModel bookDetailModel = new BookDetailModel(con);
    private AuthorModel authorModel = new AuthorModel(con);
    private AlertMessage alertMessage = new AlertMessage();
    private DialogMessage dialog = new DialogMessage();
    private MaskerPane masker = new MaskerPane();
    private ResultSet rs = null;

    public void initConstructor(HomeController homeController) {
        btClose.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                homeController.showMainMenuHome();
            }

        });
    }

    public void initConstructor2() {
        btClose.setDisable(true);
        btClose.setVisible(false);
    }

    @FXML
    private StackPane stackPane;

    @FXML
    private JFXButton btClose, btSearch;

    @FXML
    private TextField txtSearch;

    @FXML
    private TreeView<String> treeViewShowBook;

    private void showData(String value) {
        Task<Void> task = new Task<Void>() {

            @Override
            protected Void call() throws Exception {
                masker.setVisible(true);
                masker.setProgressVisible(true);
                Platform.runLater(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            // bookDetailModel = new BookDetailModel();
                            if (value != null) {
                                rs = bookDetailModel.searchData(value);
                            } else
                                rs = bookDetailModel.findAll();
                            node = new TreeItem<>();
                            while (rs.next()) {
                                root = new TreeItem<>("ລະຫັດ: " + rs.getString("book_id") + " | ຊື່ປຶ້ມ: "
                                        + rs.getString("book_name"));

                                final TreeItem<String> itemCatg = new TreeItem<>("+ ໝວດ: " + rs.getString("catg_name"));
                                final TreeItem<String> itemTypee = new TreeItem<>(
                                        "+ ປະເພດ: " + rs.getString("type_name"));
                                final TreeItem<String> itemISBN = new TreeItem<>("+ ISBN: " + rs.getString("ISBN"));
                                final TreeItem<String> itemPage = new TreeItem<>(
                                        " +ຈຳນວນໜ້າ: " + rs.getInt("page") + " ໜ້າ");
                                final TreeItem<String> itemQty = new TreeItem<>(
                                        "+ ຈຳນວນປຶ້ມ: " + rs.getInt("qty") + " ຫົວ");
                                final TreeItem<String> itemWriteYear = new TreeItem<>(
                                        "+ ແຕ່ງປີ: " + rs.getString("write_year"));
                                final TreeItem<String> itemLog = new TreeItem<>(
                                        "+ ເລກຕູ້: " + rs.getString("table_id"));

                                subItemBarcode = new TreeItem<>("ລະຫັດບາໂຄດ ແລະ ສະຖານະຂອງປຶ້ມ:");
                                final ResultSet rs_barcode = bookDetailModel.showBarcode(rs.getString("book_id"));
                                int index = 1;
                                while (rs_barcode.next()) {
                                    final TreeItem<String> itemBarrcode = new TreeItem<>(
                                            index + ". ລະຫັດບາໂຄດ: " + rs_barcode.getString("barcode") + " | ສະຖາານະ: "
                                                    + rs_barcode.getString("status") + " | ລ໋ອກຕູ້: "
                                                    + rs_barcode.getString("table_log_id"));
                                    subItemBarcode.getChildren().add(itemBarrcode);
                                    index++;
                                }
                                rs_barcode.close();
                                subItemAuthor = new TreeItem<>("ແຕ່ງໂດຍ:");
                                final ResultSet rs_author = authorModel.findByBookID(rs.getString("book_id"));
                                int i = 1;
                                while (rs_author.next()) {
                                    final TreeItem<String> itemAuthor = new TreeItem<>(i + ". "
                                            + rs_author.getString("full_name") + " " + rs_author.getString("sur_name"));
                                    subItemAuthor.getChildren().add(itemAuthor);
                                    i++;
                                }

                                root.getChildren().addAll(itemCatg, itemTypee, itemISBN, itemPage, itemQty,
                                        itemWriteYear, itemLog, subItemBarcode, subItemAuthor);
                                node.getChildren().add(root);
                            }

                            if (node.getChildren().size() == 0) {
                                node = new TreeItem<>("ບໍ່ມີຂໍ້ມູນ");
                                treeViewShowBook.setShowRoot(true);
                            } else
                                treeViewShowBook.setShowRoot(false);

                            treeViewShowBook.setRoot(node);
                        } catch (Exception e) {
                            e.printStackTrace();
                            dialog.showExcectionDialog("Error", null, "ເກິດບັນຫາໃນການໂຫຼດຂໍ້ມູນ", e);
                        }
                    }
                });
                return null;
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                masker.setVisible(false);
                masker.setProgressVisible(false);
            }

            @Override
            protected void failed() {
                super.failed();
                masker.setProgressVisible(false);
                masker.setVisible(false);
                alertMessage.showErrorMessage("Load", "Load Data Failed", 4, Pos.BOTTOM_RIGHT);
            }

        };
        new Thread(task).start();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        masker.setVisible(false);
        masker.setPrefWidth(50.0);
        masker.setPrefHeight(50.0);
        masker.setText("ກຳລັງໂຫລດຂໍ້ມູນ, ກະລຸນາລໍຖ້າ...");
        masker.setStyle("-fx-font-family: BoonBaan;");
        stackPane.getChildren().add(masker);

        showData(null);

        btSearch.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                if (!txtSearch.getText().equals("")) {
                    showData(txtSearch.getText());
                } else
                    alertMessage.showWarningMessage("Search Books", "Please enter data of you want to search...!", 4,
                            Pos.BOTTOM_RIGHT);
            }
        });

        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals(""))
                showData(null);
        });

    }
}
