package com.mycompany.library_project.Model;

import java.sql.*;
import java.text.ParseException;
import java.util.List;

import com.mycompany.library_project.Controller.HomeController;
import com.mycompany.library_project.ControllerDAOModel.*;

public class BookLostModel implements DataAccessObject {

    private DialogMessage dialog = new DialogMessage();
    private PreparedStatement ps = null;
    private ResultSet rs = null;
    // private Connection con = MyConnection.getConnect();
    private String sql = null;

    private int lost_id;
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

    public BookLostModel(int lost_id, double price) {
        this.lost_id = lost_id;
        this.price = price;
    }

    public BookLostModel(int lost_id, String barcode, double price) {
        this.lost_id = lost_id;
        this.barcode = barcode;
        this.price = price;
    }

    public BookLostModel(String memberId, Integer qty, double price, Date date) {
        this.memberId = memberId;
        this.qty = qty;
        this.date = date;
        this.price = price;
    }

    // Todo: Call by show book lost by member rentId
    public BookLostModel(String bookId, String barcode, String bookName, String page, String catg, String type,
            String table, String tableLog, String outDate, String pricePerBook) {

        this.bookId = bookId;
        this.barcode = barcode;
        this.bookName = bookName;
        this.page = page;
        this.catg = catg;
        this.type = type;
        this.table = table;
        this.tableLog = tableLog;
        this.outDate = outDate;
        this.pricePerBook = pricePerBook;
    }

    // Todo: Call by show book lost by member rentId of date
    public BookLostModel(String rentId, String bookId, String barcode, String bookName, String page, String catg,
            String type,
            String table, String tableLog, Date rentDate, Date sendDate, String outDate, String pricePerBook) {
        this.rentId = rentId;
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

    public int getLost_id() {
        return lost_id;
    }

    public void setLost_id(int lost_id) {
        this.lost_id = lost_id;
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
        try {
            sql = "select * from vwshowbooklost order by date_pay desc;";
            rs = HomeController.con.createStatement().executeQuery(sql);
            return rs;
        } catch (SQLException e) {
            dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການໂຫຼດຂໍ້ມູນປຶ້ມເສຍ", e);
            return null;
        }
    }

    @Override
    public ResultSet findById(String id) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    public ResultSet findByDate(Date date) throws SQLException {
        try {
            sql = "select * from vwshowbooklost where date_pay=?;";
            ps = HomeController.con.prepareStatement(sql);
            ps.setDate(1, date);
            rs = ps.executeQuery();
            return rs;
        } catch (SQLException e) {
            dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການໂຫຼດຂໍ້ມູນປຶ້ມເສຍ", e);
            return null;
        }
    }

    public ResultSet findRentBookByMemderID(String member_Id, String book_status) throws SQLException {
        try {
            sql = "call sendBook_ShowByMemberID(?, ?);";
            ps = HomeController.con.prepareStatement(sql);
            ps.setString(1, member_Id);
            ps.setString(2, book_status);
            rs = ps.executeQuery();
            return rs;
        } catch (SQLException e) {
            dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການໂຫຼດຂໍ້ມູນປຶ້ມເສຍ", e);
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
         * ; ps = HomeController.con.prepareStatement(sql,
         * Statement.RETURN_GENERATED_KEYS);
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
         * HomeController.con.prepareStatement(sql);
         * 
         * 
         * ps.executeUpdate(); rs = ps.executeQuery();)<==
         */

        try {
            sql = "INSERT INTO dblibrary.tbbooks_lost (member_id, total_qty, total_cost, date_pay) VALUES(?, ?, ?, ?);";
            ps = HomeController.con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, getMemberId());
            ps.setInt(2, getQty());
            ps.setDouble(3, getPrice());
            ps.setDate(4, getDate());
            ps.executeUpdate();
            rs = ps.getGeneratedKeys();
            if (rs.next())
                return rs.getInt(1);
            else {
                return 0;
            }
        } catch (SQLException e) {
            dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການບັນທືກຂໍ້ມູນປຶ້ມເສຍ", e);
            return 0;
        } finally {
            // ps.close();
            // HomeController.con.close();
        }

    }

    public int saveLostDetail(/* int lost_id, String barcode, double price */ List<BookLostModel> list)
            throws SQLException {
        // Todo: Don't use try...catch
        // sql = "call book_lostdetail_Insert(?, ?, ?)";
        // ps = HomeController.con.prepareStatement(sql);
        // ps.setInt(1, lost_id);
        // ps.setString(2, barcode);
        // ps.setDouble(3, price);
        // int result = ps.executeUpdate();
        // //ps.close();
        // // HomeController.con.close();
        // return result;

        try {
            int result = 0;
            sql = "INSERT INTO dblibrary.tbbooks_lost_detail (lost_id, barcode, book_price) VALUES(?, ?, ?)";
            HomeController.con.setAutoCommit(false);
            ps = HomeController.con.prepareStatement(sql);
            for (BookLostModel val : list) {
                ps.setInt(1, val.getLost_id());
                ps.setString(2, val.getBarcode());
                ps.setDouble(3, val.getPrice());
                ps.addBatch();
                result++;
                if (result % 100 == 0 || result == list.size()) {
                    ps.executeBatch();
                    HomeController.con.commit();
                    result = 1;
                }
            }
            return result;
        } catch (Exception e) {
            dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການບັນທືກຂໍ້ມູນລາຍລະອຽດປຶ້ມເສຍ", e);
            return 0;
        } finally {
            ps.close();
        }
    }

    @Override
    public int updateData() throws SQLException, ParseException {
        try {
            sql = "call book_lost_Update(?, ?, ?, ?, ?)";
            ps = HomeController.con.prepareStatement(sql);
            ps.setString(1, getRentId());
            ps.setString(2, getMemberId());
            ps.setInt(3, getQty());
            ps.setDouble(4, getFineCost());
            ps.setDate(5, getDate());
            return ps.executeUpdate();
        } catch (SQLException e) {
            dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການແກ້ໄຂ້ຂໍ້ມູນປຶ້ມເສຍ", e);
            return 0;
        } finally {
            // ps.close();
        }
    }

    @Override
    public int deleteData(String id) throws SQLException {
        try {
            sql = "call book_lost_Delete(?);";
            ps = HomeController.con.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(id));
            return ps.executeUpdate();
        } catch (SQLException e) {
            dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການລົບຂໍ້ມູນປຶ້ມເສຍ", e);
            return 0;
        } finally {
            // ps.close();
        }

    }

    public int deleeLostDetail(int lostid, String barcode, double price) throws SQLException {
        try {
            sql = "call book_lostdetail_Delete(?, ?, ?)";
            ps = HomeController.con.prepareStatement(sql);
            ps.setInt(1, lostid);
            ps.setString(2, barcode);
            ps.setDouble(3, price);
            return ps.executeUpdate();
        } catch (SQLException e) {
            dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການລົບຂໍ້ມູນປຶ້ມເສຍ" + lostid, e);
            return 0;
        } finally {
            // ps.close();
        }
    }

}