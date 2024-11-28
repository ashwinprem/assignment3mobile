package com.example.foodorderingapp;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class OrderPlanActivity extends AppCompatActivity {
    private EditText targetCost;
    private Button selectDate, saveOrderPlan;
    private ListView availableItems;
    private String selectedDate = "";
    private ArrayList<HashMap<String, String>> filteredItems = new ArrayList<>();
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_plan);

        targetCost = findViewById(R.id.targetCost);
        selectDate = findViewById(R.id.selectDate);
        saveOrderPlan = findViewById(R.id.saveOrderPlan);
        availableItems = findViewById(R.id.availableItems);

        dbHelper = new DatabaseHelper(this);

        selectDate.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    this, (view, year, month, dayOfMonth) -> {
                selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
                selectDate.setText(selectedDate);
            },
                    Calendar.getInstance().get(Calendar.YEAR),
                    Calendar.getInstance().get(Calendar.MONTH),
                    Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
            );
            datePickerDialog.show();
        });

        targetCost.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterFoodItems();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        saveOrderPlan.setOnClickListener(v -> saveOrderPlan());
    }

    private void filterFoodItems() {
        filteredItems.clear();
        String costText = targetCost.getText().toString();
        if (!costText.isEmpty()) {
            double target = Double.parseDouble(costText);
            ArrayList<HashMap<String, String>> allItems = dbHelper.getAllFoods();
            for (HashMap<String, String> item : allItems) {
                double cost = Double.parseDouble(item.get("cost"));
                if (cost <= target) {
                    filteredItems.add(item);
                }
            }

            SimpleAdapter adapter = new SimpleAdapter(
                    this,
                    filteredItems,
                    android.R.layout.simple_list_item_2,
                    new String[]{"name", "cost"},
                    new int[]{android.R.id.text1, android.R.id.text2}
            );
            availableItems.setAdapter(adapter);
        }
    }

    private void saveOrderPlan() {
        if (selectedDate.isEmpty()) {
            Toast.makeText(this, "Please select a date!", Toast.LENGTH_SHORT).show();
            return;
        }

        for (HashMap<String, String> item : filteredItems) {
            String name = item.get("name");
            double cost = Double.parseDouble(item.get("cost"));
            dbHelper.insertOrderPlan(selectedDate, name, cost);
        }

        Toast.makeText(this, "Order Plan Saved!", Toast.LENGTH_SHORT).show();
        finish();
    }
}
