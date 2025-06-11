package com.example.studentconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class AdminLoginActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button loginButton, buttonBack;
    private FirebaseDatabase database;
    private DatabaseReference adminsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
        }

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        buttonBack = findViewById(R.id.buttonBack);

        database = FirebaseDatabase.getInstance();
        adminsRef = database.getReference("admins");

        buttonBack.setOnClickListener(v -> {
            Intent intent = new Intent(AdminLoginActivity.this, RoleSelectionActivity.class);
            startActivity(intent);
            finish();
        });

        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (!email.isEmpty() && !password.isEmpty()) {
                authenticateAdmin(email, password);
            } else {
                Toast.makeText(AdminLoginActivity.this, "Please enter email and password.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void authenticateAdmin(String email, String password) {
        Query query = adminsRef.orderByChild("email").equalTo(email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot adminSnapshot : dataSnapshot.getChildren()) {
                        String storedPassword = adminSnapshot.child("password").getValue(String.class);
                        if (storedPassword != null && storedPassword.equals(password)) {
                            // Authentication successful, navigate to AdminPanelActivity
                            Intent intent = new Intent(AdminLoginActivity.this, AdminPanelActivity.class);
                            intent.putExtra("loggedInEmail", email);
                            intent.putExtra("isAdmin", true);
                            startActivity(intent);
                            finish();
                            return; // Exit the loop and listener
                        }
                    }
                    // If the loop completes without a match, the password was incorrect
                    Toast.makeText(AdminLoginActivity.this, "Login failed. Invalid password.", Toast.LENGTH_SHORT).show();
                } else {
                    // If no matching email is found
                    Toast.makeText(AdminLoginActivity.this, "Login failed. Invalid email.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Firebase", "Error querying admin: " + databaseError.getMessage());
                Toast.makeText(AdminLoginActivity.this, "Login failed due to a network error.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}