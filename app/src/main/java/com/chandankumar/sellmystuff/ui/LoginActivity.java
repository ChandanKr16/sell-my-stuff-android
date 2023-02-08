package com.chandankumar.sellmystuff.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.chandankumar.sellmystuff.R;
import com.chandankumar.sellmystuff.model.User;
import com.chandankumar.sellmystuff.util.SharedPref;
import com.chandankumar.sellmystuff.util.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity {

    private EditText mEmail;
    private EditText mPassword;
    private Button mSignIn;
    private Button mSignUp;
    private ProgressDialog mProgressDialog;
    private TextView mForgotPassword;

    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initComponents();

        mFirebaseAuth = FirebaseAuth.getInstance();


        mSignIn.setOnClickListener(view -> {
           String email = mEmail.getText().toString().trim().toLowerCase();
           String password = mPassword.getText().toString().trim();

           if(email.isEmpty() || !Utils.isValidEmail(email)){
               mEmail.setError(getString(R.string.email_error));
               return;
           }

           if(password.isEmpty() || password.length()  < 6){
               mPassword.setError(getString(R.string.password_error));
               return;
           }

           signIn(email, password);

        });

        mSignUp.setOnClickListener(view ->{
            startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
        });

        mForgotPassword.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), ForgotPasswordActivity.class));
        });

    }


    private void initComponents(){
        mEmail = findViewById(R.id.emailEditText);
        mPassword = findViewById(R.id.passwordEditText);
        mSignIn = findViewById(R.id.signInButton);
        mSignUp = findViewById(R.id.signUpButton);
        mForgotPassword = findViewById(R.id.forgotPasswordTextView);
        mProgressDialog = new ProgressDialog(this);
    }

    private void signIn(String email, String password){

        mProgressDialog.setMessage(getString(R.string.signin_dialog_message));
        mProgressDialog.show();

        mFirebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                if(!mFirebaseAuth.getCurrentUser().isEmailVerified()){
                    Utils.showLongMessage(getApplicationContext(), getString(R.string.is_email_verified));
                    mFirebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(task1 -> {
                        if(task1.isSuccessful()){
                            Utils.showLongMessage(getApplicationContext(), getString(R.string.email_sent_message) + email);
                        }
                        mFirebaseAuth.signOut();
                    });

                }else{

                    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    FirebaseDatabase.getInstance().getReference().child("users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            User user = snapshot.getValue(User.class);

                            SharedPref.getInstance(getApplicationContext()).setUser(user);

                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Utils.showLongMessage(getApplicationContext(), "Error: " + error.getMessage());
                        }
                    });

                }
                mProgressDialog.dismiss();
            }
            else {
                mProgressDialog.dismiss();
                Utils.showLongMessage(getApplicationContext(), task.getException().getMessage());
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(FirebaseAuth.getInstance().getCurrentUser() != null && FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}