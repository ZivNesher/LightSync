package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.Toast;

public class LoginHandler {

    private final Context context;

    public LoginHandler(Context context) {
        this.context = context;
    }

    public void setupLoginScreen() {
        // Find the Login and Sign Up buttons
        Button loginButton = ((MainActivity) context).findViewById(R.id.login_button);
        Button signUpButton = ((MainActivity) context).findViewById(R.id.signup_button);
        EditText usernameInput = ((MainActivity) context).findViewById(R.id.email_input);
        EditText passwordInput = ((MainActivity) context).findViewById(R.id.password_input);

        // Set OnClickListener for the Login button
        loginButton.setOnClickListener(v -> {
            String username = usernameInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (username.equals("Admin") && password.equals("Nimda")) {
                Toast.makeText(context, "Admin login successful", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, AdminAPI.class);
                context.startActivity(intent);
            } else {
                // Regular user flow
                ((MainActivity) context).setContentView(R.layout.activity_main);

                // Initialize TableLayout and Plus Button
                TableLayout mainTable = ((MainActivity) context).findViewById(R.id.main_table);
                ImageButton plusButton = ((MainActivity) context).findViewById(R.id.plus_button);

                // Initialize PopupHandler and setup the Plus Button
                PopupHandler popupHandler = new PopupHandler(context, mainTable);
                popupHandler.setupPlusButton(plusButton);
            }
        });

        // Set OnClickListener for the Sign Up button
        signUpButton.setOnClickListener(v -> {
            ((MainActivity) context).setContentView(R.layout.sign_up_screen);
            new SignUpHandler(context).setupSignUpScreen();
        });
    }
}
