package com.chandankumar.sellmystuff.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.chandankumar.sellmystuff.R;
import com.chandankumar.sellmystuff.model.User;
import com.chandankumar.sellmystuff.util.Utils;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private EditText mNameEditText;
    private EditText mEmailEditText;
    private EditText mPhoneEditText;
    private EditText mPasswordEditText;
    private EditText mConfirmPasswordEditText;
    private Button mRegisterButton;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initComponents();

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        mRegisterButton.setOnClickListener(view -> {

            String name = mNameEditText.getText().toString().trim();
            String email = mEmailEditText.getText().toString().toLowerCase().trim();
            String phone = mPhoneEditText.getText().toString().trim();
            String password = mPasswordEditText.getText().toString().trim();
            String confirmPassword = mConfirmPasswordEditText.getText().toString().trim();

            if(name.isEmpty() || name.length() < 3){
                mNameEditText.setError(getString(R.string.name_error));
                return;
            }

            if(!Utils.isValidEmail(email)){
                mEmailEditText.setError(getString(R.string.email_error));
                return;
            }

            if(!Utils.isPhoneValid(phone)){
                mPhoneEditText.setError(getString(R.string.phone_error));
                return;
            }


            if(password.isEmpty() || password.length() < 6){
                mPasswordEditText.setError(getString(R.string.password_error));
                return;
            }


            if(!password.equals(confirmPassword)){
                mConfirmPasswordEditText.setError(getString(R.string.confirm_password_error));
                return;
            }

            signUp(name, email, phone, confirmPassword);

        });


    }


    public void initComponents(){
        mNameEditText = findViewById(R.id.nameEditText);
        mEmailEditText = findViewById(R.id.emailEditText);
        mPhoneEditText = findViewById(R.id.phoneEditText);
        mPasswordEditText = findViewById(R.id.passwordEditText);
        mConfirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        mRegisterButton = findViewById(R.id.registerButton);

        mProgressDialog = new ProgressDialog(this);
    }


    public void signUp(String name, String email, String phone, String password){

        showProgressDialog();

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {

            if(task.isSuccessful()){
                String uid = mAuth.getCurrentUser().getUid();

                DatabaseReference databaseReference = mFirebaseDatabase.getReference("users");
                databaseReference.child(uid).setValue(new User(name, email, phone)).addOnCompleteListener(task1 -> {
                    if(task1.isSuccessful()){
                        mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(task2 -> {
                            mProgressDialog.dismiss();
                            Utils.showLongMessage(getApplicationContext(), getString(R.string.account_created_success));
                            finish();
                        });

                    }
                });

            }
            else {
                mProgressDialog.dismiss();
                Utils.showLongMessage(getApplicationContext(), task.getException().getMessage());
            }

        });
    }

    private void showProgressDialog(){
        mProgressDialog.setMessage(getString(R.string.creating_account_dialog_message));
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }


}