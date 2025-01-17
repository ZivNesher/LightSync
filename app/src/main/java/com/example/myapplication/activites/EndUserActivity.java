package com.example.myapplication.activites;

import android.content.Intent;
import android.os.Bundle;
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

public class EndUserActivity extends AppCompatActivity {

    private List<Room> roomList;
    private RoomAdapter roomAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_user);

        // Initialize RecyclerView for rooms
        RecyclerView roomRecyclerView = findViewById(R.id.roomRecyclerView);
        roomList = new ArrayList<>();
        roomAdapter = new RoomAdapter(roomList, this);
        roomRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        roomRecyclerView.setAdapter(roomAdapter);

        // Profile Button
        ImageButton profileButton = findViewById(R.id.profile_button);
        profileButton.setOnClickListener(v -> {
            Intent intent = new Intent(EndUserActivity.this, ProfileActivity.class);
            startActivity(intent);
        });

        // Initialize Room Data (for testing or actual data)
        initializeRooms();
    }

    private void initializeRooms() {
        // Add some sample rooms
        roomList.add(new Room("Room 1"));
        roomList.add(new Room("Room 2"));
        roomAdapter.notifyDataSetChanged();
    }
}
