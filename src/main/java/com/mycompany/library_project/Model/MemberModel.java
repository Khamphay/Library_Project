package com.mycompany.library_project.Model;

import com.jfoenix.controls.JFXButton;
import com.mycompany.library_project.MyConnection;
import com.mycompany.library_project.ControllerDAOModel.DataAccessObject;

import java.sql.*;
import java.text.*;

public class MemberModel implements DataAccessObject {

    private PreparedStatement ps = null;
    private Connection con = MyConnection.getConnect();
    private ResultSet rs = null;
    private String query = "";

    private String memberId;
    private String firstName;
    private String sureName;
    private String gender;
    private String tel;
    private String village;
    private String district;
    private String province;
    private Date birdate;
    private String detp;
    private Date dateRegister;
    private Date dateRegisterEnd;
    private Date dateExit;
    private byte[] byimg;
    private JFXButton action;

    public MemberModel() {

    }

    public MemberModel(String memberId, String firstName, String sureName, String gender, String tel, String village,
            String district, String province, Date birdate, String detp, Date dateRegister, Date dateRegisterEnd,
            Date dateExit, byte[] byimg) {
        this.memberId = memberId;
        this.firstName = firstName;
        this.sureName = sureName;
        this.gender = gender;
        this.tel = tel;
        this.village = village;
        this.district = district;
        this.province = province;
        this.birdate = birdate;
        this.detp = detp;
        this.dateRegister = dateRegister;
        this.dateRegisterEnd = dateRegisterEnd;
        this.dateExit = dateExit;
        this.byimg = byimg;
    }

    public MemberModel(String memberId, String firstName, String sureName, String gender, String tel, String village,
            String district, String province, Date birdate, String detp, Date dateRegister, Date dateRegisterEnd,
            Date dateExit, JFXButton action) {
        this.memberId = memberId;
        this.firstName = firstName;
        this.sureName = sureName;
        this.gender = gender;
        this.tel = tel;
        this.village = village;
        this.district = district;
        this.province = province;
        this.birdate = birdate;
        this.detp = detp;
        this.dateRegister = dateRegister;
        this.dateRegisterEnd = dateRegisterEnd;
        this.dateExit = dateExit;
        this.action = action;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String fistName) {
        this.firstName = fistName;
    }

    public String getSureName() {
        return sureName;
    }

    public void setSureName(String sureName) {
        this.sureName = sureName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public Date getBirdate() {
        return birdate;
    }

    public void setBirdate(Date birdate) {
        this.birdate = birdate;
    }

    public String getDetp() {
        return detp;
    }

    public void setDetp(String detp) {
        this.detp = detp;
    }

    public Date getDateRegister() {
        return dateRegister;
    }

    public void setDateRegister(Date dateRegister) {
        this.dateRegister = dateRegister;
    }

    public Date getDateRegisterEnd() {
        return dateRegisterEnd;
    }

    public void setDateRegisterEnd(Date dateRegisterEnd) {
        this.dateRegisterEnd = dateRegisterEnd;
    }

    public Date getDateExit() {
        return dateExit;
    }

    public void setDateExit(Date dateExit) {
        this.dateExit = dateExit;
    }

    public byte[] getByimg() {
        return byimg;
    }

    public void setByimg(byte[] byimg) {
        this.byimg = byimg;
    }

    public JFXButton getAction() {
        return action;
    }

    public void setAction(JFXButton action) {
        this.action = action;
    }

    @Override
    public ResultSet findAll() throws SQLException {
        try {
            query = "call  member_Show();";
            rs = con.createStatement().executeQuery(query);
            return rs;
        } catch (Exception e) {
            e.printStackTrace();
           return null;
       }
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
        try {
            query = "call  member_Search(?);";
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            return rs;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public int saveData() throws SQLException, ParseException {
        if (getByimg() != null) {
            query = "call  member_Insert_Img(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
            ps = con.prepareStatement(query);
            ps.setBytes(14, getByimg());
        } else {
            query = "call  member_Insert(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
            ps = con.prepareStatement(query);
        }
        ps.setString(1, getMemberId());
        ps.setString(2, getFirstName());
        ps.setString(3, getSureName());
        ps.setString(4, getGender());
        ps.setString(5, getTel());
        ps.setString(6, getVillage());
        ps.setString(7, getDistrict());
        ps.setString(8, getProvince());
        ps.setDate(10, getBirdate());
        ps.setString(9, getDetp());
        ps.setDate(11, getDateRegister());
        ps.setDate(12, getDateRegisterEnd());
        ps.setDate(13, getDateExit());
        return ps.executeUpdate();
    }

    @Override
    public int updateData() throws SQLException, ParseException {
        if (getByimg() != null) {
            query = "call  member_Update_Img(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
            ps = con.prepareStatement(query);
            ps.setBytes(14, getByimg());
        } else {
            query = "call  member_Update(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
            ps = con.prepareStatement(query);
        }
        ps.setString(1, getMemberId());
        ps.setString(2, getFirstName());
        ps.setString(3, getSureName());
        ps.setString(4, getGender());
        ps.setString(5, getTel());
        ps.setString(6, getVillage());
        ps.setString(7, getDistrict());
        ps.setString(8, getProvince());
        ps.setDate(10, getBirdate());
        ps.setString(9, getDetp());
        ps.setDate(11, getDateRegister());
        ps.setDate(12, getDateRegisterEnd());
        ps.setDate(13, getDateExit());
        return ps.executeUpdate();
    }

    @Override
    public int deleteData(String id) throws SQLException {
        query = "call  member_Delete(?);";
        ps = con.prepareStatement(query);
        ps.setString(1, id);
        return ps.executeUpdate();
    }


}
