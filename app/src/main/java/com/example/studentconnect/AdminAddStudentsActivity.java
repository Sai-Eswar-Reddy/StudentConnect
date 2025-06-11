package com.example.studentconnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminAddStudentsActivity extends AppCompatActivity {

    private EditText editTextFullName, editTextRollNo, editTextBranch, editTextSection, editTextYear,
            editTextPhoneNo, editTextEmail, editTextPassword;
    private Button buttonAddStudent;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_students); // Assuming your layout file is named this

        editTextFullName = findViewById(R.id.editTextFullName);
        editTextRollNo = findViewById(R.id.editTextRollNo);
        editTextBranch = findViewById(R.id.editTextBranch);
        editTextSection = findViewById(R.id.editTextSection);
        editTextYear = findViewById(R.id.editTextYear);
        editTextPhoneNo = findViewById(R.id.editTextPhoneNo);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonAddStudent = findViewById(R.id.buttonAddStudent);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("students");

        buttonAddStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String fullName = editTextFullName.getText().toString().trim();
                final String rollNo = editTextRollNo.getText().toString().trim();
                final String branch = editTextBranch.getText().toString().trim();
                final String section = editTextSection.getText().toString().trim();
                final String year = editTextYear.getText().toString().trim();
                final String phoneNo = editTextPhoneNo.getText().toString().trim();
                final String email = editTextEmail.getText().toString().trim();
                final String password = editTextPassword.getText().toString().trim();

                if (TextUtils.isEmpty(fullName)) {
                    editTextFullName.setError("Full Name is required.");
                    return;
                }
                if (TextUtils.isEmpty(rollNo)) {
                    editTextRollNo.setError("Roll No is required.");
                    return;
                }
                if (TextUtils.isEmpty(branch)) {
                    editTextBranch.setError("Branch is required.");
                    return;
                }
                if (TextUtils.isEmpty(section)) {
                    editTextSection.setError("Section is required.");
                    return;
                }
                if (TextUtils.isEmpty(year)) {
                    editTextYear.setError("Year is required.");
                    return;
                }
                if (TextUtils.isEmpty(phoneNo)) {
                    editTextPhoneNo.setError("Phone No is required.");
                    return;
                }
                if (TextUtils.isEmpty(email)) {
                    editTextEmail.setError("Email is required.");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    editTextPassword.setError("Password is required.");
                    return;
                }

                final Student student = new Student(fullName, rollNo, branch, section, year, phoneNo, email, password);

                mDatabase.child(rollNo).setValue(student)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(AdminAddStudentsActivity.this, "Student added successfully!", Toast.LENGTH_SHORT).show();
                                    editTextFullName.setText("");
                                    editTextRollNo.setText("");
                                    editTextBranch.setText("");
                                    editTextSection.setText("");
                                    editTextYear.setText("");
                                    editTextPhoneNo.setText("");
                                    editTextEmail.setText("");
                                    editTextPassword.setText("");

                                    mAuth.createUserWithEmailAndPassword(email, password)
                                            .addOnCompleteListener(AdminAddStudentsActivity.this, new OnCompleteListener<AuthResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                    if (task.isSuccessful()) {
                                                        // Optionally send verification email
                                                    } else {
                                                        Toast.makeText(AdminAddStudentsActivity.this, "Error creating Auth user: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });

                                } else {
                                    Toast.makeText(AdminAddStudentsActivity.this, "Failed to add student: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}