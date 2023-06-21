package com.example.lmsapplication.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

public class staffDetailsActivity extends AppCompatActivity {

    private static final String TAG = "staffDetailsActivity";

    private TableLayout userTableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_details);

        userTableLayout = findViewById(R.id.userTableLayout);

        // Get reference to the "Staff" table in the Firebase Realtime Database
        DatabaseReference staffRef = FirebaseDatabase.getInstance().getReference().child("Staff");

        // Read the staff details from the database
        staffRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot staffSnapshot : dataSnapshot.getChildren()) {
                    // Get staff details
                    String name = staffSnapshot.child("name").getValue(String.class);
                    String email = staffSnapshot.child("email").getValue(String.class);

                    // Create a new row in the table for each staff
                    TableRow row = new TableRow(staffDetailsActivity.this);
                    row.setBackground(getDrawable(R.drawable.table_row_border));

                    // Create TextViews to display the staff details
                    TextView nameTextView = createTextView(name);
                    TextView emailTextView = createTextView(email);

                    // Add TextViews to the row
                    row.addView(nameTextView);
                    row.addView(emailTextView);

                    // Add click listener to the row
                    row.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Get the staff's ID from the staffSnapshot or any unique identifier
                            String staffId = staffSnapshot.getKey();

                            // Open IndividualDetailsActivityN and pass the staff ID
                            Intent intent = new Intent(staffDetailsActivity.this, individualStaffDetailsActivity.class);
                            intent.putExtra("staffId", staffId);
                            startActivity(intent);
                        }
                    });

                    // Add the row to the table
                    userTableLayout.addView(row);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Failed to read staff details.", databaseError.toException());
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
