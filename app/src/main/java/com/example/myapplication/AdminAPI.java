package com.example.myapplication;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.api.AdminApiService;
import com.example.myapplication.api.ApiClient;
import com.example.myapplication.models.UserBoundary;
import com.example.myapplication.models.CommandBoundary;

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
        Button exportAllUsersButton = findViewById(R.id.export_all_users_button);
        Button deleteAllObjectsButton = findViewById(R.id.delete_all_objects_button);
        Button deleteAllCommandsButton = findViewById(R.id.delete_all_commands_button);
        Button exportAllCommandsButton = findViewById(R.id.export_all_commands_button);

        // Set up button listeners
        deleteAllUsersButton.setOnClickListener(v -> showConfirmationPopup("Are you sure you want to delete all users?", this::deleteAllUsers));
        exportAllUsersButton.setOnClickListener(v -> exportAllUsers());
        deleteAllObjectsButton.setOnClickListener(v -> showConfirmationPopup("Are you sure you want to delete all objects?", this::deleteAllObjects));
        deleteAllCommandsButton.setOnClickListener(v -> showConfirmationPopup("Are you sure you want to delete all commands?", this::deleteAllCommands));
        exportAllCommandsButton.setOnClickListener(v -> exportAllCommands());
    }

    private void deleteAllUsers() {
        adminApiService.deleteAllUsers().enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    showMessagePopup("All users deleted successfully!");
                } else {
                    showMessagePopup("Failed to delete users. Status: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                showMessagePopup("Error: " + t.getMessage());
            }
        });
    }

    private void exportAllUsers() {
        adminApiService.exportAllUsers().enqueue(new Callback<UserBoundary[]>() {
            @Override
            public void onResponse(Call<UserBoundary[]> call, Response<UserBoundary[]> response) {
                if (response.isSuccessful()) {
                    UserBoundary[] users = response.body();
                    showTablePopupForUsers(users); // Show users in a popup table
                } else {
                    showMessagePopup("Failed to export users. Status: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<UserBoundary[]> call, Throwable t) {
                showMessagePopup("Error: " + t.getMessage());
            }
        });
    }

    private void deleteAllObjects() {
        adminApiService.deleteAllObjects().enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    showMessagePopup("All objects deleted successfully!");
                } else {
                    showMessagePopup("Failed to delete objects. Status: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                showMessagePopup("Error: " + t.getMessage());
            }
        });
    }

    private void deleteAllCommands() {
        adminApiService.deleteAllCommands().enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    showMessagePopup("All commands deleted successfully!");
                } else {
                    showMessagePopup("Failed to delete commands. Status: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                showMessagePopup("Error: " + t.getMessage());
            }
        });
    }

    private void exportAllCommands() {
        adminApiService.exportAllCommands().enqueue(new Callback<CommandBoundary[]>() {
            @Override
            public void onResponse(Call<CommandBoundary[]> call, Response<CommandBoundary[]> response) {
                if (response.isSuccessful()) {
                    CommandBoundary[] commands = response.body();
                    showTablePopupForCommands(commands); // Show commands in a popup table
                } else {
                    showMessagePopup("Failed to export commands. Status: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<CommandBoundary[]> call, Throwable t) {
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

    private void addHeaderRow(TableLayout tableLayout) {
        TableRow headerRow = new TableRow(this);
        headerRow.setBackgroundColor(getResources().getColor(R.color.mainblue));
        headerRow.setPadding(8, 8, 8, 8);

        headerRow.addView(createHeaderTextView("System ID"));
        headerRow.addView(createHeaderTextView("Email"));
        headerRow.addView(createHeaderTextView("Role"));
        headerRow.addView(createHeaderTextView("Username"));
        headerRow.addView(createHeaderTextView("Avatar"));

        tableLayout.addView(headerRow);
    }

    private TextView createHeaderTextView(String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setTextColor(getResources().getColor(android.R.color.white));
        textView.setTextSize(16);
        textView.setPadding(16, 16, 16, 16);
        textView.setGravity(Gravity.CENTER);
        textView.setTypeface(null, Typeface.BOLD);
        return textView;
    }
    private void addDataRows(TableLayout tableLayout, UserBoundary[] users) {
        for (UserBoundary user : users) {
            TableRow row = new TableRow(this);
            row.setPadding(8, 8, 8, 8);
            row.setBackgroundColor(getResources().getColor(android.R.color.background_light));

            row.addView(createDataTextView(user.getUserId().getSystemId()));
            row.addView(createDataTextView(user.getUserId().getEmail()));
            row.addView(createDataTextView(user.getRole()));
            row.addView(createDataTextView(user.getUsername()));
            row.addView(createDataTextView(user.getAvatar()));

            tableLayout.addView(row);
        }
    }

    private TextView createDataTextView(String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setTextColor(getResources().getColor(android.R.color.black));
        textView.setPadding(16, 16, 16, 16); // Padding for spacing
        textView.setGravity(Gravity.START); // Align text to the start of the cell
        textView.setSingleLine(false); // Allow multiple lines
        textView.setEllipsize(null); // Disable truncation
        textView.setMaxWidth(600); // Optional: Limit the maximum width of the cell
        textView.setMinWidth(200); // Ensure consistent width across cells
        textView.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
        ));
        return textView;
    }

    private void showTablePopupForUsers(UserBoundary[] users) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_table, null);

        PopupWindow popupWindow = new PopupWindow(
                popupView,
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                true
        );

        TableLayout tableLayout = popupView.findViewById(R.id.tableLayout);
        addHeaderRow(tableLayout);
        addDataRows(tableLayout, users);

        Button closeButton = popupView.findViewById(R.id.close_button);
        closeButton.setOnClickListener(v -> popupWindow.dismiss());

        popupWindow.showAtLocation(findViewById(android.R.id.content), Gravity.CENTER, 0, 0);
    }
    private void addCommandDataRows(TableLayout tableLayout, CommandBoundary[] commands) {
        for (CommandBoundary command : commands) {
            TableRow row = new TableRow(this);
            row.setPadding(8, 8, 8, 8);
            row.setBackgroundColor(getResources().getColor(android.R.color.background_light));

            row.addView(createScrollableDataTextView(command.getCommandId().getId()));
            row.addView(createScrollableDataTextView(command.getCommand()));
            row.addView(createScrollableDataTextView(command.getTargetObject().getObjectId().getId()));
            row.addView(createScrollableDataTextView(command.getInvocationTimestamp()));
            row.addView(createScrollableDataTextView(command.getInvokedBy().getUserId().getEmail()));

            tableLayout.addView(row);
        }
    }

    private void addCommandHeaderRow(TableLayout tableLayout) {
        TableRow headerRow = new TableRow(this);
        headerRow.setBackgroundColor(getResources().getColor(R.color.mainblue));
        headerRow.setPadding(8, 8, 8, 8);

        headerRow.addView(createHeaderTextView("Command ID"));
        headerRow.addView(createHeaderTextView("Command"));
        headerRow.addView(createHeaderTextView("Target Object"));
        headerRow.addView(createHeaderTextView("Invocation Time"));
        headerRow.addView(createHeaderTextView("Invoked By"));

        tableLayout.addView(headerRow);
    }
    private void showTablePopupForCommands(CommandBoundary[] commands) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_table, null);

        PopupWindow popupWindow = new PopupWindow(
                popupView,
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                true
        );

        TableLayout tableLayout = popupView.findViewById(R.id.tableLayout);

        // Add header row and data rows
        addCommandHeaderRow(tableLayout);
        addCommandDataRows(tableLayout, commands);

        // Set close button action
        Button closeButton = popupView.findViewById(R.id.close_button);
        closeButton.setOnClickListener(v -> popupWindow.dismiss());

        // Show popup
        popupWindow.showAtLocation(findViewById(android.R.id.content), Gravity.CENTER, 0, 0);
    }
    private View createScrollableDataTextView(String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setTextColor(getResources().getColor(android.R.color.black));
        textView.setPadding(16, 16, 16, 16);
        textView.setGravity(Gravity.START);
        textView.setSingleLine(true); // Keep text on a single line
        textView.setEllipsize(TextUtils.TruncateAt.END); // Add ellipsis for overflow
        textView.setMaxWidth(600); // Limit the maximum width of the cell

        HorizontalScrollView scrollView = new HorizontalScrollView(this);
        scrollView.addView(textView);
        scrollView.setHorizontalScrollBarEnabled(true);
        scrollView.setFillViewport(true);

        return scrollView;
    }



}