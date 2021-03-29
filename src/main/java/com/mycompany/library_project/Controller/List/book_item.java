package com.mycompany.library_project.Controller.List;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.mycompany.library_project.ModelShow.BookEntity;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;

public class book_item implements Initializable {

    public static ArrayList<BookEntity> book;

    @FXML
    private Text id, name, isbn, page, qty, category, type, detail;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        id.setText(book.get(0).getId());
        name.setText(book.get(0).getName());
        isbn.setText(book.get(0).getISBN());
        page.setText(book.get(0).getPage());
        qty.setText(book.get(0).getQty());
        category.setText(book.get(0).getCategory());
        type.setText(book.get(0).getType());
        detail.setText(book.get(0).getDetail());
    }
}
