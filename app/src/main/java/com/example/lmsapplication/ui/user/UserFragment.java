package com.example.lmsapplication.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.lmsapplication.FirebaseManager;
import com.example.lmsapplication.LoginAndRegster.LoginActivity;
import com.example.lmsapplication.R;
import com.example.lmsapplication.ui.home.HomeFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class UserFragment extends Fragment {

    private TextView textViewWelcome, textViewFullName, textViewEmail, textViewDoB, textViewGender, textViewMobile, textViewAddress;
    private ProgressBar progressbar;
    private String fullName, email, doB, gender, mobile, nic, address;
    private ImageView imageView;
    private FirebaseAuth authProfile;

    public UserFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_user, container, false);

        textViewWelcome = rootView.findViewById(R.id.textView_show_welcome);
        textViewFullName = rootView.findViewById(R.id.textView_show_full_name);
        textViewEmail = rootView.findViewById(R.id.textView_show_email);
        textViewDoB = rootView.findViewById(R.id.textView_show_dob);
        textViewGender = rootView.findViewById(R.id.textView_show_gender);
        textViewMobile = rootView.findViewById(R.id.textView_show_mobile);
        textViewAddress = rootView.findViewById(R.id.textView_show_address);
        progressbar = rootView.findViewById(R.id.progressBar);

        FirebaseManager firebaseManager = FirebaseManager.getInstance();
        FirebaseUser firebaseUser = firebaseManager.getCurrentUser();

        if (firebaseUser == null) {
            Toast.makeText(getContext(), "Something went wrong! User's details are not available at the moment", Toast.LENGTH_LONG).show();
        } else {
            progressbar.setVisibility(View.VISIBLE);

            getCurrentUserCategory(new HomeFragment.CategoryCallback() {
                @Override
                public void onCategoryReceived(String category) {
                    // Handle the received category value here
                    // Example: Log the category
                    showUserProfile(firebaseUser,category);

                }
            });

        }

        return rootView;
    }

    private void getCurrentUserCategory(final HomeFragment.CategoryCallback callback) {
        FirebaseManager firebaseManager = FirebaseManager.getInstance();

        firebaseManager.isAdminUser().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                boolean isAdminUser = task.getResult();
                if (isAdminUser) {
                    callback.onCategoryReceived("Admin");
                } else {
                    firebaseManager.isStaffUser().addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            boolean isStaff = task1.getResult();
                            if (isStaff) {
                                callback.onCategoryReceived("Staff");
                            } else {
                                callback.onCategoryReceived("User");
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
    interface CategoryCallback {
        void onCategoryReceived(String category);
    }



    private void showUserProfile(FirebaseUser firebaseUser,String currentUser) {
        String userID = firebaseUser.getUid();
        FirebaseManager firebaseManager = FirebaseManager.getInstance();

        DatabaseReference referenceProfile = firebaseManager.getDataRef(currentUser);
        referenceProfile.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (userID != null) {
                    fullName = snapshot.child("name").getValue(String.class);
                    email = snapshot.child("email").getValue(String.class);
                    doB = snapshot.child("birthday").getValue(String.class);
                    gender = snapshot.child("gender").getValue(String.class);
                    mobile = snapshot.child("mobNum").getValue(String.class);
                    nic = snapshot.child("nic").getValue(String.class);
                    address = snapshot.child("address").getValue(String.class);

                    textViewWelcome.setText("Welcome, " + fullName + "!"+"("+currentUser+")");
                    textViewFullName.setText(fullName);
                    textViewEmail.setText(email);
                    textViewDoB.setText(doB);
                    textViewGender.setText(gender);
                    textViewMobile.setText(mobile);
                    textViewAddress.setText(address);
                }
                progressbar.setVisibility(View.GONE);

                Button editUser = getView().findViewById(R.id.button4);
                editUser.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(getActivity(), UpdateProfileActivity.class);
                        intent.putExtra("currentUser", currentUser); // Pass the login status as a boolean value
                       startActivity(intent);
                       getActivity().finish();
                    }
                });

                Button signOut = getView().findViewById(R.id.button5);
                signOut.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        firebaseManager.logoutUser();

                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Something went wrong!", Toast.LENGTH_LONG).show();
                progressbar.setVisibility(View.GONE);
            }
        });
    }
}
