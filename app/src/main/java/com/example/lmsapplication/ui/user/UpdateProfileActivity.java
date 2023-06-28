package com.example.lmsapplication.ui.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.lmsapplication.FirebaseManager;
import com.example.lmsapplication.LoginAndRegster.LoginActivity;
import com.example.lmsapplication.R;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class UpdateProfileActivity extends AppCompatActivity {

    private EditText editTextUpdateName, editTextUpdateDOB, editTextUpdateMobile,Nic,Email;
    private EditText editTextUpdateAddress;
    private RadioGroup radioGroupUpdateGender;
    private ProgressBar progressBar;
    private DatabaseReference profileRef;

    private FirebaseUser firebaseuser;

    private String fullName, email, doB, gender, mobile, nic, address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        String currentUser = getIntent().getStringExtra("currentUser");
        getSupportActionBar().setTitle("Update Profile Details");

        FirebaseManager firebaseManager = FirebaseManager.getInstance();
        // Initialize Firebase Realtime Database reference
        profileRef = firebaseManager.getDataRef(currentUser);
        firebaseuser = firebaseManager.getCurrentUser();



        // Find views
        editTextUpdateName = findViewById(R.id.editTextUpdateName);
        editTextUpdateDOB = findViewById(R.id.editTextUpdateDOB);
        editTextUpdateMobile = findViewById(R.id.editTextUpdateMobile);
        radioGroupUpdateGender = findViewById(R.id.radioGroupUpdateGender);
        editTextUpdateAddress = findViewById(R.id.editTextUpdateAddress);
        progressBar = findViewById(R.id.progressBar);


        //Load data
        showUserProfile(firebaseManager, currentUser);

        Button buttonUpdateProfile = findViewById(R.id.buttonUpdateProfile);
        buttonUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });
    }
    private void showUserProfile(FirebaseManager firebaseManager, String currentUser) {
        String userID = firebaseManager.getCurrentUser().getUid();

        DatabaseReference referenceProfile = firebaseManager.getDataRef(currentUser);
        referenceProfile.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (userID != null) {
                    fullName = snapshot.child("name").getValue(String.class);
                    doB = snapshot.child("birthday").getValue(String.class);
                    gender = snapshot.child("gender").getValue(String.class);
                    mobile = snapshot.child("mobNum").getValue(String.class);
                    address = snapshot.child("address").getValue(String.class);

                    nic = snapshot.child("nic").getValue(String.class);
                    email = snapshot.child("email").getValue(String.class);

                    editTextUpdateName.setText(fullName);
                    editTextUpdateDOB.setText(doB);
                    editTextUpdateMobile.setText(mobile);
                    editTextUpdateAddress.setText(address);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void updateProfile() {
        String name = editTextUpdateName.getText().toString();
        String dob = editTextUpdateDOB.getText().toString();
        String mobile = editTextUpdateMobile.getText().toString();
        String address = editTextUpdateAddress.getText().toString();
        String gender = getSelectedGender();

        // Perform validation if needed
        if (name.isEmpty() || dob.isEmpty() || mobile.isEmpty() || gender.isEmpty()) {
            Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Display progress bar
        progressBar.setVisibility(View.VISIBLE);

        // Create a new profile entry in Firebase Realtime Database
        String profileId = firebaseuser.getUid().toString();

        Map<String, Object> profileData = new HashMap<>();
        profileData.put("name", name);
        profileData.put("birthday", dob);
        profileData.put("mobNum", mobile);
        profileData.put("gender", gender);
        profileData.put("address", address);
        profileData.put("email", email);
        profileData.put("nic", nic);


        profileRef.child(profileId).setValue(profileData).addOnSuccessListener(aVoid -> {
                    // Profile update successful
                    Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();

                    // Clear the form fields
                    editTextUpdateName.setText("");
                    editTextUpdateDOB.setText("");
                    editTextUpdateMobile.setText("");
                    editTextUpdateAddress.setText("");
                    radioGroupUpdateGender.clearCheck();

                    // Hide progress bar
                    progressBar.setVisibility(View.GONE);
                })
                .addOnFailureListener(e -> {
                    // Profile update failed
                    Toast.makeText(this, "Profile update failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();

                    // Hide progress bar
                    progressBar.setVisibility(View.GONE);
                });
    }

    private String getSelectedGender() {
        int selectedId = radioGroupUpdateGender.getCheckedRadioButtonId();
        RadioButton radioButton = findViewById(selectedId);

        if (radioButton != null) {
            return radioButton.getText().toString();
        }

        return "";
    }
}
