package com.mycompany.library_project.Model;

import java.sql.*;
import java.text.ParseException;

import com.mycompany.library_project.MyConnection;
import com.mycompany.library_project.ControllerDAOModel.DataAccessObject;

public class BookLostModel implements DataAccessObject {

    private PreparedStatement ps = null;
    private ResultSet rs = null;
    private Connection conn = MyConnection.getConnect();
    private String sql = null;

    private String rentId;
    private String memberId;
    private String bookId;
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
    private Date date;
    private String rent_status;
    private String outDate;
    private String pricePerBook;
    private double price;
    private double fineCost;

    public BookLostModel() {

    }

    public BookLostModel(String memberId, Integer qty, double price, Date date) {
        this.memberId = memberId;
        this.qty = qty;
        this.date = date;
        this.price = price;
    }

    // Todo: Call by show book lost by member rentId
    public BookLostModel(String bookId, String barcode, String bookName, String page, String catg, String type,
            String table, String tableLog, Date rentDate, Date sendDate, String outDate, String pricePerBook) {
        this.bookId = bookId;
        this.barcode = barcode;
        this.bookName = bookName;
        this.page = page;
        this.catg = catg;
        this.type = type;
        this.table = table;
        this.tableLog = tableLog;
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

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getRent_status() {
        return rent_status;
    }

    public void setRent_status(String rent_status) {
        this.rent_status = rent_status;
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

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public double getFineCost() {
            return fineCost;
        }

        public void setFineCost(double fineCost) {
            this.fineCost = fineCost;
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

        public ResultSet findRentBookByMemderID(String member_Id, String book_status) throws SQLException {
            sql = "call sendBook_ShowByMemberID(?, ?);";
            ps = conn.prepareStatement(sql);
            ps.setString(1, member_Id);
            ps.setString(2, book_status);
            rs = ps.executeQuery();
            return rs;
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
        /*
         * //Todo:Method: #1 If you use the 'Statement.RETURN_GENERATED_KEYS' for return
         * the last Auto_id //Todo: Use for get the auto id of you insert use for return
         * auto id key value of database to the program; (But cannot use with 'stored
         * procedures') //Todo: 'Statement.RETURN_GENERATED_KEYS'
         * 
         * Java Code: ==>(sql =
         * "INSERT INTO dblibrary.tbbooks_lost (member_id, total_qty, total_cost, date_pay) VALUES(?, ?, ?, ?);"
         * ; ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
         * 
         * 
         * ps.executeUpdate(); rs = ps.getGeneratedKeys();)<==
         */

        /*
         * //Todo:Method: #2 If you use the custom stement of Stored Procedure to return
         * last Auto_id; // Todo: In the stored procedure
         * "call book_lost_Insert(?, ?, ?, ?)" I have stement select max(id_name) for
         * return last auto max id;
         * 
         * Java Code: ==>(sql = "call book_lost_Insert(?, ?, ?, ?)"; ps =
         * conn.prepareStatement(sql);
         * 
         * 
         * ps.executeUpdate(); rs = ps.executeQuery();)<==
         */

        sql = "INSERT INTO dblibrary.tbbooks_lost (member_id, total_qty, total_cost, date_pay) VALUES(?, ?, ?, ?);";
        ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, getMemberId());
        ps.setInt(2, getQty());
        ps.setDouble(3, getFineCost());
        ps.setDate(4, getDate());
        ps.executeUpdate();
        rs = ps.getGeneratedKeys();

        if (rs.next())
            return rs.getInt(1);
        else {
            return 0;
        }

    }

    public int saveLostDetail(int lost_id, String barcode, double price) throws SQLException {
        sql = "call book_lostdetail_Insert(?, ?, ?)";
        ps = conn.prepareStatement(sql);
        ps.setInt(1, lost_id);
        ps.setString(2, barcode);
        ps.setDouble(3, price);
        return ps.executeUpdate();
    }

    @Override
    public int updateData() throws SQLException, ParseException {
        sql = "call book_lost_Update(?, ?, ?, ?, ?)";
        ps = conn.prepareStatement(sql);
        ps.setString(1, getRentId());
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
        ps.setString(1, getRentId());
        return ps.executeUpdate();
    }

    public int deleeLostDetail() throws SQLException {
        sql = "call book_lostdetail_Delete(?)";
        ps = conn.prepareStatement(sql);
        ps.setString(1, getRentId());
        return ps.executeUpdate();
    }

}