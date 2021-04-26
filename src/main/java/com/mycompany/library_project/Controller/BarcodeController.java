package com.mycompany.library_project.Controller;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.jfoenix.controls.*;
import com.mycompany.library_project.Style;
import com.mycompany.library_project.ControllerDAOModel.*;
import com.mycompany.library_project.Model.BookDetailModel;

import javafx.collections.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class BarcodeController implements Initializable {

    private BookDetailModel addBarcode = null;
    private ResultSet rs = null;
    private ObservableList<BookDetailModel> data = null;
    private AlertMessage alertMessage = new AlertMessage();
    private DialogMessage dialog = null;
    private ArrayList<String> book = null;
    private ObservableList<String> status = FXCollections.observableArrayList("ຫວ່າງ", "ກຳລົງຢືມ", "ເສຍ");
    private String barcode = "", bookid = "";
    private Double x, y;

    @FXML
    private StackPane stackPane;

    @FXML
    private AnchorPane acHeaderPane;

    @FXML
    private TextField txtBarcode;

    @FXML
    private ComboBox<String> cmbStatus;

    @FXML
    private JFXButton btSave, btEdit, btCancel, btPrintBarcode, btClose;

    @FXML
    private TableView<BookDetailModel> tableBarcode;

    @FXML
    private TableColumn<BookDetailModel, String> colBarcode, colStatus;

    @FXML
    private TableColumn<BookDetailModel, JFXButton> colAction;

    private void initEvents() {

        cmbStatus.setItems(status);

        btSave.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                try {
                    if (!txtBarcode.getText().equals("")
                            && !cmbStatus.getSelectionModel().getSelectedItem().equals(null) && bookid != "") {
                        addBarcode = new BookDetailModel();
                        if (addBarcode.saveBookBarCode(txtBarcode.getText(), bookid,
                                cmbStatus.getSelectionModel().getSelectedItem()) > 0) {
                            alertMessage.showCompletedMessage("Saved", "Save data completed", 4, Pos.BOTTOM_RIGHT);
                            showBarcode(bookid);
                        } else {
                            alertMessage.showWarningMessage("Save Warning", "Save data fail", 4, Pos.BOTTOM_RIGHT);
                        }
                    } else {
                        alertMessage.showWarningMessage("Save Warning", "Please chack your information and try again.",
                                4, Pos.BOTTOM_RIGHT);
                    }
                } catch (Exception e) {
                    alertMessage.showErrorMessage("Save Error", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
                }
            }

        });

        btEdit.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                try {
                    addBarcode = new BookDetailModel();
                    if (addBarcode.updateData(bookid, txtBarcode.getText(), barcode,
                            cmbStatus.getSelectionModel().getSelectedItem()) > 0) {
                        alertMessage.showCompletedMessage("Edited", "Edit data completed", 4, Pos.BOTTOM_RIGHT);
                        showBarcode(bookid);
                    } else {
                        alertMessage.showWarningMessage("Edited", "Edit data fail", 4, Pos.BOTTOM_RIGHT);
                    }
                } catch (Exception e) {
                    alertMessage.showErrorMessage("Edited", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
                }
            }

        });

        btCancel.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                clearText();
            }

        });

        btPrintBarcode.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                // TODO Auto-generated method stub

            }

        });

        btClose.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                Stage stage = (Stage) stackPane.getScene().getWindow();
                stage.close();
            }

        });
    }

    private void initTable() {
        colBarcode.setCellValueFactory(new PropertyValueFactory<>("barcode"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("action"));

        tableBarcode.setOnMouseClicked(event -> {
            if (event.getClickCount() > 0 && tableBarcode.getSelectionModel().getSelectedItem() != null) {

                barcode = tableBarcode.getSelectionModel().getSelectedItem().getBarcode();
                txtBarcode.setText(tableBarcode.getSelectionModel().getSelectedItem().getBarcode());
                cmbStatus.getSelectionModel().select(tableBarcode.getSelectionModel().getSelectedItem().getStatus());
                bookid = book.get(tableBarcode.getSelectionModel().getSelectedIndex());
            }
        });
    }

    private void clearText() {
        txtBarcode.clear();
        cmbStatus.getSelectionModel().select("");
        barcode = "";
    }

    private void showBarcode(String book_id) {
        try {
            bookid = book_id;
            data = FXCollections.observableArrayList();
            book = new ArrayList<String>();
            addBarcode = new BookDetailModel();
            rs = addBarcode.showBarcode(book_id);
            while (rs.next()) {
                data.add(new BookDetailModel(rs.getString("barcode"), rs.getString("status"),
                        getAction(rs.getString("barcode"))));
                book.add(rs.getString("book_id"));
            }
            tableBarcode.setItems(data);
        } catch (Exception e) {

        }
    }

    private void moveForm() {
        acHeaderPane.setOnMousePressed(mouseEvent -> {
            x = mouseEvent.getSceneX();
            y = mouseEvent.getSceneY();
        });

        acHeaderPane.setOnMouseDragged(mouseEvent -> {
            BookController.addBarcode.setX(mouseEvent.getScreenX() - x);
            BookController.addBarcode.setY(mouseEvent.getScreenY() - y);
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        moveForm();
        initEvents();
        initTable();
        showBarcode(BookController._book_id);
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

    private JFXButton buttonYes(String barcodeid) {
        JFXButton btyes = new JFXButton("ຕົກລົງ");
        btyes.setStyle(Style.buttonDialogStyle);
        btyes.setOnAction(e -> {
            // Todo: Delete Data
            try {
                addBarcode = new BookDetailModel();
                if (addBarcode.deleteBarcode(bookid, barcodeid) > 0) {
                    dialog.closeDialog();
                    alertMessage.showCompletedMessage("Deleted", "Delete data successfully.", 4, Pos.BOTTOM_RIGHT);
                    showBarcode(bookid);
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
