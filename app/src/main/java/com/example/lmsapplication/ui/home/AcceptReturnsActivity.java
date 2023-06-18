package com.example.lmsapplication.ui.home;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.lmsapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AcceptReturnsActivity extends AppCompatActivity {

    private List<String> borrowedBooksList;
    private List<String> bookIdsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_returns);

        borrowedBooksList = new ArrayList<>();
        bookIdsList = new ArrayList<>();

        ListView borrowedBooksListView = findViewById(R.id.borrowedBooksListView);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, borrowedBooksList);
        borrowedBooksListView.setAdapter(adapter);

        // Retrieve borrowed books from Firebase and populate the list
        FirebaseDatabase.getInstance().getReference("BorrowedBooks")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        borrowedBooksList.clear();
                        bookIdsList.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String bookId = snapshot.child("bookID").getValue(String.class);
                            String userId = snapshot.child("userID").getValue(String.class);
                            String bookDetails = "BookId: " + bookId + ", UserId: " + userId;
                            borrowedBooksList.add(bookDetails);
                            bookIdsList.add(bookId);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Handle error
                    }
                });

        // Handle item click events
        borrowedBooksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AcceptReturnsActivity.this);
                builder.setTitle("Accept or Cancel Return")
                        .setMessage("What would you like to do?")
                        .setPositiveButton("Accept Return", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                acceptReturn(position);
                            }
                        })
                        .setNegativeButton("Cancel", null);
                builder.create().show();
            }
        });
    }

    private void acceptReturn(final int position) {
        final String bookId = bookIdsList.get(position);

        // Delete the book from the BorrowedBooks table
        FirebaseDatabase.getInstance().getReference("BorrowedBooks").child(bookId)
                .removeValue(new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (databaseError == null) {
                            // Update the AvailableBooks table
                            updateAvailableBooks(bookId);

                            Toast.makeText(AcceptReturnsActivity.this, "Accepted Return for BookId: " + bookId, Toast.LENGTH_SHORT).show();
                        } else {
                            // Handle error
                        }
                    }
                });
    }

    private void updateAvailableBooks(final String bookId) {
        FirebaseDatabase.getInstance().getReference("AvailableBooks").child(bookId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int availableCopies = dataSnapshot.child("AvailableCopies").getValue(Integer.class);

                        // Update the available copies count
                        availableCopies++;

                        // Update the AvailableBooks table
                        dataSnapshot.getRef().child("AvailableCopies").setValue(availableCopies);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Handle error
                    }
                });
    }
}
