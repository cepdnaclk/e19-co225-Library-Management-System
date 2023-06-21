package com.example.lmsapplication.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> text;
    private MutableLiveData<Integer> borrowedBooksCountLiveData;

    public HomeViewModel() {
        text = new MutableLiveData<>();
        text.setValue("This is home fragment");

        borrowedBooksCountLiveData = new MutableLiveData<>();
    }

    public LiveData<String> getText() {
        return text;
    }

    public LiveData<Integer> getBorrowedBooksCountLiveData() {
        return borrowedBooksCountLiveData;
    }

    public void fetchBorrowedBooksCount(String userId) {
        // Implement your logic here to fetch the number of borrowed books for the user with the given userId
        // For now, let's assume a sample count
        int borrowedBooksCount = 10;

        borrowedBooksCountLiveData.setValue(borrowedBooksCount);
    }
}
