package com.example.foodplannertest.views.welcome.presenter;

public interface Splash2Contract {
    interface View {
        void navigateToHome();
    }

    interface Presenter {
        void onGetStartedClicked();
    }
}
