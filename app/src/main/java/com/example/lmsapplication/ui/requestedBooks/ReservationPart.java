package com.example.lmsapplication.ui.requestedBooks;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lmsapplication.HomeActivity;
import com.example.lmsapplication.MainActivity;
import com.example.lmsapplication.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class ReservationPart extends AppCompatActivity {

    // Define the variable of CalendarView type
    // and TextView type;
    CalendarView calendar;
    TextView date_view;
    Button cancelBtn, proceedBtn ;
    String Book_name;
    DatabaseReference ReservedBookRef, BookLibraryRef;
    String Date;
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
        ReservedBookRef = FirebaseDatabase.getInstance().getReference().child("ReservedBooks");
        BookLibraryRef = FirebaseDatabase.getInstance().getReference().child("BOOKS");

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
        Book_name = getIntent().getStringExtra("bookName");
        String key = Book_name;
        ReservedBookRef.child(key).setValue(Book_name, Date);
        Toast.makeText(ReservationPart.this, "Reservation is success", Toast.LENGTH_LONG).show();
    }

    private void updateBookCount(){
        int numberOfCopies = Integer.valueOf(getIntent().getStringExtra("numberOfCopies"));
        numberOfCopies--;
        Log.i("updateBookCount: ",numberOfCopies+"");
    }
}

