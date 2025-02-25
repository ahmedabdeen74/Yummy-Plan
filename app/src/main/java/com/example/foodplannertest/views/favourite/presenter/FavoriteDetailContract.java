package com.example.foodplannertest.views.favourite.presenter;


import com.example.foodplannertest.data.models.Meal;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;

public interface FavoriteDetailContract {

    interface View {
        void showMealDetails(Meal meal);
        void showError(String message);
    }

    interface Presenter {
        void loadMealDetails(String mealId);
    }
}
