package com.example.foodplannertest.views.welcome.presenter;

public class Splash1Presenter implements Splash1Contract.Presenter {
    private Splash1Contract.View view;

    public Splash1Presenter(Splash1Contract.View view) {
        this.view = view;
    }

    @Override
    public void onGetStartedClicked() {
        view.navigateToSplash2();
    }
}
