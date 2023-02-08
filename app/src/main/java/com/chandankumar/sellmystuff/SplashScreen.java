package com.chandankumar.sellmystuff;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.chandankumar.sellmystuff.ui.LoginActivity;
import com.chandankumar.sellmystuff.ui.MainActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.io.File;


public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);


        new Handler().postDelayed(() -> {
            if(FirebaseAuth.getInstance().getCurrentUser() != null && FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()){
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
            else {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        }, 3000);



    }

}