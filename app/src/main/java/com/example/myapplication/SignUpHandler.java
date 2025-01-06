package com.example.myapplication;

import android.content.Context;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.api.ApiService;
import com.example.myapplication.models.NewUserBoundary;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignUpHandler {

    private final Context context;

    public SignUpHandler(Context context) {
        this.context = context;
    }

    public void setupSignUpScreen() {
        // Find input fields and buttons
        EditText emailInput = ((SignUpActivity) context).findViewById(R.id.email_input);
        Button signUpButton = ((SignUpActivity) context).findViewById(R.id.sign_up_button);
        Button backToLoginButton = ((SignUpActivity) context).findViewById(R.id.back_to_login_button);

        // Retrofit setup
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8081") // Replace with your local server's base URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService apiService = retrofit.create(ApiService.class);

        // Set OnClickListener for the Sign-Up button
        signUpButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();

            // Validate email input
            if (email.isEmpty()) {
                Toast.makeText(context, "Please enter your email", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create the NewUserBoundary object for the request
            NewUserBoundary newUser = new NewUserBoundary();
            newUser.setEmail(email); // Set the email
            newUser.setRole("END_USER"); // Default role
            newUser.setUsername("string"); // Default username
            newUser.setAvatar("string"); // Default avatar

            // Log the request payload
            Log.d("SignUpHandler", "Request Body: " + new Gson().toJson(newUser));

            // Make the POST call
            Call<Void> call = apiService.createUser(newUser);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(context, "Sign-up successful!", Toast.LENGTH_SHORT).show();
                        ((SignUpActivity) context).finish(); // Close SignUpActivity
                    } else {
                        try {
                            String errorBody = response.errorBody() != null ? response.errorBody().string() : "Unknown error";
                            Log.e("SignUpHandler", "Server Response: " + errorBody);
                            Toast.makeText(context, "Sign-up failed: " + errorBody, Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            Log.e("SignUpHandler", "Error parsing errorBody", e);
                            Toast.makeText(context, "Sign-up failed: Unable to parse error", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("SignUpHandler", "Request failed", t);
                }
            });
        });

        // Set OnClickListener for the Back to Login button
        backToLoginButton.setOnClickListener(v -> {
            ((SignUpActivity) context).finish(); // Close SignUpActivity and return to LoginActivity
        });
    }
}
