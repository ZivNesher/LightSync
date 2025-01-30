package com.example.myapplication.activites;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;
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

        // Determine if user is an operator
        boolean isOperator = (userRole == RoleEnum.OPERATOR);

        // Setup Room Adapter
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

        // Switch for toggling all lights
        Switch lightbulbSwitch = findViewById(R.id.main_switch);
        lightbulbSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                toggleAllLights(true);
            } else {
                toggleAllLights(false);
            }
        });

        // 🌟 Add the Spinner (Dropdown)
        setupRoomActionsSpinner();

        // Initialize room data
        fetchRoomsFromBackend();
    }

    private void setupRoomActionsSpinner() {
        Spinner actionSpinner = findViewById(R.id.room_action_spinner);

        // Define options
        String[] actions = {
                "Select Action",
                "Change all lights to blue",
                "Turn on only the first 2 rooms",
                "Make a party lights"
        };

        // Set up adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, actions);
        actionSpinner.setAdapter(adapter);

        // Handle selection
        actionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 1: // Change all lights to blue
                        changeAllLightsToColor(0x0000FF); // Blue color
                        break;
                    case 2: // Turn on only the first 2 rooms
                        turnOnFirstTwoRooms();
                        break;
                    case 3: // Make a party lights mode
                        startPartyMode();
                        break;
                    default:
                        // Do nothing if "Select Action" is chosen
                        break;
                }
                // Reset dropdown selection to "Select Action" after applying action
                actionSpinner.setSelection(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void changeAllLightsToColor(int color) {
        for (Room room : roomList) {
            for (Lightbulb lightbulb : room.getLightbulbs()) {
                lightbulb.setColor(color);
            }
            saveRoomToBackend(room);
        }
        roomAdapter.notifyDataSetChanged();
        Toast.makeText(this, "All lights changed to blue!", Toast.LENGTH_SHORT).show();
    }

    private void turnOnFirstTwoRooms() {
        int count = 0;
        for (Room room : roomList) {
            if (count < 2) {
                for (Lightbulb lightbulb : room.getLightbulbs()) {
                    lightbulb.setOn(true);
                }
                saveRoomToBackend(room);
                count++;
            }
        }
        roomAdapter.notifyDataSetChanged();
        Toast.makeText(this, "First two rooms' lights turned on!", Toast.LENGTH_SHORT).show();
    }

    private void startPartyMode() {
        for (Room room : roomList) {
            for (Lightbulb lightbulb : room.getLightbulbs()) {
                int randomColor = (int)(Math.random() * 0xFFFFFF); // Generate random color
                lightbulb.setColor(randomColor);
                lightbulb.setBrightness(100);
                lightbulb.setOn(true);
            }
            saveRoomToBackend(room);
        }
        roomAdapter.notifyDataSetChanged();
        Toast.makeText(this, "Party mode activated!", Toast.LENGTH_SHORT).show();
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
        View popupView = LayoutInflater.from(this).inflate(R.layout.new_room_popup, null);
        EditText newRoomNameEditText = popupView.findViewById(R.id.new_room_name);
        Button cancelButton = popupView.findViewById(R.id.cancel_button);
        Button createButton = popupView.findViewById(R.id.create_button);
        AlertDialog dialog = new AlertDialog.Builder(this).setView(popupView).setCancelable(false).create();

        cancelButton.setOnClickListener(v -> dialog.dismiss());

        createButton.setOnClickListener(v -> {
            String roomName = newRoomNameEditText.getText().toString().trim();
            if (roomName.isEmpty()) {
                Toast.makeText(this, "Room name cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            ObjectBoundary newRoom = new ObjectBoundary();
            CreatedBy createdBy = new CreatedBy();
            UserId userId = new UserId();
            userId.setSystemID(systemID);
            userId.setEmail(userEmail);
            createdBy.setUserId(userId);
            newRoom.setAlias(roomName);
            newRoom.setType("Room");
            newRoom.setStatus("Active");
            newRoom.setActive(true);
            newRoom.setCreatedBy(createdBy);
            newRoom.setLocation(new ObjectBoundary.Location());

            ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
            apiService.createRoom(newRoom).enqueue(new Callback<ObjectBoundary>() {
                @Override
                public void onResponse(Call<ObjectBoundary> call, Response<ObjectBoundary> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Room room = new Room(response.body().getAlias(), response.body().isActive());
                        room.setId(response.body().getObjectId().getId());
                        roomList.add(room);
                        roomAdapter.notifyItemInserted(roomList.size() - 1);
                        fetchRoomsFromBackend();
                        Toast.makeText(OperatorActivity.this, "Room created: " + response.body().getAlias(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ObjectBoundary> call, Throwable t) {
                    Toast.makeText(OperatorActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            dialog.dismiss();
        });

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

        Map<String, Object> objectDetails = new HashMap<>();
        objectDetails.put("lightbulbs", room.getSerializedLightbulbs());
        roomBoundary.setObjectDetails(objectDetails);

        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        apiService.updateObject(systemID, room.getId(), systemID, userEmail, roomBoundary)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(OperatorActivity.this, "Room updated successfully!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(OperatorActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void toggleAllLights(boolean turnOn) {
        for (Room room : roomList) {
            // Update the state of all lightbulbs in the room
            for (Lightbulb lightbulb : room.getLightbulbs()) {
                lightbulb.setOn(turnOn); // Update the lightbulb's state
            }

            // Save the updated room to the backend
            saveRoomToBackend(room);
        }

        // Refresh the UI to reflect changes
        roomAdapter.notifyDataSetChanged();
    }

    private void updateLightsInBackend(boolean turnOn) {
        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);

        for (Room room : roomList) {
            for (Lightbulb lightbulb : room.getLightbulbs()) {
                ObjectBoundary lightUpdate = new ObjectBoundary();
                lightUpdate.setAlias(lightbulb.getName());
                lightUpdate.setActive(turnOn);

                apiService.updateObject(systemID, lightbulb.getId(), systemID, userEmail, lightUpdate)
                        .enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.isSuccessful()) {
                                    Log.d("BackendUpdate", "Light state updated successfully");
                                } else {
                                    Log.e("BackendUpdate", "Failed to update light state: " + response.message());
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Log.e("BackendUpdate", "Error updating light state: " + t.getMessage());
                            }
                        });
            }
        }
    }





}
