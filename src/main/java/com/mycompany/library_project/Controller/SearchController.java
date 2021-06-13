package com.mycompany.library_project.Controller;

import java.net.URL;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTreeTableView;
import com.mycompany.library_project.ControllerDAOModel.AlertMessage;
import com.mycompany.library_project.Model.BookDetailModel;
import com.mycompany.library_project.Model.SearchModel;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.*;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;

public class SearchController implements Initializable {

    private TreeItem<SearchModel> subItem, root, node;
    private BookDetailModel bookDetailModel = null;
    private AlertMessage alertMessage = new AlertMessage();
    private ResultSet rs = null;

    public void initConstructor(HomeController homeController) {
        btClose.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                homeController.showMainMenuHome();
            }

        });
    }

    @FXML
    private JFXButton btClose, btSearch;

    @FXML
    private TextField txtSearch;

    @FXML
    private JFXTreeTableView<SearchModel> tableSearch;

    @FXML
    private TreeTableColumn<BookDetailModel, String> colBookId, colBookName, colISBN, colPage, colCatg, colType,
            collTable, colLog, colStatus;

    private void initTable() {
        colBookId.setCellValueFactory(new TreeItemPropertyValueFactory<>("bookId"));
        colBookName.setCellValueFactory(new TreeItemPropertyValueFactory<>("bookName"));
        colISBN.setCellValueFactory(new TreeItemPropertyValueFactory<>("ISBN"));
        colPage.setCellValueFactory(new TreeItemPropertyValueFactory<>("page"));
        colCatg.setCellValueFactory(new TreeItemPropertyValueFactory<>("catg"));
        colType.setCellValueFactory(new TreeItemPropertyValueFactory<>("type"));
        collTable.setCellValueFactory(new TreeItemPropertyValueFactory<>("tableId"));
        colLog.setCellValueFactory(new TreeItemPropertyValueFactory<>("logId"));
        colStatus.setCellValueFactory(new TreeItemPropertyValueFactory<>("status"));

    }

    private void showData(String value) {
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                try {
                    bookDetailModel = new BookDetailModel();
                    if (value != null) {
                        rs = bookDetailModel.searchData(value);
                    } else
                        rs = bookDetailModel.findAll();
                    node = new TreeItem<>();
                    while (rs.next()) {
                        root = new TreeItem<>(new SearchModel(rs.getString("book_id"), rs.getString("book_name"),
                                rs.getString("ISBN"), rs.getInt("page") + " ໜ້າ", rs.getString("catg_name"),
                                rs.getString("type_name"), "", "", ""));

                        final ResultSet sublog = bookDetailModel.showBarcode(rs.getString("book_id"));
                        while (sublog.next()) {
                            subItem = new TreeItem<>(new SearchModel(sublog.getString("barcode"), "", "", "", "", "",
                                    sublog.getString("tableid"), sublog.getString("table_log_id"),
                                    sublog.getString("status")));
                            root.getChildren().add(subItem);
                        }

                        node.getChildren().add(root);

                    }

                    tableSearch.setShowRoot(false);
                    tableSearch.setRoot(node);

                } catch (Exception e) {
                    alertMessage.showErrorMessage("Load Data", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        showData(null);
        initTable();

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
