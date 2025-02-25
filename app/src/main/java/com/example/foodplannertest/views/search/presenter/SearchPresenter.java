package com.example.foodplannertest.views.search.presenter;

import com.example.foodplannertest.data.models.Area;
import com.example.foodplannertest.data.models.Category;
import com.example.foodplannertest.data.models.Ingredient;
import com.example.foodplannertest.data.remote.AreaResponse;
import com.example.foodplannertest.data.remote.CategoryResponse;
import com.example.foodplannertest.data.remote.IngredientResponse;
import com.example.foodplannertest.data.remote.MealServices;
import com.example.foodplannertest.data.remote.MealRemoteDataSource;
import com.example.foodplannertest.data.repo.MealRepository;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchPresenter {

    private SearchView view;
    private MealServices api;
    private MealRepository repository;
    private CompositeDisposable disposables = new CompositeDisposable();

    public SearchPresenter(SearchView view) {
        this.view = view;
        this.api = MealRemoteDataSource.getApi();
    }

    public void loadData(String type) {
        switch (type) {
            case "ingredients":
                disposables.add(repository.getIngredients()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                view::showIngredients,
                                throwable -> view.showError(throwable.getMessage())
                        ));
                break;

            case "areas":
                disposables.add(repository.getAreas()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                view::showAreas,
                                throwable -> view.showError(throwable.getMessage())
                        ));
                break;

            case "categories":
                disposables.add(repository.getCategories()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                view::showCategories,
                                throwable -> view.showError(throwable.getMessage())
                        ));
                break;
        }
    }

    public void dispose() {
        disposables.clear();
    }


    public void filter(String text) {

    }
    public void setRepository(MealRepository repository) {
        this.repository = repository;
    }

    public void onItemClicked(String itemName, String type) {
        view.navigateToSearchResults(itemName, type);
    }
}