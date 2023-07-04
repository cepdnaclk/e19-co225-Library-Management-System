package com.example.lmsapplication.ui.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.lmsapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class individualStaffDetailsActivity extends AppCompatActivity {

    private TextView nameTextView, emailTextView, dobTextView, genderTextView, mobileNoTextView, addressTextView, nicTextView;
    private Button makeAdminButton;
    private DatabaseReference staffRef;
    private DatabaseReference adminRef;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_staff_details);

        // Get the staff ID passed from StaffDetailsActivity
        String staffId = getIntent().getStringExtra("staffId");

        // Get references to the TextViews and Button in the layout
        nameTextView = findViewById(R.id.nameTextView);
        emailTextView = findViewById(R.id.emailTextView);
        dobTextView = findViewById(R.id.dobTextView);
        genderTextView = findViewById(R.id.genderTextView);
        mobileNoTextView = findViewById(R.id.mobileNoTextView);
        addressTextView = findViewById(R.id.addressTextView);
        nicTextView = findViewById(R.id.nicTextView);
        makeAdminButton = findViewById(R.id.makeAdminButton);

        // Get reference to the staff entry in the Firebase Realtime Database
        staffRef = FirebaseDatabase.getInstance().getReference().child("Staff").child(staffId);
        adminRef = FirebaseDatabase.getInstance().getReference().child("Admin");

        // Read the staff details from the database
        staffRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Get staff details
                String name = dataSnapshot.child("name").getValue(String.class);
                String email = dataSnapshot.child("email").getValue(String.class);
                String dob = dataSnapshot.child("birthday").getValue(String.class);
                String gender = dataSnapshot.child("gender").getValue(String.class);
                String mobileNo = dataSnapshot.child("mobNum").getValue(String.class);
                String address = dataSnapshot.child("address").getValue(String.class);
                String nic = dataSnapshot.child("nic").getValue(String.class);

                // Set the staff details in the TextViews
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

        // Set click listener for the Make Admin button
        makeAdminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Move staff details to Admin table
                moveStaffToAdmin(staffId);
            }
        });
    }

    private void moveStaffToAdmin(String staffId) {
        // Read the staff details from the database again
        staffRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Get staff details
                String name = dataSnapshot.child("name").getValue(String.class);
                String email = dataSnapshot.child("email").getValue(String.class);
                String dob = dataSnapshot.child("birthday").getValue(String.class);
                String gender = dataSnapshot.child("gender").getValue(String.class);
                String mobileNo = dataSnapshot.child("mobNum").getValue(String.class);
                String address = dataSnapshot.child("address").getValue(String.class);
                String nic = dataSnapshot.child("nic").getValue(String.class);

                // Save the staff details to the Admin table in Firebase
                adminRef.child(staffId).child("name").setValue(name);
                adminRef.child(staffId).child("email").setValue(email);
                adminRef.child(staffId).child("birthday").setValue(dob);
                adminRef.child(staffId).child("gender").setValue(gender);
                adminRef.child(staffId).child("mobNum").setValue(mobileNo);
                adminRef.child(staffId).child("address").setValue(address);
                adminRef.child(staffId).child("nic").setValue(nic);

                // Remove the staff details from the Staff table
                staffRef.removeValue();

                // Finish the activity and go back to the previous screen
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
