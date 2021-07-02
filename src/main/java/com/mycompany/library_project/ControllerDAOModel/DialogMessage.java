package com.mycompany.library_project.ControllerDAOModel;

import java.util.Optional;

import com.jfoenix.controls.*;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class DialogMessage {

    private JFXDialogLayout content = new JFXDialogLayout();
    private JFXDialog dialog = null;
    private DialogPane dialogPane = null;
    private ButtonType result = null;
    private Alert alert = null;

    private String style = "com/mycompany/library_project/Style/appstyle.css";

    public DialogMessage() {
    }

    // Todo: JavaFX Dialog
    // Todo: Show Information
    public Optional<ButtonType> showInforDialog(String headerText, String message) {
        alert = new Alert(AlertType.INFORMATION);
        dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(style);
        alert.setTitle("Information");
        alert.setHeaderText(headerText);
        alert.setContentText(message);
        return alert.showAndWait();
    }

    // Todo: Show Complete
    public Optional<ButtonType> showCompleteDialog(String headerText, String message) {
        alert = new Alert(AlertType.INFORMATION);
        dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(style);
        final ImageView imgView = new ImageView();
        imgView.setFitWidth(40);
        imgView.setFitHeight(40);
        imgView.setImage(new Image("/com/mycompany/library_project/Icon/completed.png"));
        dialogPane.setGraphic(imgView);
        alert.setTitle("Completed");
        alert.setHeaderText(headerText);
        alert.setContentText(message);
        return alert.showAndWait();
    }

    // Todo: Show Warning
    public Optional<ButtonType> showWarningDialog(String headerText, String message) {
        alert = new Alert(AlertType.WARNING);
        dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(style);
        final ImageView imgView = new ImageView();
        imgView.setFitWidth(40);
        imgView.setFitHeight(40);
        imgView.setImage(new Image("/com/mycompany/library_project/Icon/warning.png"));
        dialogPane.setGraphic(imgView);
        alert.setTitle("Warning");
        alert.setHeaderText(headerText);
        alert.setContentText(message);
        return alert.showAndWait();
    }

    // Todo: Show Error
    public Optional<ButtonType> showInErrorDialog(String headerText, String message) {
        alert = new Alert(AlertType.ERROR);
        dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(style);
        final ImageView imgView = new ImageView();
        imgView.setFitWidth(40);
        imgView.setFitHeight(40);
        imgView.setImage(new Image("/com/mycompany/library_project/Icon/error.png"));
        dialogPane.setGraphic(imgView);
        alert.setTitle("Error");
        alert.setHeaderText(headerText);
        alert.setContentText(message);
        return alert.showAndWait();
    }

    // Todo: Show Exection
    public Optional<ButtonType> showExcectionDialog(String title, String headerText, String message, Exception ex) {
        alert = new Alert(AlertType.ERROR);
        dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(style);
        final ImageView imgView = new ImageView();
        imgView.setFitWidth(40);
        imgView.setFitHeight(40);
        imgView.setImage(new Image("/com/mycompany/library_project/Icon/error.png"));
        dialogPane.setGraphic(imgView);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(message);

        Label label = new Label("ລາຍລະອຽດກ່ຽວກັບບັນຫາ:");

        TextArea textArea = new TextArea(ex.toString());
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setStyle("-fx-font-size: 12;");

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

        alert.getDialogPane().setExpandableContent(expContent);

        return alert.showAndWait();
    }

    // Todo: Show Exection
    public Optional<ButtonType> showExcectionDialog(String title, String headerText, String message, Throwable ex) {
        alert = new Alert(AlertType.ERROR);
        dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(style);
        final ImageView imgView = new ImageView();
        imgView.setFitWidth(40);
        imgView.setFitHeight(40);
        imgView.setImage(new Image("/com/mycompany/library_project/Icon/error.png"));
        dialogPane.setGraphic(imgView);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(message);

        Label label = new Label("ລາຍລະອຽດກ່ຽວກັບບັນຫາ:");

        TextArea textArea = new TextArea(ex.toString());
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setStyle("-fx-font-size: 12;");

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

        alert.getDialogPane().setExpandableContent(expContent);

        return alert.showAndWait();
    }

    // Todo: Show Comfirm
    public Optional<ButtonType> showComfirmDialog(String title, String headerText, String message) {
        alert = new Alert(AlertType.CONFIRMATION);
        dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(style);
        dialogPane.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(message);
        return alert.showAndWait();
    }

    // Todo: JFXDialog
    public void showDialog(StackPane stackPane, String title, String message, JFXDialog.DialogTransition dialogStyle,
            JFXButton[] buttons, boolean topOnly) {
        if (dialog != null) {
            dialog.requestFocus();
            buttons[0].requestFocus();
        } else {
            dialog = new JFXDialog(stackPane, content, dialogStyle, topOnly);
            dialog.setStyle(
            "-fx-border-radius:0.5em; -fx-background-radius: 0.5em; -fx-font-family: 'BoonBaan'; -fx-font-size: 14;");
    content.setHeading(new Text(title));
    content.setBody(new Text(message));
    content.setActions(buttons);
    dialog.toFront();
    dialog.requestFocus();
    dialog.show();
    }
}

    public void closeDialog() {
        dialog.close();
        dialog = null;
    }

    // private JFXButton buttonYes() {
    // JFXButton btyes = new JFXButton("ຕົກລົງ");
    // btyes.setStyle(Style.buttonDialogStyle);
    // btyes.setOnAction(e -> {
    // result = ButtonType.YES;
    // dialog.close();
    // });
    // return btyes;
    // }

    // private JFXButton buttonNo() {
    // JFXButton btno = new JFXButton(" ບໍ່ ");
    // btno.setStyle(Style.buttonDialogStyle);
    // btno.setOnAction(e -> {
    // result = ButtonType.NO;
    // closeDialog();
    // });
    // return btno;
    // }

    // private JFXButton buttonCancel() {
    // JFXButton btcancel = new JFXButton("ຍົກເລີກ");
    // btcancel.setStyle(Style.buttonDialogStyle);
    // btcancel.setOnAction(e -> {
    // result = ButtonType.CANCEL;
    // closeDialog();
    // });
    // return btcancel;
    // }

    public ButtonType getResult() {
        return result;
    }

}
