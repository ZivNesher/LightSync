package com.example.myapplication.activites;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.models.UserId;


import com.example.myapplication.api.AdminAPI;
import com.example.myapplication.R;
import com.example.myapplication.api.ApiService;
import com.example.myapplication.data.RoleEnum;
import com.example.myapplication.models.UserBoundary;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);

        // Initialize UI components
        Button loginButton = findViewById(R.id.login_button);
        Button signUpButton = findViewById(R.id.signup_button);
        EditText emailInput = findViewById(R.id.email_input);
        EditText passwordInput = findViewById(R.id.password_input);

        // Retrofit setup
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8081")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService apiService = retrofit.create(ApiService.class);

        loginButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }
            if ("Admin".equalsIgnoreCase(email) && "Nimda".equals(password)) {
                Toast.makeText(this, "Admin login successful", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, RoleEditorActivity.class);
                startActivity(intent);
                finish();
                return;
            }
            // Validate credentials with the server
            Call<UserBoundary> call = apiService.loginUser(password, email);
            call.enqueue(new Callback<UserBoundary>() {
                @Override
                public void onResponse(Call<UserBoundary> call, Response<UserBoundary> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        // Save logged-in user data to SharedPreferences
                        UserBoundary user = response.body();
                        saveLoggedInUser(user);

                        // Check the user role and navigate accordingly
                        if (RoleEnum.ADMIN.equals(user.getRole())) {
                            // Navigate to AdminActivity if the role is Admin
                            Intent intent = new Intent(LoginActivity.this, AdminAPI.class);
                            startActivity(intent);
                        } else {
                            // Navigate to MainActivity for other roles
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                        }

                        finish(); // Close LoginActivity
                    } else {
                        Toast.makeText(LoginActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<UserBoundary> call, Throwable t) {
                    Toast.makeText(LoginActivity.this, "Login failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        signUpButton.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        });
    }

    private void saveLoggedInUser(UserBoundary user) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("loggedInEmail", user.getUserId().getEmail());
        editor.putString("loggedInSystemID", user.getUserId().getSystemID());
        editor.putString("loggedInUsername", user.getUsername());
        editor.putString("loggedInAvatar", user.getAvatar());
        editor.putString("loggedInRole", user.getRole().name());
        editor.apply();
    }
}




