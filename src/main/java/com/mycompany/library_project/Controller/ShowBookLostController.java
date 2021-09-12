package com.mycompany.library_project.Controller;

import java.net.URL;
import java.sql.Date;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import com.jfoenix.controls.*;
import com.mycompany.library_project.Style;
import com.mycompany.library_project.ControllerDAOModel.*;
import com.mycompany.library_project.Model.*;

import org.controlsfx.control.MaskerPane;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.*;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.*;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;

public class ShowBookLostController implements Initializable {

    private BookLostModel booklostModel = new BookLostModel();
    private ObservableList<BookLostModel> data = null;
    private ArrayList<BookLostModel> lost_id = null;
    private AlertMessage alertMessage = new AlertMessage();
    private DialogMessage dialog = new DialogMessage();
    private MaskerPane masker = new MaskerPane();
    private MyDate mydate = new MyDate();
    private DecimalFormat dcFormat = new DecimalFormat("#,##0.00 ກີບ");
    private ResultSet rs = null;

    // Todo: Call by ManageBookController Form
    public void initConstructor(ManageBookController manageBookController) {

        btClose.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                manageBookController.showMainMenuBooks();
            }
        });
    }

    @FXML
    private StackPane stackPane;

    @FXML
    private JFXButton btClose;

    @FXML
    private TextField txtSearch;

    @FXML
    private TableView<BookLostModel> tableLost;

    @FXML
    private TableColumn<BookLostModel, String> colBookId, colBarcode, colBookName, colPage, colCatg, colType, colTable,
            colTableLog, colPrice;
    @FXML
    private TableColumn<BookLostModel, Date> colDatePay;

    private void initTable() {
        colBookId.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        colBarcode.setCellValueFactory(new PropertyValueFactory<>("barcode"));
        colBookName.setCellValueFactory(new PropertyValueFactory<>("bookName"));
        colPage.setCellValueFactory(new PropertyValueFactory<>("page"));
        colCatg.setCellValueFactory(new PropertyValueFactory<>("catg"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colTable.setCellValueFactory(new PropertyValueFactory<>("table"));
        colTableLog.setCellValueFactory(new PropertyValueFactory<>("tableLog"));
        colDatePay.setCellValueFactory(new PropertyValueFactory<>("outDate"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("pricePerBook"));

        // Todo: Add column number
        final TableColumn<BookLostModel, BookLostModel> colNumber = new TableColumn<BookLostModel, BookLostModel>(
                "ລຳດັບ");
        colNumber.setMinWidth(50);
        colNumber.setMaxWidth(120);
        colNumber.setPrefWidth(60);
        colNumber.setId("colCenter");
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

    private void showData() {
        Task<Void> task = new Task<Void>() {

            @Override
            protected Void call() throws Exception {
                masker.setVisible(true);
                masker.setProgressVisible(true);

                lost_id = new ArrayList<>();
                rs = booklostModel.findAll();
                data = FXCollections.observableArrayList();
                while (rs.next()) {
                    lost_id.add(new BookLostModel(rs.getInt("lost_id"), rs.getDouble("book_price")));
                    data.add(new BookLostModel(rs.getString("book_id"), rs.getString("barcode"),
                            rs.getString("book_name"), rs.getInt("page") + " ໜ້າ", rs.getString("catg_name"),
                            rs.getString("type_name"), rs.getString("tableid"), rs.getString("tablelog"),
                            mydate.formaterDate(rs.getDate("date_pay").toLocalDate(), "dd/MM/yyyy"),
                            dcFormat.format(rs.getDouble("book_price"))));
                }

                /*
                 * tableMember.setItems(data); //Todo: if you don't filter to Search data
                 * bellow:
                 */
                // Todo: Search data
                FilteredList<BookLostModel> filterBookLost = new FilteredList<BookLostModel>(data, mb -> true);
                txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
                    filterBookLost.setPredicate(booklost -> {
                        if (newValue.isEmpty())
                            return true;
                        if (booklost.getBookId().toLowerCase().indexOf(newValue.toLowerCase()) != -1
                                || booklost.getBarcode().toLowerCase().indexOf(newValue.toLowerCase()) != -1
                                || booklost.getBookName().toLowerCase().indexOf(newValue.toLowerCase()) != -1
                                || booklost.getCatg().toLowerCase().indexOf(newValue.toLowerCase()) != -1
                                || booklost.getType().toLowerCase().indexOf(newValue.toLowerCase()) != -1
                                || booklost.getTable().toLowerCase().indexOf(newValue.toLowerCase()) != -1
                                || booklost.getTableLog().toLowerCase().indexOf(newValue.toLowerCase()) != -1
                        )
                            return true;
                        else
                            return false;
                    });
                });

                SortedList<BookLostModel> sorted = new SortedList<>(filterBookLost);
                sorted.comparatorProperty().bind(tableLost.comparatorProperty());
                tableLost.setItems(sorted);

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
                alertMessage.showErrorMessage("Load Data", "Load Data Failed", 4, Pos.BOTTOM_RIGHT);
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

        initTable();
        showData();
    }

    private void addButtonToTable() {
        TableColumn<BookLostModel, Void> colAction = new TableColumn<>("Action");
        colAction.setPrefWidth(100);
        Callback<TableColumn<BookLostModel, Void>, TableCell<BookLostModel, Void>> cellFactory = new Callback<TableColumn<BookLostModel, Void>, TableCell<BookLostModel, Void>>() {

            @Override
            public TableCell<BookLostModel, Void> call(TableColumn<BookLostModel, Void> param) {
                final TableCell<BookLostModel, Void> cell = new TableCell<BookLostModel, Void>() {
                    final JFXButton delete = new JFXButton("ລົບ");
                    {
                        final ImageView imgView = new ImageView();
                        imgView.setImage(new Image("/com/mycompany/library_project/Icon/bin.png"));
                        imgView.setFitWidth(20);
                        imgView.setFitHeight(20);
                        delete.setGraphic(imgView);
                        delete.setStyle(Style.buttonStyle);
                        delete.setOnAction(e -> {
                            Optional<ButtonType> result = dialog.showComfirmDialog("Comfirmed", null,
                                    "ຕ້ອງການລົບຂໍ້ມູນນີ້ອອກ ຫຼື ບໍ່? (ຖ້າລົບແລ້ວຈະບໍ່ສາມາດກູ້ຄືນຂໍ້ມູນໄດ້).");
                            if (result.get() == ButtonType.YES)
                                try {
                                    if (booklostModel.deleeLostDetail(lost_id.get(getIndex()).getLost_id(),
                                            tableLost.getItems().get(getIndex()).getBarcode(),
                                            lost_id.get(getIndex()).getPrice()) > 0) {
                                        alertMessage.showCompletedMessage("Deleted", "Deleted data successfully.", 4,
                                                Pos.BOTTOM_RIGHT);
                                        lost_id.remove(getIndex());
                                        showData();
                                    }
                                } catch (Exception ex) {
                                    dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການລົບຂໍ້ມູນ", ex);
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

        colAction.setCellFactory(cellFactory);
        tableLost.getColumns().add(colAction);
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
