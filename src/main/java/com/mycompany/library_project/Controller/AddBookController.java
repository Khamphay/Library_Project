package com.mycompany.library_project.Controller;

import javafx.beans.value.*;
import javafx.collections.*;
import javafx.concurrent.Task;
import javafx.event.*;
import javafx.fxml.*;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.*;

import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.mycompany.library_project.App;
import com.mycompany.library_project.ControllerDAOModel.*;
import com.mycompany.library_project.Model.*;
import com.mycompany.library_project.Report.CreateReport;

import org.controlsfx.control.MaskerPane;
import org.controlsfx.validation.*;

public class AddBookController implements Initializable {

    private ValidationSupport validRules = new ValidationSupport();
    private BookController bookcontroller;
    private ResultSet rs = null;
    private AlertMessage alertMessage = new AlertMessage();
    private TypeModel type = new TypeModel();
    private CategoryModel category = new CategoryModel();
    private TableLogModel table = new TableLogModel();
    private AuthorModel author = new AuthorModel();
    private DialogMessage dialog = new DialogMessage();
    private BookDetailModel addBook = new BookDetailModel();
    private Task<Void> task = null;
    private MaskerPane masker = new MaskerPane();

    private ArrayList<AuthorModel> author_id = null;
    private ArrayList<String> arr_type, arr_category;
    private ObservableList<String> status = FXCollections.observableArrayList("ຫວ່າງ", "ກຳລ້ງຢືມ", "ເສຍ");;
    private ObservableList<String> items = null, author_items = null;

    private String[] arr_author = new String[6];
    private String[] oldarr_author = new String[6];

    private int index_type, index_category;
    private String bookid = "";
    private double x, y;

