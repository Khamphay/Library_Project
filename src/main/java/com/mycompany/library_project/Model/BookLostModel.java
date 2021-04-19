package com.mycompany.library_project.Model;

import java.sql.*;
import java.text.ParseException;

import com.mycompany.library_project.MyConnection;
import com.mycompany.library_project.ControllerDAOModel.DataAccessObject;

public class BookLostModel {
    protected String lostId;
    protected String barcode;
    protected String memberId;
    protected Integer qty;
    protected Double fineCost;
    protected Date date;

    public BookLostModel(String lostId, String barcode, String memberId, Integer qty, Double fineCost, Date date) {
        this.lostId = lostId;
        this.barcode = barcode;
        this.memberId = memberId;
        this.qty = qty;
        this.fineCost = fineCost;
        this.date = date;
    }

    public String getLostId() {
        return lostId;
    }

    public void setLostId(String lostId) {
        this.lostId = lostId;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public Double getFineCost() {
        return fineCost;
    }

    public void setFineCost(Double fineCost) {
        this.fineCost = fineCost;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    class lostDetail extends BookLostModel implements DataAccessObject {

        String sql = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection conn = MyConnection.getConnect();

        private Double price;

        public lostDetail(String lostId, String barcode, String memberId, Integer qty, Double fineCost, Date date,
                double price) {
            super(lostId, barcode, memberId, qty, fineCost, date);
            this.price = price;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
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

            sql = "call book_lost_Insert(?, ?, ?, ?)";
            ps = conn.prepareStatement(sql);
            ps.setString(1, getMemberId());
            ps.setInt(2, getQty());
            ps.setDouble(3, getFineCost());
            ps.setDate(4, getDate());
            return ps.executeUpdate();
        }

        public int saveLostDetail() throws SQLException {
            sql = "call book_lostdetail_Insert(?, ?, ?)";
            ps = conn.prepareStatement(sql);
            ps.setString(1, getLostId());
            ps.setString(2, getBarcode());
            ps.setDouble(3, getPrice());
            return ps.executeUpdate();
        }

        @Override
        public int updateData() throws SQLException, ParseException {
            sql = "call book_lost_Update(?, ?, ?, ?, ?)";
            ps = conn.prepareStatement(sql);
            ps.setString(1, getLostId());
            ps.setString(2, getMemberId());
            ps.setInt(3, getQty());
            ps.setDouble(4, getFineCost());
            ps.setDate(5, getDate());
            return ps.executeUpdate();
        }

        @Override
        public int deleteData(String id) throws SQLException {
            sql = "call book_lost_Delete(?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, getLostId());
            return ps.executeUpdate();
        }

        public int deleeLostDetail() throws SQLException {
            sql = "call book_lostdetail_Delete(?)";
            ps = conn.prepareStatement(sql);
            ps.setString(1, getLostId());
            return ps.executeUpdate();
        }
    }
}