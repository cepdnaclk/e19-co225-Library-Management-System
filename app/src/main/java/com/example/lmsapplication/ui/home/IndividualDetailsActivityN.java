package com.example.lmsapplication.ui.home;

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

public class IndividualDetailsActivityN extends AppCompatActivity {

    private TextView nameTextView, emailTextView, dobTextView, genderTextView, mobileNoTextView, addressTextView, nicTextView;
    private Button makeStaffButton, deleteUserButton;
    private DatabaseReference userRef;
    private DatabaseReference staffRef;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_details_n);

        // Get the user ID passed from UserDetailsActivity
        String userId = getIntent().getStringExtra("userId");

        // Get references to the TextViews and Buttons in the layout
        nameTextView = findViewById(R.id.nameTextView);
        emailTextView = findViewById(R.id.emailTextView);
        dobTextView = findViewById(R.id.dobTextView);
        genderTextView = findViewById(R.id.genderTextView);
        mobileNoTextView = findViewById(R.id.mobileNoTextView);
        addressTextView = findViewById(R.id.addressTextView);
        nicTextView = findViewById(R.id.nicTextView);
        makeStaffButton = findViewById(R.id.makeStaffButton);
        deleteUserButton = findViewById(R.id.deleteUserButton);

        // Get reference to the user in the Firebase Realtime Database
        userRef = FirebaseDatabase.getInstance().getReference().child("User").child(userId);
        staffRef = FirebaseDatabase.getInstance().getReference().child("Staff");

        // Read the user details from the database
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Get user details
                String name = dataSnapshot.child("name").getValue(String.class);
                String email = dataSnapshot.child("email").getValue(String.class);
                String dob = dataSnapshot.child("birthday").getValue(String.class);
                String gender = dataSnapshot.child("gender").getValue(String.class);
                String mobileNo = dataSnapshot.child("mobNum").getValue(String.class);
                String address = dataSnapshot.child("address").getValue(String.class);
                String nic = dataSnapshot.child("nic").getValue(String.class);

                // Set the user details in the TextViews
                nameTextView.setText("Name: " + name);
                emailTextView.setText("Email: " + email);
                dobTextView.setText("Date of Birth: " + dob);
                genderTextView.setText("Gender: " + gender);
                mobileNoTextView.setText("Mobile No: " + mobileNo);
                addressTextView.setText("Address: " + address);
                nicTextView.setText("NIC: " + nic);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // Set click listener for the Make Staff button
        makeStaffButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Move user details to Staff table
                moveUserToStaff(userId);
            }
        });

        // Set click listener for the Delete User button
        deleteUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Delete the user from the database
                deleteUser(userId);
            }
        });
    }

    private void moveUserToStaff(String userId) {
        // Read the user details from the database again
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Get user details
                String name = dataSnapshot.child("name").getValue(String.class);
                String email = dataSnapshot.child("email").getValue(String.class);
                String dob = dataSnapshot.child("birthday").getValue(String.class);
                String gender = dataSnapshot.child("gender").getValue(String.class);
                String mobileNo = dataSnapshot.child("mobNum").getValue(String.class);
                String address = dataSnapshot.child("address").getValue(String.class);
                String nic = dataSnapshot.child("nic").getValue(String.class);

                // Save the user details to the Staff table in Firebase
                staffRef.child(userId).child("name").setValue(name);
                staffRef.child(userId).child("email").setValue(email);
                staffRef.child(userId).child("birthday").setValue(dob);
                staffRef.child(userId).child("gender").setValue(gender);
                staffRef.child(userId).child("mobNum").setValue(mobileNo);
                staffRef.child(userId).child("address").setValue(address);
                staffRef.child(userId).child("nic").setValue(nic);

                // Remove the user details from the User table
                userRef.removeValue();

                // Display a toast message
                Toast.makeText(IndividualDetailsActivityN.this, "User moved to Staff", Toast.LENGTH_SHORT).show();

                // Finish the activity and go back to the previous screen
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void deleteUser(String userId) {
        // Delete the user from the database
        userRef.removeValue();

        // Display a toast message
        Toast.makeText(IndividualDetailsActivityN.this, "User deleted", Toast.LENGTH_SHORT).show();

        // Finish the activity and go back to the previous screen
        finish();
    }
}
