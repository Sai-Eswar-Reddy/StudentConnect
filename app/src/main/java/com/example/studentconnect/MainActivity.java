package com.example.studentconnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextView fullNameTextView, emailTextView;
    private Button logoutButton;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        fullNameTextView = findViewById(R.id.studentFullNameTextView);
        emailTextView = findViewById(R.id.textViewEmail);
        logoutButton = findViewById(R.id.buttonLogout);
        mDatabase = FirebaseDatabase.getInstance().getReference("students");

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            mDatabase.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Student student = dataSnapshot.getValue(Student.class);
                    if (student != null) {
                        fullNameTextView.setText("Welcome, " + student.getFullName());
                        emailTextView.setText("Email: " + student.getEmail());
                    } else {
                        fullNameTextView.setText("Welcome, User");
                        emailTextView.setText("Error loading info");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    fullNameTextView.setText("Welcome, User");
                    emailTextView.setText("Error loading info");
                }
            });
        } else {
            // If no user is logged in, directly go to the LoginActivity
            Intent intentToLogin = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intentToLogin);
            finish();
        }

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intentToLogin = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intentToLogin);
                finish();
            }
        });
    }
}