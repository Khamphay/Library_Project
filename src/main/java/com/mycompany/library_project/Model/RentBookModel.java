package com.mycompany.library_project.Model;

import java.sql.*;
import java.text.ParseException;

import com.jfoenix.controls.JFXButton;
import com.mycompany.library_project.MyConnection;
import com.mycompany.library_project.ControllerDAOModel.DataAccessObject;

public class RentBookModel implements DataAccessObject {

    private Connection con = MyConnection.getConnect();
    private ResultSet rs = null;
    private PreparedStatement ps = null;
    private String sql = "";

    private String rentId;
    private String member;
    private String barcode;
    private String bookName;
    private Integer qty;
    private String status;
    private String catg;
    private String type;
    private Date rentDate;
    private Date sendDate;

    public RentBookModel() {
    }

    public RentBookModel(String rentId, String barcode, String status) {
        this.rentId = rentId;
        this.barcode = barcode;
        this.status = status;
    }

    public RentBookModel(String rentId, String member, String barcode, Integer qty, Date rentDate, Date sendDate) {
        this.rentId = rentId;
        this.member = member;
        this.barcode = barcode;
        this.qty = qty;
        this.rentDate = rentDate;
        this.sendDate = sendDate;
    }

    public RentBookModel(String barcode, String bookName, String catg, String type, Date rentDate, Date sendDate) {
        this.barcode = barcode;
        this.bookName = bookName;
        this.catg = catg;
        this.type = type;
        this.rentDate = rentDate;
        this.sendDate = sendDate;
    }

    public String getRentId() {
        return rentId;
    }

    public void setRentId(String rentId) {
        this.rentId = rentId;
    }

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCatg() {
        return catg;
    }

    public void setCatg(String catg) {
        this.catg = catg;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getRentDate() {
        return rentDate;
    }

    public void setRentDate(Date rentDate) {
        this.rentDate = rentDate;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    public ResultSet findAll() throws SQLException {
        sql = "";
        rs = con.createStatement().executeQuery(sql);
        return rs;
    }

    public ResultSet findByMemberId(String memver_id) throws SQLException {
        sql = "";
        rs = con.createStatement().executeQuery(sql);
        return rs;
    }

    public int saveRentBook() throws SQLException {
        sql = "";
        ps = con.prepareStatement(sql);

        return ps.executeUpdate();
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
        sql = "call rentbook_detail_Insert(?, ?, ?, ?, ?);";
        ps = con.prepareStatement(sql);
        ps.setString(1, getRentId());
        ps.setString(2, getMember());
        ps.setInt(3, getQty());
        ps.setDate(4, getRentDate());
        ps.setDate(5, getSendDate());
        return ps.executeUpdate();
    }

    public int saveRentBook(String id, String barcode, String status) throws SQLException {
        sql = "call rentbook_Insert(?, ?, ?)";
        ps = con.prepareStatement(sql);
        ps.setString(1, id);
        ps.setString(2, barcode);
        ps.setString(3, status);
        return ps.executeUpdate();
    }
    
    @Override
    public int updateData() throws SQLException, ParseException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int deleteData(String id) throws SQLException {
        sql = "";
        return 0;
    }

}
