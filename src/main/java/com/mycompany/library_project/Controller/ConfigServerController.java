package com.mycompany.library_project.Controller;

import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.mycompany.library_project.MyConnection;
import com.mycompany.library_project.ControllerDAOModel.AlertMessage;
import com.mycompany.library_project.config.*;

import javafx.event.*;
import javafx.fxml.*;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

public class ConfigServerController implements Initializable {

    public static Stage configStage = null;
    public static boolean chack = false;
    private Double x, y;
    private AlertMessage alertMessage = new AlertMessage();
    private CreateLogFile config = new CreateLogFile();
    private ValidationSupport validate = null;

    @FXML
    private StackPane stackPane;

    @FXML
    private AnchorPane acTopBar;

    @FXML
    private TextField txtHost, txtPort, txtUserName, txtPassword;

    @FXML
    private Text textResualt;

    @FXML
    private JFXCheckBox chEnable;

    @FXML
    private JFXButton btSave, btCancel, btTest, btClose;

    private void initEvents() {

        validate = new ValidationSupport();
        // validate.setErrorDecorationEnabled(true);
        validate.registerValidator(txtHost, false, Validator.createEmptyValidator("ກະລຸນາປ້ອນ Host Name"));

        validate = new ValidationSupport();
        validate.registerValidator(txtHost, false, Validator.createEmptyValidator("ກະລຸນາປ້ອນ Port"));

        btSave.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                if (txtHost.getText() != "" && txtPort.getText() != "") {
                    if (config.createFileConfig(txtHost.getText(), txtPort.getText(), txtUserName.getText(),
                            txtPassword.getText()) == true) {
                        MyConnection.server = txtHost.getText() + ":" + txtPort.getText();
                        MyConnection.userName = txtUserName.getText();
                        MyConnection.password = txtPassword.getText();
                        textResualt.setText("ບັນທືກຂໍ້ມູນສຳເລ້ດແລ້ວ");
                    } else {
                        textResualt.setText("ບັນທືກຂໍ້ມູນບໍ່ສຳເລ້ດ, ກະລຸນາກວດສອບຂໍ້ມຸນແລ້ວ ລອງໃຫມ່ອີກຄັ້ງ.");
                    }
                } else {
                    alertMessage.showWarningMessage("Save Configed", "Please input host name, port. and try again.", 4,
                            Pos.TOP_CENTER);
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
        chEnable.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                if (chEnable.isSelected() == true) {
                    txtUserName.setEditable(true);
                    txtPassword.setEditable(true);

                    validate = new ValidationSupport();
                    // validate.setErrorDecorationEnabled(true);
                    validate.registerValidator(txtUserName, false,
                            Validator.createEmptyValidator("ກະລຸນາປ້ອນ User Name"));

                    validate = new ValidationSupport();
                    validate.registerValidator(txtPassword, false,
                            Validator.createEmptyValidator("ກະລຸນາປ້ອນລະຫັດຜ່ານ"));
                } else {
                    txtUserName.setEditable(false);
                    txtPassword.setEditable(false);
                }
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

            if (txtUserName.getText() != "" && txtPassword.getText() != "") {
                chEnable.setSelected(true);
            }
        }

    }

}
