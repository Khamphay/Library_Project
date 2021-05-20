package com.mycompany.library_project.Model;

import java.sql.*;
import java.text.ParseException;

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
    private String rent_status;

    public RentBookModel() {
    }

    public RentBookModel(String rentId, String barcode, String status) {
        this.rentId = rentId;
        this.barcode = barcode;
        this.status = status;
    }

    public RentBookModel(String rentId, String member, String barcode, Integer qty, Date rentDate, Date sendDate,
            String rent_status) {
        this.rentId = rentId;
        this.member = member;
        this.barcode = barcode;
        this.qty = qty;
        this.rentDate = rentDate;
        this.sendDate = sendDate;
        this.rent_status = rent_status;
    }

    public RentBookModel(String barcode, String bookName, String catg, String type, Date rentDate, Date sendDate) {
        this.barcode = barcode;
        this.bookName = bookName;
        this.catg = catg;
        this.type = type;
        this.rentDate = rentDate;
        this.sendDate = sendDate;
    }

    public RentBookModel(String barcode, String bookName, String catg, String type, String member, Date rentDate,
            Date sendDate) {
        this.barcode = barcode;
        this.bookName = bookName;
        this.catg = catg;
        this.type = type;
        this.member = member;
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
    
    public String getRent_status() {
        return rent_status;
    }

    public void setRent_status(String rent_status) {
        this.rent_status = rent_status;
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
        sql = "call rentbook_detail_Insert(?, ?, ?, ?, ?, ?);";
        ps = con.prepareStatement(sql);
        ps.setString(1, getRentId());
        ps.setString(2, getMember());
        ps.setInt(3, getQty());
        ps.setDate(4, getRentDate());
        ps.setDate(5, getSendDate());
        ps.setString(6, getRent_status());
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
        sql = "call rentbook_detail_Delete(?);";
        ps = con.prepareStatement(sql);
        ps.setString(1, id);
        return ps.executeUpdate();
    }

    public String getMaxID() throws SQLException {
        sql = "select max(rent_id) as max_id From tbrent_detail;";
        rs = con.createStatement().executeQuery(sql);
        if (rs.next()) {
            return rs.getString("max_id");
        } else {
            return null;
        }

    }

    public ResultSet chackMemberRentBook(String memberid, String rent_status, String book_status) throws SQLException {
        sql = "call rentBook_Check(?, ?, ?);";
        ps = con.prepareStatement(sql);
        ps.setString(1, memberid);
        ps.setString(2, rent_status);
        ps.setString(3, book_status);
        rs = ps.executeQuery();
        return rs;
    }

    public ResultSet getSendBook(String book_barcode, String book_status) throws SQLException {
        sql = "call sendBook_Show(?,?)";
        ps = con.prepareStatement(sql);
        ps.setString(1, book_barcode);
        ps.setString(2, book_status);
        rs = ps.executeQuery();
        return rs;
    }

    public int sendBook(String rent_id, String book_barcode, String book_status) throws SQLException {
        sql = "	call sendBook(?, ?, ?);";
        ps = con.prepareStatement(sql);
        ps.setString(1, rent_id);
        ps.setString(2, book_barcode);
        ps.setString(3, book_status);
        return ps.executeUpdate();
    }
}
