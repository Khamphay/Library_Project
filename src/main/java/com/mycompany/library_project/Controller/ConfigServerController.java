package com.mycompany.library_project.Controller;

import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.mycompany.library_project.MyConnection;
import com.mycompany.library_project.ControllerDAOModel.AlertMessage;
import com.mycompany.library_project.config.ConfigDatabase;

import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ConfigServerController implements Initializable {

    public static Stage configStage = null;
    public static boolean chack = false;
    private Double x, y;
    private AlertMessage alertMessage = new AlertMessage();
    private ConfigDatabase config = new ConfigDatabase();

    @FXML
    private StackPane stackPane;

    @FXML
    private AnchorPane acTopBar;

    @FXML
    private TextField txtHost, txtPort, txtUserName, txtPassword;

    @FXML
    private Text textResualt;

    @FXML
    private JFXButton btSave, btCancel, btTest, btClose;

    private void initEvents() {

        btSave.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                if (config.createFileConfig(txtHost.getText(), txtPort.getText(), txtUserName.getText(),
                        txtPassword.getText()) == true) {
                    textResualt.setText("ບັນທືກຂໍ້ມູນແລ້ວ");
                    MyConnection.server = txtHost.getText() + ":" + txtPort.getText();
                    MyConnection.userName = txtUserName.getText();
                    MyConnection.password = txtPassword.getText();
                }
            }
        });

        btTest.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                MyConnection.server = txtHost.getText() + ":" + txtPort.getText();
                MyConnection.userName = txtUserName.getText();
                MyConnection.password = txtPassword.getText();
                Connection con = MyConnection.getConnect();
                if (con != null) {
                    textResualt.setText("ການເຊື່ອມຕໍ່ສຳເລັດແລ້ວ");
                } else {
                    textResualt.setText("ການເຊື່ອມຕໍ່ບໍ່ສຳເລັດແລ້ວ, ກະລຸນາກວດສອບຂໍ້ມຸນແລ້ວ ລອງໃຫມ່ອີກຄັ້ງ");
                }
            }

        });

        btCancel.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                txtHost.clear();
                txtPort.clear();
                txtUserName.clear();
                txtPassword.clear();
                textResualt.setText("");
            }

        });

        btClose.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                configStage.close();
            }

        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initEvents();

        // Todo: Move From
        acTopBar.setOnMousePressed(mouseEvent -> {
            x = mouseEvent.getSceneX();
            y = mouseEvent.getSceneY();
        });
        // TODO: Set for move form
        acTopBar.setOnMouseDragged(mouseEvent -> {
            configStage.setX(mouseEvent.getScreenX() - x);
            configStage.setY(mouseEvent.getScreenY() - y);
            configStage.setOpacity(0.4f);
        });

        acTopBar.setOnDragDone(mouseEvent -> {
            configStage.setOpacity(1.0f);
        });

        acTopBar.setOnMouseReleased(mouseEvent -> {
            configStage.setOpacity(1.0f);
        });

        if (chack == true) {
            String[] server_infor = config.getServerInfor();
            txtHost.setText(server_infor[0]);
            txtPort.setText(server_infor[1]);
            txtUserName.setText(server_infor[2]);
            txtPassword.setText(server_infor[3]);
        }

    }

}
