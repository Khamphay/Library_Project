package com.mycompany.library_project.Controller;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import com.mycompany.library_project.Model.MemberModel;

import javafx.fxml.*;
import javafx.scene.text.Text;

public class MemberListController implements Initializable {

    private DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @FXML
    private Text number, memberid, firstName, lastName, studyYear, depName, regisDate, endDate;

    public void initConstructor(MemberModel memberModel) {
        number.setText(Integer.toString(memberModel.getNumber()));
        memberid.setText(memberModel.getMemberId());
        firstName.setText(memberModel.getFirstName());
        lastName.setText(memberModel.getSureName());
        studyYear.setText(memberModel.getStudy_year());
        depName.setText(memberModel.getDetp());
        regisDate.setText(dateFormat.format(memberModel.getDateRegister().toLocalDate()));
        endDate.setText(dateFormat.format(memberModel.getDateRegisterEnd().toLocalDate()));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO Auto-generated method stub

    }

}
