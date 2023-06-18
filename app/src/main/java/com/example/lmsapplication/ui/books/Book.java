package com.example.lmsapplication.ui.books;

public class Book {
    private String bookName;
    private String bookAuthor;
    private int copies;
    private String bookId;

    // Default constructor (required for Firebase)
    public Book() {
    }

    public Book(String bookName, String bookAuthor, int copies, String bookId) {
        this.bookName = bookName;
        this.bookAuthor = bookAuthor;
        this.copies = copies;
        this.bookId = bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    public int getcopies() {
        return copies;
    }

    public void setcopies(int copies) {
        this.copies = copies;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }
}


