package com.example.myapplication.activites;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapters.RoomAdapter;
import com.example.myapplication.models.Room;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Room> roomList;
    private RoomAdapter roomAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retrieve user role from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String role = sharedPreferences.getString("loggedInRole", null);

        // Redirect based on role
        if ("END_USER".equalsIgnoreCase(role)) {
            Intent intent = new Intent(MainActivity.this, OperatorActivity.class);
            startActivity(intent);
            finish();
        } else if ("OPERATOR".equalsIgnoreCase(role)) {
            Intent intent = new Intent(MainActivity.this, OperatorActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Invalid role or not logged in", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        // Initialize RecyclerView and room data
        initializeUI();
    }

    private void initializeUI() {
        setContentView(R.layout.activity_main);
        RecyclerView roomRecyclerView = findViewById(R.id.roomRecyclerView);

        roomList = new ArrayList<>();
        roomAdapter = new RoomAdapter(roomList, this, null, false);
        roomRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        roomRecyclerView.setAdapter(roomAdapter);

        // Populate initial room data (example rooms)
        initializeRooms();
    }

    private void initializeRooms() {
        roomList.add(new Room("Living Room"));
        roomList.add(new Room("Bedroom"));
        roomAdapter.notifyDataSetChanged();
    }

    private void showNewRoomPopup() {
        // Inflate the popup layout for creating a new room
        View popupView = LayoutInflater.from(this).inflate(R.layout.new_room_popup, null);
        EditText newRoomNameEditText = popupView.findViewById(R.id.new_room_name);
        Button cancelButton = popupView.findViewById(R.id.cancel_button);
        Button createButton = popupView.findViewById(R.id.create_button);

        // Create an AlertDialog for the popup
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(popupView)
                .setCancelable(false)
                .create();

        // Cancel button listener
        cancelButton.setOnClickListener(v -> dialog.dismiss());

        // Create button listener
        createButton.setOnClickListener(v -> {
            String roomName = newRoomNameEditText.getText().toString().trim();
            if (roomName.isEmpty()) {
                Toast.makeText(this, "Room name cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            // Add the new room to the list and update the RecyclerView
            roomList.add(new Room(roomName));
            roomAdapter.notifyItemInserted(roomList.size() - 1);

            Toast.makeText(this, "Room created: " + roomName, Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        // Show the popup dialog
        dialog.show();
    }
}
