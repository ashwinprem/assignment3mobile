package com.example.foodorderingapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AddFoodActivity extends AppCompatActivity {

    private EditText editTextName, editTextCost;
    private Button btnSave;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);

        dbHelper = new DatabaseHelper(this);

        editTextName = findViewById(R.id.editTextName);
        editTextCost = findViewById(R.id.editTextCost);
        btnSave = findViewById(R.id.btnSave);

        btnSave.setOnClickListener(v -> {
            String name = editTextName.getText().toString();
            String cost = editTextCost.getText().toString();

            if (name.isEmpty() || cost.isEmpty()) {
                Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show();
            } else {
                dbHelper.insertFood(name, Double.parseDouble(cost));
                Toast.makeText(this, "Food item added successfully!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
