package com.example.lmsapplication.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

//import com.example.lmsapplication.AcceptReturnsActivity;
import com.example.lmsapplication.FirebaseManager;
import com.example.lmsapplication.LoginAndRegster.ForgotPasswordActivity;
import com.example.lmsapplication.R;
import com.example.lmsapplication.ui.books.BOOK_SEARCH;
import com.example.lmsapplication.ui.books.BookDetails;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize the ViewModel
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        /*final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
*/
        // Customize UI based on user category (admin, staff, user)

        getCurrentUserCategory(new CategoryCallback() {
            @Override
            public void onCategoryReceived(String category) {
                // Handle the received category value here
                // Example: Log the category
                customizeUI(root, category);

            }
        });




        return root;
    }

    private void getCurrentUserCategory(final CategoryCallback callback) {
        FirebaseManager firebaseManager = FirebaseManager.getInstance();

        firebaseManager.isAdminUser().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                boolean isAdminUser = task.getResult();
                if (isAdminUser) {
                    callback.onCategoryReceived("admin");
                } else {
                    firebaseManager.isStaffUser().addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            boolean isStaff = task1.getResult();
                            if (isStaff) {
                                callback.onCategoryReceived("staff");
                            } else {
                                callback.onCategoryReceived("user");
                            }
                        } else {
                            // Error occurred while checking the user's staff status
                            Exception exception = task1.getException();
                            // Handle the exception
                            callback.onCategoryReceived("user");
                        }
                    });
                }
            } else {
                // Error occurred while checking the user's admin status
                Exception exception = task.getException();
                // Handle the exception
            }
        });
    }

    // Define the callback interface
    public interface CategoryCallback {
        void onCategoryReceived(String category);
    }



    private void customizeUI(View root, String userCategory) {
        LinearLayout userDetailsLayout = root.findViewById(R.id.row2);
        LinearLayout transactionLayout = root.findViewById(R.id.row3);

        CardView myBooks = root.findViewById(R.id.borrowedBookCard);
        CardView requestedBooks = root.findViewById(R.id.requestedBookCard);
        CardView userDetailsButton = root.findViewById(R.id.userDetailsButton);
        CardView staffDetailsButton = root.findViewById(R.id.staffDetailsCard);
        CardView acceptBookRequestButton = root.findViewById(R.id.acceptBookRequestCard);
        CardView acceptBookReturnsButton = root.findViewById(R.id.acceptBookReturnsCard);
        CardView staffDetailsCard = root.findViewById((R.id.staffDetailsCard));
        
        //Check the user type
        
        
        // Customize UI based on user category
        if (userCategory.equals("admin")){

            userDetailsLayout.setVisibility(View.VISIBLE);
            staffDetailsCard.setVisibility(View.VISIBLE);
            transactionLayout.setVisibility(View.VISIBLE);
        }else if ( userCategory.equals("staff")) {
            userDetailsLayout.setVisibility(View.VISIBLE);
            transactionLayout.setVisibility(View.VISIBLE);
            staffDetailsCard.setVisibility(View.GONE);

        }else {
            userDetailsLayout.setVisibility(View.GONE);
            transactionLayout.setVisibility(View.GONE);
        }

        // Handle user details button click event
        userDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open user details activity
                Intent intent = new Intent(getActivity(), UserDetails.class);
                startActivity(intent);
            }
        });

        // Handle staff details button click event
        staffDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open staff details activity
                Intent intent = new Intent(getActivity(), staffDetailsActivity.class);
                startActivity(intent);
            }
        });

        // Handle accept book request button click event
        acceptBookRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open accept book request activity
                startActivity(new Intent(getActivity(), AcceptRequestActivity.class));
            }
        });

        // Handle accept book returns button click event
        acceptBookReturnsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open accept book returns activity
                startActivity(new Intent(getActivity(), AcceptReturnsActivity.class));
            }
        });

        myBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), BookDetails.class));
            }
        });
        requestedBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), BOOK_SEARCH.class));
            }
        });
    }
}
