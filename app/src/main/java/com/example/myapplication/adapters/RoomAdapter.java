package com.example.myapplication.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.activites.OperatorActivity;
import com.example.myapplication.data.RoleEnum;
import com.example.myapplication.handlers.PopupHandler;
import com.example.myapplication.models.Lightbulb;
import com.example.myapplication.models.Room;
import com.google.android.material.textview.MaterialTextView;

import java.util.List;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder> {

    private final List<Room> rooms;
    private final Context context;
    private final PopupHandler popupHandler;
    private final boolean isOperator;
    private RoleEnum userRole;

    public RoomAdapter(List<Room> rooms, Context context, PopupHandler popupHandler, boolean isOperator) {
        this.rooms = rooms;
        this.context = context;
        this.popupHandler = popupHandler;
        this.isOperator = isOperator;

        // Retrieve user role from SharedPreferences and convert it to RoleEnum
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        String roleString = sharedPreferences.getString("loggedInUserRole", "END_USER"); // Default to END_USER
        try {
            this.userRole = RoleEnum.valueOf(roleString); // Convert string to RoleEnum
        } catch (IllegalArgumentException | NullPointerException e) {
            this.userRole = RoleEnum.END_USER; // Fallback to default role
        }
    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.room_item, parent, false);
        return new RoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {
        Room room = rooms.get(position);
        holder.roomNameTextView.setText(room.getName());

        // Initialize switch state based on lightbulb states
        boolean allLightsOn = areAllLightsOn(room);
        holder.roomLightSwitch.setChecked(allLightsOn);

        // Allow switch toggle only for operators
        holder.roomLightSwitch.setEnabled(isOperator);

        // Handle switch toggle
        holder.roomLightSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!buttonView.isPressed()) return; // Prevent unwanted triggers during UI refresh

            toggleRoomLights(room, isChecked);
        });

        // Open lightbulb management popup
        holder.itemView.setOnClickListener(v -> popupHandler.showRoomPopup(room, isOperator, userRole));
    }

    @Override
    public int getItemCount() {
        return rooms.size();
    }

    public static class RoomViewHolder extends RecyclerView.ViewHolder {
        MaterialTextView roomNameTextView;
        Switch roomLightSwitch;

        public RoomViewHolder(@NonNull View itemView) {
            super(itemView);
            roomNameTextView = itemView.findViewById(R.id.roomNameTextView);
            roomLightSwitch = itemView.findViewById(R.id.room_light_switch);
        }
    }

    /**
     * Checks if all lights in the room are ON.
     */
    private boolean areAllLightsOn(Room room) {
        for (Lightbulb lightbulb : room.getLightbulbs()) {
            if (!lightbulb.isOn()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Toggles all lights in the room and updates the backend.
     */
    private void toggleRoomLights(Room room, boolean turnOn) {
        for (Lightbulb lightbulb : room.getLightbulbs()) {
            lightbulb.setOn(turnOn);
        }

        // Update the backend with the new state
        if (context instanceof OperatorActivity) {
            ((OperatorActivity) context).saveRoomToBackend(room);
        }

        // Refresh UI
        notifyDataSetChanged();
    }
}
