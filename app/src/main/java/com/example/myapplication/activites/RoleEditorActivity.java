package com.example.myapplication.activites;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.api.ApiClient;
import com.example.myapplication.api.ApiService;
import com.example.myapplication.data.RoleEnum;
import com.example.myapplication.models.UserBoundary;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RoleEditorActivity extends AppCompatActivity {

    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role_editor);

        // Initialize Retrofit service
        apiService = ApiClient.getRetrofitInstance().create(ApiService.class);

        // UI components
        EditText emailInput = findViewById(R.id.email_input);
        EditText roleInput = findViewById(R.id.role_input);
        Button updateRoleButton = findViewById(R.id.update_role_button);
        Button backButton = findViewById(R.id.back_button);

        // Update Role Button Listener
        updateRoleButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            String role = roleInput.getText().toString().trim();

            if (email.isEmpty() || role.isEmpty()) {
                Toast.makeText(RoleEditorActivity.this, "Please fill in both fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Validate role
            if (!role.equalsIgnoreCase("ADMIN") &&
                    !role.equalsIgnoreCase("END_USER") &&
                    !role.equalsIgnoreCase("OPERATOR")) {
                Toast.makeText(RoleEditorActivity.this, "Invalid role. Use ADMIN, END_USER, or OPERATOR.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Perform role update
            updateUserRole(email, role.toUpperCase());
        });

        // Back Button Listener
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(RoleEditorActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }

    private void updateUserRole(String email, String roleString) {
        try {
            // Convert the role string to RoleEnum
            RoleEnum roleEnum = RoleEnum.valueOf(roleString.toUpperCase());

            // Retrieve logged-in system ID from SharedPreferences
            SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
            String systemId = sharedPreferences.getString("loggedInSystemID", null);

            if (systemId == null || email.isEmpty()) {
                Toast.makeText(this, "Missing system ID or email. Please log in again.", Toast.LENGTH_LONG).show();
                return;
            }

            // Log for debugging
            System.out.println("System ID: " + systemId);
            System.out.println("Email: " + email);
            System.out.println("Role: " + roleEnum);

            // Create a UserBoundary object with the new role
            UserBoundary userBoundary = new UserBoundary();
            userBoundary.setRole(roleEnum);

            // Log the userBoundary for debugging
            System.out.println("UserBoundary: " + userBoundary);

            // Make the PUT request to update the user's role
            apiService.updateUser(systemId, email, userBoundary).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(RoleEditorActivity.this, "Role updated successfully!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(RoleEditorActivity.this, "Failed to update role. Status: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(RoleEditorActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IllegalArgumentException e) {
            // Handle invalid role input
            Toast.makeText(this, "Invalid role. Use ADMIN, END_USER, or OPERATOR.", Toast.LENGTH_LONG).show();
        }
    }
}
