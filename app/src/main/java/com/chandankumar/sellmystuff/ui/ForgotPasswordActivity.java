package com.chandankumar.sellmystuff.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.chandankumar.sellmystuff.R;
import com.chandankumar.sellmystuff.util.Utils;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText mEmailEditText;
    private Button mForgotPasswordButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        initComponents();

        mForgotPasswordButton.setOnClickListener( view ->{
            String email = mEmailEditText.getText().toString().trim().toLowerCase();

            if(!Utils.isValidEmail(email)){
                mEmailEditText.setError("Invalid e-mail");
                return;
            }

            sendPasswordResetLink(email);


        });

    }

    public void initComponents(){
        mEmailEditText = findViewById(R.id.emailEditText);
        mForgotPasswordButton = findViewById(R.id.forgotPasswordButton);
    }

    public void sendPasswordResetLink(String email){
        FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener(task -> {

            if(task.isSuccessful()){
                Utils.showLongMessage(getApplicationContext(), "Password reset link has been sent on " + email);
                finish();
                return;
            }

            Utils.showLongMessage(getApplicationContext(), task.getException().getMessage());

        });
    }
}