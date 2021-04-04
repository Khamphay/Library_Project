package com.mycompany.library_project.Controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import com.mycompany.library_project.App;
import com.mycompany.library_project.Controller.List.ListitemController;
import com.mycompany.library_project.ModelShow.MyArrayList;

import javafx.application.Platform;
import javafx.event.*;
import javafx.fxml.*;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.*;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.*;

import java.net.URL;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

public class HomeController implements Initializable {

    public static Stage homeStage = null;
    private boolean max_min = false;
    private double x, y;
    private Parent subroot = null;
    private Rectangle2D bounds = null;
    private HamburgerSlideCloseTransition hamburgerTransition = null;
    private boolean fragMenu = false;
    private Node node;
    private Parent rootMenu = null;
    Node[] nodeItem = new Node[100];
    String[] data = { "Computer", "sifur", "wo9eur", "setue9t8ye5", "09", "87", "efuw" };

    @FXML
    private AnchorPane acHomePaneToolbar;
    @FXML
    public ScrollPane sclSubHome;

    @FXML
    public ScrollPane sclPaneMenu;

    @FXML
    private AnchorPane acPaneMenuList;

    @FXML
    // private VBox pnItems;
    private FlowPane pnItems;

    @FXML
    public BorderPane bpDisplay;
    @FXML
    private JFXButton btCloseForm;
    @FXML
    private JFXButton btMinimize;
    @FXML
    private JFXButton btMaximum;
    @FXML
    private JFXButton btSalieder;

    @FXML
    private JFXHamburger humberger;

    // TODO: Custom move form
    private void moveWinForm() {
        acHomePaneToolbar.setOnMousePressed(mouseEvent -> {
            x = mouseEvent.getSceneX();
            y = mouseEvent.getSceneY();
        });
        // TODO: Set for move form
        acHomePaneToolbar.setOnMouseDragged(mouseEvent -> {
            homeStage.setX(mouseEvent.getScreenX() - x);
            homeStage.setY(mouseEvent.getScreenY() - y);
            homeStage.setOpacity(0.4f);
        });

        acHomePaneToolbar.setOnDragDone(dragEvent -> {
            homeStage.setOpacity(1.0f);
        });
        acHomePaneToolbar.setOnMouseReleased(mouseEvent -> {
            homeStage.setOpacity(1.0f);
        });
    }

    // private void showListItems() {

    // }

    private void sliderMenuHamburger() {
        hamburgerTransition = new HamburgerSlideCloseTransition(humberger);
        hamburgerTransition.setRate(-1);
        humberger.addEventHandler(MouseEvent.MOUSE_CLICKED, (event) -> {
            hamburgerTransition.setRate(hamburgerTransition.getRate() * -1);
            hamburgerTransition.play();
            show_menu();
        });

    }

