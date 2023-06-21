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

public class AcceptRequestActivity extends AppCompatActivity {

    private List<String> requestedBooksList;
    private List<String> bookIdsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_request);

        requestedBooksList = new ArrayList<>();
        bookIdsList = new ArrayList<>();

        ListView requestedBooksListView = findViewById(R.id.requestedBooksListView);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, requestedBooksList);
        requestedBooksListView.setAdapter(adapter);

        // Retrieve requested books from Firebase and populate the list
        FirebaseDatabase.getInstance().getReference("RequestedBooks")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        requestedBooksList.clear();
                        bookIdsList.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String bookId = snapshot.child("bookID").getValue(String.class);
                            String userId = snapshot.child("userID").getValue(String.class);
                            String bookDetails = "BookId: " + bookId + ", UserId: " + userId;
                            requestedBooksList.add(bookDetails);
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
        requestedBooksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AcceptRequestActivity.this);
                builder.setTitle("Accept or Decline Request")
                        .setMessage("What would you like to do?")
                        .setPositiveButton("Accept Request", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                acceptRequest(position);
                            }
                        })
                        .setNegativeButton("Decline Request", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                declineRequest(position);
                            }
                        });
                builder.create().show();
            }
        });
    }

    private void acceptRequest(int position) {
        String bookId = bookIdsList.get(position);

        // Move the book details to the BorrowedBooks table and update AvailableBooks
        FirebaseDatabase.getInstance().getReference("RequestedBooks").child(bookId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Retrieve the book details
                        String bookId = dataSnapshot.child("bookID").getValue(String.class);
                        String userId = dataSnapshot.child("userID").getValue(String.class);

                        // Save the book details in the BorrowedBooks table
                        FirebaseDatabase.getInstance().getReference("BorrowedBooks").push().setValue(dataSnapshot.getValue());

                        // Update the AvailableBooks table
                        updateAvailableBooks(bookId);

                        // Remove the book from the RequestedBooks table
                        dataSnapshot.getRef().removeValue();

                        Toast.makeText(AcceptRequestActivity.this, "Accepted Request for BookId: " + bookId, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Handle error
                    }
                });
    }

    private void declineRequest(int position) {
        String bookId = bookIdsList.get(position);

        // Remove the book details from the RequestedBooks table
        FirebaseDatabase.getInstance().getReference("RequestedBooks").child(bookId)
                .removeValue(new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (databaseError == null) {
                            Toast.makeText(AcceptRequestActivity.this, "Declined Request for BookId: " + bookId, Toast.LENGTH_SHORT).show();
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
                        availableCopies--;

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
