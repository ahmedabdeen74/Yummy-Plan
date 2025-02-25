package com.example.foodplannertest.views.search.presenter;



import com.example.foodplannertest.data.models.Meal;
import com.example.foodplannertest.data.repo.MealRepository;


import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SearchResultsPresenter {

    private SearchResultsView view;
    private MealRepository repository;
    private CompositeDisposable disposables = new CompositeDisposable();

    public SearchResultsPresenter(SearchResultsView view, MealRepository repository) {
        this.view = view;
        this.repository = repository;
    }

    public void loadSearchResults(String itemName, String type) {
        switch (type) {
            case "ingredient":
                disposables.add(repository.getMealsByIngredient(itemName)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                view::showSearchResults,
                                throwable -> view.showError(throwable.getMessage())
                        ));
                break;

            case "area":
                disposables.add(repository.getMealsByArea(itemName)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                view::showSearchResults,
                                throwable -> view.showError(throwable.getMessage())
                        ));
                break;

            case "category":
                disposables.add(repository.getMealsByCategory(itemName)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                view::showSearchResults,
                                throwable -> view.showError(throwable.getMessage())
                        ));
                break;
        }
    }

    public void filterResults(List<Meal> originalMeals, String query) {
        List<Meal> filteredMeals = new ArrayList<>();
        for (Meal meal : originalMeals) {
            if (meal.getStrMeal().toLowerCase().contains(query.toLowerCase())) {
                filteredMeals.add(meal);
            }
        }
        view.showFilteredResults(filteredMeals);
    }

    public void dispose() {
        disposables.clear();
    }
}
