package com.example.foodplannertest.data.local;



import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

import com.example.foodplannertest.data.models.Meal;

@Database(entities = {Meal.class}, version = 9, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;

    public abstract MealDao mealDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "favourite_meals_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}