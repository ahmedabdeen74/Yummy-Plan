package com.example.foodplannertest.data.remote;

import io.reactivex.rxjava3.core.Single;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MealServices {
    @GET("random.php")
    Single<MealResponse> getRandomMeal();
    @GET("lookup.php")
    Single<MealResponse> getMealDetails(@Query("i") String mealId);

    @GET("list.php?i=list")
    Single<IngredientResponse> getIngredients();

    @GET("list.php?a=list")
    Single<AreaResponse> getAreas();

    @GET("list.php?c=list")
    Single<CategoryResponse> getCategories();

    @GET("filter.php")
    Single<MealResponse> getMealsByIngredient(@Query("i") String ingredient);

    @GET("filter.php")
    Single<MealResponse> getMealsByArea(@Query("a") String area);

    @GET("filter.php")
    Single<MealResponse> getMealsByCategory(@Query("c") String category);

    @GET("lookup.php")
    Call<MealResponse> getMealDetails2(@Query("i") String mealId);
}


