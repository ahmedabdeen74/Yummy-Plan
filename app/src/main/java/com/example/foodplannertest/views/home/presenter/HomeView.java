package com.example.foodplannertest.views.home.presenter;


import com.example.foodplannertest.data.models.Meal;

import java.util.List;

public interface HomeView {
    void showRandomMeals(List<Meal> meals);
    void showError(String message);
    void showRandomMeals2(List<Meal> meals);
    void showMealSavedToFavorites();
    void showMealAddedToCalendar();
}