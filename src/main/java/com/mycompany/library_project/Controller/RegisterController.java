package com.mycompany.library_project.Controller;

import com.jfoenix.controls.*;

import javafx.beans.value.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.*;

import java.io.*;

import com.mycompany.library_project.Model.*;
import com.mycompany.library_project.config.CreateLogFile;

import org.imgscalr.Scalr;

import com.mycompany.library_project.ControllerDAOModel.*;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;

public class RegisterController implements Initializable {

    private MemberController memberController;
    private CreateLogFile logfile = new CreateLogFile();
    public MemberModel memberModel = null;
    private DepartmentModel depertment = new DepartmentModel();
    private AlertMessage alertMessage = new AlertMessage();
    private DateFormat dateFormat = new DateFormat();

    private BufferedImage resizeImg = null;
    private ByteArrayOutputStream byteStrem = null;
    private OutputStream outStrem = null;
    private FileChooser chooser = null;
    private File lastPath = null;
    private ObservableList<String> items = null;
    private ObservableList<String> years = FXCollections.observableArrayList("ປີ 1", "ປີ 1 ຕໍ່ເນື່ອງ", "ປີ 2",
            "ປີ 2 ຕໍ່ເນື່ອງ", "ປີ 3", "ປີ 4");
    private ArrayList<String> depIdList = null;

    private LocalDate localDateExit;
    private int index = -1;
    private String memberid_edit = "", gender = "";
    private byte[] byimg = null;
    private Double x, y;
    private LocalDate minDate = LocalDate.now().plusYears(-30), maxDate = LocalDate.now();

