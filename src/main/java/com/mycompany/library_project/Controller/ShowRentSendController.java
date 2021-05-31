package com.mycompany.library_project.Controller;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;

import com.jfoenix.controls.*;
import com.mycompany.library_project.Style;
import com.mycompany.library_project.ControllerDAOModel.*;
import com.mycompany.library_project.Model.ShowRentSendModel;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.*;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.*;
import javafx.util.Callback;

public class ShowRentSendController implements Initializable {

    private HomeController homeController = null;
    private AlertMessage alertMessage = new AlertMessage();
    // private DialogMessage dialog = null;
    private ShowRentSendModel showbookModel = null;
    private ObservableList<ShowRentSendModel> data = null;
    private ResultSet rs = null;
    private FilteredList<ShowRentSendModel> filterShow = null;
    private MyDate mydate = new MyDate();

    public void initConstructor(HomeController homeController) {
        this.homeController = homeController;
    }

    @FXML
    private JFXRadioButton rdbShowAll, rdbShowRent, rdbShowOutOfRent, rdbSend;

    @FXML
    private TextField txtSearch;

    @FXML
    private JFXButton btClose;

    @FXML
    private TableView<ShowRentSendModel> tableShow;

    @FXML
    private TableColumn<String, ShowRentSendModel> colRentId, colBarcode, colBookName, colCatg, colType, colTableLog,
            colMemberId, colMemberName, colStatus, colCause;

    @FXML
    private TableColumn<Date, ShowRentSendModel> colDateRent, colDateSend;

