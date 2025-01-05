package com.example.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class AdminAPI extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_layout);

        // Initialize buttons
        Button deleteAllUsersButton = findViewById(R.id.delete_all_users_button);
        Button deleteAllObjectsButton = findViewById(R.id.delete_all_objects_button);
        Button deleteAllCommandsButton = findViewById(R.id.delete_all_commands_button);
        Button updateExportUsersButton = findViewById(R.id.update_export_users_button);
        Button updateExportCommandsButton = findViewById(R.id.update_export_commands_button);
        Button endUserScreenButton = findViewById(R.id.end_user_screen_button);
        Button operatorScreenButton = findViewById(R.id.operator_screen_button);

        // Delete All Users button
        deleteAllUsersButton.setOnClickListener(v -> showConfirmationPopup("Are you sure you want to delete all users?"));

        // Delete All Objects button
        deleteAllObjectsButton.setOnClickListener(v -> showConfirmationPopup("Are you sure you want to delete all objects?"));

        // Delete All Commands button
        deleteAllCommandsButton.setOnClickListener(v -> showConfirmationPopup("Are you sure you want to delete all commands?"));

        // Update & Export Users button
        updateExportUsersButton.setOnClickListener(v -> {
            // Cosmetic implementation
            showMessagePopup("Update and Export Users (Pagination)");
        });

        // Update & Export Commands button
        updateExportCommandsButton.setOnClickListener(v -> {
            // Cosmetic implementation
            showMessagePopup("Update and Export Commands (Pagination)");
        });

        // Navigation: End-User Screen
        endUserScreenButton.setOnClickListener(v -> {
            // Navigate to the End-User UI
            setContentView(R.layout.activity_main);

            // Initialize the End-User UI handler and setup the UI
            EndUserUIHandler endUserUIHandler = new EndUserUIHandler(this);
            endUserUIHandler.setupEndUserUI();
        });


        // Navigation: Operator Screen
        operatorScreenButton.setOnClickListener(v -> navigateToLogin());
    }

    /**
     * Displays a confirmation popup for delete actions.
     *
     * @param message The message to display in the popup.
     */
    private void showConfirmationPopup(String message) {
        // Inflate the custom popup layout
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_confirmation, null);

        // Create the PopupWindow
        PopupWindow popupWindow = new PopupWindow(
                popupView,
                android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
                android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,
                true
        );

        // Set the confirmation message
        TextView popupMessage = popupView.findViewById(R.id.popup_message);
        popupMessage.setText(message);

        // Initialize Yes and No buttons
        Button yesButton = popupView.findViewById(R.id.yes_button);
        Button noButton = popupView.findViewById(R.id.no_button);

        // Yes button action
        yesButton.setOnClickListener(v -> {
            // Cosmetic action for now
            popupWindow.dismiss();
            showMessagePopup("Action confirmed!");
        });

        // No button action
        noButton.setOnClickListener(v -> popupWindow.dismiss());

        // Show the PopupWindow
        popupWindow.showAtLocation(findViewById(android.R.id.content), Gravity.CENTER, 0, 0);
    }

    /**
     * Displays a message popup for non-deletion actions.
     *
     * @param message The message to display in the popup.
     */
    private void showMessagePopup(String message) {
        // Inflate the custom popup layout
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_message, null);

        // Create the PopupWindow
        PopupWindow popupWindow = new PopupWindow(
                popupView,
                android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
                android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,
                true
        );

        // Set the message
        TextView popupMessage = popupView.findViewById(R.id.popup_message);
        popupMessage.setText(message);

        // Close button action
        Button closeButton = popupView.findViewById(R.id.close_button);
        closeButton.setOnClickListener(v -> popupWindow.dismiss());

        // Show the PopupWindow
        popupWindow.showAtLocation(findViewById(android.R.id.content), Gravity.CENTER, 0, 0);
    }

    /**
     * Navigates back to the Login screen.
     */
    private void navigateToLogin() {
        setContentView(R.layout.login_screen);
        // Reinitialize the login logic if necessary
    }
}
