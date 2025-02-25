package com.example.foodplannertest.data.auth;

import com.example.foodplannertest.data.models.Meal;

import java.util.List;

public class UserData {
    private List<Meal> favouriteMeals;
    private List<Meal> calendarMeals;

    public UserData() {
        // Required for Firestore
    }

    public UserData(List<Meal> favouriteMeals, List<Meal> calendarMeals) {
        this.favouriteMeals = favouriteMeals;
        this.calendarMeals = calendarMeals;
    }

    public List<Meal> getFavouriteMeals() {
        return favouriteMeals;
    }

    public void setFavouriteMeals(List<Meal> favouriteMeals) {
        this.favouriteMeals = favouriteMeals;
    }

    public List<Meal> getCalendarMeals() {
        return calendarMeals;
    }

    public void setCalendarMeals(List<Meal> calendarMeals) {
        this.calendarMeals = calendarMeals;
    }
}
