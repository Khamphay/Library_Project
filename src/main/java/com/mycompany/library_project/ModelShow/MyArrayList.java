package com.mycompany.library_project.ModelShow;

import java.util.ArrayList;

public  class MyArrayList {
    public ArrayList<BookEntity> bookDetail(String[] data) {
        ArrayList<BookEntity> book_item = new ArrayList<>();
        BookEntity book = new BookEntity(data[0], data[1], data[2], data[3], data[4], data[5],data[6]);
        book_item.add(book);
        return book_item;
    }
}
