package com.example.lmsapplication.ui.requestedBooks;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.lmsapplication.FirebaseManager;
import com.example.lmsapplication.HomeActivity;
import com.example.lmsapplication.MainActivity;
import com.example.lmsapplication.R;
import com.example.lmsapplication.ui.books.BOOK_SEARCH;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;


public class ReservationPart extends AppCompatActivity {

    // Define the variable of CalendarView type
    // and TextView type;
    CalendarView calendar;
    TextView date_view;
    Button cancelBtn, proceedBtn ;
    DatabaseReference ReservedBookRef, BookLibraryRef;
    String Date,Book_id;
    int numberOfCopies;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);

        // By ID we can use each component
        // which id is assign in xml file
        // use findViewById() to get the
        // CalendarView and TextView
        calendar =
                findViewById(R.id.calendar);
        date_view =
                findViewById(R.id.date_view);
        proceedBtn =
                findViewById(R.id.proceed);
        cancelBtn =
                findViewById(R.id.cancel);

        // Add Listener in calendar
        calendar
                .setOnDateChangeListener(
                        new CalendarView
                                .OnDateChangeListener() {
                            @Override

                            // In this Listener have one method
                            // and in this method we will
                            // get the value of DAYS, MONTH, YEARS
                            public void onSelectedDayChange(
                                    CalendarView view,
                                    int year,
                                    int month,
                                    int dayOfMonth)
                            {

                                // Store the value of date with
                                // format in String type Variable
                                // Add 1 in month because month
                                // index is start with 0
                                Date
                                        = dayOfMonth + "-"
                                        + (month + 1) + "-" + year;

                                // set this date in TextView for Display
                                date_view.setText(Date);
                                proceedBtn.setEnabled(true);

                            }
                        });
        proceedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertDataToReservedBooks();
                updateBookCount();
                openHomeActivity();
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToSearch();
            }
        });
    }
    private void openHomeActivity(){
        Intent newIntent = new Intent(ReservationPart.this, MainActivity.class);
        startActivity(newIntent);
    }
    private void backToSearch(){
        Intent newIntent = new Intent(ReservationPart.this, HomeActivity.class);
        startActivity(newIntent);
    }
    private void insertDataToReservedBooks() {

        FirebaseManager firebaseManager = FirebaseManager.getInstance();
        String userEmail =firebaseManager.getCurrentUser().getEmail().toString().substring(0,6);

        ReservedBookRef = FirebaseDatabase.getInstance().getReference().child("ReservedBooks");

        String Book_name = getIntent().getStringExtra("BookName");
        Book_id = getIntent().getStringExtra("BookID");

        // Create a HashMap to hold the reserved book details
        HashMap<String, Object> reservedBookDetails = new HashMap<>();
        reservedBookDetails.put("User-ID", userEmail);
        reservedBookDetails.put("Book-Name", Book_name);
        reservedBookDetails.put("BookID", Book_id);
        reservedBookDetails.put("Reserving Date", Date);

        ReservedBookRef.child(userEmail).setValue(reservedBookDetails);

        Toast.makeText(ReservationPart.this, "Reservation is success", Toast.LENGTH_LONG).show();
    }

    public int updateBookCount(){
        String Number = getIntent().getStringExtra("noOfCopies");
        numberOfCopies = Integer.parseInt(Number);
        numberOfCopies--;
    }


   private void queryBookByName(String bookName) {

       // Get a reference to the database
       BookLibraryRef = FirebaseDatabase.getInstance().getReference().child("BOOKS");

// Retrieve the table data
       BookLibraryRef.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {
               for (DataSnapshot rowSnapshot : dataSnapshot.getChildren()) {
                   // Identify the row
                   String rowId = rowSnapshot.getKey();
                   // Modify the specific element
                   BookLibraryRef.child(rowId).child("numberOfCopies").setValue(numberOfCopies+"");
               }
           }
           @Override
           public void onCancelled(DatabaseError databaseError) {
               // Handle the error
           }

       });



               }
}