    private void initTable() {
        colRentId.setCellValueFactory(new PropertyValueFactory<>("rentId"));
        colBarcode.setCellValueFactory(new PropertyValueFactory<>("barcode"));
        colBookName.setCellValueFactory(new PropertyValueFactory<>("bookName"));
        colCatg.setCellValueFactory(new PropertyValueFactory<>("catg"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colTableLog.setCellValueFactory(new PropertyValueFactory<>("tableLogId"));
        colDateRent.setCellValueFactory(new PropertyValueFactory<>("rentDate"));
        colDateSend.setCellValueFactory(new PropertyValueFactory<>("sendDate"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colMemberId.setCellValueFactory(new PropertyValueFactory<>("memberId"));
        colMemberName.setCellValueFactory(new PropertyValueFactory<>("memberName"));
        colCause.setCellValueFactory(new PropertyValueFactory<>("cause"));

        // Todo: Add column number
        final TableColumn<ShowRentSendModel, ShowRentSendModel> colNumber = new TableColumn<ShowRentSendModel, ShowRentSendModel>();
        colNumber.setMinWidth(50);
        colNumber.setMaxWidth(120);
        colNumber.setPrefWidth(60);
        colNumber.setCellValueFactory(
                new Callback<CellDataFeatures<ShowRentSendModel, ShowRentSendModel>, ObservableValue<ShowRentSendModel>>() {

                    @Override
                    public ObservableValue<ShowRentSendModel> call(
                            CellDataFeatures<ShowRentSendModel, ShowRentSendModel> param) {
                        return new ReadOnlyObjectWrapper<ShowRentSendModel>(param.getValue());
                    }
                });
        colNumber.setCellFactory(
                new Callback<TableColumn<ShowRentSendModel, ShowRentSendModel>, TableCell<ShowRentSendModel, ShowRentSendModel>>() {

                    @Override
                    public TableCell<ShowRentSendModel, ShowRentSendModel> call(
                            TableColumn<ShowRentSendModel, ShowRentSendModel> param) {
                        return new TableCell<ShowRentSendModel, ShowRentSendModel>() {
                            @Override
                            protected void updateItem(ShowRentSendModel item, boolean empty) {
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
        tableShow.getColumns().add(0, colNumber);

        // Todo: Add column Button
        addButtonToTable();
    }

    private void showData() {
        Platform.runLater(new Runnable() {

            @Override
            public void run() {

                try {
                    showbookModel = new ShowRentSendModel();
                    data = FXCollections.observableArrayList();

                    // Todo: Check the type of data what to show
                    if (rdbShowRent.isSelected())
                        rs = showbookModel.findByRent("ກຳລັງຢືມ");
                    else if (rdbSend.isSelected())
                        rs = showbookModel.findBySend("ສົ່ງແລ້ວ");
                    else if (rdbShowOutOfRent.isSelected())
                        rs = showbookModel.findByRentOutOfDate(Date.valueOf(LocalDate.now()), "ກຳລັງຢືມ");
                    else
                        rs = showbookModel.findAll();

                    while (rs.next()) {
                        String outdate = "";
                        if (rs.getString("book_status").equals("ກຳລັງຢືມ")) {
                            int outday = mydate.cancalarDate(rs.getDate("date_send").toLocalDate());
                            if (outday > 0) {
                                outdate = "ປຶ້ມຫົວນີ້ຢືມກາຍກຳນົດ " + outday + " ມື້";
                            }
                        }

                        data.add(new ShowRentSendModel(rs.getString("rent_id"), rs.getString("barcode"),
                                rs.getString("book_name"), rs.getString("catg_name"), rs.getString("type_name"),
                                rs.getString("table_log_id"), rs.getDate("date_rent"), rs.getDate("date_send"),
                                rs.getString("book_status"), rs.getString("member_id"),
                                rs.getString("full_name") + " " + rs.getString("sur_name"), outdate));
                    }
                    /*
                     * tableShow.setItems(data); //Todo: if you don't filter to Search data bellow:
                     */
                    // Todo: Search data
                    filterShow = new FilteredList<ShowRentSendModel>(data, book -> true);
                    txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
                        filterShow.setPredicate(book -> {
                            if (newValue.isEmpty())
                                return true;
                            if (book.getBarcode().toLowerCase().indexOf(newValue.toLowerCase()) != -1
                                    || book.getBookName().toLowerCase().indexOf(newValue.toLowerCase()) != -1
                                    || book.getCatg().toLowerCase().indexOf(newValue.toLowerCase()) != -1
                                    || book.getType().toLowerCase().indexOf(newValue.toLowerCase()) != -1
                                    || book.getMemberId().toLowerCase().indexOf(newValue.toLowerCase()) != -1
                                    || book.getMemberName().toLowerCase().indexOf(newValue.toLowerCase()) != -1
                                    || book.getStatus().toLowerCase().indexOf(newValue.toLowerCase()) != -1)
                                return true;
                            else
                                return false;
                        });
                    });
                    SortedList<ShowRentSendModel> sorted = new SortedList<>(filterShow);
                    sorted.comparatorProperty().bind(tableShow.comparatorProperty());
                    tableShow.setItems(sorted);

                } catch (Exception e) {
                    alertMessage.showErrorMessage("Load data", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
                }
            }
        });
    }

    private void initEvents() {
        rdbShowAll.setOnAction(event -> {
            showData();
        });
        rdbShowOutOfRent.setOnAction(event -> {
            showData();
        });
        rdbShowRent.setOnAction(event -> {
            showData();
        });
        rdbSend.setOnAction(event -> {
            showData();
        });

        btClose.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                homeController.showMainMenuHome();
            }
        });
    }

    /*
     * //Todo: Use Search Data private void searchData() { try {
     * Platform.runLater(new Runnable() {
     * 
     * @Override public void run() { String newValue = txtSearch.getText();
     * filterShow.setPredicate(book -> { if (newValue.isEmpty()) return true; if
     * (book.getBarcode().toLowerCase().indexOf(newValue.toLowerCase()) != -1 ||
     * book.getBookName().toLowerCase().indexOf(newValue.toLowerCase()) != -1 ||
     * book.getCatg().toLowerCase().indexOf(newValue.toLowerCase()) != -1 ||
     * book.getType().toLowerCase().indexOf(newValue.toLowerCase()) != -1 ||
     * book.getMemberId().toLowerCase().indexOf(newValue.toLowerCase()) != -1 ||
     * book.getMemberName().toLowerCase().indexOf(newValue.toLowerCase()) != -1 ||
     * book.getStatus().toLowerCase().indexOf(newValue.toLowerCase()) != -1) if
     * (rdbShowAll.isSelected()) { return true; } else if (rdbShowRent.isSelected())
     * { if (book.getStatus().toLowerCase().equals("ກຳລັງຢືມ")) return true; else
     * return false; } else if (rdbSend.isSelected()) { if
     * (book.getStatus().toLowerCase().equals("ສົ່ງແລ້ວ")) return true; else return
     * false; } else if (rdbShowOutOfRent.isSelected()) { if
     * (LocalDate.now().compareTo(book.getSendDate().toLocalDate()) > 0 &&
     * book.getStatus().toLowerCase().equals("ກຳລັງຢືມ")) return true; else return
     * false; } else return false; else return false; });
     * 
     * }
     * 
     * }); } catch (Exception e) { e.printStackTrace(); } }
     */

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        ToggleGroup group = new ToggleGroup();
        rdbShowAll.setToggleGroup(group);
        rdbShowRent.setToggleGroup(group);
        rdbShowOutOfRent.setToggleGroup(group);
        rdbSend.setToggleGroup(group);
        rdbShowAll.setSelected(true);

        initTable();
        showData();
        initEvents();
    }

    private void addButtonToTable() {
        TableColumn<ShowRentSendModel, Void> colAction = new TableColumn<>("Action");
        Callback<TableColumn<ShowRentSendModel, Void>, TableCell<ShowRentSendModel, Void>> cellFactory = new Callback<TableColumn<ShowRentSendModel, Void>, TableCell<ShowRentSendModel, Void>>() {

            @Override
            public TableCell<ShowRentSendModel, Void> call(TableColumn<ShowRentSendModel, Void> param) {
                final TableCell<ShowRentSendModel, Void> cell = new TableCell<ShowRentSendModel, Void>() {
                    final JFXButton delete = new JFXButton("ລົບ");
                    {
                        final ImageView imgView = new ImageView();
                        imgView.setImage(new Image("/com/mycompany/library_project/Icon/bin.png"));
                        imgView.setFitWidth(20);
                        imgView.setFitHeight(20);
                        delete.setGraphic(imgView);
                        delete.setStyle(Style.buttonStyle);
                        delete.setOnAction(e -> {
                            tableShow.getItems().remove(getIndex());
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
        tableShow.getColumns().add(colAction);
    }

}
