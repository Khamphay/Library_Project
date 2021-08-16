package com.mycompany.library_project.Controller;

import com.jfoenix.controls.*;

import javafx.beans.value.*;
import javafx.collections.*;
import javafx.concurrent.Task;
import javafx.event.*;
import javafx.fxml.*;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.*;
import javafx.util.Callback;

import java.io.*;

import com.mycompany.library_project.Model.*;
import com.mycompany.library_project.Report.CreateReport;

import org.controlsfx.control.MaskerPane;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import org.imgscalr.Scalr;

import com.mycompany.library_project.App;
import com.mycompany.library_project.ControllerDAOModel.*;

import java.net.URL;
import java.sql.*;
import java.text.DecimalFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;

public class RegisterController implements Initializable {

    private ValidationSupport validRules = new ValidationSupport();
    private MemberController memberController = null;
    public MemberModel memberModel = new MemberModel();
    private DepartmentModel depertment = new DepartmentModel();
    private AlertMessage alertMessage = new AlertMessage();
    private DialogMessage dialog = new DialogMessage();
    private MyDate dateFormat = new MyDate();
    private DateTimeFormatter formater = DateTimeFormatter.ofPattern("dMyy");
    private MaskerPane masker = new MaskerPane();
    private Task<Void> task = null;

    private BufferedImage resizeImg = null;
    private ByteArrayOutputStream byteStrem = null;
    private OutputStream outStrem = null;
    private FileChooser chooser = null;
    private File lastPath = null;
    private ObservableList<String> items = null;
    private ObservableList<String> years = FXCollections.observableArrayList("ປີ 1", "ປີ 1 ຕໍ່ເນື່ອງ", "ປີ 2",
            "ປີ 2 ຕໍ່ເນື່ອງ", "ປີ 3", "ປີ 4");
    private ArrayList<String> depIdList = null;
    private DecimalFormat dcFormat = new DecimalFormat("#,##0.00 ກີບ");
    private LocalDate minDate = LocalDate.now().plusYears(-30), maxDate = LocalDate.now();

    private LocalDate localDateExit;
    private int index = -1;
    private String memberid_edit = "", gender = "";
    private byte[] byimg = null;
    private Double x, y, costPrice = 0.0;

