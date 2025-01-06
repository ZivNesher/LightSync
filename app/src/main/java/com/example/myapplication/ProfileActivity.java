package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    private UpdateHandler updateHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_screen);

        // Initialize UpdateHandler
        updateHandler = new UpdateHandler(this);

        // Initialize UI components
        EditText usernameInput = findViewById(R.id.profile_username);
        EditText emailInput = findViewById(R.id.profile_email);
        EditText avatarInput = findViewById(R.id.profile_avatar);
        Button updateButton = findViewById(R.id.update_profile_button);
        Button backButton = findViewById(R.id.back_button);

        // Retrieve user data from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String loggedInEmail = sharedPreferences.getString("loggedInEmail", null);
        String loggedInUsername = sharedPreferences.getString("loggedInUsername", "");
        String loggedInAvatar = sharedPreferences.getString("loggedInAvatar", "");

        // Pre-fill the fields
        emailInput.setText(loggedInEmail != null ? loggedInEmail : "");
        usernameInput.setText(loggedInUsername);
        avatarInput.setText(loggedInAvatar);

        // Handle Update button click
        updateButton.setOnClickListener(v -> {
            String updatedEmail = emailInput.getText().toString().trim();
            String updatedUsername = usernameInput.getText().toString().trim();
            String updatedAvatar = avatarInput.getText().toString().trim();

            // Validate email
            if (loggedInEmail != null && !updatedEmail.equalsIgnoreCase(loggedInEmail)) {
                Toast.makeText(this, "Email can't be changed!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Use UpdateHandler to update user details
            updateHandler.updateUser(updatedEmail, updatedUsername, updatedAvatar);
        });

        // Handle Back button click
        backButton.setOnClickListener(v -> {
            // Navigate back to the MainActivity
            Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // Clear any previous instance of MainActivity
            startActivity(intent);
            finish(); // Close ProfileActivity
        });
    }
}
