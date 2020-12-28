package com.mycompany.library_project.Controller.Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;

import com.mycompany.library_project.MyConnection;


public class BooksModel {

    private PreparedStatement ps = null;
    private ResultSet rs = null;
    private Connection con = MyConnection.getConnect();
    private String sql = null;

    private String bookId;
    private String barcode;
    private String status;

    public BooksModel(String bookId, String barcode, String status) {
        this.bookId = bookId;
        this.barcode = barcode;
        this.status = status;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

	public int saveData(String book_id, String book_barcode, String book_status) throws SQLException, ParseException {
        sql = "call book_Insert(?, ?, ?);";
        ps = con.prepareStatement(sql);
        ps.setString(1, book_id);
        ps.setString(2, book_barcode);
        ps.setString(3, book_status);
		return ps.executeUpdate();
	}

    public int updateData(String book_id, String book_barcode, String new_book_barcode, String book_status ) throws SQLException, ParseException {
        sql = "call book_Update(?, ?, ?, ?);";
        ps = con.prepareStatement(sql);
        ps.setString(1, book_barcode);
        ps.setString(2, new_book_barcode);
        ps.setString(3, book_id);
        ps.setString(4, book_status);
        return ps.executeUpdate();
	}

	public int deleteData(String barcode) throws SQLException {
        sql = "call book_Delete(?);";
        ps = con.prepareStatement(sql);
        ps.setString(1,barcode);
		return ps.executeUpdate();
	}
}
