package com.example.lmsapplication.ui.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.lmsapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserDetails extends AppCompatActivity {

    private static final String TAG = "UserDetailsActivity";

    private TableLayout userTableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        userTableLayout = findViewById(R.id.userTableLayout);

        // Get reference to the "Users" table in the Firebase Realtime Database
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("Users");

        // Read the user details from the database
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    // Get user details
                    String name = userSnapshot.child("name").getValue(String.class);
                    String email = userSnapshot.child("email").getValue(String.class);

                    // Create a new row in the table for each user
                    TableRow row = new TableRow(UserDetails.this);
                    row.setBackground(getDrawable(R.drawable.table_row_border));

                    // Create TextViews to display the user details
                    TextView nameTextView = createTextView(name);
                    TextView emailTextView = createTextView(email);

                    // Add TextViews to the row
                    row.addView(nameTextView);
                    row.addView(emailTextView);

                    // Add click listener to the row
                    row.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Get the user's ID from the userSnapshot or any unique identifier
                            String userId = userSnapshot.getKey();

                            // Open IndividualDetailsActivity and pass the user ID
                            Intent intent = new Intent(UserDetails.this, IndividualDetailsActivityN.class);
                            intent.putExtra("userId", userId);
                            startActivity(intent);
                        }
                    });

                    // Add the row to the table
                    userTableLayout.addView(row);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Failed to read user details.", databaseError.toException());
            }
        });
    }

    private TextView createTextView(String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setPadding(27, 20, 20, 20);
        textView.setTextColor(getResources().getColor(android.R.color.black));
        textView.setTextSize(15);

        return textView;
    }


}
