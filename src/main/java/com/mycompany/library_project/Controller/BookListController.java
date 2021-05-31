package com.mycompany.library_project.Controller;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import com.mycompany.library_project.Model.ListBookModel;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;

public class BookListController implements Initializable {

    private DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @FXML
    private Text number, rentId, barcode, bookName, memberId, memberName, dateRent, dateSend, status, dateOut;

    public void initConstructor(ListBookModel listbook) {
        number.setText(Integer.toString(listbook.getNumber()));
        rentId.setText(listbook.getRentId());
        barcode.setText(listbook.getBarcode());
        bookName.setText(listbook.getBookName());
        memberId.setText(listbook.getMemberId());
        memberName.setText(listbook.getMemberName());
        dateRent.setText(dateFormat.format(listbook.getRentDate().toLocalDate()));
        dateSend.setText(dateFormat.format(listbook.getSendDate().toLocalDate()));
        status.setText(listbook.getStatus());
        dateOut.setText(listbook.getDayOut() + " ມື້");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
}