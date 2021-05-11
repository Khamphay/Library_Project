package com.mycompany.library_project.Controller;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import com.jfoenix.controls.*;
import com.mycompany.library_project.Style;
import com.mycompany.library_project.ControllerDAOModel.*;
import com.mycompany.library_project.Model.AuthorModel;
import com.mycompany.library_project.config.CreateLogFile;

import javafx.beans.value.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.util.Callback;

public class AuthorController implements Initializable {

    private AuthorModel author = null;
    private ResultSet rs = null;
    private String gender = "";
    private AlertMessage alertMessage = new AlertMessage();
    private DialogMessage dialog = null;
    private ObservableList<AuthorModel> data = null;
    private CreateLogFile logfile = new CreateLogFile();

    @FXML
    private StackPane stackPane;

    @FXML
    private JFXButton btSave, btEdit, btCancel;

    @FXML
    private TextField txtId, txtFname, txtLname, txtTel, txtEmail;

    @FXML
    private RadioButton rdbMale, rdbFemale;

    @FXML
    private TableView<AuthorModel> tableAuthor;

    @FXML
    private TableColumn<AuthorModel, String> colId, colFname, colSname, colGender, colTel, colEmail;

    private void initTable() {
        colId.setCellValueFactory(new PropertyValueFactory<>("author_id"));
        colFname.setCellValueFactory(new PropertyValueFactory<>("full_name"));
        colSname.setCellValueFactory(new PropertyValueFactory<>("sur_name"));
        colGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        colTel.setCellValueFactory(new PropertyValueFactory<>("tel"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
    }

    private void clearText() {
        txtId.clear();
        txtFname.clear();
        txtLname.clear();
        txtTel.clear();
        txtEmail.clear();
        gender = "";
    }

    private void initEvents() {
        btSave.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                try {
                    if (txtId.getText() != "" && txtFname.getText() != "" && txtLname.getText() != ""
                            && txtTel.getText() != "") {
                        gender = (rdbMale.isSelected() ? rdbMale.getText() : rdbFemale.getText());
                        author = new AuthorModel(txtId.getText(), txtFname.getText(), txtLname.getText(), gender,
                                txtTel.getText(), txtEmail.getText());
                        if (author.saveData() > 0) {
                            showData();
                            clearText();
                            alertMessage.showCompletedMessage(stackPane, "Saved", "Saved data successfully.", 4,
                                    Pos.BOTTOM_RIGHT);
                        } else {
                            alertMessage.showWarningMessage(stackPane, "Saved", "Cannot save data.", 4,
                                    Pos.BOTTOM_RIGHT);
                        }
                    } else {
                        alertMessage.showWarningMessage(stackPane, "Save Warning",
                                "Please chack your information and try again.", 4, Pos.BOTTOM_RIGHT);
                    }
                } catch (Exception e) {
                    alertMessage.showErrorMessage(stackPane, "Save Error", "Error: " + e.getMessage(), 4,
                            Pos.BOTTOM_RIGHT);
                    logfile.createLogFile("ມີບັນຫາໃນການບັນທືກຂໍ້ມູນນັກແຕ່ງປຶ້ມ", e);
                }
            }
        });

