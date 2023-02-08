package com.chandankumar.sellmystuff.util;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import com.chandankumar.sellmystuff.model.User;

public class SharedPref {


    private static SharedPref mSharedPref;
    private static SharedPreferences sharedPreferences;

    private SharedPref(){}

    public static SharedPref getInstance(Context context){
        if(mSharedPref == null){
            mSharedPref = new SharedPref();
            sharedPreferences = context.getSharedPreferences("LOGIN_DATA",MODE_PRIVATE);
        }
        return mSharedPref;
    }



    public void setUser(User user){

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name",user.getName());
        editor.putString("email", user.getEmail());
        editor.putString("phone", user.getPhone());
        editor.commit();

    }

    public void clearUser(){

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
    }

    public User getUser(){

        String name = sharedPreferences.getString("name", "");
        String email = sharedPreferences.getString("email", "");
        String phone = sharedPreferences.getString("phone", "");

        return new User(name, email, phone);
    }


}
