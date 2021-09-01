package com.example.bouncingball;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class LoginScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
    }

    public void logins(View v){

        Intent menu = new Intent(LoginScreen.this,MainActivity.class);
        startActivity(menu);
    }
    public void register(View v){
        Intent menu = new Intent(LoginScreen.this,MainActivity.class);
        startActivity(menu);
    }
}