package com.example.myapplication.handlers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.myapplication.api.AdminAPI;
import com.example.myapplication.R;
import com.example.myapplication.activites.MainActivity;
import com.example.myapplication.activites.ProfileActivity;
import com.example.myapplication.api.ApiService;
import com.example.myapplication.models.UserBoundary;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginHandler {

    private final Context context;

    public LoginHandler(Context context) {
        this.context = context;
    }

    public void setupLoginScreen() {
        // Find the Login and Sign Up buttons
        Button loginButton = ((MainActivity) context).findViewById(R.id.login_button);
        Button signUpButton = ((MainActivity) context).findViewById(R.id.signup_button);
        EditText usernameInput = ((MainActivity) context).findViewById(R.id.email_input);
        EditText passwordInput = ((MainActivity) context).findViewById(R.id.password_input);

        // Retrofit setup
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8081")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);

        loginButton.setOnClickListener(v -> {
            String username = usernameInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (username.equals("Admin") && password.equals("Nimda")) {
                // Admin login
                Toast.makeText(context, "Admin login successful", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, AdminAPI.class);
                context.startActivity(intent);
            } else if (username.isEmpty() || password.isEmpty()) {
                // Allow empty email and password for now
                Toast.makeText(context, "Login successful (empty credentials)", Toast.LENGTH_SHORT).show();
                proceedToMainLayout();
            } else {
                // Validate credentials with the server
                Call<UserBoundary> call = apiService.loginUser(password, username);
                call.enqueue(new Callback<UserBoundary>() {
                    @Override
                    public void onResponse(Call<UserBoundary> call, Response<UserBoundary> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            // Save logged-in user data to SharedPreferences
                            UserBoundary user = response.body();
                            saveLoggedInUser(user);

                            // Login successful
                            Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show();
                            proceedToMainLayout();
                        } else {
                            // Login failed
                            Toast.makeText(context, "Invalid email or password", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<UserBoundary> call, Throwable t) {
                        // Network error or server unreachable
                        Toast.makeText(context, "Login failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        signUpButton.setOnClickListener(v -> {
            ((MainActivity) context).setContentView(R.layout.sign_up_screen);
            new SignUpHandler(context).setupSignUpScreen();
        });
    }

    private void proceedToMainLayout() {
        ((MainActivity) context).setContentView(R.layout.activity_main);

        // Initialize Profile Button
        ImageButton profileButton = ((MainActivity) context).findViewById(R.id.profile_button);
        if (profileButton != null) {
            profileButton.setOnClickListener(v -> {
                // Navigate to ProfileActivity
                Intent intent = new Intent(context, ProfileActivity.class);
                context.startActivity(intent);
            });
        } else {
            Toast.makeText(context, "Profile button not found", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveLoggedInUser(UserBoundary user) {
        // Save user data to SharedPreferences
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("loggedInEmail", user.getUserId().getEmail());
        editor.putString("loggedInUsername", user.getUsername());
        editor.putString("loggedInAvatar", user.getAvatar());
        editor.apply();
    }
}
