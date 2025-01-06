package com.example.myapplication;

import android.content.Context;
import android.widget.Toast;

import com.example.myapplication.api.ApiService;
import com.example.myapplication.models.UserBoundary;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UpdateHandler {

    private final Context context;
    private final ApiService apiService;

    public UpdateHandler(Context context) {
        this.context = context;

        // Initialize Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8081") // Replace with your local server's base URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        this.apiService = retrofit.create(ApiService.class);
    }

    public void updateUser(String email, String username, String avatar) {
        // Validate input
        if (email == null || email.trim().isEmpty()) {
            Toast.makeText(context, "Email is required to update the profile", Toast.LENGTH_SHORT).show();
            return;
        }

        // Prepare the UserBoundary object for the update request
        UserBoundary updateUser = new UserBoundary();
        updateUser.setUsername(username == null || username.trim().isEmpty() ? null : username); // Null if no change
        updateUser.setAvatar(avatar == null || avatar.trim().isEmpty() ? null : avatar); // Null if no change
        updateUser.setRole(null); // Do not update role

        // Extract the system ID (use a constant or config)
        String systemId = "2025a.integrative.nagar.yuval";

        // Make the PUT call
        Call<Void> call = apiService.updateUser(systemId, email, updateUser);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "Unknown error";
                        Toast.makeText(context, "Update failed: " + errorBody, Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(context, "Update failed: Unable to parse error", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
