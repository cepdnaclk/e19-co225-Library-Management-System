package com.example.lmsapplication.ui.books;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.example.lmsapplication.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.android.gms.tasks.OnFailureListener;
import android.util.Log;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ServerValue;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import java.util.HashMap;

public class STAFF_BOOK_UPDATE extends AppCompatActivity {

    Button bookUpdateButton;
    EditText bookNameInput;
    EditText memberIDInput;
    EditText dueDateInput;

    DatabaseReference booksRef;
    DatabaseReference borrowedBookRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_book_update);

        bookUpdateButton = findViewById(R.id.bookUpdateButton);
        bookNameInput = findViewById(R.id.bookNameInput);
        memberIDInput = findViewById(R.id.memberIDInput);
        dueDateInput = findViewById(R.id.dueDateInput);

        borrowedBookRef = FirebaseDatabase.getInstance().getReference().child("BORROWED_BOOKS");
        booksRef = FirebaseDatabase.getInstance().getReference().child("BOOKS");

        bookUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String bookName = bookNameInput.getText().toString();
                String memberID = memberIDInput.getText().toString();
                String dueDate = dueDateInput.getText().toString();

                int numOfBooks = 1;

                // Check if the book exists
                checkBookExistence(bookName, memberID, dueDate, numOfBooks);
            }
        });
    }

    private void checkBookExistence(final String bookName, final String memberID, final String dueDate, final int numOfBooks) {
        DatabaseReference bookRef = booksRef.child(bookName);

        bookRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Book exists, check if the member has already borrowed a book
                    checkMemberExistence(bookName, memberID, dueDate, numOfBooks);
                } else {
                    // Book doesn't exist, show an error message
                    Toast.makeText(STAFF_BOOK_UPDATE.this, "Book not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors that occur during the data retrieval process
                Log.e("Firebase", "Error: " + databaseError.getMessage());
            }
        });
    }

    private void checkMemberExistence(final String bookName, final String memberID, final String dueDate, final int numOfBooks) {
        DatabaseReference borrowedBook = borrowedBookRef.child(memberID);

        borrowedBook.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Member ID already exists in the BORROWED_BOOKS table, show an error message
                    Toast.makeText(STAFF_BOOK_UPDATE.this, "Member already borrowed a book", Toast.LENGTH_SHORT).show();
                } else {
                    // Member ID doesn't exist, update the BORROWED_BOOKS table and numberOfCopies
                    updateBorrowedBooksTable(bookName, memberID, dueDate, numOfBooks);
                    updateNumberOfCopies(bookName, numOfBooks);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors that occur during the data retrieval process
                Log.e("Firebase", "Error: " + databaseError.getMessage());
            }
        });
    }

    private void updateBorrowedBooksTable(String bookName, String memberID, String dueDate, int numOfBooks) {
        DatabaseReference borrowedBook = borrowedBookRef.child(memberID);

        HashMap<String, Object> borrowedDetails = new HashMap<>();
        borrowedDetails.put("Book Name", bookName);
        borrowedDetails.put("Member ID", memberID);
        borrowedDetails.put("Due Date", dueDate);
        borrowedDetails.put("Timestamp", getCurrentDateTime());


        borrowedBook.setValue(borrowedDetails)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(STAFF_BOOK_UPDATE.this, "Book added successfully in BORROWED BOOK TABLE", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(STAFF_BOOK_UPDATE.this, "Failed to add book in BORROWED BOOK TABLE", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(new Date());
    }

    private void updateNumberOfCopies(final String bookName, final int numOfBooks) {
        DatabaseReference bookRef = booksRef.child(bookName);

        bookRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Book book = dataSnapshot.getValue(Book.class);
                    if (book != null) {
                        int currentCopies = dataSnapshot.child("numberOfCopies").getValue(Integer.class);
                        int updatedCopies = currentCopies - numOfBooks;

                        if (updatedCopies >= 0) {
                            book.setcopies(updatedCopies);

                            bookRef.child("numberOfCopies").setValue(updatedCopies)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(STAFF_BOOK_UPDATE.this, "Book details updated successfully", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(STAFF_BOOK_UPDATE.this, "Failed to update book details", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            Toast.makeText(STAFF_BOOK_UPDATE.this, "Insufficient copies", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Error: " + databaseError.getMessage());
            }
        });
    }
}


