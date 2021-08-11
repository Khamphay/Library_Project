package com.mycompany.library_project.Controller;

import java.net.URL;
import java.sql.Date;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ResourceBundle;

import com.mycompany.library_project.App;
import com.mycompany.library_project.ControllerDAOModel.DialogMessage;
import com.mycompany.library_project.Model.MemberModel;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

public class ManageBookController implements Initializable {

    private DialogMessage dialog = new DialogMessage();
    public static BorderPane mainBorder = null;
    // private BookLostModel booklost = new BookLostModel();
    private MemberModel memberModel = new MemberModel();
    private ResultSet rs = null;
    // private ListBookModel listbook = null;

    @FXML
    private Text txtType, txtCategory, txtBook, txtBookLost, txtTableLog, txtMember, txtEmployee, txtAuthor, txtDep;

    @FXML
    private BorderPane bpManageBook;

    @FXML
    private ScrollPane scrollMenu;

    // Todo: Books
    @FXML
    private Text textTotalListBook;

    @FXML
    private VBox pnItemsBook;

    // Todo: Personals

    @FXML
    private VBox pnItemsPerson;

    @FXML
    private Text textTotalListPerson;

    public void showMainMenuBooks() {
        bpManageBook.setCenter(scrollMenu);
    }

    // Todo: Book List
    /*
     * private void showBookLostList() { pnItemsBook.getChildren().clear();
     * Platform.runLater(new Runnable() {
     * 
     * @Override public void run() { try { int number = 1; rs =
     * booklost.findByDate(Date.valueOf(LocalDate.now())); while (rs.next()) {
     * 
     * listbook = new ListBookModel(number, rs.getString("barcode"),
     * rs.getString("book_name"), rs.getString("catg_name"),
     * rs.getString("type_name"), rs.getString("tableid"), rs.getString("tablelog"),
     * rs.getDate("date_pay"));
     * 
     * final FXMLLoader loader = new
     * FXMLLoader(App.class.getResource("frmBookLostList.fxml")); final Parent
     * listRoot = loader.load(); final BookLostListController bookLostListController
     * = loader.getController(); bookLostListController.initConstructor(listbook);
     * final Node node = listRoot; pnItemsBook.getChildren().add(node);
     * 
     * number++; } textTotalListBook.setText(pnItemsBook.getChildren().size() +
     * " ລາຍການ"); } catch (Exception e) { dialog.showExcectionDialog("Error", null,
     * "ມີບັນຫາໃນການໂຫຼດຂໍ້ມູນປຶ້ມເສຍ", e); } } }); }
     */

