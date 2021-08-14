package com.mycompany.library_project.Controller;

import com.jfoenix.controls.*;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import com.mycompany.library_project.App;
import com.mycompany.library_project.ControllerDAOModel.*;
import com.mycompany.library_project.Model.*;
import com.mycompany.library_project.ModelShow.*;

import javafx.application.Platform;
import javafx.event.*;
import javafx.fxml.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.*;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

public class HomeController implements Initializable {

    public static Connection con = null;
    public static String[] summaryValue = new String[10];
    private HamburgerSlideCloseTransition hamburgerTransition = null;
    private DialogMessage dialog = new DialogMessage();
    private ShowRentSendModel showRentSendModel = new ShowRentSendModel();
    private ListBookModel listbook = null;
    private MyDate mydate = new MyDate();
    private Rectangle2D bounds = null;
    public Stage homeStage = null;
    private Node node = null;
    private Parent rootMenu = null;
    private ResultSet rs = null;
    private boolean fragMenu = false;
    private boolean max_min = false;

    @FXML
    private StackPane stackPane;

    @FXML
    private AnchorPane acHomePaneToolbar;

    @FXML
    public ScrollPane sclSubHome;

    @FXML
    public ScrollPane sclPaneMenu;

    @FXML
    private AnchorPane acPaneMenuList;

    @FXML
    private VBox pnItems;

    @FXML
    public BorderPane bpDisplay;

    @FXML
    private JFXButton btShowRent_Send, btRentBook, btSendBook, btRegister, btConfig;

    @FXML
    private JFXButton btCloseForm, btMinimize, btMaximum, btSalieder;

    @FXML
    private JFXHamburger humberger;

    @FXML
    private Text textTotalList, txtEmplName;

    /*
     * //TODO: Custom move form private void moveWinForm() {
     * acHomePaneToolbar.setOnMousePressed(mouseEvent -> { x =
     * mouseEvent.getSceneX(); y = mouseEvent.getSceneY(); }); // TODO: Set for move
     * form acHomePaneToolbar.setOnMouseDragged(mouseEvent -> {
     * homeStage.setX(mouseEvent.getScreenX() - x);
     * homeStage.setY(mouseEvent.getScreenY() - y); homeStage.setOpacity(0.4f); });
     * 
     * acHomePaneToolbar.setOnDragDone(dragEvent -> { homeStage.setOpacity(1.0f);
     * }); acHomePaneToolbar.setOnMouseReleased(mouseEvent -> {
     * homeStage.setOpacity(1.0f); }); }
     */
    public void initConstructor(String[] userInfor) {
        txtEmplName.setText(userInfor[0] + " " + userInfor[1]);
    }

    private void sliderMenuHamburger() {
        hamburgerTransition = new HamburgerSlideCloseTransition(humberger);
        hamburgerTransition.setRate(-1);
        humberger.addEventHandler(MouseEvent.MOUSE_CLICKED, (event) -> {
            hamburgerTransition.setRate(hamburgerTransition.getRate() * -1);
            hamburgerTransition.play();
            show_menu();
        });

    }

    private void show_ImportBooks() {
        try {
            final FXMLLoader loader = new FXMLLoader(App.class.getResource("frmImportBook.fxml"));
            final Parent subroot = loader.load();
            ImportController importController = loader.getController();
            importController.initConstructor(this);
            bpDisplay.setCenter(subroot);
        } catch (IOException e) {
            e.printStackTrace();
            dialog.showExcectionDialog("Error", null, "ການເປີດຟອມນຳຂໍ້ມູນປຶ້ມເຂົ້າມີບັນຫາ", e);
        }
    }

    private void show__Register() {
        try {
            final FXMLLoader loader = new FXMLLoader(App.class.getResource("frmRegister.fxml"));
            final Parent subroot = loader.load();
            RegisterController registerController = loader.getController();
            registerController.initConstructor1(this);
            bpDisplay.setCenter(subroot);
        } catch (Exception e) {
            dialog.showExcectionDialog("Error", null, "ການເປີດຟອມລົງທະບຽນມີບັນຫາ", e);
        }
    }

    private void show_SearchBook() {
        try {
            final FXMLLoader loader = new FXMLLoader(App.class.getResource("frmSearch.fxml"));
            final Parent subroot = loader.load();
            SearchController searchController = loader.getController();
            searchController.initConstructor(this);
            bpDisplay.setCenter(subroot);
        } catch (Exception e) {
            dialog.showExcectionDialog("Error", null, "ການເປີດຟອມຄົ້ນຫາປຶ້ມມີບັນຫາ", e);
        }
    }

