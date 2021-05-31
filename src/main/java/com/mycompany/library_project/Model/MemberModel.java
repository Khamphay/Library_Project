package com.mycompany.library_project.Model;

import com.mycompany.library_project.MyConnection;
import com.mycompany.library_project.ControllerDAOModel.DataAccessObject;

import java.sql.*;
import java.text.*;

public class MemberModel implements DataAccessObject {

    private PreparedStatement ps = null;
    private Connection con = MyConnection.getConnect();
    private ResultSet rs = null;
    private String query = "";

    private int number;
    private String memberId;
    private String oldmemberId;
    private String firstName;
    private String sureName;
    private String gender;
    private String tel;
    private String village;
    private String district;
    private String province;
    private Date birdate;
    private String study_year;
    private String detp;
    private Date dateRegister;
    private Date dateRegisterEnd;
    private Date dateExit;
    private byte[] byimg;

    public MemberModel() {

    }

    public MemberModel(int number, String memberId, String firstName, String sureName, String study_year, String detp,
            Date dateRegister, Date dateRegisterEnd) {
        this.number = number;
        this.memberId = memberId;
        this.firstName = firstName;
        this.sureName = sureName;
        this.study_year = study_year;
        this.detp = detp;
        this.dateRegister = dateRegister;
        this.dateRegisterEnd = dateRegisterEnd;
    }

    public MemberModel(String memberId, String firstName, String sureName, String gender, String tel, String village,
            String district, String province, Date birdate, String study_year, String detp, Date dateRegister,
            Date dateRegisterEnd,
            Date dateExit, byte[] byimg, String oldmemberId) {
        this.memberId = memberId;
        this.firstName = firstName;
        this.sureName = sureName;
        this.gender = gender;
        this.tel = tel;
        this.village = village;
        this.district = district;
        this.province = province;
        this.birdate = birdate;
        this.study_year = study_year;
        this.detp = detp;
        this.dateRegister = dateRegister;
        this.dateRegisterEnd = dateRegisterEnd;
        this.dateExit = dateExit;
        this.byimg = byimg;
        this.oldmemberId= oldmemberId;
    }


    public MemberModel(String memberId, String firstName, String sureName, String gender, String tel, String village,
            String district, String province, Date birdate, String study_year, String detp, Date dateRegister,
            Date dateRegisterEnd,
            Date dateExit) {
        this.memberId = memberId;
        this.firstName = firstName;
        this.sureName = sureName;
        this.gender = gender;
        this.tel = tel;
        this.village = village;
        this.district = district;
        this.province = province;
        this.birdate = birdate;
        this.study_year = study_year;
        this.detp = detp;
        this.dateRegister = dateRegister;
        this.dateRegisterEnd = dateRegisterEnd;
        this.dateExit = dateExit;
    }


    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
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

    public String getStudy_year() {
        return study_year;
    }

    public void setStudy_year(String study_year) {
        this.study_year = study_year;
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

    public String getOldmemberId() {
        return oldmemberId;
    }

    public void setOldmemberId(String oldmemberId) {
        this.oldmemberId = oldmemberId;
    }

    @Override
    public ResultSet findAll() throws SQLException {
        try {
            query = "select * from vwshowmember;";
            rs = con.createStatement().executeQuery(query);
            return rs;
        } catch (Exception e) {
            e.printStackTrace();
           return null;
       }
   }

    @Override
    public ResultSet findById(String id) throws SQLException {
        try {
            query = "select * from vwshowmember where member_id=?";
            ps = con.prepareStatement(query);
            ps.setString(1, id);
            rs = ps.executeQuery();
            return rs;
        } catch (Exception e) {
            return null;
        }
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

    public ResultSet findMemberEndOfDate(Date date) {
        try {
            query = "select * from vwshowmember where date_end<?";
            ps = con.prepareStatement(query);
            ps.setDate(1, date);
            rs = ps.executeQuery();
            return rs;
        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public int saveData() throws SQLException, ParseException {
        if (getByimg() != null) {
            query = "call  member_Insert_Img(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
            ps = con.prepareStatement(query);
            ps.setBytes(15, getByimg());
        } else {
            query = "call  member_Insert(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
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
        ps.setString(11, getStudy_year());
        ps.setDate(12, getDateRegister());
        ps.setDate(13, getDateRegisterEnd());
        ps.setDate(14, getDateExit());
        return ps.executeUpdate();
    }

    @Override
    public int updateData() throws SQLException, ParseException {
        if (getByimg() != null) {
            query = "call  member_Update_Img(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
            ps = con.prepareStatement(query);
            ps.setBytes(16, getByimg());
        } else {
            query = "call  member_Update(?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
            ps = con.prepareStatement(query);
        }
        ps.setString(1, getOldmemberId());
        ps.setString(2, getMemberId());
        ps.setString(3, getFirstName());
        ps.setString(4, getSureName());
        ps.setString(5, getGender());
        ps.setString(6, getTel());
        ps.setString(7, getVillage());
        ps.setString(8, getDistrict());
        ps.setString(9, getProvince());
        ps.setDate(11, getBirdate());
        ps.setString(10, getDetp());
        ps.setString(12, getStudy_year());
        ps.setDate(13, getDateRegister());
        ps.setDate(14, getDateRegisterEnd());
        ps.setDate(15, getDateExit());
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
