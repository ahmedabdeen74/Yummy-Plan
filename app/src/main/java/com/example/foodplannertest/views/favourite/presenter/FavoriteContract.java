package com.example.foodplannertest.views.favourite.presenter;

import com.example.foodplannertest.data.models.Meal;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;

public interface FavoriteContract {

    interface View {
        void showFavouriteMeals(List<Meal> meals);
        void showEmptyView();
        void showError(String message);
    }

    interface Presenter {
        void loadFavouriteMeals();
        void deleteMealFromFavorites(Meal meal);
    }
}
