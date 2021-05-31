package com.mycompany.library_project.Model;

import java.sql.Date;

public class ListBookModel {
    private int number;
    private String rentId;
    private String barcode;
    private String bookName;
    private String memberId;
    private String memberName;
    private Date rentDate;
    private Date sendDate;
    private String status;
    private int dayOut;
    public ListBookModel() {
    }
    public ListBookModel(int number, String rentId, String barcode, String bookName, String memberId, String memberName,
            Date rentDate, Date sendDate, String status, int dayOut) {
        this.number = number;
        this.rentId = rentId;
        this.barcode = barcode;
        this.bookName = bookName;
        this.memberId = memberId;
        this.memberName = memberName;
        this.rentDate = rentDate;
        this.sendDate = sendDate;
        this.status = status;
        this.dayOut = dayOut;
    }
    public int getNumber() {
        return number;
    }
    public void setNumber(int number) {
        this.number = number;
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
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public int getDayOut() {
        return dayOut;
    }
    public void setDayOut(int dayOut) {
        this.dayOut = dayOut;
    }

    
}
