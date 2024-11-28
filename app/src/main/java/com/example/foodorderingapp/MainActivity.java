package com.example.foodorderingapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private ListView listViewFoods;
    private Button btnAddFood;
    private Button btnOrderPlan; // Declare the Order Plan button
    private DatabaseHelper dbHelper;
    private ArrayList<HashMap<String, String>> foodList;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);

        listViewFoods = findViewById(R.id.listViewFoods);
        btnAddFood = findViewById(R.id.btnAddFood);
        btnOrderPlan = findViewById(R.id.btnOrderPlan); // Initialize the Order Plan button

        loadFoods();

        btnAddFood.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddFoodActivity.class);
            startActivity(intent);
        });

        listViewFoods.setOnItemLongClickListener((adapterView, view, i, l) -> {
            HashMap<String, String> food = foodList.get(i);
            dbHelper.deleteFood(Integer.parseInt(food.get("id")));
            loadFoods();
            return true;
        });

        btnOrderPlan.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, OrderPlanActivity.class);
            startActivity(intent);
        });
    }

    private void loadFoods() {
        foodList = dbHelper.getAllFoods();
        ArrayList<String> foodNames = new ArrayList<>();

        for (HashMap<String, String> food : foodList) {
            foodNames.add(food.get("name") + " - $" + food.get("cost"));
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, foodNames);
        listViewFoods.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadFoods();
    }
}
