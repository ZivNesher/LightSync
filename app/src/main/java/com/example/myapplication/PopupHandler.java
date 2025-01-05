package com.example.myapplication;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class PopupHandler {

    private final Context context;
    private TableLayout mainTable;
    private final ArrayList<String> products;

    public PopupHandler(Context context) {
        this.context = context;
        this.products = new ArrayList<>(Arrays.asList("Milk", "Eggs", "Cheese", "Bread", "Butter"));
    }

    public void setupMainActivity() {
        ImageButton plusButton = ((MainActivity) context).findViewById(R.id.plus_button);
        mainTable = ((MainActivity) context).findViewById(R.id.main_table);

        // Set OnClickListener for the plus button
        plusButton.setOnClickListener(view -> {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View popupView = inflater.inflate(R.layout.popup, null);

            // Get screen dimensions
            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((MainActivity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int screenWidth = displayMetrics.widthPixels;
            int screenHeight = displayMetrics.heightPixels;

            PopupWindow popupWindow = new PopupWindow(
                    popupView,
                    screenWidth * 7 / 8,
                    screenHeight * 11 / 16,
                    true
            );

            Spinner spinner1 = popupView.findViewById(R.id.spinner1);
            EditText numberInput = popupView.findViewById(R.id.numberInput);
            Spinner spinner2 = popupView.findViewById(R.id.spinner2);
            EditText addProductInput = popupView.findViewById(R.id.add_product_input);
            Button addProductButton = popupView.findViewById(R.id.add_product_button);
            Button saveToTableButton = popupView.findViewById(R.id.save_to_table_button);
            Button closePopupButton = popupView.findViewById(R.id.close_popup_button);

            ArrayAdapter<String> adapter1 = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, products);
            adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner1.setAdapter(adapter1);

            ArrayAdapter<String> adapter2 = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, new String[]{"ML", "Litter", "KG", "Gram"});
            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter2);

            addProductButton.setOnClickListener(v -> {
                String newProduct = addProductInput.getText().toString().trim();
                if (!newProduct.isEmpty() && !products.contains(newProduct)) {
                    products.add(newProduct);
                    adapter1.notifyDataSetChanged();
                    addProductInput.setText("");
                } else {
                    addProductInput.setError("Invalid or duplicate product");
                }
            });

            saveToTableButton.setOnClickListener(v -> {
                // Save product to table logic
            });

            closePopupButton.setOnClickListener(v -> popupWindow.dismiss());

            popupWindow.showAtLocation(((MainActivity) context).findViewById(android.R.id.content), android.view.Gravity.CENTER, 0, 0);
        });
    }
}
