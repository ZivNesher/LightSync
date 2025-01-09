package com.example.myapplication.activites;

import android.app.AlertDialog;
import android.content.Intent;
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
        setContentView(R.layout.activity_main);

        // Initialize RecyclerView
        RecyclerView roomRecyclerView = findViewById(R.id.roomRecyclerView);
        roomList = new ArrayList<>();
        roomAdapter = new RoomAdapter(roomList, this);
        roomRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        roomRecyclerView.setAdapter(roomAdapter);

        // Handle Plus Button
        ImageButton plusButton = findViewById(R.id.plus_button);
        plusButton.setOnClickListener(v -> showNewRoomPopup());

        // Handle Profile Button
        ImageButton profileButton = findViewById(R.id.profile_button);
        profileButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(intent);
        });
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
