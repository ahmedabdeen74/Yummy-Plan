package com.example.foodplannertest.views.profile.presenter;

import com.example.foodplannertest.data.models.Meal;

import java.util.List;

public interface ProfileView {
    void showEmail(String email);
    void navigateToLogin();
    void showError(String message);
    void showLoading();
    void hideLoading();
    void showDataSavedSuccessfully();
}
