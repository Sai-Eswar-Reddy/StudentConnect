package com.example.studentconnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationActivity extends AppCompatActivity {

    private static final String TAG = "RegistrationActivity";
    private EditText editTextFullName, editTextRollNo, editTextBranch, editTextSection, editTextYear, editTextPhoneNo, editTextEmail, editTextPassword;
    private Button buttonRegister;
    private TextView textViewLogin;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // Initialize Firebase Auth and Database
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("students");

        // Initialize UI elements
        editTextFullName = findViewById(R.id.editTextFullName);
        editTextRollNo = findViewById(R.id.editTextRollNo);
        editTextBranch = findViewById(R.id.editTextBranch);
        editTextSection = findViewById(R.id.editTextSection);
        editTextYear = findViewById(R.id.editTextYear);
        editTextPhoneNo = findViewById(R.id.editTextPhoneNo);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonRegister = findViewById(R.id.buttonRegister);
        textViewLogin = findViewById(R.id.textViewLogin);

        // Set OnClickListener for the Register button
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String fullName = editTextFullName.getText().toString().trim();
                final String rollNo = editTextRollNo.getText().toString().trim();
                final String branch = editTextBranch.getText().toString().trim();
                final String section = editTextSection.getText().toString().trim();
                final String year = editTextYear.getText().toString().trim();
                final String phoneNo = editTextPhoneNo.getText().toString().trim();
                final String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                // Basic input validation
                if (TextUtils.isEmpty(fullName)) {
                    editTextFullName.setError("Full Name is required");
                    return;
                }
                if (TextUtils.isEmpty(rollNo)) {
                    editTextRollNo.setError("Roll Number is required");
                    return;
                }
                if (TextUtils.isEmpty(branch)) {
                    editTextBranch.setError("Branch is required");
                    return;
                }
                if (TextUtils.isEmpty(section)) {
                    editTextSection.setError("Section is required");
                    return;
                }
                if (TextUtils.isEmpty(year)) {
                    editTextYear.setError("Year is required");
                    return;
                }
                if (TextUtils.isEmpty(phoneNo)) {
                    editTextPhoneNo.setError("Phone Number is required");
                    return;
                }
                if (TextUtils.isEmpty(email)) {
                    editTextEmail.setError("Email is required");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    editTextPassword.setError("Password is required");
                    return;
                }
                if (password.length() < 6) {
                    editTextPassword.setError("Password should be at least 6 characters long");
                    return;
                }

                // Create user with email and password
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Get the current user
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    if (user != null) {
                                        // Get the user's unique ID
                                        String userId = user.getUid();

                                        // Create a Student object with the correct mapping
                                        Student student = new Student(fullName, rollNo, branch, section, year, phoneNo, email, password);

                                        // Store the student data in the Realtime Database under the "students" node with the user's ID as the key
                                        mDatabase.child(userId).setValue(student)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(RegistrationActivity.this, "Registration successful.", Toast.LENGTH_SHORT).show();
                                                            // Navigate back to the login screen
                                                            startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
                                                            finish();
                                                        } else {
                                                            Log.e(TAG, "Failed to save user data: " + task.getException().getMessage());
                                                            Toast.makeText(RegistrationActivity.this, "Failed to save user data.", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                    } else {
                                        Toast.makeText(RegistrationActivity.this, "Registration failed: Could not get user ID.", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    // If registration fails, display a message to the user
                                    Log.e(TAG, "Registration failed: " + task.getException().getMessage());
                                    Toast.makeText(RegistrationActivity.this, "Registration failed: " + task.getException().getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        // Set OnClickListener for the Login TextView
        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
                finish();
            }
        });
    }
}