    // Todo: Personals List
    private void showMemberEnd() {
        pnItemsPerson.getChildren().clear();
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                try {
                    int number = 1;
                    rs = memberModel.findMemberEndOfDate(Date.valueOf(LocalDate.now()));
                    while (rs.next()) {

                        memberModel = new MemberModel(number, rs.getString("member_id"), rs.getString("full_name"),
                                rs.getString("sur_name"), rs.getString("study_year"), rs.getString("dep_name"),
                                rs.getDate("date_register"), rs.getDate("date_end"));

                        final FXMLLoader loader = new FXMLLoader(App.class.getResource("frmMemberExitList.fxml"));
                        final Parent root = loader.load();
                        MemberListController memberController = loader.getController();
                        memberController.initConstructor(memberModel);

                        final Node node = root;
                        pnItemsPerson.getChildren().add(node);
                        number++;
                    }
                    textTotalListPerson.setText(pnItemsPerson.getChildren().size() + " ລາຍການ");
                } catch (Exception e) {
                    dialog.showExcectionDialog("Error", null, "ມີບັນຫາໃນການໂຫຼດຂໍ້ມູນບັດສະມາຊິກໝົດອາຍຸ", e);
                }
            }

        });
    }

    @FXML
    private void btBookType_Click(ActionEvent event) {
        try {
            final FXMLLoader loader = new FXMLLoader(App.class.getResource("frmBookType.fxml"));
            final Parent subForm = loader.load();
            BookTypeController bookTypeController = loader.getController();
            bookTypeController.initConstructor(this);
            bpManageBook.setCenter(subForm);
        } catch (Exception e) {
            dialog.showExcectionDialog("Error", null, "ມີບັນຫາໃນການເປີດຟອມຈັດການຂໍ້ມູນປະເພດປຶ້ມ", e);
        }
    }

    @FXML
    private void btBookCategory_Click(ActionEvent event) {
        try {
            final FXMLLoader loader = new FXMLLoader(App.class.getResource("frmBookCategory.fxml"));
            final Parent subForm = loader.load();
            BookCategoryController bookCategoryController = loader.getController();
            bookCategoryController.initConstructor(this);
            bpManageBook.setCenter(subForm);
        } catch (Exception e) {
            dialog.showExcectionDialog("Error", null, "ມີບັນຫາໃນການເປີດຟອມຈັດການຂໍ້ມູນໝວດປຶ້ມ", e);
        }
    }

    @FXML
    private void btBookTableLog_Click(ActionEvent event) {
        try {
            final FXMLLoader loader = new FXMLLoader(App.class.getResource("frmTableLogs.fxml"));
            final Parent subForm = loader.load();
            TableLogController tableLogController = loader.getController();
            tableLogController.initConstructor(this);
            bpManageBook.setCenter(subForm);
        } catch (Exception e) {
            dialog.showExcectionDialog("Error", null, "ມີບັນຫາໃນການເປີດຟອມຈັດການຂໍ້ມູນລ໋ອກຕູ້", e);
        }
    }

    @FXML
    private void btBooks_Click(ActionEvent event) {
        try {
            final FXMLLoader loader = new FXMLLoader(App.class.getResource("frmBooks.fxml"));
            final Parent subForm = loader.load();
            BookController bookController = loader.getController();
            bookController.initConstructor(this);
            bpManageBook.setCenter(subForm);
        } catch (Exception e) {
            dialog.showExcectionDialog("Error", null, "ມີບັນຫາໃນການເປີດຟອມຈັດການຂໍ້ມູນປຶ້ມ", e);
        }
    }

    @FXML
    private void btBookLost_Click(ActionEvent event) {
        try {
            final FXMLLoader loader = new FXMLLoader(App.class.getResource("frmShowBookLost.fxml"));
            final Parent subForm = loader.load();
            ShowBookLostController booklostController = loader.getController();
            booklostController.initConstructor(this);
            bpManageBook.setCenter(subForm);
        } catch (Exception e) {
            dialog.showExcectionDialog("Error", null, "ມີບັນຫາໃນການເປີດຟອມຈັດການຂໍ້ມູນປຶ້ມເສຍ", e);
        }
    }

    // Todo: Personal Event

    @FXML
    private void btEmployee_Click(ActionEvent event) {
        try {
            final FXMLLoader loader = new FXMLLoader(App.class.getResource("frmEmployee.fxml"));
            final Parent subForm = loader.load();
            EmployeeController employeeController = loader.getController();
            employeeController.initConstructor(this);
            bpManageBook.setCenter(subForm);
        } catch (Exception e) {
            dialog.showExcectionDialog("Error", null, "ມີບັນຫາໃນການເປີດຟອມຈັດການຂໍ້ມູນພະນັກງານ", e);
        }
    }

    @FXML
    private void btMember_Click(ActionEvent event) {
        try {
            final FXMLLoader loader = new FXMLLoader(App.class.getResource("frmMember.fxml"));
            final Parent subForm = loader.load();
            MemberController memberController = loader.getController();
            memberController.initConstructor(this);
            bpManageBook.setCenter(subForm);
        } catch (Exception e) {
            dialog.showExcectionDialog("Error", null, "ມີບັນຫາໃນການເປີດຟອມຈັດການຂໍ້ມູນສະມາຊິກ", e);
        }
    }

    @FXML
    private void btAncthor_Click(ActionEvent event) {
        try {
            final FXMLLoader loader = new FXMLLoader(App.class.getResource("frmAuthor.fxml"));
            final Parent subForm = loader.load();
            AuthorController authorController = loader.getController();
            authorController.initConstructor(this);
            bpManageBook.setCenter(subForm);
        } catch (Exception e) {
            dialog.showExcectionDialog("Error", null, "ມີບັນຫາໃນການເປີດຟອມຈັດການຂໍ້ມູນນັກແຕ່ງ", e);
        }
    }

    @FXML
    private void btDepartment_Click(ActionEvent event) {
        try {
            final FXMLLoader loader = new FXMLLoader(App.class.getResource("frmDepartment.fxml"));
            final Parent subForm = loader.load();
            DepartmentController departmentController = loader.getController();
            departmentController.initConstructor(this);
            bpManageBook.setCenter(subForm);
        } catch (Exception e) {
            dialog.showExcectionDialog("Error", null, "ມີບັນຫາໃນການເປີດຟອມຈັດການຂໍ້ມູນພາກວິຊາ", e);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        txtType.setText(HomeController.summaryValue[0] + " ປະເພດ");
        txtCategory.setText(HomeController.summaryValue[1] + " ຫວດໝູ່");
        txtBook.setText(
                "ຈຳນວນ " + ((HomeController.summaryValue[2] != null) ? HomeController.summaryValue[2] : "0") + " ຫົວ");
        txtBookLost.setText(
                "ຈຳນວນ " + ((HomeController.summaryValue[3] != null) ? HomeController.summaryValue[3] : "0") + " ຫົວ");
        txtTableLog.setText("ຈຳນວນ " + HomeController.summaryValue[4] + " ຕູ້ ແລະ "
                + ((HomeController.summaryValue[5] != null) ? HomeController.summaryValue[5] : "0") + " ລ໋ອກຕູ້");

        txtMember.setText(HomeController.summaryValue[6] + " ຄົນ");
        txtEmployee.setText(HomeController.summaryValue[7] + " ຄົນ");
        txtAuthor.setText(HomeController.summaryValue[8] + " ຄົນ");
        txtDep.setText(HomeController.summaryValue[9] + " ພາກວິຊາ");

        // showBookLostList();
        showMemberEnd();
    }

}
