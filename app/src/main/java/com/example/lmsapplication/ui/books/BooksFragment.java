package com.example.lmsapplication.ui.books;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lmsapplication.FirebaseManager;
import com.example.lmsapplication.LoginAndRegster.LoginActivity;
import com.example.lmsapplication.R;
import com.example.lmsapplication.databinding.FragmentBooksBinding;

public class BooksFragment extends Fragment {

    private BooksViewModel mViewModel;
    private FragmentBooksBinding binding;

    public static BooksFragment newInstance() {
        return new BooksFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        BooksViewModel booksViewModel = new ViewModelProvider(this).get(BooksViewModel.class);
        binding = FragmentBooksBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //final TextView textView = binding.textBooks;
        //booksViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        // Get references to the buttons
        TextView buttonTile1 = binding.buttonTile1;
        TextView buttonTile2 = binding.buttonTile2;
        TextView buttonTile3 = binding.buttonTile3;

        //Check the user isAdmin
        FirebaseManager firebaseManager = FirebaseManager.getInstance();

        firebaseManager.isAdminUser().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                boolean isAdminUser = task.getResult();
                if (isAdminUser) {
                    buttonTile3.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity(),"Admin abilities activated!",Toast.LENGTH_LONG).show();
                } else {
                }
            } else {
                // Error occurred while checking the user's admin status
                Exception exception = task.getException();
                Log.e("Firebase", "Exception occurred", exception);
                Toast.makeText(getActivity(),"Something Went Wrong!",Toast.LENGTH_LONG).show();

            }
        });

        // Set click listeners for the buttons
        buttonTile1.setOnClickListener(v -> {
            // Start a new activity or perform an action for Tile 1
            Intent intent = new Intent(getActivity(), BOOK_SEARCH.class);
            startActivity(intent);
        });

        buttonTile2.setOnClickListener(v -> {
            // Start a new activity or perform an action for Tile 2
            Intent intent = new Intent(getActivity(), STAFF_BOOK_RETURN_UPDATE.class );
            startActivity(intent);
        });

        buttonTile3.setOnClickListener(v -> {
            // Start a new activity or perform an action for Tile 3
            Intent intent = new Intent(getActivity(), ADD_BOOK.class);
            startActivity(intent);
        });

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(BooksViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
