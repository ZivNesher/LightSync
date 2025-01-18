package com.example.myapplication.activites;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.example.myapplication.models.CreatedBy;
import com.example.myapplication.models.ObjectBoundary;
import com.example.myapplication.models.Room;
import com.example.myapplication.api.ApiService;
import com.example.myapplication.api.ApiClient;
import com.example.myapplication.models.UserId;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OperatorActivity extends AppCompatActivity {

    private List<Room> roomList;
    private RoomAdapter roomAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operator);

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
        fetchRoomsFromBackend();
    }

    private void fetchRoomsFromBackend() {
        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);

        // Replace with the correct system ID and user email
        String systemID = "2025a.integrative.nagar.yuval";
        String userEmail = "z@g.com";

        apiService.getRooms(systemID, userEmail, 0, 10).enqueue(new Callback<List<ObjectBoundary>>() {
            @Override
            public void onResponse(Call<List<ObjectBoundary>> call, Response<List<ObjectBoundary>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    roomList.clear(); // Clear the existing list
                    for (ObjectBoundary boundary : response.body()) {
                        Room room = new Room(
                                boundary.getAlias(),
                                boundary.isActive()
                        );
                        roomList.add(room); // Add room to the list
                    }
                    roomAdapter.notifyDataSetChanged(); // Notify adapter about data changes
                } else {
                    Toast.makeText(OperatorActivity.this, "Failed to fetch rooms: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ObjectBoundary>> call, Throwable t) {
                Toast.makeText(OperatorActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
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

            ObjectBoundary newRoom = new ObjectBoundary();
            CreatedBy createdBy = new CreatedBy(); // Use the standalone CreatedBy class
            UserId userId = new UserId(); // Use the standalone UserId class

// Set the user ID details
            userId.setSystemID("2025a.integrative.nagar.yuval"); // Replace with the correct system ID
            userId.setEmail("z@g.com"); // Replace with the correct user email

// Set the CreatedBy object
            createdBy.setUserId(userId);

// Set the other ObjectBoundary details
            newRoom.setAlias(roomName);
            newRoom.setType("Room");
            newRoom.setStatus("Active");
            newRoom.setActive(true);
            newRoom.setCreatedBy(createdBy);

// Set default location
            ObjectBoundary.Location location = new ObjectBoundary.Location();
            location.setLat(0.0);
            location.setLng(0.0);
            newRoom.setLocation(location);

// Set empty objectDetails
            newRoom.setObjectDetails(new HashMap<>());


            // Make the API call to create the room
            ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
            Gson gson = new Gson();
            String jsonRequest = gson.toJson(newRoom);
            Log.d("CreateRoomRequest", jsonRequest);

            apiService.createRoom(newRoom).enqueue(new Callback<ObjectBoundary>() {
                @Override
                public void onResponse(Call<ObjectBoundary> call, Response<ObjectBoundary> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        // Successfully created room
                        Room room = new Room(response.body().getAlias(), response.body().isActive());
                        roomList.add(room);
                        roomAdapter.notifyItemInserted(roomList.size() - 1);
                        Toast.makeText(OperatorActivity.this, "Room created: " + response.body().getAlias(), Toast.LENGTH_SHORT).show();
                    } else {
                        // Log the error response for debugging
                        try {
                            String errorBody = response.errorBody() != null ? response.errorBody().string() : "Unknown error";
                            Log.e("API Error", "Failed to create room: " + errorBody);
                            Toast.makeText(OperatorActivity.this, "Failed to create room: " + errorBody, Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            Log.e("API Error", "Error reading errorBody", e);
                            Toast.makeText(OperatorActivity.this, "Failed to create room: Unable to parse error", Toast.LENGTH_SHORT).show();
                        }
                    }
                }


                @Override
                public void onFailure(Call<ObjectBoundary> call, Throwable t) {
                    Toast.makeText(OperatorActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            dialog.dismiss();
        });

        // Show the popup dialog
        dialog.show();
    }

}
