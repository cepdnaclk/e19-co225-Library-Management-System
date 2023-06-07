package com.example.librarymanagementsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.support.annotation.NonNull;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.android.gms.tasks.OnFailureListener;





public class ADD_BOOK extends AppCompatActivity {

    Button bookAddButton;
    EditText bookNameInput;
    EditText bookIDInput;
    EditText bookAuthorInput;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        bookAddButton = findViewById(R.id.bookAddButton);
        bookNameInput = findViewById(R.id.bookNameInput);
        bookIDInput = findViewById(R.id.bookIDInput);
        bookAuthorInput = findViewById(R.id.bookAuthorInput);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("BOOKS");

        bookAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String bookName = bookNameInput.getText().toString();
                String bookID = bookIDInput.getText().toString();
                String bookAuthor = bookAuthorInput.getText().toString();

                // Create a HashMap to hold the book details
                ArrayList bookDetails = new ArrayList();
                bookDetails.add(bookID);
                bookDetails.add(bookName);
                bookDetails.add(bookAuthor);
                //-------------------------------------------------------------

                databaseReference.child(bookID).setValue(bookDetails)

                        .addOnSuccessListener(new OnSuccessListener<Void>() {

                            public void onSuccess(Void aVoid) {
                                Toast.makeText(ADD_BOOK.this, "Book added successfully", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {

                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ADD_BOOK.this, "Failed to add book", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}
