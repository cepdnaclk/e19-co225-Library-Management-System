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

import com.example.lmsapplication.LoginAndRegster.LoginActivity;
import com.example.lmsapplication.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Initialize the ViewModel
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        // Customize UI based on user category (admin, staff, user)
        String userCategory = getCurrentUserCategory();
        customizeUI(userCategory);

        // Handle user details button click event for admins
        Button userDetailsButton = binding.userDetailsButton;
        userDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open user details activity
                Intent intent = new Intent(getActivity(), UserDetails.class);
                startActivity(intent);
            }
        });

        // Handle staff details button click event for admins
        Button staffDetailsButton = binding.staffDetailsButton;
        staffDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open staff details activity
                Intent intent = new Intent(getActivity(), staffDetailsActivity.class);
                startActivity(intent);
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private String getCurrentUserCategory() {
        // Implement your logic to retrieve the current user's category
        // Return the user category as a string (e.g., "admin", "staff", "user")
        // This is just a placeholder method, replace it with your own implementation
        return "admin";
    }

    private void customizeUI(String userCategory) {
        LinearLayout userDetailsLayout = binding.userDetailsLayout;
        Button userDetailsButton = binding.userDetailsButton;
        Button staffDetailsButton = binding.staffDetailsButton;

        // Customize UI based on user category
        if (userCategory.equals("admin")) {
            userDetailsLayout.setVisibility(View.VISIBLE);
            userDetailsButton.setVisibility(View.VISIBLE);
            staffDetailsButton.setVisibility(View.VISIBLE);
        } else {
            userDetailsLayout.setVisibility(View.GONE);
            userDetailsButton.setVisibility(View.GONE);
            staffDetailsButton.setVisibility(View.GONE);
        }
    }
}
