package com.example.foodplannertest.views.welcome.presenter;

public interface Splash1Contract {
    interface View {
        void navigateToSplash2();
    }

    interface Presenter {
        void onGetStartedClicked();
    }
}
