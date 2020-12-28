package com.mycompany.library_project.Controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import com.jfoenix.transitions.hamburger.HamburgerNextArrowBasicTransition;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import com.jfoenix.transitions.hamburger.HamburgerTransition;
import com.mycompany.library_project.DesktopController;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class HomeController implements Initializable {

    public static Stage homeStage = null;
    private boolean max_min = false;
    private double x, y;
    private Parent subroot = null;
    private Rectangle2D bounds = null;
    private HamburgerNextArrowBasicTransition hamburgerTransition = null;
    private TranslateTransition slideTranstion = null;

    @FXML
    private AnchorPane acHomePaneToolbar;
    @FXML
    public ScrollPane sclSubHome;

    @FXML
    public ScrollPane sclPaneMenu;

    @FXML
    private AnchorPane acPaneMenu;

    @FXML
    private AnchorPane acPaneMenuList;

    @FXML
    private VBox pnItems;
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

    private void sliderMenuHamburger() {
         hamburgerTransition = new HamburgerNextArrowBasicTransition(humberger);
         hamburgerTransition.setRate(-1);
        humberger.addEventHandler(MouseEvent.MOUSE_CLICKED, (event) -> {
            hamburgerTransition.setRate(hamburgerTransition.getRate()*-1);
            hamburgerTransition.play();
            
            //Todo: Slide Pane
            // slideTranstion = new TranslateTransition();
            // slideTranstion.setDuration(Duration.seconds(0.4));
            // slideTranstion.setNode(acPaneMenu);
            // slideTranstion.setToX(0);

            // acPaneMenu.setPrefWidth(49);
            // acPaneMenu.resize(49, 569);
            // acPaneMenuList.setTranslateX(-569);
            // sclPaneMenu.setTranslateX(-569);
            // acPaneMenu.setTranslateX(-569);
            // slideTranstion.setOnFinished((ActionEvent evt)->{
                
            // });
        });

    }

    private void showSubFrom(String subForm) throws Exception {
        subroot = null;
        subroot = FXMLLoader.load(getClass().getResource(subForm));
        bpDisplay.setCenter(subroot);
    }


    @FXML
    private void sliderMenu(ActionEvent event) {
        // sliderMenuHamburger();
    }

    @FXML
    private void LogOut(ActionEvent event) throws Exception {
        Parent desktopRoot = FXMLLoader.load(getClass().getResource("/com/mycompany/library_project/frmDeskTop.fxml"));
        Scene sceneDesktop = new Scene(desktopRoot);
        sceneDesktop.setFill(Color.TRANSPARENT);
        DesktopController.desktopStage = new Stage(StageStyle.TRANSPARENT);
        DesktopController.desktopStage.setTitle("FNS Library MS");
        DesktopController.desktopStage.setScene(sceneDesktop);
        DesktopController.desktopStage.show();

        // Close this form
        homeStage.close();
    }

    @FXML
    public void buttonHome_Action(ActionEvent event) {
        bpDisplay.setCenter(sclSubHome);
    }

    @FXML
    private void buttonTableLog_Action(ActionEvent event) throws Exception {
        showSubFrom("/com/mycompany/library_project/MyProjectFrom/frmTableLogs.fxml");
    }

    @FXML
    private void buttonBookType_Action(ActionEvent event) throws Exception {
        showSubFrom("/com/mycompany/library_project/MyProjectFrom/frmbookType.fxml");
    }

    @FXML
    private void buttonBookCategory_Action(ActionEvent event) throws Exception {
        showSubFrom("/com/mycompany/library_project/MyProjectFrom/frmBookCategory.fxml");
    }

    @FXML
    private void buttonBooks_Action(ActionEvent event) throws Exception {
        showSubFrom("/com/mycompany/library_project/MyProjectFrom/frmBooks.fxml");
    }

    @FXML
    private void buttonEmployee_Action(ActionEvent event) throws Exception {
        // showSubFrom("");
    }

    @FXML
    private void buttonMembers_Action(ActionEvent event) throws Exception {
        // showSubFrom("");
    }

    @FXML
    private void buttonAuthor_Action(ActionEvent event) throws Exception {
        // showSubFrom("");
    }

    @FXML
    private void buttonReportMembers_Action(ActionEvent event) throws Exception {
        // showSubFrom("");
    }

    @FXML
    private void buttonReportBooks_Action(ActionEvent event) throws Exception {
        // showSubFrom("");
    }

    @FXML
    private void buttonReportBookSend_Action(ActionEvent event) throws Exception {
        // showSubFrom("");
    }

    @FXML
    private void buttonReportReserveBook_Action(ActionEvent event) throws Exception {
        // showSubFrom("");
    }

    @FXML
    private void buttonSetting_Action(ActionEvent event) throws Exception {
        if (SettingController.settingStage == null) {
            Parent settingParent = FXMLLoader
                    .load(getClass().getResource("/com/mycompany/library_project/MyProjectFrom/frmSetting.fxml"));
            Scene settingScene = new Scene(settingParent);
            settingScene.setFill(Color.TRANSPARENT);
            SettingController.settingStage = new Stage(StageStyle.TRANSPARENT);
            SettingController.settingStage.setScene(settingScene);
            SettingController.settingStage.show();
        }
    }

    @FXML
    private void buttonRentBook_Action(ActionEvent event) throws Exception {
        showSubFrom("/com/mycompany/library_project/MyProjectFrom/frmRentBooks.fxml");
    }

    @FXML
    private void buttonSendBook_Action(ActionEvent event) throws Exception {
        showSubFrom("/com/mycompany/library_project/MyProjectFrom/frmSendBook.fxml");
    }

    @FXML
    private void buttonReserveBook_Action(ActionEvent event) throws Exception {
        // showSubFrom("");
    }

    @FXML
    private void buttonRegister_Action(ActionEvent event) throws Exception {
        showSubFrom("/com/mycompany/library_project/MyProjectFrom/frmRegister.fxml");
    }

    @FXML
    private void buttonNotify_Action(ActionEvent event) throws Exception {
        // showSubFrom("");
        showListItems();
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

    private void showListItems() {
        Node[] node = new Node[100];
        String[][] data = { { "Computer", "sifur", "wo9eur", "setue9t8ye5", "09", "87", "efuw" } };

        pnItems.getChildren().clear();
        for (int i = 0; i < node.length; i++) {
            try {
                int index = i;
                Parent listParent = FXMLLoader
                        .load(getClass().getResource("/com/mycompany/library_project/MyProjectFrom/rowBooks.fxml"));
                node[i] = listParent;
                
                pnItems.getChildren().addAll(node[i]);
                pnItems.accessibleHelpProperty().setValue("000"+i);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        moveWinForm();
        sliderMenuHamburger();
    }
}
