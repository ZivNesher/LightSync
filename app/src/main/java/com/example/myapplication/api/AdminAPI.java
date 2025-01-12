package com.example.myapplication.api;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.activites.LoginActivity;
import com.example.myapplication.adapters.CommandAdapter;
import com.example.myapplication.adapters.UserAdapter;
import com.example.myapplication.models.UserBoundary;
import com.example.myapplication.models.CommandBoundary;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminAPI extends AppCompatActivity {

    private AdminApiService adminApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_layout);

        // Check if the logged-in user is an admin
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String role = sharedPreferences.getString("loggedInRole", null);

        if (!"Admin".equalsIgnoreCase(role)) {
            // If the user is not an admin, redirect to the login screen
            Toast.makeText(this, "Unauthorized access. Redirecting to login.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(AdminAPI.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return;
        }

        // Initialize Retrofit service
        adminApiService = ApiClient.getRetrofitInstance().create(AdminApiService.class);

        // Initialize buttons
        Button deleteAllUsersButton = findViewById(R.id.delete_all_users_button);
        Button exportAllUsersButton = findViewById(R.id.export_all_users_button);
        Button deleteAllObjectsButton = findViewById(R.id.delete_all_objects_button);
        Button deleteAllCommandsButton = findViewById(R.id.delete_all_commands_button);
        Button exportAllCommandsButton = findViewById(R.id.export_all_commands_button);
        Button quitButton = findViewById(R.id.back_to_login_button);

        // Set up button listeners
        deleteAllUsersButton.setOnClickListener(v -> showConfirmationPopup("Are you sure you want to delete all users?", this::deleteAllUsers));
        exportAllUsersButton.setOnClickListener(v -> exportAllUsers());
        deleteAllObjectsButton.setOnClickListener(v -> showConfirmationPopup("Are you sure you want to delete all objects?", this::deleteAllObjects));
        deleteAllCommandsButton.setOnClickListener(v -> showConfirmationPopup("Are you sure you want to delete all commands?", this::deleteAllCommands));
        exportAllCommandsButton.setOnClickListener(v -> exportAllCommands());
        quitButton.setOnClickListener(v -> {
            Intent intent = new Intent(AdminAPI.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }


    private String getUserSystemID() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String systemID = sharedPreferences.getString("loggedInSystemID", null);
        if (systemID == null) {
            showMessagePopup("System ID not found. Please log in again.");
        }
        return systemID;
    }

    private String getUserEmail() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String email = sharedPreferences.getString("loggedInEmail", null);
        if (email == null) {
            showMessagePopup("Email not found. Please log in again.");
        }
        return email;
    }

    private void deleteAllUsers() {
        String systemID = getUserSystemID();
        String email = getUserEmail();

        if (systemID != null && email != null) {
            adminApiService.deleteAllUsers(systemID, email).enqueue(new Callback<Void>() {
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
        } else {
            showMessagePopup("Admin credentials not found. Please log in again.");
        }
    }

    private void exportAllUsers() {
        String systemID = getUserSystemID();
        String email = getUserEmail();

        if (systemID != null && email != null) {
            adminApiService.exportAllUsers(systemID, email, 0, 10).enqueue(new Callback<UserBoundary[]>() {
                @Override
                public void onResponse(Call<UserBoundary[]> call, Response<UserBoundary[]> response) {
                    if (response.isSuccessful()) {
                        UserBoundary[] users = response.body();
                        showTablePopupForUsers(users);
                    } else {
                        showMessagePopup("Failed to export users. Status: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<UserBoundary[]> call, Throwable t) {
                    showMessagePopup("Error: " + t.getMessage());
                }
            });
        } else {
            showMessagePopup("Admin credentials not found. Please log in again.");
        }
    }

    private void deleteAllObjects() {
        String systemID = getUserSystemID();
        String email = getUserEmail();

        if (systemID != null && email != null) {
            adminApiService.deleteAllObjects(systemID, email).enqueue(new Callback<Void>() {
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
        } else {
            showMessagePopup("Admin credentials not found. Please log in again.");
        }
    }

    private void deleteAllCommands() {
        String systemID = getUserSystemID();
        String email = getUserEmail();

        if (systemID != null && email != null) {
            adminApiService.deleteAllCommands(systemID, email).enqueue(new Callback<Void>() {
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
        } else {
            showMessagePopup("Admin credentials not found. Please log in again.");
        }
    }

    private void exportAllCommands() {
        String systemID = getUserSystemID();
        String email = getUserEmail();

        if (systemID != null && email != null) {
            adminApiService.exportAllCommands(systemID, email, 0, 10).enqueue(new Callback<CommandBoundary[]>() {
                @Override
                public void onResponse(Call<CommandBoundary[]> call, Response<CommandBoundary[]> response) {
                    if (response.isSuccessful()) {
                        CommandBoundary[] commands = response.body();
                        showTablePopupForCommands(commands);
                    } else {
                        showMessagePopup("Failed to export commands. Status: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<CommandBoundary[]> call, Throwable t) {
                    showMessagePopup("Error: " + t.getMessage());
                }
            });
        } else {
            showMessagePopup("Admin credentials not found. Please log in again.");
        }
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
            row.addView(createDataTextView(user.getUserId().getSystemID()));
            row.addView(createDataTextView(user.getUserId().getEmail()));
            row.addView(createDataTextView(user.getRole().name()));
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

        // Initialize RecyclerView
        RecyclerView recyclerView = popupView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Convert the array to a List for the adapter
        List<UserBoundary> userList = Arrays.asList(users);
        UserAdapter adapter = new UserAdapter(userList);
        recyclerView.setAdapter(adapter);

        // Close button to dismiss popup
        Button closeButton = popupView.findViewById(R.id.close_button);
        closeButton.setOnClickListener(v -> popupWindow.dismiss());

        // Show popup window
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

        // Initialize RecyclerView
        RecyclerView recyclerView = popupView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        CommandAdapter commandAdapter = new CommandAdapter(commands);
        recyclerView.setAdapter(commandAdapter);

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