package com.mycompany.library_project.Model;

import java.sql.*;
import java.text.ParseException;
import java.util.List;

import com.mycompany.library_project.Controller.HomeController;
import com.mycompany.library_project.ControllerDAOModel.*;

public class BookDetailModel implements DataAccessObject {

    private DialogMessage dialog = new DialogMessage();
    // public Connection con = null;
    private ResultSet rs = null;
    private PreparedStatement ps = null;

    private String sql = null;
    private String bookId;
    private String bookName;
    private Integer page;
    private String ISBN;
    private Integer qty;
    private String barcode;
    private String catgId;
    private String typeId;
    private String tableLogId;
    private String tableId;
    private String detail;
    private String status;
    private String write_year;

    public BookDetailModel() {
    }

    public BookDetailModel(String barcode, String status) {
        this.barcode = barcode;
        this.status = status;
    }

    public BookDetailModel(String barcode, String tableLogId, String status) {
        this.barcode = barcode;
        this.tableLogId = tableLogId;
        this.status = status;
    }

    public BookDetailModel(String barcode, String bookId, String tableLogId, String status) {
        this.barcode = barcode;
        this.bookId = bookId;
        this.tableLogId = tableLogId;
        this.status = status;
    }

    public BookDetailModel(String bookId, String bookName, String ISBN, Integer page, Integer qty, String catgId,
            String typeId, String tableId, String write_year, String detail) {
        this.bookId = bookId;
        this.bookName = bookName;
        this.ISBN = ISBN;
        this.page = page;
        this.qty = qty;
        this.catgId = catgId;
        this.typeId = typeId;
        this.tableId = tableId;
        this.write_year = write_year;
        this.detail = detail;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String iSBN) {
        ISBN = iSBN;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getCatgId() {
        return catgId;
    }

    public void setCatgId(String catgId) {
        this.catgId = catgId;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getTableLogId() {
        return tableLogId;
    }

    public void setTableLogId(String tableLogId) {
        this.tableLogId = tableLogId;
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public String getWrite_year() {
        return write_year;
    }

    public void setWrite_year(String write_year) {
        this.write_year = write_year;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Todo: Book Detail
    @Override
    public ResultSet findAll() throws SQLException {
        try {
            sql = "select * from showbooks;";
            rs = HomeController.con.createStatement().executeQuery(sql);
            return rs;
        } catch (SQLException e) {
            dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການໂຫຼດຂໍ້ມູນປຶ້ມ", e);
            return null;
        }
    }

    @Override
    public ResultSet findById(String id) throws SQLException {
        try {
            sql = "select * from showbooks where book_id = ?;";
            ps = HomeController.con.prepareStatement(sql);
            ps.setString(1, id);
            rs = ps.executeQuery();
            return rs;
        } catch (SQLException e) {
            dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການກວດສອບຂໍ້ມູນປຶ້ມ", e);
            return null;
        } finally {
            // ps.close();
        }
    }

    public ResultSet getMaxBarcodeID(String id) throws SQLException {
        try {
            sql = "call showMaxBarcodeId(?);";
            ps = HomeController.con.prepareStatement(sql);
            ps.setString(1, id);
            rs = ps.executeQuery();
            return rs;
        } catch (SQLException e) {
            dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການກວດສອບຂໍ້ມູນປຶ້ມ", e);
            return null;
        } finally {
            // ps.close();
        }
    }

    @Override
    public ResultSet findByName(String name) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ResultSet searchData(String values) throws SQLException {
        try {
            sql = "select * from showbooks Where book_id like concat('" + values + "','%') or book_name like concat('"
                    + values + "','%') or catg_name like concat('" + values + "','%') or type_name like concat('"
                    + values + "','%');";
            rs = HomeController.con.createStatement().executeQuery(sql);
            return rs;
        } catch (SQLException e) {
            dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການຄົ້ນຫາຂໍ້ມູນປຶ້ມ", e);
            return null;
        } finally {
            // HomeController.con.close();
        }
    }

    @Override
    public int saveData() throws SQLException, ParseException {
        try {
            sql = "call book_detail_Insert(?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
            ps = HomeController.con.prepareStatement(sql);
            ps.setString(1, getBookId());
            ps.setString(2, getBookName());
            ps.setString(3, getISBN());
            ps.setInt(4, getPage());
            ps.setInt(5, getQty());
            ps.setString(6, getWrite_year());
            ps.setString(7, getDetail());
            ps.setString(8, getCatgId());
            ps.setString(9, getTypeId());
            ps.setString(10, getTableId());
            return ps.executeUpdate();
        } catch (SQLException e) {
            dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການບັນທືກຂໍ້ມູນປຶ້ມ", e);
            return 0;
        } finally {
            // ps.close();
            // HomeController.con.close();
        }

    }

    @Override
    public int updateData() throws SQLException, ParseException {
        try {
            sql = "call book_detail_Update(?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
            ps = HomeController.con.prepareStatement(sql);
            ps.setString(1, getBookId());
            ps.setString(2, getBookName());
            ps.setString(3, getISBN());
            ps.setInt(4, getPage());
            ps.setInt(5, getQty());
            ps.setString(6, getWrite_year());
            ps.setString(7, getDetail());
            ps.setString(8, getCatgId());
            ps.setString(9, getTypeId());
            ps.setString(10, getTableId());
            return ps.executeUpdate();
        } catch (SQLException e) {
            dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການແກ້ໄຂ້ຂໍ້ມູນປຶ້ມ", e);
            return 0;
        } finally {
            // ps.close();
        }
    }

    public int updateBookQty(String book_id, int qty) throws SQLException {
        try {
            sql = "Update tbbooks_detail set qty=qty+" + qty + " Where book_id=?";
            ps = HomeController.con.prepareStatement(sql);
            ps.setString(1, book_id);
            return ps.executeUpdate();
        } catch (SQLException e) {
            dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການແກ້ໄຂ້ຈຳນວນປຶ້ມ", e);
            return 0;
        } finally {
            // ps.close();
        }
    }

    public int updateBookQty(String book_id) throws SQLException {
        try {
            sql = "Update tbbooks_detail set qty=qty-1 Where book_id=?";
            ps = HomeController.con.prepareStatement(sql);
            ps.setString(1, book_id);
            return ps.executeUpdate();
        } catch (SQLException e) {
            dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການແກ້ໄຂ້ຈຳນວນປຶ້ມ", e);
            return 0;
        } finally {
            // ps.close();
        }
    }

    @Override
    public int deleteData(String id) throws SQLException {
        try {
            sql = "call book_detail_Delete(?);";
            ps = HomeController.con.prepareStatement(sql);
            ps.setString(1, id);
            return ps.executeUpdate();
        } catch (SQLException e) {
            dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການລົບຂໍ້ມູນປຶ້ມ", e);
            return 0;
        } finally {
            // ps.close();
            // HomeController.con.close();
        }
    }

    // Todo: Book (Barcode)
    public ResultSet findBookByBarcode(String barcode) throws SQLException {
        try {
            sql = "call book_detail_ShowByBarcode('" + barcode + "');";
            rs = HomeController.con.createStatement().executeQuery(sql);
            // HomeController.con.close();
            return rs;
        } catch (SQLException e) {
            dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການໂຫຼດຂໍ້ມູນປຶ້ມ", e);
            return null;
        } finally {
            // HomeController.con.close();
        }
    }

    public ResultSet showBarcode(String book_id) throws SQLException {
        // Todo: Don't use try...catch'
        sql = "call book_ShowByBookId(?);";
        ps = HomeController.con.prepareStatement(sql);
        ps.setString(1, book_id);
        rs = ps.executeQuery();
        // ps.close();
        // HomeController.con.close();
        return rs;
    }

    public int saveBookBarCode(List<BookDetailModel> list, String id) throws SQLException {
        try {
            // sql = "call book_Insert(?, ?, ?, ?);";
            // ps = HomeController.con.prepareStatement(sql);
            // ps.setString(1, barcode);
            // ps.setString(2, bookid);
            // ps.setString(3, table_log_id);
            // ps.setString(4, status);
            // return ps.executeUpdate();

            sql = "INSERT INTO tbbook (barcode, book_id, table_log_id, status) VALUES(?, ?, ?, ?)";
            ps = HomeController.con.prepareStatement(sql);
            HomeController.con.setAutoCommit(false);
            int result = 0, count = 0;
            for (BookDetailModel item : list) {
                ps.setString(1, item.getBarcode());
                ps.setString(2, item.getBookId());
                ps.setString(3, item.getTableLogId());
                ps.setString(4, item.getStatus());
                ps.addBatch();
                count++;
                if (count % 100 == 0 || count == list.size()) {
                    ps.executeBatch();
                    HomeController.con.commit();
                    result = 1;
                }

            }
            return result;

        } catch (SQLException e) {
            dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການບັນທືກຂໍ້ມູນປຶ້ມ", e);
            if (id != null) {
                this.deleteData(id);
            }

            return 0;
        } finally {
            // ps.close();
            // HomeController.con.close();
        }

    }

    public int updateData(String book_id, String old_barcode, String new_book_barcode, String table_log_id,
            String book_status) throws SQLException, ParseException {

        try {
            sql = "call book_Update(?, ?, ?, ?, ?);";
            ps = HomeController.con.prepareStatement(sql);
            ps.setString(1, old_barcode);
            ps.setString(2, new_book_barcode);
            ps.setString(3, book_id);
            ps.setString(4, table_log_id);
            ps.setString(5, book_status);
            return ps.executeUpdate();
        } catch (SQLException e) {
            dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການແກ້ໄຂ້ຂໍ້ມູນປຶ້ມ", e);
            return 0;
        } finally {
            // ps.close();
            // HomeController.con.close();
        }

    }

    public int deleteBarcode(String book_id, String barcode) throws SQLException {

        try {
            sql = "call book_Delete(?,?);";
            ps = HomeController.con.prepareStatement(sql);
            ps.setString(1, book_id);
            ps.setString(2, barcode);
            return ps.executeUpdate();
        } catch (SQLException e) {
            dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການໂຫຼດຂໍ້ມູນບາໂຄດປຶ້ມ", e);
            return 0;
        } finally {
            // ps.close();
            // HomeController.con.close();
        }
    }

    // Todo: Write Book
    public ResultSet showWrite(String book_id) throws SQLException {
        try {
            sql = "call  write_Show('" + book_id + "');";
            rs = HomeController.con.createStatement().executeQuery(sql);
            return rs;
        } catch (SQLException e) {
            dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການໂຫຼດຂໍ້ມູນແຕ່ງປຶ້ມ", e);
            return null;
        } finally {
            // HomeController.con.close();
        }
    }

    public int saveWrite(/* String book_id, String author_id */ List<BookDetailModel> list) throws SQLException {
        try {
            int result = 0, count = 0;
            sql = "INSERT INTO dblibrary.tbwrite (book_id, author_id) VALUES(?, ?)";
            ps = HomeController.con.prepareStatement(sql);
            HomeController.con.setAutoCommit(false);
            for (BookDetailModel val : list) {
                ps.setString(1, val.getBookId());
                ps.setString(2, val.getStatus()); // Todo: use variable 'status' for get the 'author id' (ໃຊ້ຕົວປ່ຽນ
                                                  // status ເກັບຂໍ້ມູນລະຫັດນັກແຕ່ງ )
                ps.addBatch();
                count++;
                if (count % 100 == 0 || count == list.size()) {
                    ps.executeBatch();
                    HomeController.con.commit();
                    result = 1;
                }
            }
            return result;
        } catch (SQLException e) {
            dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການບັນທືກຂໍ້ມູນແຕ່ງປຶ້ມ", e);
            return 0;
        } finally {
            // ps.close();
        }
    }

    public int updateWrite(String book_id, String newauthor_id, String oldauthor_id) throws SQLException {
        // TODO: Don't use try...catch
        sql = "call write_Update(?, ?, ?);";
        ps = HomeController.con.prepareStatement(sql);
        ps.setString(1, book_id);
        ps.setString(2, newauthor_id);
        ps.setString(3, oldauthor_id);
        int result = ps.executeUpdate();
        // ps.close();
        return result;

    }

    public int updateStatus(List<BookDetailModel> list, String rentId) throws SQLException {
        try {
            int result = 0;
            // sql = " call book_UpdateStatus(?, ?);";
            // sql = "update tbbook set status=? where barcode=?";
            // ps = HomeController.con.prepareStatement(sql);
            for (BookDetailModel val : list) {
                sql = " call book_UpdateStatus(?, ?);";
                ps = HomeController.con.prepareStatement(sql);
                ps.setString(1, val.getBarcode());
                ps.setString(2, val.getStatus());
                result = ps.executeUpdate();
                if (result <= 0) {
                    return result;
                }
            }
            return result;
        } catch (BatchUpdateException e) {
            dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການປ່ຽນແປ່ງສະຖານະປຶ້ມ", e);
            if (rentId != null) {
                RentBookModel rent = new RentBookModel();
                rent.deleteData(rentId);
            }
            return 0;
        } finally {
            // ps.close();
        }
    }

    public int deleteWrite(String book_id, String authorId) throws SQLException {
        try {
            sql = "call write_Delete(?, ?);";
            ps = HomeController.con.prepareStatement(sql);
            ps.setString(1, book_id);
            ps.setString(2, authorId);
            return ps.executeUpdate();
        } catch (SQLException e) {
            dialog.showExcectionDialog("Error", null, "ເກີດບັນຫາໃນການລົບຂໍ້ມູນແຕ່ງປຶ້ມ", e);
            return 0;
        } finally {
            // ps.close();
        }
    }
}
