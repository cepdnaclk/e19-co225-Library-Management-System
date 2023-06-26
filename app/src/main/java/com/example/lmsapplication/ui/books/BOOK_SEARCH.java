package com.example.lmsapplication.ui.books;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.example.lmsapplication.FirebaseManager;
import com.example.lmsapplication.R;
import com.example.lmsapplication.databinding.ActivityBookSearchBinding;
import com.example.lmsapplication.ui.requestedBooks.ReservationPart;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;


public class BOOK_SEARCH extends AppCompatActivity {

    ActivityBookSearchBinding binding;
    DatabaseReference reference, requestBookRef;

    Button requestBtn, reservedBtn;
    String name, id, copy;

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
        String author = String.valueOf(dataSnapshot.child("author").getValue());
        copy = String.valueOf(dataSnapshot.child("numberOfCopies").getValue());
        id = String.valueOf(dataSnapshot.child("id").getValue());
        name = String.valueOf(dataSnapshot.child("name").getValue());
        binding.tvAuthor.setText(author);
        binding.tvCopies.setText(copy);
        binding.tvId.setText(id);
        binding.tvName.setText(name);
        if (Integer.parseInt(copy)>0){
            Log.i("displayBookData: ",String.valueOf(copy));
            reservedBtn = findViewById(R.id.ReservedBtn);
            reservedBtn.setVisibility(View.VISIBLE);
            reservedBtnClk(reservedBtn);
        }
        else {
            requestBtn = findViewById(R.id.RequestBtn);
            requestBtn.setVisibility(View.VISIBLE);
            requestBtnClk(requestBtn);
        }
    }

    private void reservedBtnClk(Button btn){
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BOOK_SEARCH.this, ReservationPart.class);
                intent.putExtra("BookName",name);
                intent.putExtra("BookID",id);
                intent.putExtra("noOfCopies",copy);
                startActivity(intent);
            }
        });
    }
    private void requestBtnClk(Button btn){
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestBookRef = FirebaseDatabase.getInstance().getReference().child("RequestedBooks");

                FirebaseManager firebaseManager = FirebaseManager.getInstance();
                String userEmail =firebaseManager.getCurrentUser().getEmail().toString().substring(0,6);

                // Create a HashMap to hold the requested book details
                HashMap<String, Object> requestBookDetails = new HashMap<>();
                requestBookDetails.put("Book-Name", name);
                requestBookDetails.put("BookID", id);
                requestBookDetails.put("Mail", userEmail);

                requestBookRef.child(userEmail).setValue(requestBookDetails);

            }
        });
    }

}

