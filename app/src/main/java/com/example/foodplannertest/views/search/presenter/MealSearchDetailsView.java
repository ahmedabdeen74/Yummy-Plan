package com.example.foodplannertest.views.search.presenter;

import com.example.foodplannertest.data.models.Meal;

public interface MealSearchDetailsView {
    void showMealDetails(Meal meal);
    void showError(String message);
}
