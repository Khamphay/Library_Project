package com.mycompany.library_project.Controller;

import com.jfoenix.controls.*;

import javafx.collections.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.*;

import java.io.ByteArrayOutputStream;
import java.io.File;

import com.mycompany.library_project.Model.*;

import org.imgscalr.Scalr;

import com.mycompany.library_project.ControllerDAOModel.*;

import java.net.URL;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;

public class RegisterController implements Initializable {

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private LocalDate localDateExit;

    private MemberModel memberModel = null;
    private DepartmentModel depertment = new DepartmentModel();
    private ObservableList<String> items = null;
    private ObservableList<String> years = FXCollections.observableArrayList("ປີ 1", "ປີ 1 ຕໍ່ເນື່ອງ", "ປີ 2",
            "ປີ 2 ຕໍ່ເນື່ອງ", "ປີ 3", "ປີ 4");
    private ArrayList<String> depIdList = null;
    private int index = -1;
    private FileChooser chooser = null;
    private Image img = null;
    private AlertMessage alertMessage = new AlertMessage();
    private String gender = "";
    private byte[] byimg = null;
    private BufferedImage resizeImg;
    private ByteArrayOutputStream byteStrem;

    @FXML
    private BorderPane borderPane;

    @FXML
    private TextField txtId, txtFName, txtLName, txtTel, txtVill, txtDist, txtProv;
    @FXML
    private ComboBox<String> cmbDept, cmbYears;

    @FXML
    private JFXRadioButton rdbMale, rdbFemale;

    @FXML
    private DatePicker birtDate, registerDate, endDate, exitDate;

    @FXML
    private ImageView imgPic;

    private void fillDep() {
        depertment = new DepartmentModel();
        depIdList = new ArrayList<String>();
        try {
            ResultSet rs = depertment.findAll();
            items = FXCollections.observableArrayList();
            while (rs.next()) {
                depIdList.add(rs.getString(1));
                items.add(rs.getString(2));
            }
            cmbDept.setItems(items);

        } catch (Exception e) {
            alertMessage.showErrorMessage(borderPane, "Load Department", "Error: " + e.getMessage(), 4,
                    Pos.BOTTOM_RIGHT);
        }
    }

    @FXML
    private void setImage(ActionEvent event) {
        try {
            final Stage stage = new Stage();
            stage.setTitle("Choose image");
            chooser = new FileChooser();
            File file = chooser.showOpenDialog(stage);
            if (file != null) {
                img = new Image(file.toURI().toASCIIString());
                img.isSmooth();
                imgPic.setFitWidth(173);
                imgPic.setFitHeight(221);
                imgPic.setImage(img);

                // TODO: Resize image
                resizeImg = Scalr.resize(ImageIO.read(new File(file.toURI().toURL().getPath())),
                        Scalr.Method.AUTOMATIC, /*
                                                 * Scalr. Mode. AUTOMATIC,
                                                 */
                        200, Scalr.OP_BRIGHTER);

                // TODO: Convert after resize image to byte[]
                byteStrem = new ByteArrayOutputStream();
                ImageIO.write(resizeImg, "jpg", byteStrem);
                byimg = byteStrem.toByteArray();
            }
        } catch (Exception e) {
            alertMessage.showErrorMessage(borderPane, "Choose Image", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
            e.printStackTrace();
        }
    }

    @FXML
    private void Save(ActionEvent event) {
        try {
            gender = (rdbMale.isSelected()) ? rdbMale.getText() : rdbFemale.getText();

            if (index > -1) {
                memberModel = new MemberModel(txtId.getText(), txtFName.getText(), txtLName.getText(), gender,
                        txtTel.getText(), txtVill.getText(), txtDist.getText(), txtProv.getText(),
                        Date.valueOf(birtDate.getValue()), depIdList.get(index), Date.valueOf(LocalDate.now()),
                        Date.valueOf(LocalDate.now().plusYears(1)), Date.valueOf(localDateExit), byimg);
                if (memberModel.saveData() > 0) {
                    alertMessage.showCompletedMessage(borderPane, "Save", "Save data successfully.", 4,
                            Pos.BOTTOM_RIGHT);
                }
            } else {
                alertMessage.showWarningMessage(borderPane, "Save", "Please chack your information and try again.", 4,
                        Pos.BOTTOM_RIGHT);
            }
        } catch (Exception e) {
            alertMessage.showErrorMessage(borderPane, "Save", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
            e.printStackTrace();
        }
    }

    @FXML
    private void clearText() {
        txtId.clear();
        txtFName.clear();
        txtLName.clear();
        txtTel.clear();
        txtVill.clear();
        txtDist.clear();
        txtProv.clear();

    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Todo: Groud RadioButton
        ToggleGroup group = new ToggleGroup();
        rdbMale.setToggleGroup(group);
        rdbMale.setSelected(true);
        rdbFemale.setToggleGroup(group);

        cmbYears.setItems(years);
        fillDep();

        imgPic.setOnMouseClicked(new EventHandler<>() {

            @Override
            public void handle(MouseEvent event) {
                final Stage stage = new Stage();
                stage.setTitle("Choose image");
                chooser = new FileChooser();
                File file = chooser.showOpenDialog(stage);
                if (file != null) {
                    img = new Image(file.toURI().toASCIIString());
                    img.isSmooth();
                    imgPic.setFitWidth(165);
                    imgPic.setFitHeight(221);
                    imgPic.setImage(img);
                }
            }

        });

        cmbDept.setOnAction(new EventHandler<>() {

            @Override
            public void handle(ActionEvent event) {
                index = cmbDept.getSelectionModel().getSelectedIndex();
            }

        });

        cmbYears.setOnAction(e -> {
            if (cmbYears.getSelectionModel().getSelectedItem() == "ປີ 1 ຕໍ່ເນື່ອງ") {
                localDateExit = LocalDate.now().plusYears(1);
            } else if (cmbYears.getSelectionModel().getSelectedItem() == "ປີ 2 ຕໍ່ເນື່ອງ") {
                localDateExit = LocalDate.now().plusYears(0);
            } else {
                localDateExit = LocalDate.now().plusYears((4 - (cmbYears.getSelectionModel().getSelectedIndex() - 2)));
            }
        });
    }
}
