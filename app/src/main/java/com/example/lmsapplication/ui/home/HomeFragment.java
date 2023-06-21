package com.example.lmsapplication.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

//import com.example.lmsapplication.AcceptReturnsActivity;
import com.example.lmsapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize the ViewModel
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        // Customize UI based on user category (admin, staff, user)
        String userCategory = getCurrentUserCategory();
        customizeUI(root, userCategory);

        return root;
    }

    private String getCurrentUserCategory() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            // Implement your logic to retrieve the current user's category
            // Return the user category as a string (e.g., "admin", "staff", "user")
            // This is just a placeholder method, replace it with your own implementation
            return "admin";
        } else {
            return "user";
        }
    }

    private void customizeUI(View root, String userCategory) {
        LinearLayout userDetailsLayout = root.findViewById(R.id.userDetailsLayout);
        Button userDetailsButton = root.findViewById(R.id.userDetailsButton);
        Button staffDetailsButton = root.findViewById(R.id.staffDetailsButton);
        Button acceptBookRequestButton = root.findViewById(R.id.acceptBookRequestButton);
        Button acceptBookReturnsButton = root.findViewById(R.id.acceptBookReturnsButton);

        // Customize UI based on user category
        if (userCategory.equals("admin") || userCategory.equals("staff")) {
            userDetailsLayout.setVisibility(View.VISIBLE);
            userDetailsButton.setVisibility(View.VISIBLE);
            staffDetailsButton.setVisibility(View.VISIBLE);

            if (userCategory.equals("admin")) {
                acceptBookRequestButton.setVisibility(View.VISIBLE);
                acceptBookReturnsButton.setVisibility(View.VISIBLE);
            } else {
                acceptBookRequestButton.setVisibility(View.GONE);
                acceptBookReturnsButton.setVisibility(View.GONE);
            }
        } else {
            userDetailsLayout.setVisibility(View.GONE);
            userDetailsButton.setVisibility(View.GONE);
            staffDetailsButton.setVisibility(View.GONE);
            acceptBookRequestButton.setVisibility(View.GONE);
            acceptBookReturnsButton.setVisibility(View.GONE);
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
    }
}
