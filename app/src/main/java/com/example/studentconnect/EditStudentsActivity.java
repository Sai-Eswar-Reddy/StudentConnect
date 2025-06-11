package com.example.studentconnect;

import android.text.TextUtils;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EditStudentsActivity extends AppCompatActivity implements StudentListAdapter.OnItemClickListener {

    private RecyclerView recyclerViewStudents;
    private StudentListAdapter adapter;
    private List<Student> studentList;
    private List<Student> originalStudentList; // To hold the original list for filtering
    private EditText searchEditText;
    private FirebaseDatabase database;
    private DatabaseReference studentsRef;
    private String loggedInEmail;
    private boolean isAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_students);

        recyclerViewStudents = findViewById(R.id.recyclerViewEditStudents);
        recyclerViewStudents.setLayoutManager(new LinearLayoutManager(this));
        studentList = new ArrayList<>();
        originalStudentList = new ArrayList<>(); // Initialize the original list
        searchEditText = findViewById(R.id.searchEditText);

        // Retrieve isAdmin and loggedInEmail (assuming you are passing these to EditStudentsActivity)
        isAdmin = getIntent().getBooleanExtra("isAdmin", false);
        loggedInEmail = getIntent().getStringExtra("loggedInEmail");

        adapter = new StudentListAdapter(studentList, this, isAdmin, loggedInEmail);
        adapter.setOnItemClickListener(this); // Set the listener to this activity
        recyclerViewStudents.setAdapter(adapter);

        database = FirebaseDatabase.getInstance();
        studentsRef = database.getReference("students");

        loadAllStudents();

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterStudents(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Not needed
            }
        });
    }

    private void loadAllStudents() {
        studentsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                studentList.clear();
                originalStudentList.clear(); // Clear both lists before populating
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Student student = snapshot.getValue(Student.class);
                    if (student != null) {
                        student.setUid(snapshot.getKey()); // Set the UID for each student
                        studentList.add(student);
                        originalStudentList.add(new Student(student.getFullName(), student.getRollNo(), student.getBranch(), student.getSection(), student.getYear(), student.getPhoneNo(), student.getEmail(), student.getPassword())); // Create a new copy
                        originalStudentList.get(originalStudentList.size() - 1).setUid(student.getUid()); // Copy the UID as well
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Error loading students: " + databaseError.getMessage());
                Toast.makeText(EditStudentsActivity.this, "Failed to load students.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filterStudents(String searchText) {
        studentList.clear();
        if (TextUtils.isEmpty(searchText)) {
            studentList.addAll(originalStudentList);
        } else {
            searchText = searchText.toLowerCase();
            for (Student student : originalStudentList) {
                if (student.getFullName().toLowerCase().contains(searchText) ||
                        student.getRollNo().toLowerCase().contains(searchText) ||
                        student.getEmail().toLowerCase().contains(searchText) ||
                        student.getBranch().toLowerCase().contains(searchText) ||
                        student.getSection().toLowerCase().contains(searchText) ||
                        student.getYear().toLowerCase().contains(searchText) ||
                        student.getPhoneNo().toLowerCase().contains(searchText)) {
                    studentList.add(student);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(Student student) {
        // Start the EditStudentDetailsActivity and pass the student's UID
        Intent intent = new Intent(this, EditStudentDetailsActivity.class);
        intent.putExtra("studentId", student.getUid());
        startActivity(intent);
    }
}