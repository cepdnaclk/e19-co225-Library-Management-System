package com.example.lmsapplication.LoginAndRegster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lmsapplication.FirebaseManager;
import com.example.lmsapplication.MainActivity;
import com.example.lmsapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;
import java.util.Date;

public class LoginActivity extends AppCompatActivity {
    private static final String PREF_NAME = "LoginPrefs";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_REMEMBER_ME = "rememberMe";
    private static final String KEY_EXPIRY_TIME = "expiryTime";

    SharedPreferences loginPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);




        EditText inpEmail = (EditText) findViewById(R.id.emailLogin);
        EditText inpPassword = (EditText) findViewById(R.id.pwdLogin);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBarLogin);
        TextView forgotPw = (TextView)findViewById(R.id.forgotpwBt);
        CheckBox rememberBt = (CheckBox)findViewById(R.id.rememberBt);



        loginPrefs = getSharedPreferences(PREF_NAME,MODE_PRIVATE);
        boolean rememberMe = loginPrefs.getBoolean(KEY_REMEMBER_ME, false);
        if (rememberMe) {
            String username = loginPrefs.getString(KEY_USERNAME, "");
            String password = loginPrefs.getString(KEY_PASSWORD, "");
            inpEmail.setText(username);
            inpPassword.setText(password);
            rememberBt.setChecked(true);
        }
        //Go to the forget password page
        forgotPw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
                finish();
            }
        });


        //go to tha register page
        Button registerBt = (Button)findViewById(R.id.registerBt);
        registerBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
        Button loginBt = (Button) findViewById(R.id.loginBt);
        loginBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txtEmail = inpEmail.getText().toString();
                String txtPwd = inpPassword.getText().toString();

                if (TextUtils.isEmpty(txtEmail)){
                    Toast.makeText(LoginActivity.this,"Please Enter Your Email",Toast.LENGTH_LONG).show();
                    inpEmail.setError("Email is Required");
                    inpEmail.requestFocus();

                }else if (TextUtils.isEmpty(txtPwd)){
                    Toast.makeText(LoginActivity.this,"Please Enter Your Password",Toast.LENGTH_LONG).show();
                    inpPassword.setError("Password is Required");
                    inpPassword.requestFocus();
                }else {
                    progressBar.setVisibility(View.VISIBLE);
                    loginUser(txtEmail, txtPwd, new LoginCallback() {
                        @Override
                        public void onLoginSuccess() {
                            Toast.makeText(LoginActivity.this,"You Are Logged In",Toast.LENGTH_LONG).show();

                            if(rememberBt.isChecked()){
                                rememberLogin(txtEmail, txtPwd);
                            }else {
                                clearLoginPrefs();
                            }



                            //Load Main Activity
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("loginStatus", true); // Pass the login status as a boolean value
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onLoginFailure() {
                            Toast.makeText(LoginActivity.this,"Login Failed!",Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                            inpEmail.clearComposingText();
                            inpPassword.clearComposingText();
                            inpEmail.setError("Invalid Login, Please Try Again!");
                            }
                    });
                }
            }
        });

    }

    private void rememberLogin(String txtEmail, String txtPwd) {
        SharedPreferences.Editor editor = loginPrefs.edit();
        editor.putString(KEY_USERNAME, txtEmail);
        editor.putString(KEY_PASSWORD, txtPwd);
        editor.putBoolean(KEY_REMEMBER_ME, true);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1); // Set expiry time to 24 hours
        Date expiryDate = calendar.getTime();
        editor.putLong(KEY_EXPIRY_TIME, expiryDate.getTime());

        editor.apply();
    }

    private void clearLoginPrefs() {
        SharedPreferences.Editor editor = loginPrefs.edit();
        editor.clear();
        editor.apply();
    }

    private void loginUser(String txtEmail, String txtPwd,LoginCallback callback) {
        FirebaseManager firebaseManager = FirebaseManager.getInstance();
        firebaseManager.getAuth().signInWithEmailAndPassword(txtEmail,txtPwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){
                     callback.onLoginSuccess();
                }else {
                     callback.onLoginFailure();
                }

            }
        });
    }
    public interface LoginCallback {
        void onLoginSuccess();
        void onLoginFailure();
    }
}