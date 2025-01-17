package com.example.myapplication.activites;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapters.RoomAdapter;
import com.example.myapplication.handlers.PopupHandler;
import com.example.myapplication.models.Room;

import java.util.ArrayList;
import java.util.List;

public class OperatorActivity extends AppCompatActivity {

    private List<Room> roomList;
    private RoomAdapter roomAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize RecyclerView
        RecyclerView roomRecyclerView = findViewById(R.id.roomRecyclerView);
        roomList = new ArrayList<>();
        PopupHandler popupHandler = new PopupHandler(this);
        roomAdapter = new RoomAdapter(roomList, this, popupHandler, true); // true for isOperator
        roomRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        roomRecyclerView.setAdapter(roomAdapter);

        // Profile Button
        ImageButton profileButton = findViewById(R.id.profile_button);
        profileButton.setOnClickListener(v -> {
            Intent intent = new Intent(OperatorActivity.this, ProfileActivity.class);
            startActivity(intent);
        });

        // Add Room Button
        ImageButton plusButton = findViewById(R.id.plus_button);
        plusButton.setOnClickListener(v -> showNewRoomPopup());

        // Initialize room data
        initializeRooms();
    }

    private void initializeRooms() {
        // Add sample rooms
        roomList.add(new Room("Living Room", new ArrayList<>()));
        roomList.add(new Room("Bedroom", new ArrayList<>()));
        roomAdapter.notifyDataSetChanged();
    }

    private void showNewRoomPopup() {
        View popupView = LayoutInflater.from(this).inflate(R.layout.new_room_popup, null);
        EditText newRoomNameEditText = popupView.findViewById(R.id.new_room_name);
        Button cancelButton = popupView.findViewById(R.id.cancel_button);
        Button createButton = popupView.findViewById(R.id.create_button);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(popupView)
                .setCancelable(false)
                .create();

        cancelButton.setOnClickListener(v -> dialog.dismiss());
        createButton.setOnClickListener(v -> {
            String roomName = newRoomNameEditText.getText().toString().trim();
            if (roomName.isEmpty()) {
                Toast.makeText(this, "Room name cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            roomList.add(new Room(roomName, new ArrayList<>()));
            roomAdapter.notifyItemInserted(roomList.size() - 1);
            Toast.makeText(this, "Room created: " + roomName, Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        dialog.show();
    }
}