    /*
     * Todo: Use from class MemberController for both form can communicate or use
     * for Refresh table after add and delete
     */
    public void initConstructor1(HomeController homeController) {
        dateRegister.setValue(LocalDate.now());
        getDateRegisterEnd();
        btClose.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                homeController.showMainMenuHome();
            }

        });
    }

    public void initConstructor2(MemberController memberController, Stage addMemberStage) {
        this.memberController = memberController;

        if (MemberController.add || memberModel != null) {
            moveForm();
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
            txtMemberId.setEditable(false);
            memberid_edit = memberModel.getMemberId();
            txtMemberId.setText(memberModel.getMemberId());
            txtStudentId.setText(memberModel.getStudentId());
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
            convertByteToImage(memberModel.getByimg());

            memberModel = null;
        }
    }

    @FXML
    private StackPane stackPane;

    @FXML
    private BorderPane borderPane;

    @FXML
    private AnchorPane acPaneHeader;

    @FXML
    private JFXButton btClose, btAddDepartment;

    @FXML
    private TextField txtMemberId, txtStudentId, txtFName, txtLName, txtTel, txtVill, txtDist, txtProv, txtCostRegister;
    @FXML
    private ComboBox<String> cmbDept, cmbYears;

    @FXML
    private JFXRadioButton rdbMale, rdbFemale;

    @FXML
    private DatePicker birtDate, dateRegister, dateEnd;

    @FXML
    private ImageView imgPic;

    private void convertByteToImage(byte[] value) {
        if (value != null) {
            try {
                outStrem = new FileOutputStream(new File("img.png"));
                try {
                    outStrem.write(value);
                    imgPic.setImage(new Image("file:img.png"));
                } catch (IOException e) {
                    alertMessage.showErrorMessage("Read Image", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
                }
            } catch (FileNotFoundException e) {
                alertMessage.showErrorMessage("Write Image", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
            }
        }
    }

    public void getDateRegisterEnd() {
        if (!dateRegister.getValue().equals(null)) {
            int year = dateRegister.getValue().getYear();
            int month = dateRegister.getValue().getMonthValue();
            if (month >= 9)
                dateEnd.setValue(LocalDate.of(year + 1, 8, 31));
            else
                dateEnd.setValue(LocalDate.of(year, 8, 31));
        }
    }

    public void fillDep() {
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

                // imgPic.setFitWidth(173);
                // imgPic.setFitHeight(221);
                imgPic.setImage(new Image(file.toURI().toASCIIString(), imgPic.getFitWidth(), imgPic.getFitHeight(),
                        true, true));

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
            dialog.showExcectionDialog("Error", null, "ເກິດບັນຫາໃນການສະແດງຮູບພາບ", e);
        }
    }

    private void showAddDep() {
        try {
            final FXMLLoader loader = new FXMLLoader(App.class.getResource("frmDepartment.fxml"));
            final Parent deproot = loader.load();
            DepartmentController departmentController = loader.getController();
            final Scene scene = new Scene(deproot);
            scene.setFill(Color.TRANSPARENT);
            final Stage stage = new Stage();
            stage.initStyle(StageStyle.TRANSPARENT);
            departmentController.initConstructor2(this, stage);
            stage.setTitle("Add New Department");
            stage.setScene(scene);
            stage.getIcons().add(new Image("/com/mycompany/library_project/Icon/icon.png"));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (Exception e) {
            dialog.showExcectionDialog("Error", null, "ເກິດບັນຫາໃນການເປີດຟອມຈັດການຂໍ້ມູນພາກວິຊາ", e);
        }
    }

    private void initEvents() {

        btAddDepartment.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                showAddDep();
            }

        });
        txtTel.textProperty().addListener(new ChangeListener<String>() {
            // Todo: set properties type only numeric
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    txtTel.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        /*
         * // Todo: Set properties to txtCostRegister for type only numeric
         * txtCostRegister.textProperty().addListener(new ChangeListener<String>() {
         * 
         * @Override public void changed(ObservableValue<? extends String> observable,
         * String oldValue, String newValue) { if (!newValue.matches("\\d*")) {
         * txtCostRegister.setText(newValue.replaceAll("[^\\d]", "")); } } });
         */

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
            int year = dateRegister.getValue().getYear();
            int month = dateRegister.getValue().getMonthValue();
            if (cmbYears.getSelectionModel().getSelectedItem() == "ປີ 1 ຕໍ່ເນື່ອງ") {
                if (month >= 9)
                    localDateExit = LocalDate.of(year + 2, 8, 31);
                else
                    localDateExit = LocalDate.of(year + 1, 8, 31);
            } else if (cmbYears.getSelectionModel().getSelectedItem() == "ປີ 2 ຕໍ່ເນື່ອງ") {
                if (month >= 9)
                    localDateExit = LocalDate.of(year + 1, 8, 31);
                else
                    localDateExit = LocalDate.of(year, 8, 31);
            } else if (cmbYears.getSelectionModel().getSelectedItem() == "ປີ 1") {
                if (month >= 9)
                    localDateExit = LocalDate.of(year + 4, 8, 31);
                else
                    localDateExit = LocalDate.of(year + 3, 8, 31);
            } else if (cmbYears.getSelectionModel().getSelectedItem() == "ປີ 2") {
                if (month >= 9)
                    localDateExit = LocalDate.of(year + 3, 8, 31);
                else
                    localDateExit = LocalDate.of(year + 2, 8, 31);
            } else if (cmbYears.getSelectionModel().getSelectedItem() == "ປີ 3") {
                if (month >= 9)
                    localDateExit = LocalDate.of(year + 2, 8, 31);
                else
                    localDateExit = LocalDate.of(year + 1, 8, 31);
            } else {
                if (month >= 9)
                    localDateExit = LocalDate.of(year + 1, 8, 31);
                else
                    localDateExit = LocalDate.of(year, 8, 31);
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

    private void initKeyEvents() {
        txtMemberId.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                txtStudentId.requestFocus();
                try {
                    if (memberid_edit != "")
                        return;

                    ResultSet rs = memberModel.findById(txtMemberId.getText());
                    if (rs.next()) {
                        memberid_edit = rs.getString("student_id");
                        txtStudentId.setText(rs.getString("student_id"));
                        txtFName.setText(rs.getString("full_name"));
                        txtLName.setText(rs.getString("sur_name"));
                        txtTel.setText(rs.getString("tel"));
                        if (rs.getString("gender").equals("ຊາຍ"))
                            rdbMale.setSelected(true);
                        else
                            rdbFemale.setSelected(true);

                        txtVill.setText(rs.getString("village"));
                        txtDist.setText(rs.getString("district"));
                        txtProv.setText(rs.getString("province"));
                        cmbDept.getSelectionModel().select(rs.getString("dep_name"));
                        birtDate.setValue(rs.getDate("birthdate").toLocalDate());
                        cmbYears.getSelectionModel().select(rs.getString("study_year"));
                        dateRegister.setValue(rs.getDate("date_register").toLocalDate());

                        // Todo: Show image
                        convertByteToImage(rs.getBytes("img"));

                    }
                } catch (SQLException e) {
                    e.getNextException();
                }
            }
        });

        txtStudentId.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                if (txtMemberId.getText().equals(""))
                    txtMemberId.setText(getMemberId(txtStudentId.getText()));
                txtFName.requestFocus();
            }
        });
        txtFName.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER)
                txtLName.requestFocus();
        });
        txtLName.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER)
                birtDate.requestFocus();
        });
        birtDate.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER)
                txtTel.requestFocus();
        });
        txtTel.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER)
                txtVill.requestFocus();
        });
        txtVill.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER)
                txtDist.requestFocus();
        });
        txtDist.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER)
                txtProv.requestFocus();
        });
        txtProv.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER)
                txtStudentId.requestFocus();
        });

        dateRegister.setDayCellFactory(new Callback<DatePicker, DateCell>() {

            @Override
            public DateCell call(DatePicker param) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        DayOfWeek day = DayOfWeek.from(item);
                        if (day == DayOfWeek.SUNDAY || day == DayOfWeek.SATURDAY)
                            this.setTextFill(Color.RED);
                    }
                };
            }

        });

        dateRegister.setOnAction(e -> {
            getDateRegisterEnd();
        });
    }

    private void initRules() {
        validRules.setErrorDecorationEnabled(false);
        validRules.registerValidator(txtStudentId, false, Validator.createEmptyValidator("ກະລຸນາປ້ອນລະຫັດນັກສຶກສາ"));
        validRules.registerValidator(txtFName, false, Validator.createEmptyValidator("ກະລຸນາປ້ອນຊື່ສະມາຊິດ"));
        validRules.registerValidator(txtLName, false, Validator.createEmptyValidator("ກະລຸນາປ້ອນນາມສະກຸນ"));
        validRules.registerValidator(txtVill, false, Validator.createEmptyValidator("ກະລຸນາປ້ອນຂໍ້ມູນບ້ານ"));
        validRules.registerValidator(txtDist, false, Validator.createEmptyValidator("ກະລຸນາປ້ອນຂໍ້ມູນເມືອງ"));
        validRules.registerValidator(txtProv, false, Validator.createEmptyValidator("ກະລຸນາປ້ອນຂໍ້ມູນແຂວງ"));
        validRules.registerValidator(txtTel, false, Validator.createEmptyValidator("ກະລຸນາປ້ອນເບີໂທລະສັບ"));
        validRules.registerValidator(cmbDept, false, Validator.createEmptyValidator("ກະລຸນາເລືອກຊື່ພາກວີຊາ"));
        validRules.registerValidator(cmbYears, false, Validator.createEmptyValidator("ກະລຸນາເລືອກປີຮຽນຂອງນັກສຶກສາ"));
        validRules.registerValidator(birtDate, false, Validator
                .createEmptyValidator("ກະລຸນາປ້ອນຂໍ້ມູນວັນເດືອນປີເກີດ ແລະ ວັນເດືອນປີເກີດຄວນຢູ່ລະຫວ່າງ 15-30 ປີ"));
        validRules.registerValidator(dateRegister, false, Validator.createEmptyValidator("ກະລຸນາເລືອກວັນທີລົງທະບຽນ"));
        validRules.registerValidator(dateEnd, false, Validator.createEmptyValidator("ກະລຸນາເລືອກວັນທີໝົດອາຍຸຂອງບັດ"));

    }

    private String getMemberId(String studentid) {
        try {
            int startIndex = 0, endIndex;
            if (studentid.indexOf('S') > 0)
                startIndex = studentid.indexOf('S') + 1;
            else if (studentid.indexOf('N') > 0)
                startIndex = studentid.indexOf('N') + 1;
            else if (studentid.indexOf('Q') > 0)
                startIndex = studentid.indexOf('Q') + 1;

            if (studentid.indexOf(".") > 0)
                endIndex = studentid.indexOf('.');
            else if (studentid.indexOf("/") > 0)
                endIndex = studentid.indexOf('/');
            else
                endIndex = studentid.length();
            String stid = "FNSLM" + studentid.substring(startIndex, endIndex) + "/" + formater.format(LocalDate.now());
            return stid;
        } catch (Exception e) {
            return studentid;
        }
    }

    private void clearValues() {
        validRules.setErrorDecorationEnabled(false);
        memberid_edit = "";
        byimg = null;
        index = -1;
        if (txtMemberId.isEditable() == false)
            txtMemberId.setEditable(true);
        txtMemberId.clear();
        txtStudentId.clear();
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
        txtStudentId.requestFocus();
    }

    @FXML
    private void setImage(ActionEvent event) {
        loadImager();
    }

    @FXML
    private void Save(ActionEvent event) {
        try {
            gender = (rdbMale.isSelected() ? rdbMale.getText() : rdbFemale.getText());
            if (index > -1 && !txtStudentId.getText().equals("") && !txtFName.getText().equals("")
                    && !txtLName.getText().equals("") && !txtTel.getText().equals("") && !txtVill.getText().equals("")
                    && !txtDist.getText().equals("") && !txtProv.getText().equals("") && gender != ""
                    && !birtDate.getValue().equals(null) && cmbYears.getSelectionModel().getSelectedItem() != null
                    && !dateRegister.getValue().equals(null) && !dateEnd.getValue().equals(null)) {

                if (txtTel.getText().length() < 7 || txtTel.getText().length() > 11) {
                    dialog.showWarningDialog(null, "ເບີໂທລະສັບຕ້ອງຢູ່ລະຫວ່າງ 7 ຫາ 11 ຕົວເລກເທົ່ານັ້ນ.");
                    txtTel.requestFocus();
                    return;
                }
                memberModel = new MemberModel(txtMemberId.getText(), txtStudentId.getText(), txtFName.getText(),
                        txtLName.getText(), gender, txtTel.getText(), txtVill.getText(), txtDist.getText(),
                        txtProv.getText(), Date.valueOf(birtDate.getValue()),
                        cmbYears.getSelectionModel().getSelectedItem(), depIdList.get(index),
                        Date.valueOf(dateRegister.getValue()), Date.valueOf(dateEnd.getValue()),
                        Date.valueOf(localDateExit), byimg, costPrice);

                if (memberid_edit == "") {
                    ResultSet rs = memberModel.findById(txtMemberId.getText());
                    if (rs.next()) {
                        dialog.showWarningDialog(null,
                                "ລະຫັດສະມາຊິກຊ້ຳກັບຂໍ້ມູນທີ່ມີຢູ່ໃນລະບົບກະລຸນາກວດສອບຂໍ້ມູນ ແລ້ວລອງບັນທຶກໃຫມ່ອີກຄັ້ງ");
                        return;
                    }
                    // Todo: Insert (if save memberid_edit if null)
                    if (memberModel.saveData() > 0) {
                        alertMessage.showCompletedMessage("Saved", "Saved data successfully.", 4, Pos.BOTTOM_RIGHT);
                        task = new Task<Void>() {

                            @Override
                            protected Void call() throws Exception {
                                masker.setVisible(true);
                                masker.setProgressVisible(true);

                                InputStream imgInputStream = this.getClass().getResourceAsStream("bin/Logo.png");
                                CreateReport printCard = new CreateReport();
                                Map<String, Object> map = new HashMap<String, Object>();
                                map.put("memberid", memberModel.getMemberId());
                                map.put("logo", imgInputStream);
                                printCard.showReport(map, "printCardById.jrxml", "Print Member Card Error");

                                map.put("memberid", memberModel.getMemberId());
                                map.put("logo", imgInputStream);
                                printCard.showReport(map, "billRegister.jrxml", "Print Bill Error");

                                return null;
                            }

                            @Override
                            protected void succeeded() {
                                super.succeeded();
                                masker.setProgressVisible(false);
                                masker.setVisible(false);
                            }

                            @Override
                            protected void failed() {
                                super.failed();
                                masker.setProgressVisible(false);
                                masker.setVisible(false);
                                dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການພີມບັດສະມາຊິກ",
                                        task.getException());
                            }
                        };
                        new Thread(task).start();


                        clearValues();
                        if (memberController != null)
                            memberController.showData();
                    }
                } else {
                    if (memberModel.updateData() > 0) {
                        alertMessage.showCompletedMessage("Edited", "Edited data successfully.", 4, Pos.BOTTOM_RIGHT);
                        clearValues();
                        if (memberController != null)
                            memberController.showData();
                    }
                }
            } else {
                validRules.setErrorDecorationEnabled(true);
                alertMessage.showWarningMessage("Save Warning", "Please chack your information and try again.", 4,
                        Pos.BOTTOM_RIGHT);
            }
        } catch (Exception e) {
            dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການບັນທຶກຂໍ້ມູນ", e);
            e.printStackTrace();
        }
    }

    @FXML
    private void clearText() {
        clearValues();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        masker.setVisible(false);
        masker.setText("ກຳລັງໂຫລດຂໍ້ມູນ, ກະລຸນາລໍຖ້າ...");
        masker.setStyle("-fx-font-family: BoonBaan;");
        stackPane.getChildren().add(masker);

        birtDate = dateFormat.formateDatePicker(birtDate);
        dateRegister = dateFormat.formateDatePicker(dateRegister);
        dateEnd = dateFormat.formateDatePicker(dateEnd);

        // Todo: Groud RadioButton
        ToggleGroup group = new ToggleGroup();
        rdbMale.setToggleGroup(group);
        rdbFemale.setToggleGroup(group);
        rdbMale.setSelected(true);
        cmbYears.setItems(years);
        fillDep();
        initRules();
        initEvents();
        initKeyEvents();
        costPrice = StaticCostPrice.RegisterCost;
        txtCostRegister.setText(dcFormat.format(costPrice));

        dateRegister.setValue(LocalDate.now());
        getDateRegisterEnd();
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
