package com.example.foodplannertest.views.home.presenter;

import com.example.foodplannertest.data.models.Meal;
import com.example.foodplannertest.data.repo.MealRepository;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class HomePresenter {

    private HomeView view;
    private MealRepository repository;

    private CompositeDisposable disposables = new CompositeDisposable();

    public HomePresenter(HomeView view, MealRepository repository) {
        this.view = view;
        this.repository = repository;
    }

    public void getRandomMeals() {
        Disposable disposable = repository.getRandomMeals()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(meals -> view.showRandomMeals(meals),

                        throwable -> view.showError(throwable.getMessage()));

        disposables.add(disposable);
    }
    public void getRandomMeals2() {
        Disposable disposable = repository.getRandomMeals()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(meals -> view.showRandomMeals2(meals),
                        throwable -> view.showError(throwable.getMessage()));

        disposables.add(disposable);
    }

    public void saveMealToFavorites(Meal meal) {
        Disposable disposable = repository.saveMealToFavorites(meal)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> view.showMealSavedToFavorites(),
                        throwable -> view.showError(throwable.getMessage()));

        disposables.add(disposable);
    }

    public void saveMealToCalendar(Meal meal, String date) {
        Disposable disposable = repository.saveMealToCalendar(meal, date)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> view.showMealAddedToCalendar(),
                        throwable -> view.showError(throwable.getMessage()));

        disposables.add(disposable);
    }


    public void onDestroy() {
        if (!disposables.isDisposed()) {
            disposables.dispose();
        }
    }
}
