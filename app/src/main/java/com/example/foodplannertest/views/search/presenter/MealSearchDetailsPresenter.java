package com.example.foodplannertest.views.search.presenter;



import com.example.foodplannertest.data.models.Meal;
import com.example.foodplannertest.data.repo.MealRepository;


import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MealSearchDetailsPresenter {

    private MealSearchDetailsView view;
    private MealRepository repository;
    private CompositeDisposable disposables = new CompositeDisposable();

    public MealSearchDetailsPresenter(MealSearchDetailsView view, MealRepository repository) {
        this.view = view;
        this.repository = repository;
    }


    public void loadMealDetails(String mealId) {
        disposables.add(repository.getMealDetails(mealId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        view::showMealDetails,
                        throwable -> view.showError(throwable.getMessage())
                ));
    }


    public void dispose() {
        disposables.clear();
    }
}