    private void show_RentSendBook() {
        try {
            final FXMLLoader loader = new FXMLLoader(App.class.getResource("frmRentSendBooks.fxml"));
            final Parent subroot = loader.load();
            final RentSendController rentSendController = loader.getController();
            rentSendController.initConstructor(this);
            bpDisplay.setCenter(subroot);

        } catch (Exception e) {
            // TODO: handle exception
        }
    }
    private void showSubFrom(String subForm) {
        try {
            final FXMLLoader loader = new FXMLLoader(App.class.getResource(subForm));
            final Parent subroot = loader.load();
            bpDisplay.setCenter(subroot);
        } catch (Exception e) {
            dialog.showExcectionDialog("Error", null, "ເກິດບັນຫາໃນການເປີດຟອມ", e);
        }
    }

    private void show_menu() {
        try {
            if (fragMenu == false) {
                rootMenu = FXMLLoader.load(App.class.getResource("menu_1.fxml"));
                node = rootMenu;
                bpDisplay.setLeft(node);
                fragMenu = true;
            } else {
                rootMenu = FXMLLoader.load(App.class.getResource("menu_2.fxml"));
                node = rootMenu;
                bpDisplay.setLeft(node);
                fragMenu = false;
            }

            // Todo!: Set event to menu
            node.lookup("#btHome").setOnMouseClicked(new EventHandler<Event>() {

                @Override
                public void handle(Event event) {
                    bpDisplay.setCenter(sclSubHome);
                }
            });

            node.lookup("#btManage").setOnMouseClicked(new EventHandler<Event>() {

                @Override
                public void handle(Event event) {
                    SummaryData books = new SummaryData();
                    books.start();
                    showSubFrom("frmManageBook.fxml");
                }
            });

            node.lookup("#btRegister").setOnMouseClicked(new EventHandler<Event>() {

                @Override
                public void handle(Event event) {
                    show__Register();
                }
            });

            node.lookup("#btImport").setOnMouseClicked(new EventHandler<Event>() {

                @Override
                public void handle(Event event) {
                    show_ImportBooks();
                }
            });

            node.lookup("#btSearch").setOnMouseClicked(new EventHandler<Event>() {

                @Override
                public void handle(Event event) {
                    try {
                        show_SearchBook();
                    } catch (Exception e) {
                        dialog.showExcectionDialog("Error", null, "ເກິດບັນຫາໃນການເປີດຟອມຄົ້ນຫາຂໍ້ມູນ", e);
                    }
                }
            });

            node.lookup("#btRentSend").setOnMouseClicked(new EventHandler<Event>() {

                @Override
                public void handle(Event event) {
                    try {
                        show_RentSendBook();
                    } catch (Exception e) {
                        dialog.showExcectionDialog("Error", null, "ເກິດບັນຫາໃນການເປີດຟອມຢືມ-ສົ່ງປຶ້ມ", e);
                    }
                }
            });

            node.lookup("#btLogOut").setOnMouseClicked(new EventHandler<Event>() {

                @Override
                public void handle(Event event) {
                    Optional<ButtonType> result = dialog.showComfirmDialog("Comfirmed", null,
                            "ຕ້ອງການອອກຈາກລະບົບ ຫຼື ບໍ?");
                    if (result.get() == ButtonType.YES)
                        try {
                            final FXMLLoader loader = new FXMLLoader(App.class.getResource("Login.fxml"));
                            final Parent desktopRoot = loader.load();
                            final Scene sceneDesktop = new Scene(desktopRoot);
                            final LoginController loginController = loader.getController();
                            sceneDesktop.setFill(Color.TRANSPARENT);
                            final Stage stage = new Stage();
                            stage.setTitle("FNS Library Management System - Login");
                            stage.setScene(sceneDesktop);
                            stage.initStyle(StageStyle.TRANSPARENT);
                            stage.show();
                            loginController.initConstructor(stage);
                            homeStage.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                            dialog.showExcectionDialog("Error", null, "ເກິດບັນຫາໃນການອອກຈາກລະບົບ", e);
                        }
                }
            });

        } catch (Exception e) {

        }
    }

    public void showMainMenuHome() {
        bpDisplay.setCenter(sclSubHome);
        showRentBookOutOfDate();
    }

