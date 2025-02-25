package com.example.foodplannertest.views.calendar.presenter;

import com.example.foodplannertest.data.models.Meal;

import java.util.List;


import com.example.foodplannertest.data.models.Meal;

import java.util.List;

public interface CalendarContract {

    interface View {
        void showMeals(List<Meal> meals);
        void showEmptyCalendar();
        void showEmptyDay();
        void showError(String message);
    }

    interface Presenter {
        void loadMealsForDate(String date);
        void deleteMealFromCalendar(Meal meal);
    }
}
