package com.mycompany.library_project.Model;

import java.sql.*;
import java.text.ParseException;

import com.mycompany.library_project.ControllerDAOModel.AlertMessage;
import com.mycompany.library_project.ControllerDAOModel.DataAccessObject;
import com.mycompany.library_project.config.CreateLogFile;

import javafx.geometry.Pos;

public class ShowRentSendModel implements DataAccessObject {

    private AlertMessage alertMessage = new AlertMessage();
    private CreateLogFile logfile = new CreateLogFile();
    private Connection con = null;
    private ResultSet rs = null;
    private PreparedStatement ps = null;
    private String sql = "";

    private String rentId;
    private String barcode;
    private String bookName;
    private String catg;
    private String type;
    private String tableLogId;
    private Date rentDate;
    private Date sendDate;
    private String status;
    private String memberId;
    private String memberName;
    private String cause;

    public ShowRentSendModel(Connection con) {
        this.con = con;
    }

    public ShowRentSendModel(String rentId, String barcode, String status) {
        this.rentId = rentId;
        this.barcode = barcode;
        this.status = status;
    }

    public ShowRentSendModel(String rentId, String barcode, String bookName, String catg, String type,
            String tableLogId, Date rentDate, Date sendDate, String status, String memberId, String memberName,
            String cause) {
        this.rentId = rentId;
        this.barcode = barcode;
        this.bookName = bookName;
        this.catg = catg;
        this.type = type;
        this.tableLogId = tableLogId;
        this.rentDate = rentDate;
        this.sendDate = sendDate;
        this.status = status;
        this.memberId = memberId;
        this.memberName = memberName;
        this.cause = cause;
    }

    public String getRentId() {
        return rentId;
    }

    public void setRentId(String rentId) {
        this.rentId = rentId;
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

    public String getTableLogId() {
        return tableLogId;
    }

    public void setTableLogId(String tableLogId) {
        this.tableLogId = tableLogId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    @Override
    public ResultSet findAll() throws SQLException {
        try {
            sql = "select * from showRent_SendBook order by date_rent desc;";
            rs = con.createStatement().executeQuery(sql);
            return rs;
        } catch (SQLException e) {
            alertMessage.showErrorMessage("Load Data Error", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
            logfile.createLogFile("Load Rent and Send Book Error", e);
            return null;
        } finally {
            //con.close();
        }
    }

    public ResultSet findByRent(String status) throws SQLException {
        try {
            sql = "select * from showRent_SendBook Where book_status=?;";
            ps = con.prepareStatement(sql);
            ps.setString(1, status);
            rs = ps.executeQuery();
            return rs;
        } catch (SQLException e) {
            alertMessage.showErrorMessage("Load Data Error", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
            logfile.createLogFile("Load Rent and Send Book By Status Error", e);
            return null;
        } finally {
            ps.close();
            //con.close();
        }
    }

    public ResultSet findByRentOutOfDate(Date date, String status) throws SQLException {
        try {
            sql = "select * from showRent_SendBook Where date_send<? and book_status=?;";
            ps = con.prepareStatement(sql);
            ps.setDate(1, date);
            ps.setString(2, status);
            rs = ps.executeQuery();
            return rs;
        } catch (SQLException e) {
            alertMessage.showErrorMessage("Load rent book out of error", "Error: " + e.getMessage(), 4,
                    Pos.BOTTOM_RIGHT);
            logfile.createLogFile("Load Renting Book Out Of Error", e);
            return null;
        } finally {
            ps.close();
            //con.close();
        }
    }

    public ResultSet findBySend(String status) throws SQLException {
        try {
            sql = "select * from showRent_SendBook Where book_status=? order by date_rent desc;";
            ps = con.prepareStatement(sql);
            ps.setString(1, status);
            rs = ps.executeQuery();
            return rs;
        } catch (SQLException e) {
            alertMessage.showErrorMessage("Load Data Error", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
            logfile.createLogFile("Load Sended Book Error", e);
            return null;
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
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int updateData() throws SQLException, ParseException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int deleteData(String id) throws SQLException {
        try {
            sql = "call rentbook_Delete(?, ?, ?);";
            ps = con.prepareStatement(sql);
            ps.setString(1, getRentId());
            ps.setString(2, getBarcode());
            ps.setString(3, getStatus());
            return ps.executeUpdate();
        } catch (SQLException e) {
            alertMessage.showErrorMessage("Delete Error", "Error: " + e.getMessage(), 4, Pos.BOTTOM_RIGHT);
            logfile.createLogFile("Delete Sended Book Error", e);
            return 0;
        } finally {
            ps.close();
            //con.close();
        }
    }

}
