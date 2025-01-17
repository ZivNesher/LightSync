package com.example.myapplication.handlers;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.Toast;
import yuku.ambilwarna.AmbilWarnaDialog;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapters.LightbulbAdapter;
import com.example.myapplication.models.Lightbulb;
import com.example.myapplication.models.Room;

import java.util.ArrayList;

public class PopupHandler {

    private final Context context;

    public PopupHandler(Context context) {
        this.context = context;
    }

    public void showRoomPopup(Room room, boolean isOperator) {
        // Inflate the popup layout for lightbulbs
        View popupView = LayoutInflater.from(context).inflate(R.layout.lightbulb_popup, null);

        // RecyclerView for lightbulbs
        RecyclerView lightbulbRecyclerView = popupView.findViewById(R.id.lightbulbRecyclerView);
        lightbulbRecyclerView.setLayoutManager(new LinearLayoutManager(context));

        // Lightbulb data for the room
        ArrayList<Lightbulb> lightbulbs = (ArrayList<Lightbulb>) room.getLightbulbs();
        LightbulbAdapter adapter = new LightbulbAdapter(lightbulbs, context, this, isOperator);
        lightbulbRecyclerView.setAdapter(adapter);

        // Add Lightbulb button (only visible for operators)
        Button addLightbulbButton = popupView.findViewById(R.id.add_lightbulb_button);
        if (isOperator) {
            addLightbulbButton.setVisibility(View.VISIBLE);
            addLightbulbButton.setOnClickListener(v -> showAddLightbulbPopup(lightbulbs, adapter));
        } else {
            addLightbulbButton.setVisibility(View.GONE);
        }

        // Close button
        Button closeButton = popupView.findViewById(R.id.close_popup_button);
        closeButton.setOnClickListener(v -> {
            AlertDialog dialog = new AlertDialog.Builder(context)
                    .setView(popupView)
                    .setCancelable(true)
                    .create();
            dialog.dismiss();
        });

        // Show the popup
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setView(popupView)
                .setCancelable(true);
        builder.create().show();
    }


    private void showAddLightbulbPopup(ArrayList<Lightbulb> lightbulbs, LightbulbAdapter adapter) {
        // Inflate the popup layout for adding a lightbulb
        View addPopupView = LayoutInflater.from(context).inflate(R.layout.add_lightbulb_popup, null);
        EditText lightbulbNameEditText = addPopupView.findViewById(R.id.lightbulb_name_input);
        Button addButton = addPopupView.findViewById(R.id.add_button);
        Button cancelButton = addPopupView.findViewById(R.id.cancel_button);

        // Create the dialog for the popup
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(addPopupView)
                .setCancelable(false)
                .create();

        // Cancel button listener
        cancelButton.setOnClickListener(v -> dialog.dismiss());

        // Add button listener
        addButton.setOnClickListener(v -> {
            String name = lightbulbNameEditText.getText().toString().trim();
            if (name.isEmpty()) {
                Toast.makeText(context, "Lightbulb name cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            // Add the new lightbulb to the list and update the adapter
            lightbulbs.add(new Lightbulb(name, false, 0 , 122222));
            adapter.notifyItemInserted(lightbulbs.size() - 1);
            Toast.makeText(context, "Lightbulb added: " + name, Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        dialog.show();
    }

    public void showSettingsPopup(Lightbulb lightbulb, LightbulbAdapter adapter, int position, boolean isOperator) {
        View settingsView = LayoutInflater.from(context).inflate(R.layout.settings_popup, null);

        SeekBar brightnessSeekBar = settingsView.findViewById(R.id.brightness_seekbar);
        Button colorPickerButton = settingsView.findViewById(R.id.color_picker_button);
        Button deleteButton = settingsView.findViewById(R.id.delete_button);
        Button saveButton = settingsView.findViewById(R.id.save_button);

        // Initialize SeekBar
        brightnessSeekBar.setProgress(lightbulb.getBrightness());
        brightnessSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                lightbulb.setBrightness(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // Handle Color Picker Button
        colorPickerButton.setOnClickListener(v -> {
            AmbilWarnaDialog colorPickerDialog = new AmbilWarnaDialog(context, lightbulb.getColor(), new AmbilWarnaDialog.OnAmbilWarnaListener() {
                @Override
                public void onOk(AmbilWarnaDialog dialog, int color) {
                    lightbulb.setColor(color); // Save the selected color
                    Toast.makeText(context, "Color updated!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancel(AmbilWarnaDialog dialog) {
                    // No action needed
                }
            });
            colorPickerDialog.show();
        });

        // Delete button (only for operators)
        if (isOperator) {
            deleteButton.setVisibility(View.VISIBLE);
            deleteButton.setOnClickListener(v -> {
                adapter.getLightbulbs().remove(position);
                adapter.notifyItemRemoved(position);
                Toast.makeText(context, "Lightbulb deleted", Toast.LENGTH_SHORT).show();
            });
        } else {
            deleteButton.setVisibility(View.GONE);
        }

        // Save button
        saveButton.setOnClickListener(v -> {
            Toast.makeText(context, "Settings saved!", Toast.LENGTH_SHORT).show();
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setView(settingsView)
                .setCancelable(true);
        builder.create().show();
    }



}
