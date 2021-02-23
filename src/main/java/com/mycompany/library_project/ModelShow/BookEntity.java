package com.mycompany.library_project.ModelShow;

public class BookEntity {
    private String id, name, type, category, page, qty, lang;

    public BookEntity(String id, String name, String type, String category, String page, String qty, String lang) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.category = category;
        this.page = page;
        this.qty = qty;
        this.lang = lang;
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

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }
    
}