    private void showSubFrom(String subForm) {
        try {
            subroot = null;
            subroot = FXMLLoader.load(App.class.getResource(subForm));
            bpDisplay.setCenter(subroot);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void show_menu() {
        try {
            if (fragMenu == true) {
                rootMenu = FXMLLoader.load(App.class.getResource("menu_1.fxml"));
                node = rootMenu;
                bpDisplay.setLeft(node);
                fragMenu = false;
            } else {
                rootMenu = FXMLLoader.load(App.class.getResource("menu_2.fxml"));
                node = rootMenu;
                bpDisplay.setLeft(node);
                fragMenu = true;
            }

            // Todo!: Set event to menu
            node.lookup("#btHome").setOnMouseClicked(new EventHandler<Event>() {

                @Override
                public void handle(Event event) {
                    bpDisplay.setCenter(sclSubHome);
                }
            });

            node.lookup("#btManageBook").setOnMouseClicked(new EventHandler<Event>() {

                @Override
                public void handle(Event event) {
                    showSubFrom("frmManageBook.fxml");
                }
            });

            node.lookup("#btManagePerson").setOnMouseClicked(new EventHandler<Event>() {

                @Override
                public void handle(Event event) {
                    showSubFrom("frmManagePersonal.fxml");
                }
            });

            node.lookup("#btImport").setOnMouseClicked(new EventHandler<Event>() {

                @Override
                public void handle(Event event) {
                    showSubFrom("frmAddBooks.fxml");
                }
            });

            node.lookup("#btReport").setOnMouseClicked(new EventHandler<Event>() {

                @Override
                public void handle(Event event) {
                    showSubFrom("frmReport.fxml");
                }
            });

            node.lookup("#btSetting").setOnMouseClicked(new EventHandler<Event>() {

                @Override
                public void handle(Event event) {
                    try {
                        if (SettingController.settingStage == null) {
                            Parent settingParent = FXMLLoader.load(App.class.getResource("frmSetting.fxml"));
                            Scene settingScene = new Scene(settingParent);
                            settingScene.setFill(Color.TRANSPARENT);
                            SettingController.settingStage = new Stage(StageStyle.TRANSPARENT);
                            SettingController.settingStage.setScene(settingScene);
                            SettingController.settingStage.setTitle("FNS Library Management System - Setting");
                            SettingController.settingStage.show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error: " + e.getMessage());
        }
    }

    @FXML
    private void sliderMenu(ActionEvent event) {
        // sliderMenuHamburger();
    }

    @FXML
    private void LogOut(ActionEvent event) throws Exception {
        Parent desktopRoot = FXMLLoader.load(App.class.getResource("Login.fxml"));
        Scene sceneDesktop = new Scene(desktopRoot);
        sceneDesktop.setFill(Color.TRANSPARENT);
        // DesktopController.desktopStage = new Stage(StageStyle.TRANSPARENT);
        LoginController.loginSatge.setTitle("FNS Library Management System - Login");
        LoginController.loginSatge.setScene(sceneDesktop);
        LoginController.loginSatge.show();

        // Close this form
        homeStage.close();
    }

    @FXML
    public void buttonHome_Action(ActionEvent event) {
        bpDisplay.setCenter(sclSubHome);
    }

    @FXML
    private void buttonSetting_Action(ActionEvent event) throws Exception {
        if (SettingController.settingStage == null) {
            Parent settingParent = FXMLLoader.load(App.class.getResource("frmSetting.fxml"));
            Scene settingScene = new Scene(settingParent);
            settingScene.setFill(Color.TRANSPARENT);
            SettingController.settingStage = new Stage(StageStyle.TRANSPARENT);
            SettingController.settingStage.setScene(settingScene);
            SettingController.settingStage.show();
        }
    }

    @FXML
    private void buttonRentBook_Action(ActionEvent event) throws Exception {
        showSubFrom("frmRentBooks.fxml");
    }

    @FXML
    private void buttonSendBook_Action(ActionEvent event) throws Exception {
        showSubFrom("frmSendBook.fxml");
    }

    @FXML
    private void buttonReserveBook_Action(ActionEvent event) throws Exception {
        // showSubFrom("");
    }

    @FXML
    private void buttonRegister_Action(ActionEvent event) throws Exception {
        showSubFrom("frmRegister.fxml");
    }

    @FXML
    private void buttonNotify_Action(ActionEvent event) throws Exception {
        // showSubFrom("");
        // showListItems();

        // thItem.start();

        pnItems.getChildren().clear();
        Platform.runLater(new Runnable() {
            int i = 0;

            @Override
            public void run() {
                try {
                    while (i < nodeItem.length) {

                        // !Set data to "rowBooks" Layout
                        MyArrayList list = new MyArrayList();
                        ListitemController.book_list = list.bookDetail(data);
                        Parent listParent = FXMLLoader.load(App.class.getResource("rowBooks.fxml"));
                        nodeItem[i] = listParent;

                        pnItems.getChildren().addAll(nodeItem[i]);

                        i++;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            };
        });
    }

    @FXML
    private void minimixeFrom(ActionEvent event) {
        homeStage.setIconified(true);
    }

    @FXML
    private void maximunForm(ActionEvent actionEvent) {
        if (max_min == false) {
            // Todo: Set Maximun to show only the screen (don't hide the taskbar)
            bounds = Screen.getPrimary().getVisualBounds();
            homeStage.setX(bounds.getMinX());
            homeStage.setY(bounds.getMinY());
            homeStage.setHeight(bounds.getHeight());
            homeStage.setWidth(bounds.getWidth());

            // Todo: set form maximized
            homeStage.setMaximized(true);
            max_min = true;
        } else {
            homeStage.setMaximized(false);
            max_min = false;
        }
    }

    @FXML
    private void closePrograme(ActionEvent actionEvent) {
        System.exit(0);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // moveWinForm();
        show_menu();
        sliderMenuHamburger();
        // thItem.start();
    }

}
