package com.example.studentconnect;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.EditText;

public class TestSearchActivity extends AppCompatActivity {

    EditText editTextTestSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_search);

        editTextTestSearch = findViewById(R.id.editTextTestSearch);
        // You don't need any functionality here for this test
    }
}