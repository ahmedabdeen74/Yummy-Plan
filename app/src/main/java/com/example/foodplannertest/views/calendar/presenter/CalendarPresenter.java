package com.example.foodplannertest.views.calendar.presenter;

import com.example.foodplannertest.data.models.Meal;
import com.example.foodplannertest.data.repo.MealRepository;
import com.example.foodplannertest.views.calendar.presenter.CalendarContract;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class CalendarPresenter implements CalendarContract.Presenter {

    private CalendarContract.View view;
    private MealRepository mealRepository;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public CalendarPresenter(CalendarContract.View view, MealRepository mealRepository) {
        this.view = view;
        this.mealRepository = mealRepository;
    }

    @Override
    public void loadMealsForDate(String date) {
        compositeDisposable.add(
                mealRepository.getAllCalendarMeals1()
                        .flatMap(calendarMeals -> {
                            if (calendarMeals.isEmpty()) {
                                view.showEmptyCalendar();
                                return Observable.empty();
                            } else {
                                return mealRepository.getMealsByDate(date);
                            }
                        })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(meals -> {
                            if (meals.isEmpty()) {
                                view.showEmptyDay();
                            } else {
                                view.showMeals(meals);
                            }
                        }, throwable -> {
                            throwable.printStackTrace();
                            view.showError(" Error During loading meals " + throwable.getMessage());
                        })
        );
    }

    @Override
    public void deleteMealFromCalendar(Meal meal) {
        compositeDisposable.add(
                mealRepository.deleteMealFromCalendar(meal)
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