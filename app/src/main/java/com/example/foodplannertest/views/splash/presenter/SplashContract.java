package com.example.foodplannertest.views.splash.presenter;

public interface SplashContract {
    interface View {
        void navigateToHome();
        void navigateToLogin();
    }

    interface Presenter {
        void checkUserLoggedIn();
    }
}
