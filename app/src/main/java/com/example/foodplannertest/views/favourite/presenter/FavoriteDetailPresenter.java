package com.example.foodplannertest.views.favourite.presenter;



import com.example.foodplannertest.data.models.Meal;
import com.example.foodplannertest.data.repo.MealRepository;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class FavoriteDetailPresenter implements FavoriteDetailContract.Presenter {

    private FavoriteDetailContract.View view;
    private MealRepository mealRepository;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public FavoriteDetailPresenter(FavoriteDetailContract.View view, MealRepository mealRepository) {
        this.view = view;
        this.mealRepository = mealRepository;
    }

    @Override
    public void loadMealDetails(String mealId) {
        compositeDisposable.add(
                mealRepository.getMealById(mealId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(meal -> {
                            if (meal != null) {
                                view.showMealDetails(meal);
                            } else {
                                view.showError("Meal not found");
                            }
                        }, throwable -> view.showError(throwable.getMessage()))
        );
    }

    public void onDestroy() {
        compositeDisposable.clear();
    }
}