        btEdit.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                try {
                    if (txtId.getText() != "" && txtFname.getText() != "" && txtLname.getText() != ""
                            && txtTel.getText() != "") {
                        gender = (rdbMale.isSelected() ? rdbMale.getText() : rdbFemale.getText());
                        author = new AuthorModel(txtId.getText(), txtFname.getText(), txtLname.getText(), gender,
                                txtTel.getText(), txtEmail.getText());
                        if (author.updateData() > 0) {
                            showData();
                            clearText();
                            alertMessage.showCompletedMessage(stackPane, "Edited", "Edit data successfully.", 4,
                                    Pos.BOTTOM_RIGHT);
                        } else {
                            alertMessage.showWarningMessage(stackPane, "Edited", "Can not edit data.", 4,
                                    Pos.BOTTOM_RIGHT);
                        }
                    } else {
                        alertMessage.showWarningMessage(stackPane, "Edit Warning",
                                "Please chack your information and try again.", 4, Pos.BOTTOM_RIGHT);
                    }
                } catch (Exception e) {
                    alertMessage.showErrorMessage(stackPane, "Edit Error", "Error: " + e.getMessage(), 4,
                            Pos.BOTTOM_RIGHT);
                    logfile.createLogFile("ມີບັນຫາໃນການແກ້ໄຂຂໍ້ມູນນັກແຕ່ງປຶ້ມ", e);
                }
            }
        });

        btCancel.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                clearText();
            }

        });

        txtTel.textProperty().addListener(new ChangeListener<String>() {
            // Todo: set properties type only numeric
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    txtTel.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        tableAuthor.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() >= 2 && tableAuthor.getSelectionModel().getSelectedItem() != null) {
                    txtId.setText(tableAuthor.getSelectionModel().getSelectedItem().getAuthor_id());
                    txtFname.setText(tableAuthor.getSelectionModel().getSelectedItem().getFull_name());
                    txtLname.setText(tableAuthor.getSelectionModel().getSelectedItem().getSur_name());
                    txtTel.setText(tableAuthor.getSelectionModel().getSelectedItem().getTel());
                    txtEmail.setText(tableAuthor.getSelectionModel().getSelectedItem().getEmail());
                    if (tableAuthor.getSelectionModel().getSelectedItem().getGender().equals("ຊາຍ")) {
                        rdbMale.setSelected(true);
                    } else {
                        rdbFemale.setSelected(true);
                    }
                }
            }
        });
    }

    private void showData() {
        try {
            data = FXCollections.observableArrayList();
            author = new AuthorModel();
            rs = author.findAll();
            while (rs.next()) {
                data.add(new AuthorModel(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
                        rs.getString(5), rs.getString(6)));
            }
            tableAuthor.setItems(data);
        } catch (Exception e) {
            alertMessage.showErrorMessage(stackPane, "Load Data", "Error" + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        ToggleGroup group = new ToggleGroup();
        rdbMale.setToggleGroup(group);
        rdbFemale.setToggleGroup(group);
        rdbMale.setSelected(true);

        initTable();
        addButtonToTable();
        showData();
        initEvents();
    }

    private void addButtonToTable() {
        TableColumn<AuthorModel, Void> colAtion = new TableColumn<>("Action");
        Callback<TableColumn<AuthorModel, Void>, TableCell<AuthorModel, Void>> cellFactory = new Callback<TableColumn<AuthorModel, Void>, TableCell<AuthorModel, Void>>() {

            @Override
            public TableCell<AuthorModel, Void> call(TableColumn<AuthorModel, Void> param) {
                final TableCell<AuthorModel, Void> cell = new TableCell<AuthorModel, Void>() {
                    final JFXButton delete = new JFXButton("ລົບ");

                    {
                        final ImageView imgView = new ImageView();
                        imgView.setFitWidth(20);
                        imgView.setFitHeight(20);
                        imgView.setImage(new Image("/com/mycompany/library_project/Icon/bin.png"));
                        delete.setStyle(Style.buttonStyle);
                        delete.setGraphic(imgView);

                        delete.setOnAction(new EventHandler<ActionEvent>() {

                            @Override
                            public void handle(ActionEvent event) {
                                JFXButton[] buttons = {
                                        buttonYes(tableAuthor.getItems().get(getIndex()).getAuthor_id()), buttonNo(),
                                        buttonCancel() };
                                dialog = new DialogMessage(stackPane, "ຄຳເຕືອນ", "ຕ້ອງການລົບຂໍ້ມູນອອກບໍ?",
                                        JFXDialog.DialogTransition.CENTER, buttons, false);
                                dialog.showDialog();
                            }

                        });
                    }

                };
                return cell;
            }

        };
        colAtion.setCellFactory(cellFactory);
        tableAuthor.getColumns().add(colAtion);

    }

    protected JFXButton buttonYes(String id) {
        JFXButton btyes = new JFXButton("ຕົກລົງ");
        btyes.setStyle(Style.buttonDialogStyle);
        btyes.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                author = new AuthorModel();
                try {
                    if (author.deleteData(id) > 0) {
                        showData();
                        alertMessage.showCompletedMessage(stackPane, "Deleted", "Delete data successfully.", 4,
                                Pos.BOTTOM_RIGHT);
                        dialog.closeDialog();
                    } else {
                        alertMessage.showWarningMessage(stackPane, "Deleted", "Can not delete data, Please try again.",
                                4, Pos.BOTTOM_RIGHT);
                    }
                } catch (SQLException e) {
                    alertMessage.showErrorMessage(stackPane, "Deleted", "Error: " + e.getMessage(), 4,
                            Pos.BOTTOM_RIGHT);
                    logfile.createLogFile("ມີບັນຫາໃນການລົບຂໍ້ມູນນັກແຕ່ງປຶ້ມ", e);
                }
            }
        });
        return btyes;
    }

    protected JFXButton buttonNo() {
        JFXButton btno = new JFXButton("  ບໍ່  ");
        btno.setStyle(Style.buttonDialogStyle);
        btno.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                dialog.closeDialog();
            }
        });
        return btno;
    }

    protected JFXButton buttonCancel() {
        JFXButton btcancel = new JFXButton("ຍົກເລີກ");
        btcancel.setStyle(Style.buttonDialogStyle);
        btcancel.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                dialog.closeDialog();
            }

        });
        return btcancel;
    }

}
