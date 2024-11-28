package com.example.foodorderingapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "food_ordering.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_ORDER_PLAN = "order_plan";

    // Table Names
    public static final String TABLE_FOOD = "food";

    // Food Table Columns
    public static final String COLUMN_FOOD_ID = "id";
    public static final String COLUMN_FOOD_NAME = "name";
    public static final String COLUMN_FOOD_COST = "cost";

    // SQL to create the food table
    private static final String CREATE_TABLE_FOOD = "CREATE TABLE " + TABLE_FOOD + " (" +
            COLUMN_FOOD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_FOOD_NAME + " TEXT, " +
            COLUMN_FOOD_COST + " REAL);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create food table
        db.execSQL(CREATE_TABLE_FOOD);

        // Prepopulate food items
        prepopulateFoodItems(db);
        db.execSQL("CREATE TABLE " + TABLE_FOOD + " (id INTEGER PRIMARY KEY, name TEXT, cost REAL)");
        db.execSQL("CREATE TABLE " + TABLE_ORDER_PLAN + " (id INTEGER PRIMARY KEY, date TEXT, food_item TEXT, cost REAL)");

    }

    private void prepopulateFoodItems(SQLiteDatabase db) {
        // Array of food items and their costs
        String[] foodItems = {
                "('Pizza', 8.5)",
                "('Burger', 5.0)",
                "('Pasta', 7.0)",
                "('Salad', 4.0)",
                "('Taco', 3.5)",
                "('Sushi', 10.0)",
                "('Steak', 15.0)",
                "('Fries', 2.5)",
                "('Sandwich', 4.5)",
                "('Soup', 3.0)",
                "('Ice Cream', 3.5)",
                "('Cake', 4.5)",
                "('Coffee', 2.0)",
                "('Tea', 1.5)",
                "('Juice', 2.5)",
                "('Smoothie', 4.0)",
                "('Pancakes', 5.5)",
                "('Waffles', 6.0)",
                "('Hotdog', 3.5)",
                "('Donut', 1.5)"
        };

        // Insert food items into the food table
        for (String item : foodItems) {
            db.execSQL("INSERT INTO " + TABLE_FOOD + " (name, cost) VALUES " + item + ";");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOOD);
        onCreate(db);
    }

    public void insertFood(String name, double cost) {
        // Get writable database
        SQLiteDatabase db = this.getWritableDatabase();

        // Create a ContentValues object
        ContentValues values = new ContentValues();
        values.put(COLUMN_FOOD_NAME, name);
        values.put(COLUMN_FOOD_COST, cost);

        // Insert into the food table
        db.insert(TABLE_FOOD, null, values);

        // Close the database
        db.close();
    }

    public void deleteFood(int id) {
        // Get writable database
        SQLiteDatabase db = this.getWritableDatabase();

        // Delete the food item from the table
        db.delete(TABLE_FOOD, "id = ?", new String[]{String.valueOf(id)});

        // Close the database
        db.close();
    }

    public ArrayList<HashMap<String, String>> getAllFoods() {
        ArrayList<HashMap<String, String>> foodList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_FOOD, null);

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> food = new HashMap<>();
                food.put("id", cursor.getString(cursor.getColumnIndexOrThrow("id")));
                food.put("name", cursor.getString(cursor.getColumnIndexOrThrow("name")));
                food.put("cost", cursor.getString(cursor.getColumnIndexOrThrow("cost")));
                foodList.add(food);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return foodList;
    }

    public void insertOrderPlan(String date, String foodItem, double cost) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("date", date);
        values.put("food_item", foodItem);
        values.put("cost", cost);
        db.insert(TABLE_ORDER_PLAN, null, values);
        db.close();
    }

}
