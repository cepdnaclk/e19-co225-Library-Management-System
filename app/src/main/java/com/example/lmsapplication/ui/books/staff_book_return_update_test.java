package com.example.lmsapplication.ui.books;/*
package com.example.librarymanagementsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.support.annotation.NonNull;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.android.gms.tasks.OnFailureListener;
import android.util.Log;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.HashMap;

public class STAFF_BOOK_RETURN_UPDATE extends AppCompatActivity {

    Button bookUpdateButton;
    EditText bookNameInput;
    EditText MemberIDInput;
    EditText dueDateInput;

    DatabaseReference booksRef;
    DatabaseReference borrowedBookRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_book_return_update);

        bookUpdateButton = findViewById(R.id.bookUpdateButton);
        bookNameInput = findViewById(R.id.bookNameInput);
        MemberIDInput = findViewById(R.id.MemberIDInput);


        borrowedBookRef = FirebaseDatabase.getInstance().getReference().child("BORROWED_BOOKS");
        booksRef = FirebaseDatabase.getInstance().getReference().child("BOOKS");

        bookUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    if (bookNameInput != null && MemberIDInput != null) {
                        String bookName = bookNameInput.getText().toString();
                        String memberID = MemberIDInput.getText().toString();

                        int numOfBooks = 1;

                        // Check if the book exists
                        checkBookExistence(bookName, memberID, numOfBooks);
                    }else if(bookNameInput = null || MemberIDInput = null){
                        // Handle the case where EditText objects are null
                        Toast.makeText(STAFF_BOOK_RETURN_UPDATE.this, "Input Data correctly", Toast.LENGTH_SHORT).show();
                    }
            }
        });
    }

    private void checkBookExistence(final String bookName, final String memberID, final int numOfBooks) {
        DatabaseReference bookRef = booksRef.child(bookName);

        bookRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Book exists, update the BORROWED_BOOKS table and numberOfCopies
                    updateBorrowedBooksTable(bookName, memberID, numOfBooks);
                    updateNumberOfCopies(bookName, numOfBooks);

                    // Check if the member ID exists in BORROWED_BOOKS table
                    DatabaseReference memberRef = borrowedBookRef.child(memberID);
                    memberRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                // Member ID exists, remove it from BORROWED_BOOKS table
                                removeMemberFromBorrowedBooks(memberID);
                            } else {
                                // Member ID doesn't exist, show a message
                                Toast.makeText(STAFF_BOOK_RETURN_UPDATE.this, "Member did not borrow any book before", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.e("Firebase", "Error: " + databaseError.getMessage());
                        }
                    });
                } else {
                    // Book doesn't exist, show an error message
                    Toast.makeText(STAFF_BOOK_RETURN_UPDATE.this, "Book not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors that occur during the data retrieval process
                Log.e("Firebase", "Error: " + databaseError.getMessage());
            }
        });
    }

    private void updateBorrowedBooksTable(String bookName, String memberID,  int numOfBooks) {
        DatabaseReference borrowedBook = borrowedBookRef.child(memberID);

        HashMap<String, Object> borrowedDetails = new HashMap<>();
        borrowedDetails.put("Book Name", bookName);
        borrowedDetails.put("Member ID", memberID);

        borrowedBook.setValue(borrowedDetails)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(STAFF_BOOK_RETURN_UPDATE.this, "Book added successfully in RETURNED BOOK TABLE", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(STAFF_BOOK_RETURN_UPDATE.this, "Failed to add book in RETURNED BOOK TABLE", Toast.LENGTH_SHORT).show();
                    }
                });
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
                        int updatedCopies = currentCopies + numOfBooks;

                        if (updatedCopies >= 0) {
                            book.setcopies(updatedCopies);

                            bookRef.child("numberOfCopies").setValue(updatedCopies)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(STAFF_BOOK_RETURN_UPDATE.this, "Book details updated successfully", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(STAFF_BOOK_RETURN_UPDATE.this, "Failed to update book details", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            Toast.makeText(STAFF_BOOK_RETURN_UPDATE.this, "Insufficient copies", Toast.LENGTH_SHORT).show();
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

    private void removeMemberFromBorrowedBooks(final String memberID) {
        DatabaseReference memberRef = borrowedBookRef.child(memberID);

        memberRef.removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(STAFF_BOOK_RETURN_UPDATE.this, "Member ID removed from BORROWED_BOOKS table", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(STAFF_BOOK_RETURN_UPDATE.this, "Failed to remove Member ID from BORROWED_BOOKS table", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}

 */