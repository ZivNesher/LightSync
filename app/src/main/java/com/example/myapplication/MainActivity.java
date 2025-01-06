package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private LoginHandler loginHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the initial loading screen layout
        setContentView(R.layout.loading_screen);

        // Delay for 3 seconds before switching to the login screen
        new Handler().postDelayed(() -> {
            // Set the login screen layout
            setContentView(R.layout.login_screen);

            // Initialize the login screen logic
            loginHandler = new LoginHandler(this);
            loginHandler.setupLoginScreen();
        }, 3000); // 3 seconds delay
    }
}
