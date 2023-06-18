package com.example.lmsapplication.ui.books;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.example.lmsapplication.databinding.ActivityBookSearchBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class BOOK_SEARCH extends AppCompatActivity {

    ActivityBookSearchBinding binding;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBookSearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.bookSearchBtn.setOnClickListener(v -> {
            String bookName = binding.etBookName.getText().toString();
            if (!bookName.isEmpty()) {
                readData(bookName);
            } else {
                Toast.makeText(BOOK_SEARCH.this, "Please Enter a Book Name", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void readData(String bookName) {
        reference = FirebaseDatabase.getInstance().getReference("BOOKS");

        // Check if bookName is a valid book ID
        reference.child(bookName).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult().exists()) {
                    // Book found by ID
                    displayBookData(task.getResult());
                } else {
                    // Book not found by ID, search by name
                    queryBookByName(bookName);
                }
            } else {
                Toast.makeText(BOOK_SEARCH.this, "Failed to Read", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void queryBookByName(String bookName) {
        reference.orderByChild("name").equalTo(bookName).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult().exists()) {
                    // Book found by name
                    DataSnapshot dataSnapshot = task.getResult().getChildren().iterator().next();
                    displayBookData(dataSnapshot);
                } else {
                    Toast.makeText(BOOK_SEARCH.this, "Book Doesn't Exist", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(BOOK_SEARCH.this, "Failed to Read", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayBookData(DataSnapshot dataSnapshot) {
        Toast.makeText(BOOK_SEARCH.this, "Successfully Read", Toast.LENGTH_SHORT).show();
        String author = String.valueOf(dataSnapshot.child("author").getValue());
        int copy = dataSnapshot.child("copies").getValue(Integer.class);
        String id = String.valueOf(dataSnapshot.child("id").getValue());
        String name = String.valueOf(dataSnapshot.child("name").getValue());
        binding.tvAuthor.setText(author);
        binding.tvCopies.setText(String.valueOf(copy));
        binding.tvId.setText(id);
        binding.tvName.setText(name);
    }

}