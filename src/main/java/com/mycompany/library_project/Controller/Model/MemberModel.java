package com.mycompany.library_project.Controller.Model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import com.mycompany.library_project.MyConnection;
import com.mycompany.library_project.Controller.ControllerDAOModel.DataAccessObject;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class MemberModel implements DataAccessObject {

    private PreparedStatement ps = null;
    private Connection con = MyConnection.getConnect();
    private ResultSet rs = null;
    private String query = "";

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private StringProperty memberId;
    private StringProperty fistName;
    private StringProperty sureName;
    private StringProperty gender;
    private StringProperty tel;
    private StringProperty village;
    private StringProperty district;
    private StringProperty province;
    private StringProperty birdate;
    private StringProperty detp;
    private StringProperty dateRegister;
    private StringProperty dateRegisterEnd;
    private StringProperty dateExit;
    private StringProperty status;

    public MemberModel(String memberId, String fistName, String sureName, String gender, String tel, String village,
            String district, String province, String birdate, String detp, String dateRegister, String dateRegisterEnd,
            String dateExit, String status) {
        this.memberId = new SimpleStringProperty(memberId);
        this.fistName = new SimpleStringProperty(fistName);
        this.sureName = new SimpleStringProperty(sureName);
        this.gender = new SimpleStringProperty(gender);
        this.tel = new SimpleStringProperty(tel);
        this.village = new SimpleStringProperty(village);
        this.district = new SimpleStringProperty(district);
        this.province = new SimpleStringProperty(province);
        this.birdate = new SimpleStringProperty(birdate);
        this.detp = new SimpleStringProperty(detp);
        this.dateRegister = new SimpleStringProperty(dateRegister);
        this.dateRegisterEnd = new SimpleStringProperty(dateRegisterEnd);
        this.dateExit = new SimpleStringProperty(dateExit);
        this.status = new SimpleStringProperty(status);
    }

    // Todo: set values
    public void setMemberId(String memberId) {
        this.memberId.set(memberId);
    }

    private void setFistName(String fistName) {
        this.fistName.set(fistName);
    }

    private void setSureName(String sureName) {
        this.sureName.set(sureName);
    }

    public void setGender(String gender) {
        this.gender.set(gender);
    }

    public void setTel(String tel) {
        this.tel.set(tel);
    }

    public void setVillage(String village) {
        this.village.set(village);
    }

    public void setDistrict(String district) {
        this.district.set(district);
    }

    public void setProvince(String province) {
        this.province.set(province);
    }

    public void setBirdate(String birdate) {
        this.birdate.set(birdate);
    }

    public void setDetp(String detp) {
        this.detp.set(detp);
    }

    public void setDateRegister(String dateRegister) {
        this.dateRegister.set(dateRegister);
    }

    public void setDateRegisterEnd(String dateRegisterEnd) {
        this.dateRegisterEnd.set(dateRegisterEnd);
    }

    public void setDateExit(String dateExit) {
        this.dateExit.set(dateExit);
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    // Todo: return values
    private String getMemberId() {
        return memberId.get();
    }

    private String getFirstName() {
        return fistName.get();
    }

    private String getSureName() {
        return sureName.get();
    }

    public String getGender() {
        return gender.get();
    }

    public String getTel() {
        return tel.get();
    }

    public String getVillage() {
        return village.get();
    }

    public String getDistrict() {
        return district.get();
    }

    public String getProvince() {
        return province.get();
    }

    public String getBirdate() {
        return birdate.get();
    }

    public String getDetp() {
        return detp.get();
    }

    public String getDateRegister() {
        return dateRegister.get();
    }

    public String getDateRegisterEnd() {
        return dateRegisterEnd.get();
    }

    public String getDateExit() {
        return dateExit.get();
    }

    public String getStatus() {
        return status.get();
    }

    // TODO: Set to StringProperty
    public StringProperty memberIdProperty() {
        return memberId;
    }

    public StringProperty fistNameProperty() {
        return fistName;
    }

    public StringProperty sureNameProperty() {
        return sureName;
    }

    public StringProperty genderProperty() {
        return gender;
    }

    public StringProperty telProperty() {
        return tel;
    }

    public StringProperty villageProperty() {
        return village;
    }

    public StringProperty districtProperty() {
        return district;
    }

    public StringProperty provinceProperty() {
        return province;
    }

    public StringProperty birdateProperty() {
        return birdate;
    }

    public StringProperty detpProperty() {
        return detp;
    }

    public StringProperty dateRegisterProperty() {
        return dateRegister;
    }

    public StringProperty dateRegisterEndProperty() {
        return dateRegisterEnd;
    }

    public StringProperty dateExitProperty() {
        return dateExit;
    }

    public StringProperty statusProperty() {
        return status;
    }

    @Override
    public ResultSet findAll() throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ResultSet findById(String id) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ResultSet findByName(String name) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ResultSet searchData(String values) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int saveData() throws SQLException, ParseException {

        query = "call saveMember(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
        ps = con.prepareStatement(query);
        ps.setString(1, getMemberId());
        ps.setString(2, getFirstName());
        ps.setString(3, getSureName());
        ps.setString(4, getGender());
        ps.setString(5, getTel());
        ps.setString(6, getVillage());
        ps.setString(7, getDistrict());
        ps.setString(8, getProvince());
        ps.setDate(9, (Date) dateFormat.parse(getBirdate()));
        ps.setString(10, getDetp());
        ps.setDate(11, (Date) dateFormat.parse(getDateRegister()));
        ps.setDate(12, (Date) dateFormat.parse(getDateRegisterEnd()));
        ps.setDate(13, (Date) dateFormat.parse(getDateExit()));
        ps.setString(14, getStatus());

        return ps.executeUpdate();
    }

    @Override
    public int updateData() throws SQLException, ParseException {
        query = "call updateMember(:id, :frname, :lsname, :genders, :tels, :vill, :dist, :prov, :bird, :detp, :dateRg, :dateRE, :dateCEn, :stat);";
        ps = con.prepareStatement(query);
        ps.setString(1, getMemberId());
        ps.setString(2, getFirstName());
        ps.setString(3, getSureName());
        ps.setString(4, getGender());
        ps.setString(5, getTel());
        ps.setString(6, getVillage());
        ps.setString(7, getDistrict());
        ps.setString(8, getProvince());
        ps.setDate(9, (Date) dateFormat.parse(getBirdate()));
        ps.setString(10, getDetp());
        ps.setDate(11, (Date) dateFormat.parse(getDateRegister()));
        ps.setDate(12, (Date) dateFormat.parse(getDateRegisterEnd()));
        ps.setDate(13, (Date) dateFormat.parse(getDateExit()));
        ps.setString(14, getStatus());
        return ps.executeUpdate();
    }

    @Override
    public int deleteData() throws SQLException {
        query = "call deleteMember(:id);";
        ps = con.prepareStatement(query);
        ps.setString(1, getMemberId());
        return ps.executeUpdate();
    }
}
