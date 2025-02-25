package com.example.foodplannertest.data.remote;

import com.example.foodplannertest.data.models.Ingredient;

import java.util.List;

public class IngredientResponse {
    private List<Ingredient> meals;

    public List<Ingredient> getMeals() {
        return meals;
    }
}
