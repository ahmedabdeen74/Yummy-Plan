package com.example.foodplannertest.data.local;



import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.foodplannertest.data.models.Meal;

import java.util.List;

@Dao
public interface MealDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Meal meal);
    @Query("DELETE FROM meals WHERE idMeal = :mealId")
    void deleteByMealId(String mealId);
    @Query("SELECT * FROM meals WHERE idMeal = :mealId")
    Meal getMealById(String mealId);

    @Query("SELECT * FROM meals WHERE type = 'favourite'")
    List<Meal> getAllFavouriteMeals();
    @Query("SELECT * FROM meals WHERE type = 'calendar' AND date = :date")
    List<Meal> getMealsByDate(String date);

    @Query("SELECT * FROM meals WHERE type = 'calendar'")
    List<Meal> getAllCalendarMeals();
    @Query("DELETE FROM meals WHERE type = 'favourite'")
    void deleteAllFavouriteMeals();

    @Query("DELETE FROM meals WHERE type = 'calendar'")
    void deleteAllCalendarMeals();

    @Insert
    void insertAll(List<Meal> meals);
    @Query("DELETE FROM meals WHERE idMeal = :mealId AND type = :type")
    void deleteByMealIdAndType(String mealId, String type);
}