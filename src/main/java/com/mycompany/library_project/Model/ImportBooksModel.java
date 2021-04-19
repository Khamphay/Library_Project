package com.mycompany.library_project.Model;

import java.sql.*;
import java.text.ParseException;

import com.mycompany.library_project.MyConnection;
import com.mycompany.library_project.ControllerDAOModel.DataAccessObject;

class ImportBook implements DataAccessObject {

    protected Connection con = MyConnection.getConnect();
    protected PreparedStatement ps = null;
    protected String sql = null;

    protected String import_id, book_id;
    private int qty_total;
    private double price_total;
    private Date date;

    public ImportBook(String import_id, int qty_total, double price_total, Date date) {
        this.import_id = import_id;
        this.qty_total = qty_total;
        this.price_total = price_total;
        this.date = date;
    }

    public String getImport_id() {
        return import_id;
    }

    public void setImport_id(String import_id) {
        this.import_id = import_id;
    }

    public String getBook_id() {
        return book_id;
    }

    public void setBook_id(String book_id) {
        this.book_id = book_id;
    }

    public int getQty_total() {
        return qty_total;
    }

    public void setQty_total(int qty_total) {
        this.qty_total = qty_total;
    }

    public double getPrice_total() {
        return price_total;
    }

    public void setPrice_total(double price_total) {
        this.price_total = price_total;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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
        int result = 0;
        try {
            sql = "call inertImport(:importid, :qty_total, :price_total, :import_in)";
            ps = con.prepareStatement(sql);
            ps.setString(1, getImport_id());
            ps.setInt(2, getQty_total());
            ps.setDouble(3, getPrice_total());
            ps.setDate(4, getDate());
            if (ps.executeUpdate() > 0) {
                result = 1;
            }
            return result;
        } catch (Exception e) {
            return -1;
        }
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

public class ImportBooksModel extends ImportBook {

    private int qty;
    private double price;

    public ImportBooksModel(String import_id, int qty_total, double price_total, Date date, int qty, double price) {
        super(import_id, qty_total, price_total, date);
        this.qty = qty;
        this.price = price;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int saveImportDetail() throws SQLException {
        int result = 0;
        sql = "call inertImport_Detail(:importid, :bookid, :book_qty, :book_price);";
        ps = con.prepareStatement(sql);
        ps.setString(1, getImport_id());
        ps.setString(2, getBook_id());
        ps.setInt(3, getQty());
        ps.setDouble(4, getPrice());
        if (ps.executeUpdate() > 0) {
            result = 1;
        }
        return result;
    }

}