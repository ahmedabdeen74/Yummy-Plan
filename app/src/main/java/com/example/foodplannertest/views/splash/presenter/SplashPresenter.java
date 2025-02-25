package com.example.foodplannertest.views.splash.presenter;

import com.example.foodplannertest.data.pref.UserPreferences;

public class SplashPresenter implements SplashContract.Presenter {
    private SplashContract.View view;
    private UserPreferences userPreferences;

    public SplashPresenter(SplashContract.View view, UserPreferences userPreferences) {
        this.view = view;
        this.userPreferences = userPreferences;
    }

    @Override
    public void checkUserLoggedIn() {
        if (userPreferences.isLoggedIn()) {
            view.navigateToHome();
        } else {
            view.navigateToLogin();
        }
    }
}
