package com.example.lmsapplication.ui.books;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.lmsapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class IndividualBookDetails extends AppCompatActivity {

    private TextView authorTextView, idTextView, nameTextView, numberOfCopiesTextView;
    //private Button makeStaffButton, deleteUserButton;
    private DatabaseReference userRef;
    private DatabaseReference staffRef;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_book_details);

        // Get the user ID passed from UserDetailsActivity
        String userId = getIntent().getStringExtra("userId");

        // Get references to the TextViews and Buttons in the layout
        authorTextView = findViewById(R.id.authorTextView);
        idTextView = findViewById(R.id.idTextView);
        nameTextView = findViewById(R.id.nameTextView);
        numberOfCopiesTextView = findViewById(R.id.numberOfCopiesTextView);


        //Get reference to the user in the Firebase Realtime Database
        userRef = FirebaseDatabase.getInstance().getReference().child("BOOKS").child(userId);


        // Read the user details from the database
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Get user details
                String author = dataSnapshot.child("author").getValue(String.class);
                String id = dataSnapshot.child("id").getValue(String.class);
                String name = dataSnapshot.child("name").getValue(String.class);
                int numberOfCopies = dataSnapshot.child("numberOfCopies").getValue(Integer.class);

                // Set the user details in the TextViews
                nameTextView.setText("Name: " + name);
                authorTextView.setText("Author: " + author);
                numberOfCopiesTextView.setText("Number of Copies: " + numberOfCopies);
                idTextView.setText("Id: " + id);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }






}
