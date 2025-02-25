package com.example.foodplannertest.data.repo;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;

import com.example.foodplannertest.data.local.MealDao;
import com.example.foodplannertest.data.models.Area;
import com.example.foodplannertest.data.models.Category;
import com.example.foodplannertest.data.models.Ingredient;
import com.example.foodplannertest.data.models.Meal;
import com.example.foodplannertest.data.remote.AreaResponse;
import com.example.foodplannertest.data.remote.CategoryResponse;
import com.example.foodplannertest.data.remote.IngredientResponse;
import com.example.foodplannertest.data.remote.MealResponse;
import com.example.foodplannertest.data.remote.MealServices;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MealRepository {

    private MealServices mealServices;
    private MealDao mealDao;
    private Executor executor;


    public MealRepository(MealServices mealServices, MealDao mealDao) {
        this.mealServices = mealServices;
        this.mealDao = mealDao;
        this.executor = Executors.newSingleThreadExecutor();
    }
    // fetch random meal in home fragment
    public Observable<List<Meal>> getRandomMeals() {
        return Observable.range(0, 10)
                .flatMap(i -> mealServices.getRandomMeal()
                        .subscribeOn(Schedulers.io())
                        .toObservable()
                        .map(response -> response.getMeals().get(0)))
                .toList()
                .toObservable();
    }
    // meal detail
    public Single<Meal> getMealDetails(String mealId) {
        return mealServices.getMealDetails(mealId)
                .subscribeOn(Schedulers.io())
                .map(response -> response.getMeals().get(0));
    }

    // meal adapter
    public Completable saveMealToFavorites(Meal meal) {
        return Completable.fromAction(() -> {
            meal.setType("favourite");
            mealDao.insert(meal);
        }).subscribeOn(Schedulers.io());
           }

    public Completable saveMealToCalendar(Meal meal, String date) {
        return Completable.fromAction(() -> {
            meal.setType("calendar");
            meal.setDate(date);
            mealDao.insert(meal);
        }).subscribeOn(Schedulers.io());
            }


            // fetch ingredients
            public Single<List<Ingredient>> getIngredients() {
                return mealServices.getIngredients()
                        .subscribeOn(Schedulers.io())
                        .map(IngredientResponse::getMeals);
            }

            // fetch areas
            public Single<List<Area>> getAreas() {
                return mealServices.getAreas()
                        .subscribeOn(Schedulers.io())
                        .map(AreaResponse::getMeals);
            }

            // fetch categories
            public Single<List<Category>> getCategories() {
                return mealServices.getCategories()
                        .subscribeOn(Schedulers.io())
                        .map(CategoryResponse::getMeals);
            }

           // fetch ingredients by id
            public Single<List<Meal>> getMealsByIngredient(String ingredient) {
                return mealServices.getMealsByIngredient(ingredient)
                        .subscribeOn(Schedulers.io())
                        .map(MealResponse::getMeals);
            }

           // fetch area by id
            public Single<List<Meal>> getMealsByArea(String area) {
                return mealServices.getMealsByArea(area)
                        .subscribeOn(Schedulers.io())
                        .map(MealResponse::getMeals);
            }

            // fetch Category by id
            public Single<List<Meal>> getMealsByCategory(String category) {
                return mealServices.getMealsByCategory(category)
                        .subscribeOn(Schedulers.io())
                        .map(MealResponse::getMeals);
            }
            // handle profile fragment
            public List<Meal> getAllFavoriteMeals() {
               return mealDao.getAllFavouriteMeals();
            }
            public List<Meal> getAllCalendarMeals() {
               return mealDao.getAllCalendarMeals();
            }
            public Completable deleteAllFavorites() {
               return Completable.fromAction(() -> mealDao.deleteAllFavouriteMeals());
            }

            public Completable deleteAllCalendarMeals() {
               return Completable.fromAction(() -> mealDao.deleteAllCalendarMeals());
            }

           // handle calendar fragment
            public Observable<List<Meal>> getAllCalendarMeals1() {
              return Observable.fromCallable(() -> mealDao.getAllCalendarMeals())
                .subscribeOn(Schedulers.io());
             }
            public Observable<List<Meal>> getMealsByDate(String date) {
               return Observable.fromCallable(() -> mealDao.getMealsByDate(date))
                .subscribeOn(Schedulers.io());
             }
             public Completable deleteMealFromCalendar(Meal meal) {
               return Completable.fromAction(() -> mealDao.deleteByMealIdAndType(meal.getIdMeal(), "calendar"))
                .subscribeOn(Schedulers.io());
             }

           // handle favourite fragment
            public Observable<List<Meal>> getAllFavouriteMeals2() {
               return Observable.fromCallable(() -> mealDao.getAllFavouriteMeals())
                .subscribeOn(Schedulers.io());
             }

           public Completable deleteMealFromFavorites(Meal meal) {
               return Completable.fromAction(() -> mealDao.deleteByMealIdAndType(meal.getIdMeal(), "favourite"))
                .subscribeOn(Schedulers.io());
             }


          public Observable<Meal> getMealById(String mealId) {
              return Observable.fromCallable(() -> mealDao.getMealById(mealId))
                .subscribeOn(Schedulers.io());
            }



    }

