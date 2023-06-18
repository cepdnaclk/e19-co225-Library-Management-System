package com.example.lmsapplication;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class FirebaseManager {

    private static FirebaseManager instance;
    private FirebaseAuth auth;
    private FirebaseDatabase database;

    private FirebaseManager() {
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

    }

    public void logoutUser() {
        auth.signOut();
    }

    public FirebaseUser getCurrentUser() {
        return auth.getCurrentUser();
    }

    public static synchronized FirebaseManager getInstance() {
        if (instance == null) {
            instance = new FirebaseManager();
        }
        return instance;
    }
    public FirebaseAuth getAuth() {
        return auth;
    }
    public DatabaseReference getDataRef(String path) {
        return database.getReference(path);
    }

    public Task<Boolean> isAdminUser() {
        FirebaseUser currentUser = getCurrentUser();
        DatabaseReference adminRef = getDataRef("Admin");

        if (currentUser != null) {
            String email = currentUser.getEmail();

            if (email != null) {
                return adminRef.get().continueWith(task -> {
                    if (task.isSuccessful()) {
                        DataSnapshot dataSnapshot = task.getResult();
                        boolean isAdmin = false;

                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                            DataSnapshot emailSnapshot = childSnapshot.child("email");
                            String userEmail = emailSnapshot.getValue(String.class);

                            if (userEmail != null && userEmail.equals(email)) {
                                isAdmin = true;
                                break;
                            }
                        }

                        return isAdmin;
                    } else {
                        throw task.getException();
                    }
                });
            }
        }

        // Resolve the task with a false value if user or email is null
        TaskCompletionSource<Boolean> completionSource = new TaskCompletionSource<>();
        completionSource.setResult(false);
        return completionSource.getTask();
    }

    public Task<Boolean> isStaffUser() {
        FirebaseUser currentUser = getCurrentUser();
        DatabaseReference adminRef = getDataRef("Admin");

        if (currentUser != null) {
            String email = currentUser.getEmail();

            if (email != null) {
                return adminRef.get().continueWith(task -> {
                    if (task.isSuccessful()) {
                        DataSnapshot dataSnapshot = task.getResult();
                        boolean isStaff = false;

                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                            DataSnapshot emailSnapshot = childSnapshot.child("email");
                            String userEmail = emailSnapshot.getValue(String.class);

                            if (userEmail != null && userEmail.equals(email)) {
                                isStaff = true;
                                break;
                            }
                        }

                        return isStaff;
                    } else {
                        throw task.getException();
                    }
                });
            }
        }

        // Resolve the task with a false value if user or email is null
        TaskCompletionSource<Boolean> completionSource = new TaskCompletionSource<>();
        completionSource.setResult(false);
        return completionSource.getTask();

        //How to call
        /*FirebaseManager firebaseManager = FirebaseManager.getInstance();

        firebaseManager.isAdminUser().addOnCompleteListener(task -> {
        if (task.isSuccessful()) {
        boolean isAdminUser = task.getResult();
        if (isAdminUser) {
            // User is an admin
        } else {
            // User is not an admin
        }
    } else {
        // Error occurred while checking the user's admin status
        Exception exception = task.getException();
        // Handle the exception
    }
});
*/
    }

}
