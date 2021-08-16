package com.mycompany.library_project.Controller;

import java.net.URL;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import com.jfoenix.controls.*;
import com.mycompany.library_project.ControllerDAOModel.*;
import com.mycompany.library_project.Model.BookDetailModel;
import com.mycompany.library_project.Report.CreateReport;

import org.controlsfx.control.MaskerPane;
import org.controlsfx.control.textfield.TextFields;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;

public class ReportBookController implements Initializable {

    private DialogMessage dialog = new DialogMessage();
    private CreateReport report = new CreateReport();
    Map<String, Object> map = null;
    private Task<Void> task = null;
    private MaskerPane masker = new MaskerPane();
    private BookDetailModel books = new BookDetailModel();
    String logo = Paths.get("bin/Logo.png").toAbsolutePath().toString();

    @FXML
    private StackPane stackPane;

    @FXML
    private JFXRadioButton rdbReportAll, rdbReportByBookId;

    @FXML
    private TextField txtBookId;

    @FXML
    private JFXButton btReport;

    private void autoComplete() {
        try {
            final ArrayList<String> bookId = new ArrayList<String>();
            final ArrayList<String> bookName = new ArrayList<String>();
            final ResultSet rs = books.findAll();
            while (rs.next()) {
                bookId.add(rs.getString("book_id"));
                bookName.add(rs.getString("book_name"));
            }
            TextFields.bindAutoCompletion(txtBookId, bookId).getAutoCompletionPopup()
                    .setStyle("-fx-font-family: 'BoonBaan';  -fx-font-size: 14;");
            TextFields.bindAutoCompletion(txtBookId, bookName).getAutoCompletionPopup()
                    .setStyle("-fx-font-family: 'BoonBaan';  -fx-font-size: 14;");
            // -fx-font-size: 14;");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initEvents() {

        rdbReportAll.setOnAction(e -> {
            txtBookId.setDisable(true);
            txtBookId.clear();
        });

        rdbReportByBookId.setOnAction(e -> {
            txtBookId.setDisable(false);
        });

        btReport.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                if (rdbReportByBookId.isSelected())
                    if (txtBookId.getText().equals("")) {
                        dialog.showWarningDialog(null, "ກະລຸນາປ້ອນລະຫັດປຶ້ມທີ່ຕ້ອງການລາຍງານແລ້ວ ລອງໃຫມ່ອີກຄັ້ງ.");
                        return;
                    }

                task = new Task<Void>() {

                    @Override
                    protected Void call() throws Exception {
                        masker.setVisible(true);
                        masker.setProgressVisible(true);
                        if (rdbReportByBookId.isSelected()) {
                            report = new CreateReport();
                            map = new HashMap<String, Object>();
                            map.put("logo", logo);
                            map.put("bookid", txtBookId.getText());
                            report.showReport(map, "reportBookById.jrxml", "Report Book By ID Error");
                        } else {
                            report = new CreateReport();
                            map = new HashMap<String, Object>();
                            map.put("logo", logo);
                            report.showReport(map, "reportAllBooks.jrxml", "Report All Book Error");
                        }
                        return null;
                    }

                    @Override
                    protected void succeeded() {
                        super.succeeded();
                        masker.setProgressVisible(false);
                        masker.setVisible(false);
                    }

                    @Override
                    protected void failed() {
                        super.failed();
                        masker.setProgressVisible(false);
                        masker.setVisible(false);
                        dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການລາຍງານ", task.getException());
                    }
                };
                new Thread(task).start();
            }

        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ToggleGroup group = new ToggleGroup();
        rdbReportAll.setToggleGroup(group);
        rdbReportByBookId.setToggleGroup(group);
        rdbReportAll.setSelected(true);
        txtBookId.setDisable(true);

        masker.setVisible(false);
        masker.setPrefWidth(50.0);
        masker.setPrefHeight(50.0);
        masker.setText("ກຳລັງໂຫຼດຂໍ້ມູນ, ກະລຸນາລໍຖ້າ...");
        masker.setStyle("-fx-font-family: BoonBaan;");
        stackPane.getChildren().add(masker);

        initEvents();
        autoComplete();
    }
}
