package com.example.studentconnect;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import androidx.annotation.NonNull;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    private RecyclerView recyclerViewStudents;
    private StudentListAdapter adapter;
    private List<Student> studentList;
    private EditText editTextSearch;
    private Button buttonFilter;
    private LinearLayout filterOptionsLayout;
    private Spinner spinnerBranch, spinnerSection, spinnerYear;
    private Button buttonApplyFilter;
    private String loggedInEmail;
    private boolean isAdmin;

    private FirebaseDatabase database;
    private DatabaseReference studentsRef;
    private String initialSelectedBranch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Initialize views
        recyclerViewStudents = findViewById(R.id.recyclerViewStudents);
        recyclerViewStudents.setLayoutManager(new LinearLayoutManager(this));
        studentList = new ArrayList<>();
        adapter = new StudentListAdapter(studentList, this, isAdmin, loggedInEmail);
        recyclerViewStudents.setAdapter(adapter);

        editTextSearch = findViewById(R.id.editTextSearch);
        buttonFilter = findViewById(R.id.buttonFilter);
        filterOptionsLayout = findViewById(R.id.filterOptionsLayout);
        spinnerBranch = findViewById(R.id.spinnerBranch);
        spinnerSection = findViewById(R.id.spinnerSection);
        spinnerYear = findViewById(R.id.spinnerYear);
        buttonApplyFilter = findViewById(R.id.buttonApplyFilter);

        // Get login info from previous activity
        loggedInEmail = getIntent().getStringExtra("loggedInEmail");
        isAdmin = getIntent().getBooleanExtra("isAdmin", false);

        // Get the selected branch from the Intent
        initialSelectedBranch = getIntent().getStringExtra("selectedBranch");
        if (initialSelectedBranch == null) {
            initialSelectedBranch = "All Students"; // Default to All if no branch is passed
        }

        // Set adapter configurations
        adapter.setIsAdmin(isAdmin);
        adapter.setLoggedInEmail(loggedInEmail);

        // Firebase initialization
        database = FirebaseDatabase.getInstance();
        studentsRef = database.getReference("students");

        // Load students from Firebase based on the initial selected branch
        loadStudents(initialSelectedBranch);

        // Toggle filter visibility
        buttonFilter.setOnClickListener(v -> {
            filterOptionsLayout.setVisibility(filterOptionsLayout.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
        });

        // Apply filter on students (using the spinners)
        buttonApplyFilter.setOnClickListener(v -> {
            String branch = spinnerBranch.getSelectedItem().toString();
            String section = spinnerSection.getSelectedItem().toString();
            String year = spinnerYear.getSelectedItem().toString();

            filterStudents(branch, section, year);
            filterOptionsLayout.setVisibility(View.GONE);
        });

        // Populate spinners
        populateSpinners();

        // Set the initial selection of the Branch spinner
        setInitialBranchSpinnerSelection(initialSelectedBranch);

        // Implement Search Functionality
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Not needed
            }
        });
    }

    private void filter(String query) {
        query = query.toLowerCase().trim();
        List<Student> filteredList = new ArrayList<>();
        for (Student student : studentList) {
            if (student.getFullName().toLowerCase().contains(query) ||
                    student.getRollNo().toLowerCase().contains(query) ||
                    student.getBranch().toLowerCase().contains(query)) {
                filteredList.add(student);
            }
        }
        adapter.setStudents(filteredList);
    }

    // Load students from Firebase, optionally filtering by branch
    private void loadStudents(String branchFilter) {
        Query query = studentsRef;
        if (!branchFilter.equals("All Students")) {
            query = query.orderByChild("branch").equalTo(branchFilter);
        }

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                studentList.clear();
                List<Student> initialList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Student student = snapshot.getValue(Student.class);
                    if (student != null) {
                        initialList.add(student);
                    }
                }
                studentList = initialList; // Store the initially loaded list
                adapter.setStudents(studentList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Error loading students: " + databaseError.getMessage());
                Toast.makeText(DashboardActivity.this, "Failed to load students.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Filter students based on selected branch, section, and year (from spinners)
    private void filterStudents(String branch, String section, String year) {
        String selectedBranch = spinnerBranch.getSelectedItem().toString(); // Get current branch filter
        Query query = studentsRef;

        if (!selectedBranch.equals("All")) {
            query = query.orderByChild("branch").equalTo(selectedBranch);
        }

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Student> filteredList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Student student = snapshot.getValue(Student.class);
                    if (student != null) {
                        boolean sectionMatch = section.equals("All") || student.getSection().equalsIgnoreCase(section);
                        boolean yearMatch = year.equals("All") || student.getYear().equals(year);

                        if (sectionMatch && yearMatch) {
                            filteredList.add(student);
                        }
                    }
                }
                adapter.setStudents(filteredList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Error filtering students: " + databaseError.getMessage());
                Toast.makeText(DashboardActivity.this, "Failed to filter students.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Populate the spinners with available branches, sections, and years
    private void populateSpinners() {
        List<String> branches = new ArrayList<>();
        branches.add("All");
        branches.add("CSE");
        branches.add("CSD");
        branches.add("CSM");
        branches.add("EEE");
        branches.add("ECE");
        branches.add("CHEM");
        branches.add("MECH");
        branches.add("CIVIL");

        List<String> sections = new ArrayList<>();
        sections.add("All");
        sections.add("A");
        sections.add("B");
        sections.add("C");

        List<String> years = new ArrayList<>();
        years.add("All");
        years.add("1");
        years.add("2");
        years.add("3");
        years.add("4");

        ArrayAdapter<String> branchAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, branches);
        branchAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBranch.setAdapter(branchAdapter);

        ArrayAdapter<String> sectionAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sections);
        sectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSection.setAdapter(sectionAdapter);

        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, years);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerYear.setAdapter(yearAdapter);
    }

    // Set the initial selection of the Branch spinner based on the passed branch
    private void setInitialBranchSpinnerSelection(String branch) {
        if (branch != null) {
            ArrayAdapter<String> spinnerAdapter = (ArrayAdapter<String>) spinnerBranch.getAdapter();
            if (spinnerAdapter != null) {
                int position = spinnerAdapter.getPosition(branch);
                if (position != -1) {
                    spinnerBranch.setSelection(position);
                }
            }
        }
    }
}