package com.example.foodplannertest.views.search.presenter;

import com.example.foodplannertest.data.models.Meal;

import java.util.List;

public interface SearchResultsView {
    void showSearchResults(List<Meal> meals);
    void showFilteredResults(List<Meal> meals);
    void showError(String message);
}