    /*
     * Todo: Use from class BookController for both form can communicate or use for
     * Refresh table after add and delete
     */
    public void initConstructor1(HomeController homeController) {
        btClose.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                homeController.showMainMenuHome();
            }
        });
    }

    public void initConstructor2(BookController bookcontroller, BookDetailModel book) {
        this.bookcontroller = bookcontroller;
        moveForm();
        btClose.setVisible(true);
        btClose.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                BookController.addNewBook.close();
            }

        });

        if (book != null) {
            txtQty.setEditable(false);
            cmbStatus.setDisable(true);

            bookid = book.getBookId();
            txtId.setText(book.getBookId());
            txtName.setText(book.getBookName());
            txtISBN.setText(book.getISBN());
            txtPage.setText(book.getPage().toString());
            txtQty.setText(book.getQty().toString());
            txtYear.setText(book.getWrite_year());
            txtDetail.setText(book.getDetail());
            cmbType.getSelectionModel().select(book.getTypeId());
            index_type = cmbType.getSelectionModel().getSelectedIndex();

            cmbCagtegory.getSelectionModel().select(book.getCatgId());
            index_category = cmbCagtegory.getSelectionModel().getSelectedIndex();

            cmbTable.getSelectionModel().select(book.getTableId());

            try {
                // book = new BookDetailModel();
                rs = book.showBarcode(bookid);
                while (rs.next()) {
                    txtBarcode.appendText(rs.getString("barcode") + "\n");
                }
            } catch (SQLException e) {
                alertMessage.showErrorMessage("Load Barcode Error", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
            }

            try {
                rs = book.showWrite(bookid);
                int index = 0;
                String[] author_name = new String[6];
                while (rs.next()) {
                    oldarr_author[index] = rs.getString("author_id");
                    author_name[index] = rs.getString("full_name") + " " + rs.getString("sur_name");
                    index++;
                }

                if (author_name[0] != null)
                    cmbAuthor1.getSelectionModel().select(author_name[0]);
                cmbAuthor2.setVisible(true);
                if (author_name[1] != null) {
                    cmbAuthor3.setVisible(true);
                    cmbAuthor2.getSelectionModel().select(author_name[1]);
                }
                if (author_name[2] != null) {
                    cmbAuthor4.setVisible(true);
                    cmbAuthor3.getSelectionModel().select(author_name[2]);
                }
                if (author_name[3] != null) {
                    cmbAuthor5.setVisible(true);
                    cmbAuthor4.getSelectionModel().select(author_name[3]);
                }
                if (author_name[4] != null) {
                    cmbAuthor6.setVisible(true);
                    cmbAuthor5.getSelectionModel().select(author_name[4]);
                }
                if (author_name[5] != null) {
                    cmbAuthor6.getSelectionModel().select(author_name[5]);
                }

            } catch (Exception e) {
                alertMessage.showErrorMessage("Load Write Error", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
            }
        }
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
    private TextField txtId, txtName, txtPage, txtQty, txtISBN, txtYear;

    @FXML
    private TextArea txtBarcode, txtDetail;

    @FXML
    private ComboBox<String> cmbType, cmbCagtegory, cmbTable, cmbtableLog, cmbStatus;

    @FXML
    private ComboBox<String> cmbAuthor1, cmbAuthor2, cmbAuthor3, cmbAuthor4, cmbAuthor5, cmbAuthor6;

    private void initRules() {
        validRules.setErrorDecorationEnabled(false);
        validRules.registerValidator(txtId, false, Validator.createEmptyValidator("ກະລຸນາປ້ອນລະຫັດປຶ້ມ"));
        validRules.registerValidator(txtName, false, Validator.createEmptyValidator("ກະລຸນາປ້ອນຊື່ປຶ້ມ"));
        validRules.registerValidator(txtPage, false, Validator.createEmptyValidator("ກະລຸນາປ້ອນຈຳນວນໜ້າ"));
        validRules.registerValidator(txtQty, false, Validator.createEmptyValidator("ກະລຸນາປ້ອນຈຳປື້ມ"));

        validRules.registerValidator(txtISBN, false,
                Validator.createEmptyValidator("ກະລຸນາປ້ອນເລກ ISBN (ຖ້າມີ)", Severity.WARNING));
        validRules.registerValidator(txtYear, false,
                Validator.createEmptyValidator("ກະລຸນາລະບຸປີແຕ່ງ (ຖ້າມີ)", Severity.WARNING));

        validRules.registerValidator(txtBarcode, false, Validator.createEmptyValidator("ລະຫັດ Barcode ຕ້ອງບໍ່ຫວ່າງ"));
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
            categoryController.initConstructor2(this, stage);
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
            typeController.initConstructor2(this, stage);
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
            tableLogController.initConstructor2(this, stage);
            stage.setTitle("Add New Table Log");
            stage.setScene(scene);
            stage.getIcons().add(new Image("/com/mycompany/library_project/Icon/icon.png"));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (Exception e) {
            dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການເປີດຟອມຈັດການຂໍ້ມູນລ໋ອກຕູ້", e);
        }
    }

    private void moveForm() {
        acHeaderPane.setOnMousePressed(mouseEvent -> {
            x = mouseEvent.getSceneX();
            y = mouseEvent.getSceneY();
        });

        acHeaderPane.setOnMouseDragged(mouseEvent -> {
            BookController.addNewBook.setX(mouseEvent.getScreenX() - x);
            BookController.addNewBook.setY(mouseEvent.getScreenY() - y);
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
        String newMaxID = idStr + idInt.substring(0, idInt.length() - Integer.toString(increment).length()) + increment;
        return newMaxID;
    }

    private void generatedBarcode() {
        try {
            txtBarcode.clear();
            if (!txtId.getText().equals("") && !txtQty.getText().equals("")) {
                for (int i = 1; i <= Integer.parseInt(txtQty.getText()); i++) {
                    txtBarcode.appendText(newBarcodeID(txtId.getText() + "0000", i) + "\n");
                }
            }
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
        txtYear.clear();
        txtQty.clear();
        txtQty.setEditable(true);
        txtBarcode.clear();
        txtDetail.clear();
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

        bookid = "";
        arr_author = new String[6];
        oldarr_author = new String[6];

    }

    private void showAddAuthor() {
        try {
            final FXMLLoader loader = new FXMLLoader(App.class.getResource("frmAuthor.fxml"));
            final Parent subForm = loader.load();
            final AuthorController authorController = loader.getController();
            final Scene scene = new Scene(subForm);
            scene.setFill(Color.TRANSPARENT);
            final Stage stage = new Stage();
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setScene(scene);
            authorController.initConstructor2(this, stage);
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
            if (keyEvent.getCode() == KeyCode.ENTER)
                txtName.requestFocus();
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
                cmbCagtegory.requestFocus();
        });
        cmbCagtegory.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER)
                cmbType.requestFocus();
        });
        cmbType.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER)
                cmbStatus.requestFocus();
        });
        cmbStatus.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER)
                cmbTable.requestFocus();
        });
        cmbTable.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER)
                cmbtableLog.requestFocus();
        });
        cmbtableLog.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER)
                txtDetail.requestFocus();
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

        txtId.setOnKeyTyped(e -> {
            generatedBarcode();
        });
        txtQty.setOnKeyTyped(e -> {
            generatedBarcode();
        });

    }

    public void fillType() {

        // Todo: set event to combo box
        cmbType.setOnAction(e -> {
            index_type = cmbType.getSelectionModel().getSelectedIndex();
        });

        // Todo: set value
        try {
            arr_type = new ArrayList<>();
            items = FXCollections.observableArrayList();
            // type = new TypeModel();
            ResultSet rs = type.findAll();
            while (rs.next()) {
                items.add(rs.getString(2));
                arr_type.add(rs.getString(1));
            }
            cmbType.setItems(items);
        } catch (Exception e) {
            alertMessage.showErrorMessage("Load type", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
        }
    }

    public void fillCategory() {
        // Todo: set event to combo box
        cmbCagtegory.setOnAction(e -> {
            index_category = cmbCagtegory.getSelectionModel().getSelectedIndex();
        });

        // Todo: set value
        try {
            arr_category = new ArrayList<>();
            items = FXCollections.observableArrayList();
            // category = new CategoryModel();
            ResultSet rs = category.findAll();
            while (rs.next()) {
                arr_category.add(rs.getString(1));
                items.add(rs.getString(2));
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

    private void printBarcode(String book_id) {
        task = new Task<Void>() {

            @Override
            protected Void call() throws Exception {
                masker.setVisible(true);
                masker.setProgressVisible(true);
                final CreateReport printBarcode = new CreateReport();
                final Map<String, Object> map = new HashMap<String, Object>();

                map.put("bookid", book_id);
                printBarcode.showReport(map, "printBarcodeByBookId.jrxml", "Print Barcode Error");
                return null;
            }

            @Override
            protected void succeeded() {
                masker.setVisible(false);
                masker.setProgressVisible(false);
            }

            @Override
            protected void failed() {
                masker.setVisible(false);
                masker.setProgressVisible(false);
                dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການພິມບາໂຄດ", task.getException());
            }
        };
        new Thread(task).start();
    }

    @FXML
    private void btSave(ActionEvent event) {
        // cmbType.g
        String msg = null;
        try {
            if (!txtId.getText().equals("") && !txtName.getText().equals("") && !txtPage.getText().equals("")
                    && !txtQty.getText().equals("") && !cmbCagtegory.getSelectionModel().getSelectedItem().equals(null)
                    && !cmbType.getSelectionModel().getSelectedItem().equals(null)
                    && !cmbTable.getSelectionModel().getSelectedItem().equals(null)) {

                if (bookid == "" && addBook.findById(txtId.getText()).next()) {// Todo: chack if insert new book
                    dialog.showWarningDialog(null,
                            "ລະຫັດປຶ້ມຊ້ຳກັບຂໍ້ມູນທີ່ມີຢູ່ໃນລະບົບກະລຸນາກວດສອບຂໍ້ມູນ ແລ້ວລອງບັນທຶກໃຫມ່ອີກຄັ້ງ");
                    return;
                }

                addBook = new BookDetailModel(txtId.getText(), txtName.getText(), txtISBN.getText(),
                        Integer.parseInt(txtPage.getText()), Integer.parseInt(txtQty.getText()),
                        arr_category.get(index_category), arr_type.get(index_type),
                        cmbTable.getSelectionModel().getSelectedItem().toString(), txtYear.getText(),
                        txtDetail.getText());

                final ArrayList<BookDetailModel> list = new ArrayList<>();
                String line = txtBarcode.getText();
                String[] lineCount = line.split("\n");
                // Todo: Save Barcode
                for (int i = 0; i < lineCount.length; i++) {
                    list.add(new BookDetailModel(lineCount[i], txtId.getText(),
                            cmbtableLog.getSelectionModel().getSelectedItem(),
                            cmbStatus.getSelectionModel().getSelectedItem().toString()));
                }

                final ArrayList<BookDetailModel> listWrite = new ArrayList<>();
                for (String val : arr_author) {
                    if (val != null) {
                        listWrite.add(new BookDetailModel(txtId.getText(), val));
                    }
                }
                if (bookid == "") {
                    if (addBook.saveData() > 0) {
                        if (addBook.saveBookBarCode(list, txtId.getText()) > 0) {
                            // Todo: Save Write
                            if (addBook.saveWrite(listWrite) > 0) {
                                alertMessage.showCompletedMessage("Saved", "Save data successfully.", 4,
                                        Pos.BOTTOM_RIGHT);
                                printBarcode(txtId.getText());
                            }
                        }
                    } else {
                        msg = null;
                    }
                } else {
                    if (addBook.updateData() > 0) {
                        int old_id = 0;
                        if (oldarr_author[0] != null) {
                            for (String val : arr_author) {
                                if (val != null) {
                                    try {
                                        // Todo: Update if have
                                        if (addBook.updateWrite(bookid, val, oldarr_author[old_id]) > 0) {
                                            msg = "Edit data successfully.";
                                            // Todo: 'getStatus()' use for set new 'Author ID'
                                            if (listWrite.get(old_id).getStatus() == val
                                                    || listWrite.get(old_id).getStatus().equals(val))
                                                listWrite.remove(old_id);
                                        }
                                    } catch (SQLException e) {
                                        dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການແກ້ໄຂ້ຂໍ້ມູນແຕ່ງປຶ້ມ",
                                                e);
                                        return;
                                    }
                                    old_id++;
                                }
                            }
                        }
                        if (listWrite.size() > 0) {
                            // Todo: Save Write
                            if (addBook.saveWrite(listWrite) > 0) {
                                msg = "Edit data successfully.";
                            }
                        }
                        msg = "Edit data successfully.";
                    }

                    if (msg != null) {
                        alertMessage.showCompletedMessage("Edit", msg, 4, Pos.BOTTOM_RIGHT);

                        if (bookcontroller != null)
                            bookcontroller.showData();
                        if (bookid != "")
                            closeForm();

                        clearText();
                    }
                }

            } else {
                validRules.setErrorDecorationEnabled(true);
                alertMessage.showWarningMessage("Save Warning", "Please chack your information and try again.", 4,
                        Pos.BOTTOM_RIGHT);
            }
        } catch (Exception e) {
            alertMessage.showErrorMessage("Save Error", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
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
}
