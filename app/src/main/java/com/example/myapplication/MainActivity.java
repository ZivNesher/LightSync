package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find the ImageButton
        ImageButton plusButton = findViewById(R.id.plus_button);

        // Set OnClickListener for the button
        plusButton.setOnClickListener(view -> {
            // Inflate the popup layout
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View popupView = inflater.inflate(R.layout.popup, null);

            // Create the PopupWindow
            PopupWindow popupWindow = new PopupWindow(
                    popupView,
                    600, // Width in pixels
                    400, // Height in pixels
                    true // Focusable
            );

            // Set text dynamically if needed (optional)
            TextView popupText = popupView.findViewById(R.id.popup_text);
            popupText.setText("Hello World");

            // Show the popup
            popupWindow.showAsDropDown(plusButton, 0, 0);
        });
    }
}
