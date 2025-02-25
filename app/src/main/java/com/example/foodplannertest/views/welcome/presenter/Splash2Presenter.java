package com.example.foodplannertest.views.welcome.presenter;

public class Splash2Presenter implements Splash2Contract.Presenter {
    private Splash2Contract.View view;

    public Splash2Presenter(Splash2Contract.View view) {
        this.view = view;
    }

    @Override
    public void onGetStartedClicked() {
        view.navigateToHome();
    }
}
