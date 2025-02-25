package com.example.foodplannertest.data.remote;

import com.example.foodplannertest.data.models.Category;

import java.util.List;

public class CategoryResponse {
    private List<Category> meals;

    public List<Category> getMeals() {
        return meals;
    }
}
