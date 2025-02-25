package com.example.foodplannertest.data.remote;


import com.example.foodplannertest.data.models.Meal;

import java.util.List;

public class MealResponse {
    private List<Meal> meals;

    public List<Meal> getMeals() {
        return meals;
    }
}