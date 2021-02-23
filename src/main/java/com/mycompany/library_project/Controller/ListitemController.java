package com.mycompany.library_project.Controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import com.mycompany.library_project.ModelShow.BookEntity;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;

public class ListitemController implements Initializable {

    public static ArrayList<BookEntity> book_list;

    @FXML
    private HBox hbox;
    @FXML
    private MenuItem showDetail;

    @FXML
    private MenuItem edite;

    @FXML
    private MenuItem delete;

    @FXML
    public Label id, name, catg, type, page, qty, lng;

    @FXML
    private void showBookDetail(ActionEvent event) {
        JOptionPane.showMessageDialog(null, "Detail: " + id.getText() + "\n" + name.getText() + "\n" + page.getText());
    }
    
    @FXML
    private void deleteBooks(ActionEvent event) {
        hbox.setVisible(false);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO Auto-generated method stub
        id.setText(book_list.get(0).getId());
        name.setText(book_list.get(0).getName());
        type.setText(book_list.get(0).getType());
        catg.setText(book_list.get(0).getCategory());
        page.setText(book_list.get(0).getPage());
        qty.setText(book_list.get(0).getQty());
        lng.setText(book_list.get(0).getLang());


    }
    
}
