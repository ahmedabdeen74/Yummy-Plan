package com.example.foodplannertest.views.favourite.presenter;



import com.example.foodplannertest.data.models.Meal;
import com.example.foodplannertest.data.repo.MealRepository;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class FavoritePresenter implements FavoriteContract.Presenter {

    private FavoriteContract.View view;
    private MealRepository mealRepository;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public FavoritePresenter(FavoriteContract.View view, MealRepository mealRepository) {
        this.view = view;
        this.mealRepository = mealRepository;
    }

    @Override
    public void loadFavouriteMeals() {
        compositeDisposable.add(
                mealRepository.getAllFavouriteMeals2()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(meals -> {
                            if (meals.isEmpty()) {
                                view.showEmptyView();
                            } else {
                                view.showFavouriteMeals(meals);
                            }
                        }, throwable -> view.showError(throwable.getMessage()))
        );
    }

    @Override
    public void deleteMealFromFavorites(Meal meal) {
        compositeDisposable.add(
                mealRepository.deleteMealFromFavorites(meal)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                        }, throwable -> view.showError(throwable.getMessage()))
        );
    }

    public void onDestroy() {
        compositeDisposable.clear();
    }
}
