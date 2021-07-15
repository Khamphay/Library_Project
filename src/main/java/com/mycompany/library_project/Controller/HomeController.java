package com.mycompany.library_project.Controller;

import com.jfoenix.controls.*;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import com.mycompany.library_project.App;
import com.mycompany.library_project.MyConnection;
import com.mycompany.library_project.ControllerDAOModel.*;
import com.mycompany.library_project.Model.*;
import com.mycompany.library_project.ModelShow.*;
import com.mycompany.library_project.config.CreateLogFile;

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


    public Stage homeStage = null;
    public static String[] summaryValue = new String[10];

    private Connection con = MyConnection.getConnect();
    private boolean max_min = false;
    private Rectangle2D bounds = null;
    private HamburgerSlideCloseTransition hamburgerTransition = null;
    private boolean fragMenu = false;
    private Node node;
    private Parent rootMenu = null;
    private AlertMessage alertMessage = new AlertMessage();
    private DialogMessage dialog = new DialogMessage();
    private ResultSet rs = null;
    private ShowRentSendModel showRentSendModel = new ShowRentSendModel(con);
    private ListBookModel listbook = null;
    private MyDate mydate = new MyDate();

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
    private JFXButton btShowRent_Send, btRentBook, btSendBook, btRegister;

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

    private void showSubFrom(String subForm) {
        try {
            final FXMLLoader loader = new FXMLLoader(App.class.getResource(subForm));
            final Parent subroot = loader.load();
            bpDisplay.setCenter(subroot);
        } catch (Exception e) {
            alertMessage.showErrorMessage("Open Form", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
            CreateLogFile config = new CreateLogFile();
            config.createLogFile("ການເປີດຟອມຈັດການຂໍ້ມູນປຶ້ມມີບັນຫາ: " + subForm, e);
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

            node.lookup("#btManageBook").setOnMouseClicked(new EventHandler<Event>() {

                @Override
                public void handle(Event event) {
                    SummaryData catg = new SummaryData("call sumCategory();", "ThreadingCatg");
                    catg.start();

                    SummaryData type = new SummaryData("call sumType();", "ThreadingType");
                    type.start();

                    SummaryData book = new SummaryData("call sumBook();", "ThreadingBook");
                    book.start();

                    SummaryData booklost = new SummaryData("call sumBookLost();", "ThreadingBookLost");
                    booklost.start();

                    SummaryData tablelog = new SummaryData("call sumTableLog();", "ThreadingTableLog");
                    tablelog.start();

                    showSubFrom("frmManageBook.fxml");
                }
            });

            node.lookup("#btManagePerson").setOnMouseClicked(new EventHandler<Event>() {

                @Override
                public void handle(Event event) {
                    SummaryData member = new SummaryData("call sumMember();", "ThreadingMember");
                    member.start();

                    SummaryData employee = new SummaryData("call sumEmployee();", "ThreadingEmployee");
                    employee.start();

                    SummaryData author = new SummaryData("call sumAuthor();", "ThreadingAuthor");
                    author.start();

                    SummaryData dep = new SummaryData("call sumDepartment();", "ThreadingDep");
                    dep.start();
                    showSubFrom("frmManagePersonal.fxml");
                }
            });

            node.lookup("#btImport").setOnMouseClicked(new EventHandler<Event>() {

                @Override
                public void handle(Event event) {
                    show_ImportBooks();
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
                        alertMessage.showErrorMessage("Open Form", "Error: " + e.getMessage(), 4,
                                Pos.BOTTOM_RIGHT);
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
            ShowRentSendController showRent_SendBookController = loader.getController();
            showRent_SendBookController.initConstructor(this);
            bpDisplay.setCenter(subroot);
        } catch (Exception e) {
            dialog.showExcectionDialog("Error", null, "ການເປີດຟອມຈັດການຂໍ້ມູນປຶ້ມມີບັນຫາ", e);
        }
    }

    @FXML
    private void buttonRentBook_Action(ActionEvent event) throws Exception {
        try {
            final FXMLLoader loader = new FXMLLoader(App.class.getResource("frmRentBooks.fxml"));
            final Parent subroot = loader.load();
            RentBookController rentBookController = loader.getController();
            rentBookController.initConstructor(this);
            bpDisplay.setCenter(subroot);
        } catch (Exception e) {
            dialog.showExcectionDialog("Error", null, "ການເປີດຟອມຢືມປຶ້ມມີບັນຫາ", e);
        }
    }

    @FXML
    private void buttonSendBook_Action(ActionEvent event) throws Exception {
        try {
            final FXMLLoader loader = new FXMLLoader(App.class.getResource("frmSendBook.fxml"));
            final Parent subroot = loader.load();
            SendBookController sendBookController = loader.getController();
            sendBookController.initConstructor(this);
            bpDisplay.setCenter(subroot);
        } catch (Exception e) {
            dialog.showExcectionDialog("Error", null, "ການເປີດຟອມສົ່ງປຶ້ມມີບັນຫາ", e);
        }
    }

    @FXML
    private void buttonRegister_Action(ActionEvent event) throws Exception {
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

    @FXML
    private void buttonSearch_Action(ActionEvent event) throws Exception {
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
                    alertMessage.showErrorMessage("Load rent book out of Error", "Error: " + e.getMessage(), 4,
                            Pos.BOTTOM_RIGHT);
                }
            };
        });
    }

    private void getAnyCostPrice() {
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                try {
                    final CostModel cost = new CostModel(con);
                    ResultSet rs = cost.findAll();
                    if (rs.next()) {
                        StaticCostPrice.RegisterCost = rs.getDouble("cost_register");
                        StaticCostPrice.OutOfDateCost = rs.getDouble("cost_outofdate");
                        StaticCostPrice.LostCost = rs.getDouble("cost_lost");
                    }
                } catch (SQLException e) {
                    alertMessage.showErrorMessage("Load Data Error", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
                }

            }

        });
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
