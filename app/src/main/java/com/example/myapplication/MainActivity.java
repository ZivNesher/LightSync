package com.example.myapplication;

import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Gravity;
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
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private TableLayout mainTable;
    private ArrayList<String> products; // Make product list a class-level variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the initial loading screen layout
        setContentView(R.layout.loading_screen);

        // Initialize the product list once
        products = new ArrayList<>(Arrays.asList("Milk", "Eggs", "Cheese", "Bread", "Butter"));

        // Delay for 3 seconds before switching to the login screen
        new Handler().postDelayed(() -> {
            // Set the login screen layout
            setContentView(R.layout.login_screen);

            // Initialize the login screen logic
            setupLoginScreen();
        }, 3000); // 3 seconds delay
    }

    private void setupLoginScreen() {
        // Find the Login and Sign Up buttons
        Button loginButton = findViewById(R.id.login_button);
        Button signUpButton = findViewById(R.id.signup_button);

        // Set OnClickListener for the Login button
        loginButton.setOnClickListener(v -> {
            // Move to the main activity layout
            setContentView(R.layout.activity_main);

            // Initialize the rest of your main activity logic here
            setupMainActivity();
        });

        // Set OnClickListener for the Sign Up button
        signUpButton.setOnClickListener(v -> {
            // Move to the sign-up screen
            setContentView(R.layout.sign_up_screen);

            // Initialize the sign-up screen logic
            setupSignUpScreen();
        });
    }

    private void setupSignUpScreen() {
        // Find the Back to Login button
        Button backToLoginButton = findViewById(R.id.back_to_login_button);

        // Set OnClickListener for the Back to Login button
        backToLoginButton.setOnClickListener(v -> {
            // Move back to the login screen
            setContentView(R.layout.login_screen);

            // Reinitialize the login screen logic
            setupLoginScreen();
        });
    }

    private void setupMainActivity() {
        // Find the ImageButton and main table
        ImageButton plusButton = findViewById(R.id.plus_button);
        mainTable = findViewById(R.id.main_table);

        // Set OnClickListener for the plus button
        plusButton.setOnClickListener(view -> {
            // Inflate the popup layout
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View popupView = inflater.inflate(R.layout.popup, null);

            // Get screen dimensions
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int screenWidth = displayMetrics.widthPixels;
            int screenHeight = displayMetrics.heightPixels;

            // Create the PopupWindow with specific size
            PopupWindow popupWindow = new PopupWindow(
                    popupView,
                    screenWidth * 7 / 8,
                    screenHeight * 11 / 16,
                    true // Focusable
            );

            // Initialize popup components
            Spinner spinner1 = popupView.findViewById(R.id.spinner1);
            EditText numberInput = popupView.findViewById(R.id.numberInput);
            Spinner spinner2 = popupView.findViewById(R.id.spinner2);
            EditText addProductInput = popupView.findViewById(R.id.add_product_input);
            Button addProductButton = popupView.findViewById(R.id.add_product_button);
            Button saveToTableButton = popupView.findViewById(R.id.save_to_table_button);
            Button closePopupButton = popupView.findViewById(R.id.close_popup_button);

            // Set up adapters using the class-level products list
            ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, products);
            adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner1.setAdapter(adapter1);

            ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new String[]{"ML", "Litter", "KG", "Gram"});
            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter2);

            // Handle "Add Product" button click
            addProductButton.setOnClickListener(v -> {
                String newProduct = addProductInput.getText().toString().trim();
                if (!newProduct.isEmpty() && !products.contains(newProduct)) {
                    products.add(newProduct); // Add the new product to the list
                    adapter1.notifyDataSetChanged(); // Update the adapter
                    addProductInput.setText(""); // Clear the input field
                } else {
                    addProductInput.setError(newProduct.isEmpty() ? "Product name cannot be empty" : "Product already exists");
                }
            });

            // Handle "Save to Table" button click
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

                // Check if the product already exists in the table
                boolean productExists = false;
                for (int i = 1; i < mainTable.getChildCount(); i++) { // Start at 1 to skip header row
                    TableRow row = (TableRow) mainTable.getChildAt(i);
                    TextView productCell = (TextView) row.getChildAt(0); // First column
                    TextView amountCell = (TextView) row.getChildAt(1); // Second column

                    if (productCell.getText().toString().equals(product)) {
                        // Update the amount
                        String[] amountParts = amountCell.getText().toString().split(" ");
                        int currentAmount = Integer.parseInt(amountParts[0]);
                        int newAmount = currentAmount + amount;
                        amountCell.setText(newAmount + " " + unit);
                        productExists = true;
                        break;
                    }
                }

                if (!productExists) {
                    // Add a new row if the product doesn't exist
                    TableRow newRow = new TableRow(this);

                    TextView productCell = new TextView(this);
                    productCell.setText(product);
                    newRow.addView(productCell);

                    TextView amountCell = new TextView(this);
                    amountCell.setText(amount + " " + unit);
                    newRow.addView(amountCell);

                    mainTable.addView(newRow);
                }

                // Clear popup inputs for reuse
                numberInput.setText("");
                spinner1.setSelection(0);
                spinner2.setSelection(0);
            });

            // Close popup functionality
            closePopupButton.setOnClickListener(v -> popupWindow.dismiss());

            // Show the PopupWindow at the center of the screen
            popupWindow.showAtLocation(findViewById(android.R.id.content), Gravity.CENTER, 0, 0);
        });
    }
}
