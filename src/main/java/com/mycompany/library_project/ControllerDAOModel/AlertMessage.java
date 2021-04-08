package com.mycompany.library_project.ControllerDAOModel;

import org.controlsfx.control.Notifications;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.*;
import javafx.util.Duration;

public class AlertMessage {
    private Image img = null;
    private Notifications notificationsBuilder = null;
    private ImageView imgView = null;

    public AlertMessage() {
    }

    public void showCompletedMessage(String title, String message, int time, Pos pos) {
        img = new Image("/com/mycompany/library_project/Icon/completed.png");
        imgView = new ImageView();
        imgView.setFitWidth(90);
        imgView.setFitHeight(90);
        imgView.setImage(img);

        notificationsBuilder = Notifications.create().title(title).text(message).graphic(imgView)
                .hideAfter(Duration.seconds(time)).position(pos).onAction(null);
        notificationsBuilder.show();
    }

    public void showWarningMessage(String title, String message, int time, Pos pos) {
        img = new Image("/com/mycompany/library_project/Icon/warning.png");
        imgView = new ImageView();
        imgView.setFitWidth(90);
        imgView.setFitHeight(90);
        imgView.setImage(img);

        notificationsBuilder = Notifications.create().title(title).text(message).graphic(imgView)
                .hideAfter(Duration.seconds(time)).position(pos).onAction(null);
        notificationsBuilder.show();
    }

    public void showErrorMessage(String title, String message, int time, Pos pos) {
        img = new Image("/com/mycompany/library_project/Icon/error.png");
        imgView = new ImageView();
        imgView.setFitWidth(90);
        imgView.setFitHeight(90);
        imgView.setImage(img);

        notificationsBuilder = Notifications.create().title(title).text(message).graphic(imgView)
                .hideAfter(Duration.seconds(time)).position(pos).onAction(null);
        notificationsBuilder.show();
    }

    public void showDefultInforMessage(String title, String message, int time, Pos pos) {
        notificationsBuilder = Notifications.create().title(title).text(message).graphic(null)
                .hideAfter(Duration.seconds(time)).position(pos).onAction(null);
        notificationsBuilder.showInformation();
    }

    public void showDefultWarningMessage(String title, String message, int time, Pos pos) {
        notificationsBuilder = Notifications.create().title(title).text(message).graphic(null)
                .hideAfter(Duration.seconds(time)).position(pos).onAction(null);
        notificationsBuilder.showWarning();
    }

    public void showDefultErrorMessage(String title, String message, int time, Pos pos) {
        notificationsBuilder = Notifications.create().title(title).text(message).graphic(null)
                .hideAfter(Duration.seconds(time)).position(pos).onAction(null);
        notificationsBuilder.showError();
    }

    // Todo: ===========================Node==========================
    public void showCompletedMessage(Node node, String title, String message, int time, Pos pos) {
        img = new Image("/com/mycompany/library_project/Icon/completed.png");
        imgView = new ImageView();
        imgView.setFitWidth(90);
        imgView.setFitHeight(90);
        imgView.setImage(img);

        notificationsBuilder = Notifications.create().owner(node).title(title).text(message).graphic(imgView)
                .hideAfter(Duration.seconds(time)).position(pos).onAction(null);
        notificationsBuilder.show();
    }

    public void showWarningMessage(Node node, String title, String message, int time, Pos pos) {
        img = new Image("/com/mycompany/library_project/Icon/warning.png");
        imgView = new ImageView();
        imgView.setFitWidth(90);
        imgView.setFitHeight(90);
        imgView.setImage(img);

        notificationsBuilder = Notifications.create().owner(node).title(title).text(message).graphic(imgView)
                .hideAfter(Duration.seconds(time)).position(pos).onAction(null);
        notificationsBuilder.show();
    }

    public void showErrorMessage(Node node, String title, String message, int time, Pos pos) {
        img = new Image("/com/mycompany/library_project/Icon/error.png");
        imgView = new ImageView();
        imgView.setFitWidth(90);
        imgView.setFitHeight(90);
        imgView.setImage(img);

        notificationsBuilder = Notifications.create().owner(node).title(title).text(message).graphic(imgView)
                .hideAfter(Duration.seconds(time)).position(pos).onAction(null);
        notificationsBuilder.show();
    }

    public void showDefultInforMessage(Node node, String title, String message, int time, Pos pos) {
        notificationsBuilder = Notifications.create().owner(node).title(title).text(message).graphic(null)
                .hideAfter(Duration.seconds(time)).position(pos).onAction(null);
        notificationsBuilder.showInformation();
    }

    public void showDefultWarningMessage(Node node, String title, String message, int time, Pos pos) {
        notificationsBuilder = Notifications.create().owner(node).title(title).text(message).graphic(null)
                .hideAfter(Duration.seconds(time)).position(pos).onAction(null);
        notificationsBuilder.showWarning();
    }

    public void showDefultErrorMessage(Node node, String title, String message, int time, Pos pos) {
        notificationsBuilder = Notifications.create().owner(node).title(title).text(message).graphic(null)
                .hideAfter(Duration.seconds(time)).position(pos).onAction(null);
        notificationsBuilder.showError();
    }

}
