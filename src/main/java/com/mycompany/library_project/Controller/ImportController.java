package com.mycompany.library_project.Controller;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.*;
import javafx.collections.*;
import javafx.concurrent.Task;
import javafx.event.*;
import javafx.fxml.*;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

import java.net.URL;
import java.nio.file.Paths;
import java.sql.Date;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.mycompany.library_project.App;
import com.mycompany.library_project.Style;
import com.mycompany.library_project.ControllerDAOModel.*;
import com.mycompany.library_project.Model.*;
import com.mycompany.library_project.Report.CreateReport;

import org.controlsfx.control.MaskerPane;
import org.controlsfx.validation.Severity;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

public class ImportController implements Initializable {

    private ValidationSupport validRules = new ValidationSupport();
    private ResultSet rs = null;
    private AlertMessage alertMessage = new AlertMessage();
    private TypeModel type = new TypeModel();
    private CategoryModel category = new CategoryModel();
    private TableLogModel table = new TableLogModel();
    private AuthorModel author = new AuthorModel();
    private DialogMessage dialog = new DialogMessage();
    private BookDetailModel addBook = new BookDetailModel();
    private ImportModel importBook = new ImportModel();
    private ArrayList<AuthorModel> author_id = null;
    private ArrayList<TypeModel> arr_type;
    private ArrayList<CategoryModel> arr_category;
    private ObservableList<String> status = FXCollections.observableArrayList("ຫວ່າງ", "ກຳລ້ງຢືມ", "ເສຍ");
    private ObservableList<String> items = null, author_items = null;
    private MaskerPane masker = new MaskerPane();
    private Task<Void> task = null;
    private CreateReport printbill = new CreateReport();

    private String[] arr_author = new String[6];
    private ArrayList<String[]> listAuthor = new ArrayList<>();
    private String _bookid = "";
    private Double _totalPrice = 0.0;
    private int _totalQty = 0;
    private String categoryId = "", typeId = "";

