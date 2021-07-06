package com.mycompany.library_project.Model;

import java.sql.*;
import java.text.ParseException;

import com.mycompany.library_project.ControllerDAOModel.AlertMessage;
import com.mycompany.library_project.ControllerDAOModel.DataAccessObject;
import com.mycompany.library_project.config.CreateLogFile;

import javafx.geometry.Pos;

public class RentBookModel implements DataAccessObject {

    private AlertMessage alertMessage = new AlertMessage();
    private CreateLogFile logfile = new CreateLogFile();
    private Connection con = null;
    private ResultSet rs = null;
    private PreparedStatement ps = null;
    private String sql = "";

    private String rentId;
    private String member;
    private String barcode;
    private String bookName;
    private String page;
    private Integer qty;
    private String status;
    private String catg;
    private String type;
    private String table;
    private String tableLog;
    private Date rentDate;
    private Date sendDate;
    private String outDate;
    private String pricePerBook;

    public RentBookModel(Connection con) {
        this.con = con;
    }

    public RentBookModel(String rentId, String barcode, String status) {
        this.rentId = rentId;
        this.barcode = barcode;
        this.status = status;
    }

    // Todo: Call by save rent books
    public RentBookModel(String rentId, String member, String barcode, Integer qty, Date rentDate, Date sendDate) {
        this.rentId = rentId;
        this.member = member;
        this.barcode = barcode;
        this.qty = qty;
        this.rentDate = rentDate;
        this.sendDate = sendDate;
    }

    // Todo: Call by rent books
    public RentBookModel(String barcode, String bookName, String page, String catg, String type, String table,
            String tableLog, Date rentDate, Date sendDate) {
        this.barcode = barcode;
        this.bookName = bookName;
        this.page = page;
        this.catg = catg;
        this.type = type;
        this.table = table;
        this.tableLog = tableLog;
        this.rentDate = rentDate;
        this.sendDate = sendDate;

    }

