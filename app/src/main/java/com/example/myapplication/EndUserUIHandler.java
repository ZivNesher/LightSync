package com.example.myapplication;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TableLayout;

import java.util.ArrayList;
import java.util.Arrays;

public class EndUserUIHandler {

    private final Context context;
    private final ArrayList<String> products;

    public EndUserUIHandler(Context context) {
        this.context = context;
        this.products = new ArrayList<>(Arrays.asList("Milk", "Eggs", "Cheese", "Bread", "Butter"));
    }

    public void setupEndUserUI() {
        TableLayout mainTable = ((AdminAPI) context).findViewById(R.id.main_table);
        ImageButton plusButton = ((AdminAPI) context).findViewById(R.id.plus_button);

        // Setup Plus Button to show popup
        plusButton.setOnClickListener(view -> {
            // Inflate popup layout
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View popupView = inflater.inflate(R.layout.popup, null);

            // Configure popup size
            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((AdminAPI) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int screenWidth = displayMetrics.widthPixels;
            int screenHeight = displayMetrics.heightPixels;

            PopupWindow popupWindow = new PopupWindow(
                    popupView,
                    screenWidth * 7 / 8,
                    screenHeight * 11 / 16,
                    true
            );

            // Initialize popup components
            Spinner spinner1 = popupView.findViewById(R.id.spinner1);
            Spinner spinner2 = popupView.findViewById(R.id.spinner2);

            ArrayAdapter<String> adapter1 = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, products);
            adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner1.setAdapter(adapter1);

            ArrayAdapter<String> adapter2 = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, new String[]{"ML", "Litter", "KG", "Gram"});
            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter2);

            // Show popup
            popupWindow.showAtLocation(((AdminAPI) context).findViewById(android.R.id.content), Gravity.CENTER, 0, 0);
        });
    }
}
