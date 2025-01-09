package com.example.myapplication.handlers;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.Spinner;

import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.Arrays;

public class EndUserUIHandler {

    private final Context context;
    private final ArrayList<String> products;

    public EndUserUIHandler(Context context) {
        this.context = context;
        this.products = new ArrayList<>(Arrays.asList("Milk", "Eggs", "Cheese", "Bread", "Butter"));
    }

    public void setupEndUserUI(ImageButton plusButton) {
        // Setup Plus Button to show popup
        plusButton.setOnClickListener(view -> {
            // Inflate popup layout
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View popupView = inflater.inflate(R.layout.popup, null);

            // Configure popup size
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            int screenWidth = displayMetrics.widthPixels;
            int screenHeight = displayMetrics.heightPixels;

            PopupWindow popupWindow = new PopupWindow(
                    popupView,
                    screenWidth * 7 / 8,
                    screenHeight * 11 / 16,
                    true
            );

            // Show popup
            popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        });
    }
}
