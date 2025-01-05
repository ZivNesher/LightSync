package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.api.AdminApiService;
import com.example.myapplication.api.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminAPI extends AppCompatActivity {

    private AdminApiService adminApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_layout);

        // Initialize Retrofit service
        adminApiService = ApiClient.getRetrofitInstance().create(AdminApiService.class);

        // Initialize buttons
        Button deleteAllUsersButton = findViewById(R.id.delete_all_users_button);
        Button deleteAllObjectsButton = findViewById(R.id.delete_all_objects_button);
        Button deleteAllCommandsButton = findViewById(R.id.delete_all_commands_button);

        // Set up buttons
        deleteAllUsersButton.setOnClickListener(v -> showConfirmationPopup(
                "Are you sure you want to delete all users?",
                this::deleteAllUsers
        ));

        deleteAllObjectsButton.setOnClickListener(v -> showConfirmationPopup(
                "Are you sure you want to delete all objects?",
                this::deleteAllObjects
        ));

        deleteAllCommandsButton.setOnClickListener(v -> showConfirmationPopup(
                "Are you sure you want to delete all commands?",
                this::deleteAllCommands
        ));
    }

    private void deleteAllUsers() {
        adminApiService.deleteAllUsers().enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("AdminAPI", "Users deleted successfully.");
                    showMessagePopup("All users deleted successfully!");
                } else {
                    Log.e("AdminAPI", "Failed to delete users. Status: " + response.code());
                    showMessagePopup("Failed to delete users. Status: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("AdminAPI", "Error deleting users", t);
                showMessagePopup("Error: " + t.getMessage());
            }
        });
    }

    private void deleteAllObjects() {
        adminApiService.deleteAllObjects().enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("AdminAPI", "Objects deleted successfully.");
                    showMessagePopup("All objects deleted successfully!");
                } else {
                    Log.e("AdminAPI", "Failed to delete objects. Status: " + response.code());
                    showMessagePopup("Failed to delete objects. Status: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("AdminAPI", "Error deleting objects", t);
                showMessagePopup("Error: " + t.getMessage());
            }
        });
    }

    private void deleteAllCommands() {
        adminApiService.deleteAllCommands().enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("AdminAPI", "Commands deleted successfully.");
                    showMessagePopup("All commands deleted successfully!");
                } else {
                    Log.e("AdminAPI", "Failed to delete commands. Status: " + response.code());
                    showMessagePopup("Failed to delete commands. Status: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("AdminAPI", "Error deleting commands", t);
                showMessagePopup("Error: " + t.getMessage());
            }
        });
    }

    private void showConfirmationPopup(String message, Runnable action) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_confirmation, null);

        PopupWindow popupWindow = new PopupWindow(
                popupView,
                android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
                android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,
                true
        );

        TextView popupMessage = popupView.findViewById(R.id.popup_message);
        popupMessage.setText(message);

        Button yesButton = popupView.findViewById(R.id.yes_button);
        Button noButton = popupView.findViewById(R.id.no_button);

        yesButton.setOnClickListener(v -> {
            popupWindow.dismiss();
            action.run();
        });

        noButton.setOnClickListener(v -> popupWindow.dismiss());

        popupWindow.showAtLocation(findViewById(android.R.id.content), Gravity.CENTER, 0, 0);
    }

    private void showMessagePopup(String message) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_message, null);

        PopupWindow popupWindow = new PopupWindow(
                popupView,
                android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
                android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,
                true
        );

        TextView popupMessage = popupView.findViewById(R.id.popup_message);
        popupMessage.setText(message);

        Button closeButton = popupView.findViewById(R.id.close_button);
        closeButton.setOnClickListener(v -> popupWindow.dismiss());

        popupWindow.showAtLocation(findViewById(android.R.id.content), Gravity.CENTER, 0, 0);
    }
}

