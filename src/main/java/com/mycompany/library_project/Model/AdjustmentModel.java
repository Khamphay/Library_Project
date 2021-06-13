package com.mycompany.library_project.Model;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;

import com.mycompany.library_project.MyConnection;
import com.mycompany.library_project.ControllerDAOModel.DataAccessObject;

public class AdjustmentModel implements DataAccessObject {

    private Connection con = MyConnection.getConnect();
    private ResultSet rs = null;
    private PreparedStatement ps = null;
    private String sql = null;

    private String rent_id;
    private int qty;
    private double adj_fee;
    private Date datePay;
    private String detail;

    public AdjustmentModel() {
    }

    public AdjustmentModel(String rent_id, int qty, double adj_fee, Date datePay, String detail) {
        this.rent_id = rent_id;
        this.qty = qty;
        this.adj_fee = adj_fee;
        this.datePay = datePay;
        this.detail = detail;
    }

    public String getRent_id() {
        return rent_id;
    }

    public void setRent_id(String rent_id) {
        this.rent_id = rent_id;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getAdj_fee() {
        return adj_fee;
    }

    public void setAdj_fee(double adj_fee) {
        this.adj_fee = adj_fee;
    }

    public Date getDatePay() {
        return datePay;
    }

    public void setDatePay(Date datePay) {
        this.datePay = datePay;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
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
        sql = "call adjustment_Insert(?, ?, ?, ?, ?);";
        ps = con.prepareStatement(sql);
        ps.setString(1, getRent_id());
        ps.setInt(2, getQty());
        ps.setDouble(3, getAdj_fee());
        ps.setDate(4, getDatePay());
        ps.setString(5, getDetail());
        return ps.executeUpdate();
    }

    @Override
    public int updateData() throws SQLException, ParseException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int deleteData(String id) throws SQLException {
        // TODO Auto-generated method stub
        return 0;
    }

}
