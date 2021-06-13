package com.mycompany.library_project.Controller;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import com.mycompany.library_project.Model.ListBookModel;

import javafx.fxml.*;
import javafx.scene.text.Text;

public class BookLostListController implements Initializable {

    private DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @FXML
    private Text number, barcode, bookName, category, type, tableLog, logId, datePay;

    public void initConstructor(ListBookModel listbook) {
        number.setText(Integer.toString(listbook.getNumber()));
        barcode.setText(listbook.getBarcode());
        bookName.setText(listbook.getBookName());
        category.setText(listbook.getCategory());
        type.setText(listbook.getType());
        tableLog.setText(listbook.getTablelog());
        logId.setText(listbook.getLogid());
        datePay.setText(dateFormat.format(listbook.getDatePay().toLocalDate()));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }


}
