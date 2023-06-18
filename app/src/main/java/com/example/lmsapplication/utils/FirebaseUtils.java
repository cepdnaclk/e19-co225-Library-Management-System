package com.example.lmsapplication.utils;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseUtils {

    private static final String REQUESTED_BOOKS_TABLE = "RequestedBooks";
    private static final String BORROWED_BOOKS_TABLE = "BorrowedBooks";
    private static final String AVAILABLE_BOOKS_TABLE = "AvailableBooks";

    private static DatabaseReference getDatabaseReference() {
        return FirebaseDatabase.getInstance().getReference();
    }

    public static DatabaseReference getRequestedBooksRef() {
        return getDatabaseReference().child(REQUESTED_BOOKS_TABLE);
    }

    public static DatabaseReference getBorrowedBooksRef() {
        return getDatabaseReference().child(BORROWED_BOOKS_TABLE);
    }

    public static DatabaseReference getAvailableBooksRef() {
        return getDatabaseReference().child(AVAILABLE_BOOKS_TABLE);
    }
}
