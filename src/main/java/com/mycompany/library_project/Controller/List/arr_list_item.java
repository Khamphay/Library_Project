package com.mycompany.library_project.Controller.List;

public class arr_list_item {
    private String id, name;

    public arr_list_item() {
    }

    public arr_list_item(String id, String name) {
        this.id = id;
        this.name = name;
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

}
