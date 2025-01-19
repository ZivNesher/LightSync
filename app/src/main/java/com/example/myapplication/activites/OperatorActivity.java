package com.example.myapplication.activites;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.myapplication.data.RoleEnum;
import com.example.myapplication.handlers.PopupHandler;
import com.example.myapplication.models.CreatedBy;
import com.example.myapplication.models.Lightbulb;
import com.example.myapplication.models.ObjectBoundary;
import com.example.myapplication.models.Room;
import com.example.myapplication.api.ApiService;
import com.example.myapplication.api.ApiClient;
import com.example.myapplication.models.UserId;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OperatorActivity extends AppCompatActivity {

    private List<Room> roomList;
    private RoomAdapter roomAdapter;

    private String systemID;
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operator);

        // Retrieve user data from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        systemID = sharedPreferences.getString("loggedInSystemID", "");
        userEmail = sharedPreferences.getString("loggedInEmail", "");
        String roleString = sharedPreferences.getString("loggedInRole", "END_USER"); // Default to END_USER

        RoleEnum userRole;
        try {
            userRole = RoleEnum.valueOf(roleString); // Convert the string to RoleEnum
        } catch (IllegalArgumentException | NullPointerException e) {
            userRole = RoleEnum.END_USER; // Fallback to default role
        }

        if (systemID.isEmpty() || userEmail.isEmpty()) {
            Toast.makeText(this, "User not logged in. Please login again.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        // Initialize RecyclerView
        RecyclerView roomRecyclerView = findViewById(R.id.roomRecyclerView);
        roomList = new ArrayList<>();
        PopupHandler popupHandler = new PopupHandler(this);

// Determine the value of isOperator based on RoleEnum
        boolean isOperator = (userRole == RoleEnum.OPERATOR); // true if OPERATORzz, false if END_USER

// Pass the correct isOperator value
        roomAdapter = new RoomAdapter(roomList, this, popupHandler, isOperator);
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
        if (userRole == RoleEnum.END_USER) {
            plusButton.setVisibility(View.GONE); // Hide for END_USER
        } else {
            plusButton.setOnClickListener(v -> showNewRoomPopup());
        }

        // Initialize room data
        fetchRoomsFromBackend();
    }


    private void fetchRoomsFromBackend() {
        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);

        apiService.getRooms(systemID, userEmail, 0, 10).enqueue(new Callback<List<ObjectBoundary>>() {
            @Override
            public void onResponse(Call<List<ObjectBoundary>> call, Response<List<ObjectBoundary>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    roomList.clear();
                    for (ObjectBoundary boundary : response.body()) {
                        Room room = new Room(boundary.getAlias(), boundary.isActive());
                        if (boundary.getObjectId() != null) {
                            room.setId(boundary.getObjectId().getId()); // Store the system-generated ID
                        }
                        room.setLightbulbs(fetchLightbulbsForRoom(boundary));
                        roomList.add(room);
                    }
                    roomAdapter.notifyDataSetChanged();
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



    private List<Lightbulb> fetchLightbulbsForRoom(ObjectBoundary roomBoundary) {
        List<Lightbulb> lightbulbs = new ArrayList<>();
        if (roomBoundary.getObjectDetails() != null && roomBoundary.getObjectDetails().get("lightbulbs") instanceof List) {
            List<Map<String, Object>> lightbulbData = (List<Map<String, Object>>) roomBoundary.getObjectDetails().get("lightbulbs");
            for (Map<String, Object> data : lightbulbData) {
                Lightbulb lightbulb = new Lightbulb(
                        (String) data.get("name"),
                        (Boolean) data.get("isOn"),
                        ((Double) data.get("brightness")).intValue(),
                        ((Double) data.get("color")).intValue()
                );
                lightbulbs.add(lightbulb);
            }
        }
        return lightbulbs;
    }

    public void saveRoomToBackend(Room room) {
        ObjectBoundary updatedRoom = new ObjectBoundary();
        updatedRoom.setAlias(room.getName());
        updatedRoom.setType("Room");
        updatedRoom.setStatus("Active");
        updatedRoom.setActive(room.isActive());

        // Add lightbulbs to objectDetails
        HashMap<String, Object> objectDetails = new HashMap<>();
        objectDetails.put("lightbulbs", room.getSerializedLightbulbs());
        updatedRoom.setObjectDetails(objectDetails);

        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        apiService.updateObject(
                systemID,
                room.getId(),
                systemID,
                userEmail,
                updatedRoom
        ).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(OperatorActivity.this, "Room updated successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(OperatorActivity.this, "Failed to update room: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
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
            userId.setSystemID(systemID); // Replace with the correct system ID
            userId.setEmail(userEmail); // Replace with the correct user email

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

    public void updateRoomInBackend(Room room, List<Lightbulb> updatedLightbulbs) {
        if (room.getId() == null || room.getId().isEmpty()) {
            Toast.makeText(this, "Cannot update room without ID", Toast.LENGTH_SHORT).show();
            return;
        }

        ObjectBoundary roomBoundary = new ObjectBoundary();
        roomBoundary.setAlias(room.getName());
        roomBoundary.setType("Room");
        roomBoundary.setActive(room.isActive());

        // Add lightbulbs to objectDetails
        Map<String, Object> objectDetails = new HashMap<>();
        objectDetails.put("lightbulbs", room.getSerializedLightbulbs());
        roomBoundary.setObjectDetails(objectDetails);

        // Log the payload for debugging
        Gson gson = new Gson();
        String jsonPayload = gson.toJson(roomBoundary);
        Log.d("UpdateRoomPayload", jsonPayload);

        // Make the API call
        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        apiService.updateObject(
                systemID, // systemId
                room.getId(),                  // Use the system-generated ID
                systemID, // userSystemId
                userEmail,                      // userEmail
                roomBoundary
        ).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(OperatorActivity.this, "Room updated successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "Unknown error";
                        Log.e("API Error", "Failed to update room: " + errorBody);
                        Toast.makeText(OperatorActivity.this, "Failed to update room: " + errorBody, Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Log.e("API Error", "Error reading errorBody", e);
                        Toast.makeText(OperatorActivity.this, "Failed to update room: Unable to parse error", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("API Error", "Failed to update room", t);
                Toast.makeText(OperatorActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }




}
