package com.example.lmsapplication.LoginAndRegster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.example.lmsapplication.FirebaseManager;
import com.example.lmsapplication.MainActivity;
import com.example.lmsapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.regex.Pattern;
public class RegisterActivity extends AppCompatActivity {

    DatePickerDialog picker;

    EditText name , email , nic , address , birthday , pwd , confirmPwd,mobileNum ;
    Button register;
    RadioGroup genderGroup;
    RadioButton genderSelect;

    TextView loginBt;

    ProgressBar progressBarSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Get Birthdate using selecting calander
        birthday = (EditText) findViewById(R.id.birthdaySignup);

        birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(RegisterActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                birthday.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });
        //Connect xml ids
        name = (EditText) findViewById(R.id.nameSignup);
        email = (EditText) findViewById(R.id.emailSignup);
        nic = (EditText) findViewById(R.id.nicSignup);
        address = (EditText) findViewById(R.id.addressSignup);
        birthday = (EditText) findViewById(R.id.birthdaySignup);
        pwd = (EditText) findViewById(R.id.passwordSignup);
        confirmPwd = (EditText) findViewById(R.id.passwordCnfSignup);
        mobileNum = (EditText)findViewById(R.id.mobNumSignup) ;
        progressBarSignUp =  (ProgressBar)findViewById(R.id.progressReg);
        loginBt = (TextView)findViewById(R.id.loginRegPage);


        //Radio button for gender
        genderGroup = (RadioGroup) findViewById(R.id.genderRadioGroup);
        genderGroup.clearCheck();

        loginBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        //Register button for sava data into database
        register = (Button) findViewById(R.id.registerFBt);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get gender from radio group
                int selectedId=genderGroup.getCheckedRadioButtonId();
                genderSelect=(RadioButton)findViewById(selectedId);

                //obtain the entered data
                String txtfullName = name.getText().toString();
                String txtEmail = email.getText().toString();
                String txtNIC = nic.getText().toString();
                String txtAddress = address.getText().toString();
                String txtBirthday = birthday.getText().toString();
                String txtPwd = pwd.getText().toString();
                String txtConfirmPwd = confirmPwd.getText().toString();
                String txtmobileNum = mobileNum.getText().toString();
                String txtGender;
                String userType = "user";


                /*txtNIC ="235";
                txtAddress="sga";
                txtBirthday="wrgq";
                txtmobileNum="wahr";
                txtGender="hse";*/

                //Check Entered data
                if(TextUtils.isEmpty(txtfullName)){
                    Toast.makeText(RegisterActivity.this,"Please Enter Full Name ",Toast.LENGTH_LONG).show();
                    name.setError("Full Name is Required");
                    name.requestFocus();
                }else if(TextUtils.isEmpty(txtEmail)){
                    Toast.makeText(RegisterActivity.this,"Please Enter Email ",Toast.LENGTH_LONG).show();
                    email.setError("Email is Required");
                    email.requestFocus();
                }else if(!Patterns.EMAIL_ADDRESS.matcher(txtEmail).matches()){
                    Toast.makeText(RegisterActivity.this,"Please Re-Enter Enter Email ",Toast.LENGTH_LONG).show();
                    email.setError("Valid Email is Required");
                    email.requestFocus();
                }else if(TextUtils.isEmpty(txtNIC)){
                    Toast.makeText(RegisterActivity.this,"Please Enter NIC ",Toast.LENGTH_LONG).show();
                    nic.setError("NIC is Required");
                    nic.requestFocus();
                }else if(TextUtils.isEmpty(txtAddress)){
                    Toast.makeText(RegisterActivity.this,"Please Enter Address ",Toast.LENGTH_LONG).show();
                    address.setError("Address is Required");
                    address.requestFocus();
                }else if(TextUtils.isEmpty(txtBirthday)){
                    Toast.makeText(RegisterActivity.this,"Please Enter Birth day ",Toast.LENGTH_LONG).show();
                    birthday.setError("Birth day is Required");
                    birthday.requestFocus();
                }else if(TextUtils.isEmpty(txtPwd)){
                    Toast.makeText(RegisterActivity.this,"Please Enter Password ",Toast.LENGTH_LONG).show();
                    pwd.setError("Password is Required");
                    pwd.requestFocus();
                }else if(txtPwd.length()<8){
                    Toast.makeText(RegisterActivity.this,"Password must contain at least 8 characters",Toast.LENGTH_LONG).show();
                    pwd.setError("Password must contain at least 8 characters");
                    pwd.requestFocus();
                }else if(TextUtils.isEmpty(txtConfirmPwd)){
                    Toast.makeText(RegisterActivity.this,"Please Confirm Your Password ",Toast.LENGTH_LONG).show();
                    confirmPwd.setError("Password Confirmation is Required");
                    confirmPwd.requestFocus();
                }else if(!txtPwd.equals(txtConfirmPwd)){
                    Toast.makeText(RegisterActivity.this,"Please Enter Same Password ",Toast.LENGTH_LONG).show();
                    pwd.setError("Password Confirmation is Required");
                    pwd.requestFocus();
                    //Clear the passwords
                    pwd.clearComposingText();
                    confirmPwd.clearComposingText();
                }else if(TextUtils.isEmpty(txtmobileNum)){
                    Toast.makeText(RegisterActivity.this,"Please Enter Mobile Number ",Toast.LENGTH_LONG).show();
                    mobileNum.setError("Mobile Number is Required");
                    mobileNum.requestFocus();

                }else if(txtmobileNum.length()<10){
                    Toast.makeText(RegisterActivity.this,"Please Enter Valid Mobile Number ",Toast.LENGTH_LONG).show();
                    mobileNum.setError("Valid Mobile Number is Required");
                    mobileNum.requestFocus();
                }else {

                    txtGender= genderSelect.getText().toString();
                    progressBarSignUp.setVisibility(View.VISIBLE);
                    registerUser(txtfullName,txtEmail,txtNIC,txtAddress,txtBirthday,txtmobileNum,txtGender,txtPwd);
                }


            }
        });

    }
    //Register User using the credentials given
    private void registerUser(String txtfullName, String txtEmail, String txtNIC, String txtAddress, String txtBirthday, String txtmobileNum, String txtGender, String txtPwd) {
        FirebaseManager firebaseManager = FirebaseManager.getInstance();
        firebaseManager.getAuth().createUserWithEmailAndPassword(txtEmail, txtPwd).addOnCompleteListener(RegisterActivity.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            String userId = firebaseManager.getCurrentUser().getUid();
                            //Store user Detils in the database
                            User newUser = new User(txtfullName, txtEmail, txtNIC, txtAddress, txtBirthday, txtmobileNum, txtGender);

                            firebaseManager.getDataRef("User").child(userId).setValue(newUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(RegisterActivity.this, "User registration successful", Toast.LENGTH_SHORT).show();
                                        // Navigate back to the previous activity
                                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();

                                    } else {
                                        Toast.makeText(RegisterActivity.this, "Failed to store user details in the database", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            FirebaseUser firebaseUser = firebaseManager.getCurrentUser();
                            //Send verification Email
                            firebaseUser.sendEmailVerification();


                        }else {
                            String errorMessage = task.getException().getMessage();
                            Log.e("RegistrationError", errorMessage);
                            Toast.makeText(RegisterActivity.this, "User registration failed Try again!", Toast.LENGTH_SHORT).show();
                            progressBarSignUp.setVisibility(View.GONE);
                        }

                    }
                });

    }
    public class User {
        private String name;
        private String email;
        private String nic;
        private String address;
        private String birthday;
        private String mobNum;
        private String gender;



        public User() {
            // Required empty constructor for Firebase
        }
        public User(String name, String email,String nic,String address,String birthday,String mobNum,String gender) {
            this.name = name;
            this.email = email;
            this.nic =nic;
            this.address=address;
            this.birthday=birthday;
            this.mobNum=mobNum;
            this.gender=gender;
        }


        // Getters and setters (optional)
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String sex) {
            this.gender = sex;
        }

        public String getNic() {
            return nic;
        }

        public void setNic (String idNumber) {
            this.nic = idNumber;
        }

        public String getMobNum() {
            return mobNum;
        }

        public void setMobNum(String mobileNumber) {
            this.mobNum= mobileNumber;
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // Navigate back to the previous activity
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}