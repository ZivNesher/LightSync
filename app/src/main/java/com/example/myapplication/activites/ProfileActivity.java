package com.example.myapplication.activites;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.handlers.UpdateHandler;
import com.example.myapplication.data.RoleEnum;
import com.example.myapplication.models.UserBoundary;

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
        EditText roleInput = findViewById(R.id.profile_role); // Role field
        Button updateButton = findViewById(R.id.update_profile_button);
        Button backButton = findViewById(R.id.back_button);
        Button logoutButton = findViewById(R.id.logout_button);

        // Retrieve user data from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String loggedInEmail = sharedPreferences.getString("loggedInEmail", null);
        String loggedInUsername = sharedPreferences.getString("loggedInUsername", "");
        String loggedInAvatar = sharedPreferences.getString("loggedInAvatar", "");
        String loggedInRole = sharedPreferences.getString("loggedInRole", RoleEnum.END_USER.name()); // Default role

        // Pre-fill the fields
        emailInput.setText(loggedInEmail != null ? loggedInEmail : "");
        usernameInput.setText(loggedInUsername);
        avatarInput.setText(loggedInAvatar);
        roleInput.setText(loggedInRole);

        // Make email non-editable
        emailInput.setFocusable(false);
        emailInput.setClickable(false);

        // Handle Update button click
        updateButton.setOnClickListener(v -> {
            String updatedUsername = usernameInput.getText().toString().trim();
            String updatedAvatar = avatarInput.getText().toString().trim();
            String updatedRoleString = roleInput.getText().toString().trim();

            RoleEnum updatedRole;
            try {
                updatedRole = RoleEnum.valueOf(updatedRoleString.toUpperCase());
            } catch (IllegalArgumentException e) {
                Toast.makeText(this, "Invalid role. Use ADMIN, END_USER, or OPERATOR.", Toast.LENGTH_SHORT).show();
                return;
            }

            updateHandler.updateUser(loggedInEmail, updatedUsername, updatedAvatar, updatedRole);
        });


        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        logoutButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}
