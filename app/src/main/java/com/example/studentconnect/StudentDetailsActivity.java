package com.example.studentconnect;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.annotation.NonNull;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;
import android.util.Log;

public class StudentDetailsActivity extends AppCompatActivity {

    private TextView fullNameTextView, rollNoTextView, emailTextView, branchTextView, sectionTextView, yearTextView, phoneNoTextView;
    private Button editButton, deleteButton, connectButton, chatButton, emailButton;
    private boolean isAdmin = false;
    private String studentPhoneNumber;
    private String studentEmailAddress;
    private String loggedInStudentEmail;

    private static final int REQUEST_CALL_PHONE_PERMISSION = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_details);

        fullNameTextView = findViewById(R.id.textViewFullName);
        rollNoTextView = findViewById(R.id.textViewRollNo);
        emailTextView = findViewById(R.id.textViewEmail);
        branchTextView = findViewById(R.id.textViewBranch);
        sectionTextView = findViewById(R.id.textViewSection);
        yearTextView = findViewById(R.id.textViewYear);
        phoneNoTextView = findViewById(R.id.textViewPhoneNo);
        editButton = findViewById(R.id.editButton);
        deleteButton = findViewById(R.id.deleteButton);
        connectButton = findViewById(R.id.connectButton);
        chatButton = findViewById(R.id.chatButton);
        emailButton = findViewById(R.id.emailButton);

        // Retrieve student details from Intent
        String fullName = getIntent().getStringExtra("fullName");
        String rollNo = getIntent().getStringExtra("rollNo");
        studentEmailAddress = getIntent().getStringExtra("email");
        String branch = getIntent().getStringExtra("branch");
        String section = getIntent().getStringExtra("section");
        String year = getIntent().getStringExtra("year");
        studentPhoneNumber = getIntent().getStringExtra("phoneNo");
        isAdmin = getIntent().getBooleanExtra("isAdmin", false);
        loggedInStudentEmail = getIntent().getStringExtra("loggedInEmail");

        // Set the TextViews
        fullNameTextView.setText("Full Name: " + fullName);
        rollNoTextView.setText("Roll No: " + rollNo);
        emailTextView.setText("Email: " + studentEmailAddress);
        branchTextView.setText("Branch: " + branch);
        sectionTextView.setText("Section: " + section);
        yearTextView.setText("Year: " + year);
        phoneNoTextView.setText("Phone No: " + studentPhoneNumber);

        // Conditionally show/hide Edit and Delete buttons (only for admin)
        if (!isAdmin) {
            editButton.setVisibility(View.GONE);
            deleteButton.setVisibility(View.GONE);
        }

        // Set OnClickListeners for the buttons
        connectButton.setOnClickListener(v -> initiateCall());
        chatButton.setOnClickListener(v -> initiateChat());
        emailButton.setOnClickListener(v -> sendEmail());
    }

    private void initiateCall() {
        if (studentPhoneNumber != null && !studentPhoneNumber.isEmpty()) {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + studentPhoneNumber));
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                startActivity(callIntent);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PHONE_PERMISSION);
            }
        } else {
            Toast.makeText(this, "Phone number not available", Toast.LENGTH_SHORT).show();
        }
    }

    private void initiateChat() {
        if (studentPhoneNumber != null && !studentPhoneNumber.isEmpty()) {
            Intent chatIntent = new Intent(Intent.ACTION_VIEW);
            chatIntent.setData(Uri.parse("sms:" + studentPhoneNumber));
            startActivity(chatIntent);
        } else {
            Toast.makeText(this, "Phone number not available", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendEmail() {
        if (studentEmailAddress != null && !studentEmailAddress.isEmpty()) {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            emailIntent.setData(Uri.parse("mailto:" + studentEmailAddress));
            if (loggedInStudentEmail != null && !loggedInStudentEmail.isEmpty()) {
                Log.d("EmailIntent", "Logged-in student's email (From - conceptually): " + loggedInStudentEmail);
            }
            startActivity(emailIntent);
        } else {
            Toast.makeText(this, "Email address not available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CALL_PHONE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initiateCall();
            } else {
                Toast.makeText(this, "Call permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}