package com.mycompany.library_project.Model;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

public class SearchModel extends RecursiveTreeObject<SearchModel> {

    private String bookId;
    private String bookName;
    private String page;
    private String ISBN;
    private String catg;
    private String type;
    private String tableId;
    private String logId;
    private String status;

    public SearchModel(String bookId, String bookName, String ISBN, String page, String catg, String type,
            String tableId, String logId, String status) {
        this.bookId = bookId;
        this.bookName = bookName;
        this.page = page;
        this.ISBN = ISBN;
        this.catg = catg;
        this.type = type;
        this.tableId = tableId;
        this.logId = logId;
        this.status = status;
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

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String iSBN) {
        ISBN = iSBN;
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

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