    @FXML
    public void buttonHome_Action(ActionEvent event) {
        showMainMenuHome();
    }

    @FXML
    private void buttonSetting_Action(ActionEvent event) throws Exception {
        if (SettingController.settingStage == null) {
            Parent settingParent = FXMLLoader.load(App.class.getResource("frmSetting.fxml"));
            Scene settingScene = new Scene(settingParent);
            settingScene.setFill(Color.TRANSPARENT);
            SettingController.settingStage = new Stage(StageStyle.TRANSPARENT);
            SettingController.settingStage.setScene(settingScene);
            SettingController.settingStage.getIcons().add(new Image("/com/mycompany/library_project/Icon/icon.png"));
            SettingController.settingStage.show();
        }
    }

    @FXML
    private void buttonShowRent_SendBook_Action(ActionEvent event) throws Exception {
        try {
            final FXMLLoader loader = new FXMLLoader(App.class.getResource("frmShowRent_SendBook.fxml"));
            final Parent subroot = loader.load();
            bpDisplay.setCenter(subroot);
        } catch (Exception e) {
            dialog.showExcectionDialog("Error", null, "ການເປີດຟອມຈັດການຂໍ້ມູນປຶ້ມມີບັນຫາ", e);
        }
    }

    @FXML
    private void buttonRegister_Action(ActionEvent event) throws Exception {
        show__Register();
    }

    @FXML
    private void buttonSearch_Action(ActionEvent event) throws Exception {
        show_SearchBook();
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

    public void showRentBookOutOfDate() {

        Platform.runLater(new Runnable() {

            int number = 1;

            @Override
            public void run() {
                try {
                    pnItems.getChildren().clear();
                    rs = showRentSendModel.findByRentOutOfDate(Date.valueOf(LocalDate.now()), "ກຳລັງຢືມ");
                    while (rs.next()) {
                        // Todo: Cancalar Date
                        int outdate = mydate.cancalarDate(rs.getDate("date_send").toLocalDate());
                        listbook = new ListBookModel(number, rs.getString("rent_id"), rs.getString("barcode"),
                                rs.getString("book_name"), rs.getString("member_id"),
                                rs.getString("full_name") + " " + rs.getString("sur_name"), rs.getDate("date_rent"),
                                rs.getDate("date_send"), rs.getString("book_status"), outdate);

                        final FXMLLoader loader = new FXMLLoader(App.class.getResource("frmBookList.fxml"));
                        final Parent listParent = loader.load();
                        final Node node = listParent;
                        BookListController item = loader.getController();
                        item.initConstructor(listbook);

                        pnItems.getChildren().addAll(node);
                        number++;
                    }
                    textTotalList.setText(pnItems.getChildren().size() + " ລາຍການ");
                } catch (Exception e) {
                    dialog.showExcectionDialog("Error", null, "ເກິດບັນຫາໃນການໂຫຼດຂໍ້ມູນຢືມປຶ້ມກາຍກຳນົດ", e);
                    e.printStackTrace();
                }
            };
        });
    }

    private void getAnyCostPrice() {
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                try {
                    final CostModel cost = new CostModel();
                    ResultSet rs = cost.findAll();
                    if (rs.next()) {
                        StaticCostPrice.RegisterCost = rs.getDouble("cost_register");
                        StaticCostPrice.OutOfDateCost = rs.getDouble("cost_outofdate");
                        StaticCostPrice.LostCost = rs.getDouble("cost_lost");
                    }
                } catch (SQLException e) {
                    dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການໂຫຼດຂໍ້ມູນຄ່າປັບໃຫມ", e);
                }

            }

        });
    }

    @FXML
    private void openConfigSer(ActionEvent event) {
        try {
            final FXMLLoader loader = new FXMLLoader(App.class.getResource("frmConfig.fxml"));
            final Parent root = loader.load();
            final Scene scene = new Scene(root);
            final Stage stage = new Stage(StageStyle.TRANSPARENT);
            final ConfigServerController configServerController = loader.getController();
            configServerController.initConstructor(stage);
            scene.setFill(Color.TRANSPARENT);
            stage.setTitle("Config Database");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (Exception e) {
            dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການເປີດຟອມ Config ຖານຂໍ້ມູນ", e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // moveWinForm();
        showRentBookOutOfDate();
        show_menu();
        sliderMenuHamburger();
        getAnyCostPrice();
        // thItem.start();
    }
}
