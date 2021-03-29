package com.mycompany.library_project.ModelShow;

public class BookEntity {
    private String id, name, isbn, type, category, page, qty, detail;

    public BookEntity(String id, String name, String type, String category, String page, String qty, String detail) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.category = category;
        this.page = page;
        this.qty = qty;
        this.detail = detail;
    }

    public BookEntity(String id, String name, String isbn, String page, String qty, String type, String category, 
            String detail) {
        this.id = id;
        this.name = name;
        this.isbn = isbn;
        this.type = type;
        this.category = category;
        this.page = page;
        this.qty = qty;
        this.detail = detail;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setISBN(String isbn) {
        this.isbn = isbn;
    }

    public String getISBN() {
        return isbn;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

}
