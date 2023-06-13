package com.example.librarymanagementsystem;

public class Book {
    private String bookName;
    private String bookId;
    private String author;

    public Book() {
        
    }

    public Book(String bookName, String bookId, String author) {
        this.bookName = bookName;
        this.bookId = bookId;
        this.author = author;
    }

    // Add getters and setters for the fields
    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
