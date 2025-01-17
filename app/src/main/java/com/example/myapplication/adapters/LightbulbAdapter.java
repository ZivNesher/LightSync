package com.example.myapplication.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.handlers.PopupHandler;
import com.example.myapplication.models.Lightbulb;
import com.google.android.material.textview.MaterialTextView;

import java.util.List;

public class LightbulbAdapter extends RecyclerView.Adapter<LightbulbAdapter.LightbulbViewHolder> {
    private final List<Lightbulb> lightbulbs;
    private final Context context;
    private final PopupHandler popupHandler;
    private final boolean isOperator; // Flag to check the role

    // Updated constructor to include isOperator flag
    public LightbulbAdapter(List<Lightbulb> lightbulbs, Context context, PopupHandler popupHandler, boolean isOperator) {
        this.lightbulbs = lightbulbs;
        this.context = context;
        this.popupHandler = popupHandler;
        this.isOperator = isOperator; // Set role flag
    }

    @NonNull
    @Override
    public LightbulbViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.lightbulb_item, parent, false);
        return new LightbulbViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LightbulbViewHolder holder, int position) {
        Lightbulb lightbulb = lightbulbs.get(position);
        holder.lightbulbName.setText(lightbulb.getName());
        holder.lightbulbSwitch.setChecked(lightbulb.isOn());

        // Update the lightbulb's on/off state
        holder.lightbulbSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> lightbulb.setOn(isChecked));

        // Open the settings popup when the settings button is clicked
        holder.settingsButton.setOnClickListener(v -> popupHandler.showSettingsPopup(lightbulb, this, position, isOperator));

        // Hide delete options if not an operator
        if (!isOperator) {
            holder.settingsButton.setVisibility(View.GONE); // Optionally hide the settings button for end users
        }
    }

    @Override
    public int getItemCount() {
        return lightbulbs.size();
    }

    // Method to return the lightbulb list
    public List<Lightbulb> getLightbulbs() {
        return lightbulbs;
    }

    public static class LightbulbViewHolder extends RecyclerView.ViewHolder {
        MaterialTextView lightbulbName;
        Switch lightbulbSwitch;
        ImageButton settingsButton;

        @SuppressLint("WrongViewCast")
        public LightbulbViewHolder(@NonNull View itemView) {
            super(itemView);
            lightbulbName = itemView.findViewById(R.id.lightbulb_name);
            lightbulbSwitch = itemView.findViewById(R.id.lightbulb_switch);
            settingsButton = itemView.findViewById(R.id.lightbulb_settings_button);
        }
    }
}
