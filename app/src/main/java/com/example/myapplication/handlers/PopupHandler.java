package com.example.myapplication.handlers;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.activites.OperatorActivity;
import com.example.myapplication.adapters.LightbulbAdapter;
import com.example.myapplication.data.RoleEnum;
import com.example.myapplication.models.Lightbulb;
import com.example.myapplication.models.Room;

import java.util.ArrayList;

import yuku.ambilwarna.AmbilWarnaDialog;

public class PopupHandler {

    private final Context context;

    public PopupHandler(Context context) {
        this.context = context;
    }

    public void showRoomPopup(Room room, boolean isOperator, RoleEnum userRole) {
        // Inflate the popup layout for lightbulbs
        View popupView = LayoutInflater.from(context).inflate(R.layout.lightbulb_popup, null);

        // RecyclerView for lightbulbs
        RecyclerView lightbulbRecyclerView = popupView.findViewById(R.id.lightbulbRecyclerView);
        lightbulbRecyclerView.setLayoutManager(new LinearLayoutManager(context));

        // Lightbulb data for the room
        ArrayList<Lightbulb> lightbulbs = (ArrayList<Lightbulb>) room.getLightbulbs();
        LightbulbAdapter adapter = new LightbulbAdapter(lightbulbs, context, this, isOperator, room);
        lightbulbRecyclerView.setAdapter(adapter);

        // Add Lightbulb button
        Button addLightbulbButton = popupView.findViewById(R.id.add_lightbulb_button);

        // Role-based logic for button visibility
        if (!isOperator) {
            // Hide the button for END_USER
            Log.d("PopupHandler", "Hiding Add Lightbulb Button for END_USER");
            addLightbulbButton.setVisibility(View.GONE);
        } else if ( isOperator == true) {
            // Show the button for OPERATOR
            Log.d("PopupHandler", "Showing Add Lightbulb Button for OPERATOR");
            addLightbulbButton.setVisibility(View.VISIBLE);
            addLightbulbButton.setOnClickListener(v -> showAddLightbulbPopup(lightbulbs, adapter, room));
        } else {
            // Default case: Hide the button
            Log.d("PopupHandler", "Default Case - Hiding Add Lightbulb Button");
            addLightbulbButton.setVisibility(View.GONE);
        }

        // Close button
        Button closeButton = popupView.findViewById(R.id.close_popup_button);
        closeButton.setOnClickListener(v -> {
            // Dismiss the popup when the close button is clicked
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




    private void showAddLightbulbPopup(ArrayList<Lightbulb> lightbulbs, LightbulbAdapter adapter, Room room) {
        View addPopupView = LayoutInflater.from(context).inflate(R.layout.add_lightbulb_popup, null);
        EditText lightbulbNameEditText = addPopupView.findViewById(R.id.lightbulb_name_input);
        Button addButton = addPopupView.findViewById(R.id.add_button);
        Button cancelButton = addPopupView.findViewById(R.id.cancel_button);

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(addPopupView)
                .setCancelable(false)
                .create();

        cancelButton.setOnClickListener(v -> dialog.dismiss());

        addButton.setOnClickListener(v -> {
            String name = lightbulbNameEditText.getText().toString().trim();
            if (name.isEmpty()) {
                Toast.makeText(context, "Lightbulb name cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            // Add the lightbulb to the list
            Lightbulb newLightbulb = new Lightbulb(name, false, 0, 0xFFFFFF);
            lightbulbs.add(newLightbulb);
            adapter.notifyItemInserted(lightbulbs.size() - 1);

            // Update room in backend
            if (context instanceof OperatorActivity) {
                ((OperatorActivity) context).updateRoomInBackend(room, lightbulbs); // Pass room and updated lightbulbs
            }

            Toast.makeText(context, "Lightbulb added: " + name, Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        dialog.show();
    }

    public void showSettingsPopup(Lightbulb lightbulb, LightbulbAdapter adapter, int position, boolean isOperator, String userRole) {
        View settingsView = LayoutInflater.from(context).inflate(R.layout.settings_popup, null);

        SeekBar brightnessSeekBar = settingsView.findViewById(R.id.brightness_seekbar);
        Button colorPickerButton = settingsView.findViewById(R.id.color_picker_button);
        Button deleteButton = settingsView.findViewById(R.id.delete_button);
        if (userRole.equals("END_USER")) {
            deleteButton.setVisibility(View.GONE);
        }
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
        if (isOperator&&userRole.equals("OPERATOR")) {
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
