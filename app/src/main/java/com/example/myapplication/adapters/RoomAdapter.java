package com.example.myapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.handlers.PopupHandler;
import com.example.myapplication.models.Room;
import com.google.android.material.textview.MaterialTextView;

import java.util.List;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder> {

    private final List<Room> rooms;
    private final Context context;
    private final PopupHandler popupHandler;
    private final boolean isOperator;

    public RoomAdapter(List<Room> rooms, Context context, PopupHandler popupHandler, boolean isOperator) {
        this.rooms = rooms;
        this.context = context;
        this.popupHandler = popupHandler;
        this.isOperator = isOperator;
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

        // Open lightbulb management popup
        holder.itemView.setOnClickListener(v -> popupHandler.showRoomPopup(room, isOperator));
    }

    @Override
    public int getItemCount() {
        return rooms.size();
    }

    public static class RoomViewHolder extends RecyclerView.ViewHolder {
        MaterialTextView roomNameTextView;

        public RoomViewHolder(@NonNull View itemView) {
            super(itemView);
            roomNameTextView = itemView.findViewById(R.id.roomNameTextView);
        }
    }
}
