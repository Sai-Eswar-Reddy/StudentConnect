package com.example.studentconnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditStudentDetailsActivity extends AppCompatActivity {

    private EditText editTextFullName;
    private EditText editTextRollNo;
    private EditText editTextEmail;
    private EditText editTextBranch;
    private EditText editTextSection;
    private EditText editTextYear;
    private EditText editTextPhoneNo;
    private Button buttonSave;
    private Button buttonCancel;
    private Button buttonDelete; // New delete button

    private FirebaseDatabase database;
    private DatabaseReference studentsRef;
    private String studentId; // To store the ID of the student being edited

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_student_details); // Make sure you have this layout

        // Initialize UI elements
        editTextFullName = findViewById(R.id.editTextEditFullName);
        editTextRollNo = findViewById(R.id.editTextEditRollNo);
        editTextEmail = findViewById(R.id.editTextEditEmail);
        editTextBranch = findViewById(R.id.editTextEditBranch);
        editTextSection = findViewById(R.id.editTextEditSection);
        editTextYear = findViewById(R.id.editTextEditYear);
        editTextPhoneNo = findViewById(R.id.editTextEditPhoneNo);
        buttonSave = findViewById(R.id.buttonSaveEdit);
        buttonCancel = findViewById(R.id.buttonCancelEdit);
        buttonDelete = findViewById(R.id.buttonDeleteStudent); // Initialize delete button

        database = FirebaseDatabase.getInstance();
        studentsRef = database.getReference("students");

        // Get the student ID passed from the previous activity
        studentId = getIntent().getStringExtra("studentId");

        if (studentId != null) {
            loadStudentDetails(studentId);
        } else {
            Toast.makeText(this, "Error: Student ID not found.", Toast.LENGTH_SHORT).show();
            finish();
        }

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveStudentDetails();
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Set OnClickListener for the delete button
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteStudent();
            }
        });
    }

    private void loadStudentDetails(String studentId) {
        studentsRef.child(studentId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Student student = dataSnapshot.getValue(Student.class);
                if (student != null) {
                    editTextFullName.setText(student.getFullName());
                    editTextRollNo.setText(student.getRollNo());
                    editTextEmail.setText(student.getEmail());
                    editTextBranch.setText(student.getBranch());
                    editTextSection.setText(student.getSection());
                    editTextYear.setText(student.getYear());
                    editTextPhoneNo.setText(student.getPhoneNo());
                } else {
                    Toast.makeText(EditStudentDetailsActivity.this, "Error: Student not found.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Error loading student details: " + databaseError.getMessage());
                Toast.makeText(EditStudentDetailsActivity.this, "Failed to load student details.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void saveStudentDetails() {
        if (studentId == null) {
            Toast.makeText(this, "Error: Cannot save student details.", Toast.LENGTH_SHORT).show();
            return;
        }

        String fullName = editTextFullName.getText().toString().trim();
        String rollNo = editTextRollNo.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String branch = editTextBranch.getText().toString().trim();
        String section = editTextSection.getText().toString().trim();
        String year = editTextYear.getText().toString().trim();
        String phoneNo = editTextPhoneNo.getText().toString().trim();

        // Basic validation - add more as needed
        if (fullName.isEmpty() || rollNo.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Please fill in all required fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        Student updatedStudent = new Student(fullName, rollNo, branch, section, year, phoneNo, email, ""); // Password not edited here

        studentsRef.child(studentId).setValue(updatedStudent)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(EditStudentDetailsActivity.this, "Student details updated successfully.", Toast.LENGTH_SHORT).show();
                    finish(); // Go back to the previous screen
                })
                .addOnFailureListener(e -> {
                    Log.e("Firebase", "Error updating student details: " + e.getMessage());
                    Toast.makeText(EditStudentDetailsActivity.this, "Failed to update student details.", Toast.LENGTH_SHORT).show();
                });
    }

    private void deleteStudent() {
        if (studentId == null) {
            Toast.makeText(this, "Error: Cannot delete student.", Toast.LENGTH_SHORT).show();
            return;
        }

        studentsRef.child(studentId).removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(EditStudentDetailsActivity.this, "Student deleted successfully.", Toast.LENGTH_SHORT).show();
                        finish(); // Go back to the previous screen
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Firebase", "Error deleting student: " + e.getMessage());
                        Toast.makeText(EditStudentDetailsActivity.this, "Failed to delete student.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}