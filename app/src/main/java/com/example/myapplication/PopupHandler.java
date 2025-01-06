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
    private final TableLayout mainTable;
    private final ArrayList<String> products;
    private boolean isPopupActive = false;

    public PopupHandler(Context context, TableLayout mainTable) {
        this.context = context;
        this.mainTable = mainTable;
        this.products = new ArrayList<>(Arrays.asList("Milk", "Eggs", "Cheese", "Bread", "Butter"));
    }

    public void setupPlusButton(ImageButton plusButton) {
        plusButton.setOnClickListener(view -> {
            if (isPopupActive) return; // Prevent overlapping popups

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View popupView = inflater.inflate(R.layout.popup, null);

            // Get screen dimensions
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            int screenWidth = displayMetrics.widthPixels;
            int screenHeight = displayMetrics.heightPixels;

            PopupWindow popupWindow = new PopupWindow(
                    popupView,
                    screenWidth * 7 / 8,
                    screenHeight * 11 / 16,
                    true
            );

            isPopupActive = true;

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
                String product = spinner1.getSelectedItem().toString();
                String unit = spinner2.getSelectedItem().toString();
                int amount;

                try {
                    amount = Integer.parseInt(numberInput.getText().toString());
                } catch (NumberFormatException e) {
                    numberInput.setError("Please enter a valid number");
                    return;
                }

                // Add product to the table
                addProductToTable(product, amount, unit);
                popupWindow.dismiss();
            });

            closePopupButton.setOnClickListener(v -> popupWindow.dismiss());

            popupWindow.setOnDismissListener(() -> isPopupActive = false);

            if (context instanceof AdminAPI) {
                popupWindow.showAtLocation(((AdminAPI) context).findViewById(android.R.id.content), android.view.Gravity.CENTER, 0, 0);
            } else if (context instanceof MainActivity) {
                popupWindow.showAtLocation(((MainActivity) context).findViewById(android.R.id.content), android.view.Gravity.CENTER, 0, 0);
            } else {
                throw new IllegalArgumentException("Context must be an instance of AdminAPI or MainActivity");
            }
        });
    }

    private void addProductToTable(String product, int amount, String unit) {
        boolean productExists = false;

        for (int i = 1; i < mainTable.getChildCount(); i++) {
            TableRow row = (TableRow) mainTable.getChildAt(i);
            TextView productCell = (TextView) row.getChildAt(0);
            TextView amountCell = (TextView) row.getChildAt(1);

            if (productCell.getText().toString().equals(product)) {
                String[] amountParts = amountCell.getText().toString().split(" ");
                int currentAmount = Integer.parseInt(amountParts[0]);
                int newAmount = currentAmount + amount;
                amountCell.setText(newAmount + " " + unit);
                productExists = true;
                break;
            }
        }

        if (!productExists) {
            TableRow newRow = new TableRow(context);

            TextView productCell = new TextView(context);
            productCell.setText(product);
            newRow.addView(productCell);

            TextView amountCell = new TextView(context);
            amountCell.setText(amount + " " + unit);
            newRow.addView(amountCell);

            mainTable.addView(newRow);
        }
    }
}
