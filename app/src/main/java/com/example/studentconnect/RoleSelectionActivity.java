package com.example.studentconnect;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class RoleSelectionActivity extends AppCompatActivity {

    private Button studentButton;
    private Button adminButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role_selection);

        studentButton = findViewById(R.id.studentButton);
        adminButton = findViewById(R.id.adminButton);

        studentButton.setOnClickListener(v -> {
            navigateToLogin("student");
        });

        adminButton.setOnClickListener(v -> {
            navigateToLogin("admin");
        });
    }

    private void navigateToLogin(String role) {
        Intent intent;
        if (role.equals("student")) {
            intent = new Intent(RoleSelectionActivity.this, LoginActivity.class);
        } else if (role.equals("admin")) {
            intent = new Intent(RoleSelectionActivity.this, AdminLoginActivity.class);
        } else {
            // Handle potential errors or unknown roles
            return;
        }
        startActivity(intent);
        finish(); // Optional: Finish the RoleSelectionActivity
    }
}