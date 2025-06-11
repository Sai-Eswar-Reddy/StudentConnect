package com.example.studentconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BranchSelectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch_selection);

        TextView textViewHome = findViewById(R.id.textViewHome);
        TextView textViewSelectBranch = findViewById(R.id.textViewSelectBranch);
        LinearLayout linearLayoutButtons = findViewById(R.id.linearLayoutButtons);

        // Assuming your buttons have these IDs
        Button buttonCSE = findViewById(R.id.buttonCSE);
        Button buttonCSD = findViewById(R.id.buttonCSD);
        Button buttonCSM = findViewById(R.id.buttonCSM);
        Button buttonEEE = findViewById(R.id.buttonEEE);
        Button buttonECE = findViewById(R.id.buttonECE);
        Button buttonCHEM = findViewById(R.id.buttonCHEM);
        Button buttonMECH = findViewById(R.id.buttonMECH);
        Button buttonCIVIL = findViewById(R.id.buttonCIVIL);
        Button buttonAllStudents = findViewById(R.id.buttonAllStudents);

        setBranchButtonClick(buttonCSE, "CSE");
        setBranchButtonClick(buttonCSD, "CSD");
        setBranchButtonClick(buttonCSM, "CSM");
        setBranchButtonClick(buttonEEE, "EEE");
        setBranchButtonClick(buttonECE, "ECE");
        setBranchButtonClick(buttonCHEM, "CHEM");
        setBranchButtonClick(buttonMECH, "MECH");
        setBranchButtonClick(buttonCIVIL, "CIVIL");
        setBranchButtonClick(buttonAllStudents, "All Students");
    }

    private void setBranchButtonClick(View view, final String branch) {
        if (view instanceof Button) {
            Button branchButton = (Button) view;
            branchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(BranchSelectionActivity.this, DashboardActivity.class);
                    intent.putExtra("selectedBranch", branch);
                    startActivity(intent);
                }
            });
        }
    }
}