    // Todo: Call by send books
    public RentBookModel(String rentId, String barcode, String bookName, String page, String catg, String type,
            String table, String tableLog, String member, Date rentDate, Date sendDate, String outDate,
            String pricePerBook) {
        this.rentId = rentId;
        this.barcode = barcode;
        this.bookName = bookName;
        this.page = page;
        this.catg = catg;
        this.type = type;
        this.table = table;
        this.tableLog = tableLog;
        this.member = member;
        this.rentDate = rentDate;
        this.sendDate = sendDate;
        this.outDate = outDate;
        this.pricePerBook = pricePerBook;
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

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
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

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getTableLog() {
        return tableLog;
    }

    public void setTableLog(String tableLog) {
        this.tableLog = tableLog;
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

    public String getOutDate() {
        return outDate;
    }

    public void setOutDate(String outDate) {
        this.outDate = outDate;
    }

    public String getPricePerBook() {
        return pricePerBook;
    }

    public void setPricePerBook(String pricePerBook) {
        this.pricePerBook = pricePerBook;
    }

    public ResultSet findAll() throws SQLException {

        try {
            sql = "";
            rs = con.createStatement().executeQuery(sql);
            return rs;
        } catch (SQLException e) {
            alertMessage.showErrorMessage("Load Data Error", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
            // logfile.createLogFile("Load Error", e);
            return null;
        } finally {
            //con.close();
        }

    }

    public ResultSet findByMemberId(String memver_id) throws SQLException {
        try {
            sql = "";
            rs = con.createStatement().executeQuery(sql);
            return rs;
        } catch (SQLException e) {
            alertMessage.showErrorMessage("Load Data Error", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
            // logfile.createLogFile("Load Error", e);
            return null;
        } finally {
            //con.close();
        }
    }

    public int saveRentBook() throws SQLException {

        try {
            sql = "";
            ps = con.prepareStatement(sql);

            return ps.executeUpdate();
        } catch (SQLException e) {
            alertMessage.showErrorMessage("Save Error", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
            // logfile.createLogFile("Save Error", e);
            return 0;
        } finally {
            ps.close();
            //con.close();
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
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int saveData() throws SQLException, ParseException {
        try {
            sql = "call rentbook_detail_Insert(?, ?, ?, ?, ?);";
            ps = con.prepareStatement(sql);
            ps.setString(1, getRentId());
            ps.setString(2, getMember());
            ps.setInt(3, getQty());
            ps.setDate(4, getRentDate());
            ps.setDate(5, getSendDate());
            return ps.executeUpdate();
        } catch (SQLException e) {
            alertMessage.showErrorMessage("Rent Book Error", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
            logfile.createLogFile("Save Rent Book Error", e);
            return 0;
        } finally {
            ps.close();
            //con.close();
        }

    }

    public int saveRentBook(String id, String barcode, String status) throws SQLException {
        // TODO: Don't use try...catch'
        sql = "call rentbook_Insert(?, ?, ?)";
        ps = con.prepareStatement(sql);
        ps.setString(1, id);
        ps.setString(2, barcode);
        ps.setString(3, status);
        int result = ps.executeUpdate();
        ps.close();
        //con.close();
        return result;

    }

    @Override
    public int updateData() throws SQLException, ParseException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int deleteData(String id) throws SQLException {
        try {
            sql = "call rentbook_detail_Delete(?);";
            ps = con.prepareStatement(sql);
            ps.setString(1, id);
            return ps.executeUpdate();
        } catch (SQLException e) {
            alertMessage.showErrorMessage("Delete Error", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
            logfile.createLogFile("Delete Rent Book Error", e);
            return 0;
        } finally {
            ps.close();
            //con.close();
        }
    }

    public String getMaxID() throws SQLException {
        try {
            sql = "select max(rent_id) as max_id From tbrent_detail;";
            rs = con.createStatement().executeQuery(sql);
            if (rs.next()) {
                return rs.getString("max_id");
            } else {
                return null;
            }
        } catch (SQLException e) {
            alertMessage.showErrorMessage("Load Max Id Error", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
            logfile.createLogFile("Load Max Rent ID Error", e);
            return null;
        } finally {
            //con.close();
        }

    }

    public ResultSet chackMemberRentBook(String memberid, String book_status) throws SQLException {
        try {
            sql = "call rentBook_Check(?, ?);";
            ps = con.prepareStatement(sql);
            ps.setString(1, memberid);
            ps.setString(2, book_status);
            rs = ps.executeQuery();
            return rs;
        } catch (SQLException e) {
            alertMessage.showErrorMessage("Check Rent Book Error", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
            logfile.createLogFile("Check Rent Book Error", e);
            return null;
        } finally {
            //con.close();
        }

    }

    public ResultSet getSendBook(String book_barcode, String book_status) throws SQLException {
        try {
            sql = "call sendBook_ShowByBarcode(?,?)";
            ps = con.prepareStatement(sql);
            ps.setString(1, book_barcode);
            ps.setString(2, book_status);
            rs = ps.executeQuery();
            return rs;
        } catch (SQLException e) {
            alertMessage.showErrorMessage("Load Data Error", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
            logfile.createLogFile("Load Book Rent For Send Error", e);
            return null;
        } finally {
            //con.close();
        }
    }

    public int sendBook(String rent_id, String book_barcode, String book_status) throws SQLException {
        try {
            sql = "	call sendBook(?, ?, ?);";
            ps = con.prepareStatement(sql);
            ps.setString(1, rent_id);
            ps.setString(2, book_barcode);
            ps.setString(3, book_status);
            return ps.executeUpdate();
        } catch (SQLException e) {
            alertMessage.showErrorMessage("Save Error", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
            logfile.createLogFile("Save Send Books Error", e);
            return 0;
        } finally {
            ps.close();
            //con.close();
        }
    }
}
