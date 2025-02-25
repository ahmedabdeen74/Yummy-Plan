package com.example.foodplannertest.views.search.presenter;

import com.example.foodplannertest.data.models.Area;
import com.example.foodplannertest.data.models.Category;
import com.example.foodplannertest.data.models.Ingredient;

import java.util.List;

public interface SearchView {
    void showIngredients(List<Ingredient> ingredients);
    void showAreas(List<Area> areas);
    void showCategories(List<Category> categories);
    void showError(String message);
    void navigateToSearchResults(String itemName, String type);
}

