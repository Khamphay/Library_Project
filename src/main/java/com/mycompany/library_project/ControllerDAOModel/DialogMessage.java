package com.mycompany.library_project.ControllerDAOModel;

import com.jfoenix.controls.*;

import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class DialogMessage {

    private JFXDialogLayout content = new JFXDialogLayout();
    private String title, message;
    public String result = null;
    private JFXDialog dialog = null;
    private StackPane stackPane;
    private JFXDialog.DialogTransition dialogStyle;
    private JFXButton[] buttons;
    private boolean topOnly;
    

    public DialogMessage() {
    }

    public DialogMessage(StackPane stackPane, String title, String message, JFXDialog.DialogTransition dialogStyle,
            JFXButton[] buttons, boolean topOnly) {
        this.title = title;
        this.message = message;
        this.dialogStyle = dialogStyle;
        this.stackPane = stackPane;
        this.buttons = buttons;
        this.topOnly = topOnly;
    }

    public void setDialogProperty(StackPane stackPane, String title, String message,
            JFXDialog.DialogTransition dialogStyle, JFXButton[] buttons, boolean topOnly) {
        this.title = title;
        this.message = message;
        this.dialogStyle = dialogStyle;
        this.stackPane = stackPane;
        this.buttons = buttons;
        this.topOnly = topOnly;
    }

    public void showDialog() {
        dialog = new JFXDialog(stackPane, content, dialogStyle, topOnly);
        dialog.setStyle(
                "-fx-border-radius:0.5em;   -fx-background-radius: 0.5em; -fx-font-family: 'BoonBaan'; -fx-font-size: 14;");
        content.setHeading(new Text(title));
        content.setBody(new Text(message));
        content.setActions(buttons);
        dialog.toFront();
        dialog.show();        
    }

    public void closeDialog() {
        dialog.close();
    }

}