    /*
     * Todo: Use from class MemberController for both form can communicate or use
     * for Refresh table after add and delete
     */
    public void initConstructor(MemberController memberController) {
        this.memberController = memberController;

        if (MemberController.add || memberModel != null) {
            moveForm();
            btClose.setVisible(true);
            btClose.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent event) {
                    MemberController.addMemberStage.close();
                    MemberController.add = false;
                }
            });
        }

        if (memberModel != null) {

            // Todo: if by edit from 'Form Member'
            memberid_edit = memberModel.getMemberId();
            txtId.setText(memberModel.getMemberId());
            txtFName.setText(memberModel.getFirstName());
            txtLName.setText(memberModel.getSureName());

            if (memberModel.getGender().equals("ຊາຍ")) {
                rdbMale.setSelected(true);
            } else {
                rdbFemale.setSelected(true);
            }
            txtTel.setText(memberModel.getTel());
            txtVill.setText(memberModel.getVillage());
            txtDist.setText(memberModel.getDistrict());
            txtProv.setText(memberModel.getProvince());
            birtDate.setValue(memberModel.getBirdate().toLocalDate());
            cmbDept.getSelectionModel().select(memberModel.getDetp());
            index = cmbDept.getSelectionModel().getSelectedIndex();
            cmbYears.getSelectionModel().select(memberModel.getStudy_year());
            // cmbYears.getSelectionModel().select(memberModel.getYears());
            localDateExit = memberModel.getDateExit().toLocalDate();

            // TODO: Show Image
            if (memberModel.getByimg() != null) {
                try {
                    outStrem = new FileOutputStream(new File("img.png"));
                    try {
                        outStrem.write(memberModel.getByimg());
                        imgPic.setImage(new Image("file:img.png"));
                    } catch (IOException e) {
                        alertMessage.showErrorMessage("Read Image", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
                    }
                } catch (FileNotFoundException e) {
                    alertMessage.showErrorMessage("Write Image", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
                }
            }
            memberModel = null;
        }
    }

    @FXML
    private BorderPane borderPane;

    @FXML
    private AnchorPane acPaneHeader;

    @FXML
    private JFXButton btClose;

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
            alertMessage.showErrorMessage("Load Department", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
        }
    }

    private void loadImager() {
        try {
            final Stage stage = new Stage();
            stage.setTitle("Choose image");
            chooser = new FileChooser();
            if (lastPath != null) {
                chooser.setInitialDirectory(lastPath);
            }
            File file = chooser.showOpenDialog(stage);
            if (file != null) {
                lastPath = file.getParentFile();// Todo: set last path of open

                imgPic.setFitWidth(173);
                imgPic.setFitHeight(221);
                imgPic.setImage(new Image(file.toURI().toASCIIString()));

                // TODO: Resize image
                resizeImg = Scalr.resize(ImageIO.read(new File(file.getAbsolutePath())), Scalr.Method.AUTOMATIC, /*
                                                                                                                  * Scalr.
                                                                                                                  * Mode.
                                                                                                                  * AUTOMATIC,
                                                                                                                  */
                        200, Scalr.OP_BRIGHTER);

                // TODO: Convert after resize image to byte[]
                byteStrem = new ByteArrayOutputStream();
                ImageIO.write(resizeImg, "jpg", byteStrem);
                byimg = byteStrem.toByteArray();
            }
        } catch (Exception e) {
            alertMessage.showErrorMessage("Choose Image", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
            logfile.createLogFile("ເກີດບັນຫາໃນການເລືອກຮູບພາບ", e);
        }
    }

    private void initEvents() {
        txtTel.textProperty().addListener(new ChangeListener<String>() {
            // Todo: set properties type only numeric
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    txtTel.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        imgPic.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                loadImager();
            }
        });

        cmbDept.setOnAction(event -> {
            index = cmbDept.getSelectionModel().getSelectedIndex();
        });

        cmbYears.setOnAction(evt -> {
            if (cmbYears.getSelectionModel().getSelectedItem() == "ປີ 1 ຕໍ່ເນື່ອງ") {
                localDateExit = LocalDate.now().plusYears(1);
            } else if (cmbYears.getSelectionModel().getSelectedItem() == "ປີ 2 ຕໍ່ເນື່ອງ") {
                localDateExit = LocalDate.now().plusYears(0);
            } else {
                localDateExit = LocalDate.now().plusYears((4 - (cmbYears.getSelectionModel().getSelectedIndex() - 2)));
            }
        });

        birtDate.setDayCellFactory(d -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setDisable(item.isAfter(maxDate) || item.isBefore(minDate));
            }
        });

    }

    @FXML
    private void setImage(ActionEvent event) {
        loadImager();
    }

    @FXML
    private void Save(ActionEvent event) {
        try {
            gender = (rdbMale.isSelected()) ? rdbMale.getText() : rdbFemale.getText();

            if (index > -1 && !txtId.getText().equals("") && !txtFName.getText().equals("")
                    && !txtLName.getText().equals("") && !txtTel.getText().equals("") && !txtVill.getText().equals("")
                    && !txtDist.getText().equals("") && !txtProv.getText().equals("") && gender != ""
                    && !birtDate.getValue().equals(null)) {

                memberModel = new MemberModel(txtId.getText(), txtFName.getText(), txtLName.getText(), gender,
                        txtTel.getText(), txtVill.getText(), txtDist.getText(), txtProv.getText(),
                        Date.valueOf(birtDate.getValue()), cmbYears.getSelectionModel().getSelectedItem(),
                        depIdList.get(index), Date.valueOf(LocalDate.now()), Date.valueOf(LocalDate.now().plusYears(1)),
                        Date.valueOf(localDateExit), byimg, memberid_edit);

                if (memberid_edit == "") {
                    // Todo: Insert (if save memberid_edit if null)
                    if (memberModel.saveData() > 0) {
                        alertMessage.showCompletedMessage("Saved", "Saved data successfully.", 4, Pos.BOTTOM_RIGHT);
                        if (memberController != null)
                            memberController.showData();
                    }
                } else {
                    if (memberModel.updateData() > 0) {
                        alertMessage.showCompletedMessage("Edited", "Edited data successfully.", 4, Pos.BOTTOM_RIGHT);
                        memberController.showData();
                    }
                }

            } else {
                alertMessage.showWarningMessage("Save Warning", "Please chack your information and try again.", 4,
                        Pos.BOTTOM_RIGHT);
            }
        } catch (Exception e) {
            alertMessage.showErrorMessage("Save Error", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
            logfile.createLogFile("ມີບັນຫາໃນການບັນທືກຂໍ້ມູນສະມາຊີກ", e);
        }
    }

    @FXML
    private void clearText() {
        memberid_edit = "";
        byimg = null;
        index = -1;
        txtId.clear();
        txtFName.clear();
        txtLName.clear();
        txtTel.clear();
        txtTel.clear();
        txtVill.clear();
        txtDist.clear();
        txtProv.clear();
        birtDate.setValue(null);
        imgPic.setImage(null);
        cmbDept.getSelectionModel().clearSelection();
        cmbYears.getSelectionModel().clearSelection();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        birtDate = dateFormat.formateDatePicker(birtDate);

        // Todo: Groud RadioButton
        ToggleGroup group = new ToggleGroup();
        rdbMale.setToggleGroup(group);
        rdbFemale.setToggleGroup(group);
        rdbMale.setSelected(true);
        cmbYears.setItems(years);
        fillDep();
        initEvents();
    }

    private void moveForm() {

        acPaneHeader.setOnMousePressed(event -> {
            x = event.getSceneX();
            y = event.getSceneY();
        });
        acPaneHeader.setOnMouseDragged(event -> {
            MemberController.addMemberStage.setX(event.getScreenX() - x);
            MemberController.addMemberStage.setY(event.getScreenY() - y);
        });
    }
}
