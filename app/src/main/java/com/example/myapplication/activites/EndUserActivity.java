package com.example.myapplication.activites;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.activites.LoginActivity;
import com.example.myapplication.activites.ProfileActivity;
import com.example.myapplication.adapters.RoomAdapter;
import com.example.myapplication.api.ApiClient;
import com.example.myapplication.api.ApiService;
import com.example.myapplication.handlers.PopupHandler;
import com.example.myapplication.models.Lightbulb;
import com.example.myapplication.models.ObjectBoundary;
import com.example.myapplication.models.Room;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EndUserActivity extends AppCompatActivity {

    private List<Room> roomList;
    private RoomAdapter roomAdapter;

    private String systemID;
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_user); // Use the EndUser layout

        // Retrieve user data from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        systemID = sharedPreferences.getString("loggedInSystemID", "");
        userEmail = sharedPreferences.getString("loggedInEmail", "");

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
        roomAdapter = new RoomAdapter(roomList, this, popupHandler, false); // Pass false for end user
        roomRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        roomRecyclerView.setAdapter(roomAdapter);

        // Profile Button
        ImageButton profileButton = findViewById(R.id.profile_button);
        profileButton.setOnClickListener(v -> {
            Intent intent = new Intent(EndUserActivity.this, ProfileActivity.class);
            startActivity(intent);
        });

        // Fetch room data
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
                            room.setId(boundary.getObjectId().getId());
                        }
                        room.setLightbulbs(fetchLightbulbsForRoom(boundary));
                        roomList.add(room);
                    }
                    roomAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(EndUserActivity.this, "Failed to fetch rooms: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ObjectBoundary>> call, Throwable t) {
                Toast.makeText(EndUserActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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
}
