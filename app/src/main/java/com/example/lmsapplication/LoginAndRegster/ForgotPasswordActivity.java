package com.example.lmsapplication.LoginAndRegster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lmsapplication.FirebaseManager;
import com.example.lmsapplication.MainActivity;
import com.example.lmsapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    Button resetBt;
    EditText email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        // Add the following code to enable the back button
        /*ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }*/

        email = findViewById(R.id.emailLogin);
        //ProgressBar progressBar = findViewById(R.id.progressBarReset);
        resetBt = findViewById(R.id.resetPwBt);
        resetBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txtEmail= email.getText().toString();
                if(TextUtils.isEmpty(txtEmail)){
                    Toast.makeText(ForgotPasswordActivity.this,"Please Enter Your Email",Toast.LENGTH_LONG).show();
                    email.setError("Email is Required");
                    email.requestFocus();
                }else {

                    resetPassword(txtEmail);
                }
            }
        });


    }

    private void resetPassword(String email) {
        FirebaseManager firebaseManager = FirebaseManager.getInstance();
        firebaseManager.getAuth().sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ForgotPasswordActivity.this,"Please Check yor inbox for password reset liink!",Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(ForgotPasswordActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }else {

                    Toast.makeText(ForgotPasswordActivity.this,"Something went wrong!",Toast.LENGTH_LONG).show();

                }

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // Navigate back to the previous activity
        // You can replace MainActivity.class with the desired activity class
        Intent intent = new Intent(ForgotPasswordActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}