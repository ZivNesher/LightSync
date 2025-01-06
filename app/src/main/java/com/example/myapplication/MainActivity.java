package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Retrieve user data from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String loggedInEmail = sharedPreferences.getString("loggedInEmail", null);

        if (loggedInEmail == null) {
            // If no user is logged in, navigate back to LoginActivity
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        // Initialize TableLayout and Plus Button
        TableLayout mainTable = findViewById(R.id.main_table);
        ImageButton plusButton = findViewById(R.id.plus_button);

        // Initialize PopupHandler and setup the Plus Button
        PopupHandler popupHandler = new PopupHandler(this, mainTable);
        popupHandler.setupPlusButton(plusButton);

        // Initialize Profile Button
        ImageButton profileButton = findViewById(R.id.profile_button);
        profileButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        });
    }
}
