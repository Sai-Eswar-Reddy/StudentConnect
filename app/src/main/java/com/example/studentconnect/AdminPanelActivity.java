package com.example.studentconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AdminPanelActivity extends AppCompatActivity {

    private Button registerStudentButton;
    private Button editStudentsButton;
    private TextView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);

        registerStudentButton = findViewById(R.id.registerStudentButton);
        editStudentsButton = findViewById(R.id.editStudentsButton);
        backButton = findViewById(R.id.backButton);

        registerStudentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminPanelActivity.this, AdminAddStudentsActivity.class);
                startActivity(intent);
            }
        });

        editStudentsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent to the EditStudentsActivity (to be created later)
                Intent intent = new Intent(AdminPanelActivity.this, EditStudentsActivity.class);
                startActivity(intent);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to the previous activity (LoginActivity in this case)
                finish();
            }
        });
    }
}