    /*
     * Todo: Use from class BookController for both form can communicate or use for
     * Refresh table after add and delete
     */
    public void initConstructor(HomeController homeController) {
        btClose.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                homeController.showMainMenuHome();
            }
        });
    }

    @FXML
    private StackPane stackPane;

    @FXML
    private BorderPane borderPane;

    @FXML
    private AnchorPane acHeaderPane;

    @FXML
    private JFXButton btClose, btAddNewAuthor, btAddCategory, btAddType, btAddTableLog;

    @FXML
    private TextField txtId, txtName, txtPage, txtQty, txtISBN, txtYear, txtPrice, txtTotalPrice;

    @FXML
    private ComboBox<String> cmbType, cmbCagtegory, cmbTable, cmbtableLog, cmbStatus;

    @FXML
    private ComboBox<String> cmbAuthor1, cmbAuthor2, cmbAuthor3, cmbAuthor4, cmbAuthor5, cmbAuthor6;

    @FXML
    private TableView<ImportModel> tableHistoryImportBook;

    @FXML
    private TableColumn<ImportModel, String> colBookid, colBookname, colBookisbn, colBookcategory, colBooktype, colYear,
            colTable, colLog;

    @FXML
    private TableColumn<ImportModel, Integer> colBookpage, colBookqty;

    @FXML
    private TableColumn<ImportModel, Double> colBookPrice, colTotalPrice;

    private void printBin(String importID) {

        task = new Task<Void>() {

            @Override
            protected Void call() throws Exception {
                masker.setVisible(true);
                masker.setProgressVisible(true);

                Map<String, Object> map = new HashMap<String, Object>();
                map.put("importid", importID);
                map.put("logo", Paths.get("bin/Logo.png").toAbsolutePath().toString());
                printbill.showReport(map, "billImport.jrxml", "Error print bin");
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
                masker.setVisible(false);
                masker.setProgressVisible(false);
                dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການພີມໃບບິນ", task.getException());
            }

        };
        new Thread(task).start();
    }

    private String getMaxImportId() {
        try {
            int maxId = importBook.getMaxID();
            String new_id = "";
            if (maxId <= 0)
                new_id = "IMP1";
            else {
                new_id = "IMP" + (maxId + 1);
            }
            return new_id;
        } catch (Exception e) {
            dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາຈັດການລະຫັດການນຳປຶ້ມເຂົ້າ", e);
            return "";
        }
    }

    private void initTable() {
        colBookid.setCellValueFactory(new PropertyValueFactory<>("bookid"));
        colBookname.setCellValueFactory(new PropertyValueFactory<>("bookname"));
        colBookisbn.setCellValueFactory(new PropertyValueFactory<>("bookisbn"));
        colBookpage.setCellValueFactory(new PropertyValueFactory<>("bookpage"));
        colBookcategory.setCellValueFactory(new PropertyValueFactory<>("bookcategory"));
        colBooktype.setCellValueFactory(new PropertyValueFactory<>("booktype"));
        colYear.setCellValueFactory(new PropertyValueFactory<>("writeYear"));
        colTable.setCellValueFactory(new PropertyValueFactory<>("tableid"));
        colLog.setCellValueFactory(new PropertyValueFactory<>("tbalelog"));
        colBookqty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colBookPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colTotalPrice.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));

        // Todo: Add column number
        final TableColumn<ImportModel, ImportModel> colNumber = new TableColumn<ImportModel, ImportModel>("ລຳດັບ");
        colNumber.setMinWidth(50);
        colNumber.setMaxWidth(120);
        colNumber.setPrefWidth(60);
        colNumber.setId("colCenter");
        colNumber.setCellValueFactory(
                new Callback<CellDataFeatures<ImportModel, ImportModel>, ObservableValue<ImportModel>>() {

                    @Override
                    public ObservableValue<ImportModel> call(CellDataFeatures<ImportModel, ImportModel> param) {
                        return new ReadOnlyObjectWrapper<ImportModel>(param.getValue());
                    }

                });
        colNumber.setCellFactory(
                new Callback<TableColumn<ImportModel, ImportModel>, TableCell<ImportModel, ImportModel>>() {

                    @Override
                    public TableCell<ImportModel, ImportModel> call(TableColumn<ImportModel, ImportModel> param) {
                        return new TableCell<ImportModel, ImportModel>() {
                            @Override
                            protected void updateItem(ImportModel item, boolean empty) {
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
        tableHistoryImportBook.getColumns().add(0, colNumber);

        // Todo: Add column Button
        addButtonToTable();
    }

    private void initRules() {
        validRules.setErrorDecorationEnabled(false);
        validRules.registerValidator(txtId, false, Validator.createEmptyValidator("ກະລຸນາປ້ອນລະຫັດປຶ້ມ"));
        validRules.registerValidator(txtName, false, Validator.createEmptyValidator("ກະລຸນາປ້ອນຊື່ປຶ້ມ"));
        validRules.registerValidator(txtPage, false, Validator.createEmptyValidator("ກະລຸນາປ້ອນຈຳນວນໜ້າ"));
        validRules.registerValidator(txtQty, false, Validator.createEmptyValidator("ກະລຸນາປ້ອນຈຳນວນປື້ມ"));
        validRules.registerValidator(txtPrice, false, Validator.createEmptyValidator("ກະລຸນາປ້ອນລາຄາປື້ມ"));

        validRules.registerValidator(txtISBN, false,
                Validator.createEmptyValidator("ກະລຸນາປ້ອນເລກ ISBN (ຖ້າມີ)", Severity.WARNING));
        validRules.registerValidator(txtYear, false,
                Validator.createEmptyValidator("ກະລຸນາລະບຸປີແຕ່ງ (ຖ້າມີ)", Severity.WARNING));

        validRules.registerValidator(cmbType, false, Validator.createEmptyValidator("ກະລຸນາເລືອກປະເພດປຶ້ມ"));
        validRules.registerValidator(cmbCagtegory, false, Validator.createEmptyValidator("ກະລຸນາເລືອກໝວດປຶ້ມ"));
        validRules.registerValidator(cmbTable, false, Validator.createEmptyValidator("ກະລຸນາເລືອກເລກຕູ້"));
        validRules.registerValidator(cmbtableLog, false, Validator.createEmptyValidator("ກະລຸນາເລືອກເລກລ໋ອກຕູ້"));
        validRules.registerValidator(cmbStatus, false, Validator.createEmptyValidator("ກະລຸນາກຳນົດສະຖານະຂອງປຶ້ມ"));

        validRules.registerValidator(cmbAuthor1, false,
                Validator.createEmptyValidator("ກະລຸນາລະບຸຊື່ຜູ້ແຕ່ງປຶ້ມ (ຖ້າມີ)", Severity.WARNING));
        validRules.registerValidator(cmbAuthor2, false,
                Validator.createEmptyValidator("ກະລຸນາລະບຸຊື່ຜູ້ແຕ່ງປຶ້ມ (ຖ້າມີ)", Severity.WARNING));
        validRules.registerValidator(cmbAuthor3, false,
                Validator.createEmptyValidator("ກະລຸນາລະບຸຊື່ຜູ້ແຕ່ງປຶ້ມ (ຖ້າມີ)", Severity.WARNING));
        validRules.registerValidator(cmbAuthor4, false,
                Validator.createEmptyValidator("ກະລຸນາລະບຸຊື່ຜູ້ແຕ່ງປຶ້ມ (ຖ້າມີ)", Severity.WARNING));
        validRules.registerValidator(cmbAuthor5, false,
                Validator.createEmptyValidator("ກະລຸນາລະບຸຊື່ຜູ້ແຕ່ງປຶ້ມ (ຖ້າມີ)", Severity.WARNING));
        validRules.registerValidator(cmbAuthor6, false,
                Validator.createEmptyValidator("ກະລຸນາລະບຸຊື່ຜູ້ແຕ່ງປຶ້ມ (ຖ້າມີ)", Severity.WARNING));
    }

    private void showAddCategory() {
        try {
            final FXMLLoader loader = new FXMLLoader(App.class.getResource("frmBookCategory.fxml"));
            final Parent catgroot = loader.load();
            final BookCategoryController categoryController = loader.getController();
            final Scene scene = new Scene(catgroot);
            scene.setFill(Color.TRANSPARENT);
            Stage stage = new Stage();
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setTitle("Add New Book Category");
            stage.setScene(scene);
            categoryController.initConstructor3(this, stage);
            stage.getIcons().add(new Image("/com/mycompany/library_project/Icon/icon.png"));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (Exception e) {
            dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການເປີດຟອມຈັດການຂໍ້ມູນໝວດປຶ້ມ", e);
        }
    }

    private void showAddType() {
        try {
            final FXMLLoader loader = new FXMLLoader(App.class.getResource("frmBookType.fxml"));
            final Parent typeroot = loader.load();
            final BookTypeController typeController = loader.getController();
            final Scene scene = new Scene(typeroot);
            scene.setFill(Color.TRANSPARENT);
            Stage stage = new Stage();
            stage.initStyle(StageStyle.TRANSPARENT);
            typeController.initConstructor3(this, stage);
            stage.setTitle("Add New Book Type");
            stage.setScene(scene);
            stage.getIcons().add(new Image("/com/mycompany/library_project/Icon/icon.png"));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (Exception e) {
            dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການເປີດຟອມຈັດການຂໍ້ມູນປະເພດປຶ້ມ", e);
        }
    }

    private void showAddTablesLog() {
        try {
            final FXMLLoader loader = new FXMLLoader(App.class.getResource("frmTableLogs.fxml"));
            final Parent tableroot = loader.load();
            final TableLogController tableLogController = loader.getController();
            final Scene scene = new Scene(tableroot);
            scene.setFill(Color.TRANSPARENT);
            final Stage stage = new Stage();
            stage.initStyle(StageStyle.TRANSPARENT);
            tableLogController.initConstructor3(this, stage);
            stage.setTitle("Add New Table Log");
            stage.setScene(scene);
            stage.getIcons().add(new Image("/com/mycompany/library_project/Icon/icon.png"));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (Exception e) {
            dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການເປີດຟອມຈັດການຂໍ້ມູນລ໋ອກຕູ້", e);
        }
    }

    private void generatedBarcode() {
        try {
            // txtBarcode.clear();
            // if (!txtId.getText().equals("") && !txtQty.getText().equals("")) {
            // for (int i = 1; i <= Integer.parseInt(txtQty.getText()); i++) {
            // txtBarcode.appendText(txtId.getText() + "000" + i + "\n");
            // }
            // }
        } catch (Exception e) {
            dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການສ້າງລະຫັດບາໂຄດ", e);
        }
    }

    private void clearText() {
        validRules.setErrorDecorationEnabled(false);
        txtId.clear();
        txtName.clear();
        txtISBN.clear();
        txtPage.clear();
        txtQty.clear();
        txtYear.clear();
        txtPrice.clear();
        txtTotalPrice.clear();
        cmbCagtegory.getSelectionModel().clearSelection();
        cmbType.getSelectionModel().clearSelection();
        cmbTable.getSelectionModel().clearSelection();
        cmbtableLog.getSelectionModel().clearSelection();
        cmbStatus.getSelectionModel().select(0);

        cmbAuthor1.getSelectionModel().clearSelection();
        cmbAuthor2.getSelectionModel().clearSelection();
        cmbAuthor3.getSelectionModel().clearSelection();
        cmbAuthor4.getSelectionModel().clearSelection();
        cmbAuthor5.getSelectionModel().clearSelection();
        cmbAuthor6.getSelectionModel().clearSelection();

        cmbAuthor2.setVisible(false);
        cmbAuthor3.setVisible(false);
        cmbAuthor4.setVisible(false);
        cmbAuthor5.setVisible(false);
        cmbAuthor6.setVisible(false);

        arr_author = new String[6];

    }

    private void clearAllVal() {
        categoryId = "";
        typeId = "";
        _totalPrice = 0.0;
        _totalQty = 0;
        _bookid = "";
        listAuthor.clear();
        tableHistoryImportBook.getItems().clear();
        // btSave.setDisable(true);
    }

    private void showAddAuthor() {
        try {
            final FXMLLoader loader = new FXMLLoader(App.class.getResource("frmAuthor.fxml"));
            final Parent subForm = loader.load();
            AuthorController authorController = loader.getController();
            final Scene scene = new Scene(subForm);
            scene.setFill(Color.TRANSPARENT);
            final Stage stage = new Stage();
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setScene(scene);
            authorController.initConstructor3(this, stage);
            stage.setTitle("Add New Author");
            stage.getIcons().add(new Image("/com/mycompany/library_project/Icon/icon.png"));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
            ;
        } catch (Exception e) {
            dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການເປີດຟອມຈັດການຂໍ້ມູນນັກແຕ່ງ", e);
        }
    }

    private void initKeyEvents() {
        txtId.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                txtName.requestFocus();
                if (!txtId.getText().equals("")) {
                    try {
                        final ResultSet rs = addBook.findById(txtId.getText());
                        if (rs.next()) {
                            txtName.setText(rs.getString("book_name"));
                            txtISBN.setText(rs.getString("ISBN"));
                            txtPage.setText(rs.getString("page"));
                            txtYear.setText(rs.getString("write_year"));
                            cmbCagtegory.getSelectionModel().select(rs.getString("catg_name"));
                            cmbType.getSelectionModel().select(rs.getString("type_name"));
                            cmbTable.getSelectionModel().select(rs.getString("table_id"));
                        }
                    } catch (Exception e) {
                    }
                }
            }
        });
        txtName.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER)
                txtISBN.requestFocus();
        });
        txtISBN.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER)
                txtYear.requestFocus();
        });
        txtYear.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER)
                txtPage.requestFocus();
        });
        txtPage.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER)
                txtQty.requestFocus();
        });
        txtQty.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER)
                txtPrice.requestFocus();
        });

    }

    private void initEvents() {

        btAddNewAuthor.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                showAddAuthor();
            }

        });
        btAddCategory.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                showAddCategory();
            }

        });
        btAddType.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                showAddType();
            }

        });
        btAddTableLog.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                showAddTablesLog();
            }
        });
        txtPage.textProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    txtPage.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }

        });

        txtQty.textProperty().addListener(new ChangeListener<String>() {
            // Todo: set properties type only numeric
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    txtQty.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }

        });

        txtQty.setOnKeyTyped(e -> {
            txtTotalPrice.clear();
            if (!txtQty.getText().equals("") && !txtPrice.getText().equals(""))
                txtTotalPrice.setText(
                        Double.toString(Integer.parseInt(txtQty.getText()) * Double.parseDouble(txtPrice.getText())));
        });

        txtPrice.setOnKeyTyped(e -> {
            txtTotalPrice.clear();
            if (!txtQty.getText().equals("") && !txtPrice.getText().equals(""))
                txtTotalPrice.setText(
                        Double.toString(Integer.parseInt(txtQty.getText()) * Double.parseDouble(txtPrice.getText())));
        });

        txtId.setOnKeyTyped(e -> {
            generatedBarcode();
        });
        txtQty.setOnKeyTyped(e -> {
            generatedBarcode();
        });

    }

    public void fillType() {
        // Todo: set value
        try {
            arr_type = new ArrayList<>();
            items = FXCollections.observableArrayList();
            ResultSet rs = type.findAll();
            while (rs.next()) {
                items.add(rs.getString("type_name"));
                arr_type.add(new TypeModel(rs.getString("type_id"), rs.getString("type_name")));
            }
            cmbType.setItems(items);
        } catch (Exception e) {
            alertMessage.showErrorMessage("Load type", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
        }
    }

    public void fillCategory() {
        // Todo: set value
        try {
            arr_category = new ArrayList<>();
            items = FXCollections.observableArrayList();
            ResultSet rs = category.findAll();
            while (rs.next()) {
                items.add(rs.getString("catg_name"));
                arr_category.add(new CategoryModel(rs.getString("catg_id"), rs.getString("catg_name")));
            }
            cmbCagtegory.setItems(items);
        } catch (Exception e) {
            alertMessage.showErrorMessage("Load category", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
        }
    }

    public void fillTable() {
        try {
            items = FXCollections.observableArrayList();
            ResultSet rs = table.findAll();
            while (rs.next()) {
                items.add(rs.getString(1));
            }
            cmbTable.setItems(items);
        } catch (Exception e) {
            alertMessage.showErrorMessage("Load table", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
        }

        cmbTable.setOnAction(e -> {
            try {
                // index_table = cmbTable.getSelectionModel().getSelectedIndex();
                if (cmbTable.getSelectionModel().getSelectedItem() != null) {
                    items = FXCollections.observableArrayList();
                    ResultSet rs = table.findById(cmbTable.getSelectionModel().getSelectedItem().toString());
                    while (rs.next()) {
                        items.add(rs.getString(2));
                    }
                    cmbtableLog.setItems(items);
                }
            } catch (Exception ex) {
                alertMessage.showErrorMessage("Load Table Log", "Error: " + ex.getMessage(), 4, Pos.BOTTOM_RIGHT);
            }
        });
    }

    private void setAuthoID(int index, String authName) {
        for (AuthorModel val : author_id) {
            if (val.getFull_name().equals(authName)) {
                arr_author[index] = val.getAuthor_id();
                break;
            }
        }
    }

    public void fillAuthor() {
        try {
            author_items = FXCollections.observableArrayList();
            author_id = new ArrayList<>();
            rs = author.findAll();
            while (rs.next()) {
                author_items.add(rs.getString("full_name") + " " + rs.getString("sur_name"));
                author_id.add(new AuthorModel(rs.getString("author_id"),
                        rs.getString("full_name") + " " + rs.getString("sur_name")));
            }
            cmbAuthor1.setItems(author_items);

        } catch (Exception e) {
            alertMessage.showErrorMessage("Load Author", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
        }

        // Todo: ComboBox 1
        cmbAuthor1.setOnAction(selectItem -> {
            if (cmbAuthor1.getSelectionModel().getSelectedItem() != null) {

                cmbAuthor2.setVisible(true);
                cmbAuthor2.getItems().clear();

                author_items.forEach(item -> {
                    if (item != cmbAuthor1.getSelectionModel().getSelectedItem()) {
                        cmbAuthor2.getItems().add(item);
                    }
                });

                // Todo: Set author id
                setAuthoID(0, cmbAuthor1.getSelectionModel().getSelectedItem());
            }
        });

        // Todo: ComboBox 2
        cmbAuthor2.setOnAction(selectItem -> {
            if (cmbAuthor2.getSelectionModel().getSelectedItem() != null) {
                cmbAuthor3.setVisible(true);
                cmbAuthor3.getItems().clear();
                author_items.forEach(item -> {
                    if (!item.equals(cmbAuthor1.getSelectionModel().getSelectedItem())
                            && !item.equals(cmbAuthor2.getSelectionModel().getSelectedItem())) {
                        cmbAuthor3.getItems().add(item);
                    }
                });

                // Todo: Set author id
                setAuthoID(1, cmbAuthor2.getSelectionModel().getSelectedItem());
            } else {
                cmbAuthor2.setVisible(false);
                arr_author[1] = null;
            }
        });

        // Todo: ComboBox 3
        cmbAuthor3.setOnAction(selectItem -> {
            if (cmbAuthor3.getSelectionModel().getSelectedItem() != null) {
                cmbAuthor4.setVisible(true);
                cmbAuthor4.getItems().clear();
                author_items.forEach(item -> {
                    if (!item.equals(cmbAuthor1.getSelectionModel().getSelectedItem())
                            && !item.equals(cmbAuthor2.getSelectionModel().getSelectedItem())
                            && !item.equals(cmbAuthor3.getSelectionModel().getSelectedItem())) {
                        cmbAuthor4.getItems().add(item);
                    }
                });

                // Todo: Set author id
                setAuthoID(2, cmbAuthor3.getSelectionModel().getSelectedItem());
            } else {
                cmbAuthor4.setVisible(false);
                arr_author[2] = null;
            }
        });

        // Todo: ComboBox 4
        cmbAuthor4.setOnAction(selectItem -> {
            if (cmbAuthor4.getSelectionModel().getSelectedItem() != null) {
                cmbAuthor5.setVisible(true);
                cmbAuthor5.getItems().clear();
                author_items.forEach(item -> {
                    if (!item.equals(cmbAuthor1.getSelectionModel().getSelectedItem())
                            && !item.equals(cmbAuthor2.getSelectionModel().getSelectedItem())
                            && !item.equals(cmbAuthor3.getSelectionModel().getSelectedItem())
                            && !item.equals(cmbAuthor4.getSelectionModel().getSelectedItem())) {
                        cmbAuthor5.getItems().add(item);
                    }
                });

                // Todo: Set author id
                setAuthoID(3, cmbAuthor4.getSelectionModel().getSelectedItem());
            } else {
                cmbAuthor5.setVisible(false);
                arr_author[3] = null;
            }
        });

        // Todo: ComboBox 5
        cmbAuthor5.setOnAction(selectItem -> {
            if (cmbAuthor5.getSelectionModel().getSelectedItem() != null) {
                cmbAuthor6.setVisible(true);
                cmbAuthor6.getItems().clear();
                author_items.forEach(item -> {
                    if (!item.equals(cmbAuthor1.getSelectionModel().getSelectedItem())
                            && !item.equals(cmbAuthor2.getSelectionModel().getSelectedItem())
                            && !item.equals(cmbAuthor3.getSelectionModel().getSelectedItem())
                            && !item.equals(cmbAuthor4.getSelectionModel().getSelectedItem())
                            && !item.equals(cmbAuthor5.getSelectionModel().getSelectedItem())) {
                        cmbAuthor6.getItems().add(item);
                    }
                });

                // Todo: Set author id
                setAuthoID(4, cmbAuthor5.getSelectionModel().getSelectedItem());
            } else {
                cmbAuthor6.setVisible(false);
                arr_author[4] = null;
            }
        });
        // Todo: ComboBox 6
        cmbAuthor6.setOnAction(selectItem -> {
            if (cmbAuthor6.getSelectionModel().getSelectedItem() != null) {
                // Todo: Set author id
                setAuthoID(5, cmbAuthor5.getSelectionModel().getSelectedItem());
            } else {
                arr_author[5] = null;
            }
        });
    }

    private String newBarcodeID(String id, int increment) {
        String idStr = "";
        char[] arr = id.toCharArray();

        // Todo: Sub only the String
        for (char c : arr) {
            if (!Character.toString(c).matches("\\d*")) {
                idStr += c;
            }
        }

        String idInt = id.substring(idStr.length()); // Todo: Sub only the numeric
        // int idAdd = Integer.parseInt(idInt) + 1; // Todo: convert to Int and
        // increment by 1
        return idStr + idInt.substring(0, idInt.length() - Integer.toString(increment).length()) + increment;
    }

    @FXML
    private void addBook(ActionEvent event) {
        if (!txtId.getText().equals("") && !txtName.getText().equals("") && !txtPage.getText().equals("")
                && !txtQty.getText().equals("") && !cmbCagtegory.getSelectionModel().getSelectedItem().equals(null)
                && !cmbType.getSelectionModel().getSelectedItem().equals(null)
                && !cmbTable.getSelectionModel().getSelectedItem().equals(null)) {

            tableHistoryImportBook.getItems().add(new ImportModel(txtId.getText(), txtName.getText(), txtISBN.getText(),
                    Integer.parseInt(txtPage.getText()), cmbCagtegory.getSelectionModel().getSelectedItem().toString(),
                    cmbType.getSelectionModel().getSelectedItem().toString(), txtYear.getText(),
                    cmbTable.getSelectionModel().getSelectedItem().toString(),
                    cmbtableLog.getSelectionModel().getSelectedItem().toString(), Integer.parseInt(txtQty.getText()),
                    Double.parseDouble(txtPrice.getText()), Double.parseDouble(txtTotalPrice.getText())));

            _totalPrice += Double.parseDouble(txtPrice.getText());
            _totalQty += Double.parseDouble(txtQty.getText());

            listAuthor.add(arr_author);
            // btSave.setDisable(false);
            clearText();
        } else {
            validRules.setErrorDecorationEnabled(true);
            alertMessage.showWarningMessage("Save Warning", "Please chack your information and try again.", 4,
                    Pos.BOTTOM_RIGHT);
        }
    }

    @FXML
    private void btSave(ActionEvent event) {
        try {
            String importid = getMaxImportId(), result = null;
            importBook = new ImportModel(importid, _totalQty, _totalPrice, Date.valueOf(LocalDate.now()));

            if (importBook.saveData() > 0) {

                int index = 0;
                for (ImportModel model : tableHistoryImportBook.getItems()) {

                    importBook = new ImportModel(importid, model.getBookid(), model.getQty(), model.getPrice());
                    addBook = new BookDetailModel(model.getBookid(), model.getBookname(), model.getBookisbn(),
                            model.getBookpage(), model.getQty(), categoryId, typeId, model.getTableid(),
                            model.getWriteYear(), "");

                    final ResultSet rs = addBook.getMaxBarcodeID(model.getBookid());
                    String idSub = "0";
                    if (rs.next()) {
                        _bookid = rs.getString("book_id");
                        char[] arr = rs.getString("maxbarcodeid").toCharArray();
                        // Todo: Sub only the String

                        for (char c : arr) {
                            if (Character.toString(c).matches("\\d*")) {
                                idSub += c;
                            }
                        }
                    }

                    final ArrayList<BookDetailModel> listBarcode = new ArrayList<>();
                    for (int i = 1; i <= model.getQty(); i++) {
                        listBarcode.add(new BookDetailModel(
                                newBarcodeID(model.getBookid() + "0000", Integer.parseInt(idSub) + i),
                                model.getBookid(), model.getTbalelog(), "ຫວ່າງ"));
                    }

                    // Todo: Add Write Book
                    final ArrayList<BookDetailModel> listWrite = new ArrayList<>();
                    for (String val : listAuthor.get(index)) {
                        if (val != null) {
                            listWrite.add(new BookDetailModel(model.getBookid(), val));
                        }
                    }

                    // Todo: find the category id
                    for (CategoryModel val : arr_category) {
                        if (val.getCatgName().equals(model.getBookcategory())) {
                            categoryId = val.getCatgId();
                            break;
                        }
                    }

                    // Todo: find the type id
                    for (TypeModel val : arr_type) {
                        if (val.getTypeName().equals(model.getBooktype())) {
                            typeId = val.getTypeId();
                            break;
                        }
                    }

                    // Todo: Save All Data
                    if (importBook.saveDataImportDetail(importid) > 0) {
                        if (_bookid == "") {
                            // Todo: Save New Book Data
                            if (addBook.saveData() > 0) {
                                // Todo: Save Barcode
                                if (addBook.saveBookBarCode(listBarcode, txtId.getText()) > 0) {
                                    // Todo: Save Write
                                    if (addBook.saveWrite(listWrite) > 0) {
                                        result = "Save successfully";
                                    }
                                } else {
                                    result = null;
                                    return;
                                }
                            } else {
                                result = null;
                                return;
                            }
                        } else {
                            // Todo: Update Qty Of Books
                            if (addBook.updateBookQty(model.getBookid(), model.getQty()) > 0) {
                                // Todo: Save Barcode
                                if (addBook.saveBookBarCode(listBarcode, null) > 0) {
                                    result = "Save successfully";
                                    _bookid = "";
                                } else {
                                    result = null;
                                    return;
                                }
                            } else {
                                result = null;
                                return;
                            }
                        }
                    } else {
                        result = null;
                        return;
                    }
                    index++;
                }
            } else {
                result = null;
            }
            if (result != null) {
                alertMessage.showCompletedMessage("Saved", "Save data successfully.", 4, Pos.BOTTOM_RIGHT);
                printBin(importid);
                clearAllVal();
            }

        } catch (Exception e) {
            dialog.showExcectionDialog("Error", null, "ເກິດບັນຫາໃນການບັນທືກຂໍ້ມູນນຳປຶ້ມເຂົ້າລະບົບ", e);
            e.printStackTrace();
        }

    }

    @FXML
    private void btClearText() {
        clearText();
    }

    @FXML
    private void closeForm() {
        if (BookController.addNewBook != null) {
            final Stage stage = (Stage) borderPane.getScene().getWindow();
            stage.close();
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        masker.setVisible(false);
        masker.setText("ກຳລັງໂຫລດຂໍ້ມູນ, ກະລຸນາລໍຖ້າ...");
        masker.setStyle("-fx-font-family: BoonBaan;");
        stackPane.getChildren().add(masker);

        cmbStatus.setItems(status);
        cmbStatus.getSelectionModel().select(0);
        initRules();
        initTable();
        cmbAuthor2.setVisible(false);
        cmbAuthor3.setVisible(false);
        cmbAuthor4.setVisible(false);
        cmbAuthor5.setVisible(false);
        cmbAuthor6.setVisible(false);
        initEvents();
        initKeyEvents();
        fillType();
        fillCategory();
        fillTable();
        fillAuthor();
    }

    private void addButtonToTable() {
        TableColumn<ImportModel, Void> colAction = new TableColumn<>("Action");
        colAction.setPrefWidth(100);
        Callback<TableColumn<ImportModel, Void>, TableCell<ImportModel, Void>> cellFactory = new Callback<TableColumn<ImportModel, Void>, TableCell<ImportModel, Void>>() {

            @Override
            public TableCell<ImportModel, Void> call(TableColumn<ImportModel, Void> param) {
                final TableCell<ImportModel, Void> cell = new TableCell<ImportModel, Void>() {
                    final JFXButton delete = new JFXButton("ຍົກເລີກ");
                    {
                        final ImageView imgView = new ImageView();
                        imgView.setImage(new Image("/com/mycompany/library_project/Icon/bin.png"));
                        imgView.setFitWidth(20);
                        imgView.setFitHeight(20);
                        delete.setGraphic(imgView);
                        delete.setStyle(Style.buttonStyle);
                        delete.setOnAction(e -> {
                            Optional<ButtonType> result = dialog.showComfirmDialog("Comfirmed", null,
                                    "ຕ້ອງການຍົກເລີກ ຫຼື ບໍ?");
                            if (result.get() == ButtonType.YES) {
                                _totalPrice -= tableHistoryImportBook.getItems().get(getIndex()).getPrice();
                                _totalQty -= tableHistoryImportBook.getItems().get(getIndex()).getQty();
                                tableHistoryImportBook.getItems().remove(getIndex());
                                listAuthor.remove(getIndex());
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
        tableHistoryImportBook.getColumns().add(colAction);
    }
}
