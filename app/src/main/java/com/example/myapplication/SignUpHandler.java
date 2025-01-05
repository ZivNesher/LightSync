package com.example.myapplication;

import android.content.Context;
import android.widget.Button;

public class SignUpHandler {

    private final Context context;

    public SignUpHandler(Context context) {
        this.context = context;
    }

    public void setupSignUpScreen() {
        // Find the Back to Login button
        Button backToLoginButton = ((MainActivity) context).findViewById(R.id.back_to_login_button);

        // Set OnClickListener for the Back to Login button
        backToLoginButton.setOnClickListener(v -> {
            ((MainActivity) context).setContentView(R.layout.login_screen);
            new LoginHandler(context).setupLoginScreen();
        });
    }
}
