package com.mycompany.library_project.Controller;

import javafx.beans.value.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.mycompany.library_project.App;
import com.mycompany.library_project.ControllerDAOModel.*;
import com.mycompany.library_project.Model.*;
import com.mycompany.library_project.config.CreateLogFile;

public class AddBookController implements Initializable {

    private BookController bookcontroller;
    private ResultSet rs = null;
    private AlertMessage alertMessage = new AlertMessage();
    private TypeModel type = null;
    private CategoryModel category = null;
    private TableLogModel table = null;
    private AuthorModel author = null;
    public static BookDetailModel addBook = null;
    private CreateLogFile logfile = new CreateLogFile();

    private ArrayList<AuthorModel> author_id = null;
    private ArrayList<String> arr_type, arr_category;

    private ObservableList<String> status = FXCollections.observableArrayList("ຫວ່າງ", "ກຳລົງຢືມ", "ເສຍ");;
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

    public void initConstructor2(BookController bookcontroller) {
        this.bookcontroller = bookcontroller;
    }

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

    private void showAddCategory() {
        try {
            final FXMLLoader loader = new FXMLLoader(App.class.getResource("frmBookCategory.fxml"));
            final Parent catgroot = loader.load();
            final BookCategoryController categoryController = loader.getController();
            categoryController.initConstructor2(this);
            final Scene scene = new Scene(catgroot);
            Stage stage = new Stage();
            stage.setTitle("Add New Book Category");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            alertMessage.showErrorMessage("Warning Open Form", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
            logfile.createLogFile("Open Form Category", e);
        }
    }

    private void showAddType() {
        try {
            final FXMLLoader loader = new FXMLLoader(App.class.getResource("frmBookType.fxml"));
            final Parent typeroot = loader.load();
            final BookTypeController typeController = loader.getController();
            typeController.initConstructor2(this);
            final Scene scene = new Scene(typeroot);
            Stage stage = new Stage();
            stage.setTitle("Add New Book Type");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            alertMessage.showErrorMessage("Warning Open Form", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
            logfile.createLogFile("Open Form Category", e);
        }
    }

    private void showAddTablesLog() {
        try {
            final FXMLLoader loader = new FXMLLoader(App.class.getResource("frmTableLogs.fxml"));
            final Parent tableroot = loader.load();
            final TableLogController tableLogController = loader.getController();
            tableLogController.initConstructor2(this);
            final Scene scene = new Scene(tableroot);
            Stage stage = new Stage();
            stage.setTitle("Add New Table Log");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            alertMessage.showErrorMessage("Warning Open Form", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
            logfile.createLogFile("Open Form Category", e);
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

    private void generatedBarcode() {
        try {
            txtBarcode.clear();
            if (!txtId.getText().equals("") && !txtQty.getText().equals("")) {
                for (int i = 1; i <= Integer.parseInt(txtQty.getText()); i++) {
                    txtBarcode.appendText(txtId.getText() + "000" + i + "\n");
                }
            }
        } catch (Exception e) {
            alertMessage.showErrorMessage("Gennerate barcode", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
            logfile.createLogFile("ມີບັນຫາໃນການສ້າງ Barcode ປຶ້ມ", e);
        }
    }

    private void clearText() {
        txtId.clear();
        txtName.clear();
        txtISBN.clear();
        txtPage.clear();
        txtQty.clear();
        txtBarcode.clear();
        txtDetail.clear();
        cmbCagtegory.getSelectionModel().select("");
        cmbType.getSelectionModel().select("");
        cmbTable.getSelectionModel().select("");
        cmbtableLog.getSelectionModel().select("");
        bookid = "";
    }

    private void showAddAuthor() {
        try {
            final FXMLLoader loader = new FXMLLoader(App.class.getResource("frmAuthor.fxml"));
            final Parent subForm = loader.load();
            AuthorController authorController = loader.getController();
            authorController.initConstructor2(this);
            final Scene scene = new Scene(subForm);
            final Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Add New Author");
            stage.showAndWait();
            ;
        } catch (Exception e) {
            alertMessage.showErrorMessage(borderPane, "Open Form", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
            logfile.createLogFile("ການເປີດຟອມຈັດການຂໍ້ມູນສ່ວນບຸກຄົນມີບັນຫາ: " + "Form Author", e);
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
            type = new TypeModel();
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
            category = new CategoryModel();
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
            table = new TableLogModel();
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
                    table = new TableLogModel();
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

    public void fillValue() {

        if (addBook != null) {
            txtQty.setEditable(false);
            cmbStatus.setDisable(true);

            bookid = addBook.getBookId();
            txtId.setText(addBook.getBookId());
            txtName.setText(addBook.getBookName());
            txtISBN.setText(addBook.getISBN());
            txtPage.setText(addBook.getPage().toString());
            txtQty.setText(addBook.getQty().toString());
            txtDetail.setText(addBook.getDetail());
            cmbType.getSelectionModel().select(addBook.getTypeId());
            index_type = cmbType.getSelectionModel().getSelectedIndex();

            cmbCagtegory.getSelectionModel().select(addBook.getCatgId());
            index_category = cmbCagtegory.getSelectionModel().getSelectedIndex();

            cmbTable.getSelectionModel().select(addBook.getTableId());

            try {
                rs = addBook.showBarcode(bookid);
                while (rs.next()) {
                    txtBarcode.appendText(rs.getString("barcode") + "\n");
                }
            } catch (SQLException e) {
                alertMessage.showErrorMessage("Load Barcode Error", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
            }

            try {
                rs = addBook.showWrite(bookid);
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
        addBook = null;
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
            author = new AuthorModel();
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
                arr_author[3] = null;
            }
        });
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

                addBook = new BookDetailModel(txtId.getText(), txtName.getText(), txtISBN.getText(),
                        Integer.parseInt(txtPage.getText()), Integer.parseInt(txtQty.getText()),
                        arr_category.get(index_category), arr_type.get(index_type),
                        cmbTable.getSelectionModel().getSelectedItem().toString(), txtDetail.getText());
                if (bookid == "") {
                    if (addBook.saveData() > 0) {
                        String line = txtBarcode.getText();
                        String[] lineCount = line.split("\n");
                        // Todo: Save Barcode
                        for (int i = 0; i < lineCount.length; i++) {
                            try {
                                int result = addBook.saveBookBarCode(lineCount[i], txtId.getText(),
                                        cmbtableLog.getSelectionModel().getSelectedItem(),
                                        cmbStatus.getSelectionModel().getSelectedItem().toString());
                                if (result > 0) {
                                    msg = "Save data successfully.";
                                } else {
                                    msg = null;
                                }
                            } catch (Exception e) {
                                msg = null;
                                alertMessage.showWarningMessage("Save Barcode Error", "Can not save barcode.", 4,
                                        Pos.BOTTOM_RIGHT);
                                return;
                            }
                        }
                        // Todo: Save Write
                        for (String val : arr_author) {
                            if (val != null) {
                                try {
                                    if (addBook.saveWrite(txtId.getText(), val, txtYear.getText()) > 0) {
                                        msg = "Save data successfully.";
                                    }
                                } catch (Exception e) {
                                    alertMessage.showWarningMessage("Save Write Error", "Can not save writer.", 4,
                                            Pos.BOTTOM_RIGHT);
                                    return;
                                }
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
                                        if (addBook.updateWrite(bookid, val, oldarr_author[old_id],
                                                txtYear.getText()) > 0) {
                                            msg = "Edit data successfully.";
                                        }
                                    } catch (Exception e) {
                                        alertMessage.showErrorMessage("Edit Write Error", "Error: " + e.getMessage(), 4,
                                                Pos.BOTTOM_RIGHT);
                                        return;
                                    }
                                    old_id++;
                                }
                            }
                        } else {
                            // Todo: Save Write
                            for (String val : arr_author) {
                                if (val != null) {
                                    try {
                                        if (addBook.saveWrite(txtId.getText(), val, txtYear.getText()) > 0) {
                                            msg = "Save data successfully.";
                                        }
                                    } catch (Exception e) {
                                        alertMessage.showWarningMessage("Save Write Error", "Can not save writer.", 4,
                                                Pos.BOTTOM_RIGHT);
                                        return;
                                    }
                                }
                            }
                        }
                        msg = "Edit data successfully.";
                    }
                }

                if (msg != null) {
                    alertMessage.showCompletedMessage("Saved", msg, 4, Pos.BOTTOM_RIGHT);
                    if (bookcontroller != null)
                        bookcontroller.showData();
                } else {
                    alertMessage.showWarningMessage("Saved Warning", "Can not save data.", 4, Pos.BOTTOM_RIGHT);
                }
            } else {
                alertMessage.showWarningMessage("Save Warning", "Please chack your information and try again.", 4,
                        Pos.BOTTOM_RIGHT);
            }
        } catch (Exception e) {
            alertMessage.showErrorMessage("Save Error", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
            logfile.createLogFile("ມີບັນຫາໃນການບັນທືກຂໍ້ມູນປຶ້ມ", e);
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
        cmbStatus.setItems(status);

        cmbAuthor2.setVisible(false);
        cmbAuthor3.setVisible(false);
        cmbAuthor4.setVisible(false);
        cmbAuthor5.setVisible(false);
        cmbAuthor6.setVisible(false);

        if (BookController.add) {
            moveForm();
            btClose.setVisible(true);
            btClose.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent event) {
                    BookController.addNewBook.close();
                    BookController.add = false;
                }

            });
        }
        initEvents();
        initKeyEvents();
        fillType();
        fillCategory();
        fillTable();
        fillAuthor();
        fillValue();
    }

}
