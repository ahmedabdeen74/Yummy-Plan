package com.example.foodplannertest.views.signup.presenter;

import android.content.Context;

public interface SignupContract {
    interface View {
        void showError(String message);
        void navigateToSplash1();
        Context getContext();

    }

    interface Presenter {
        void registerUser(String email, String password);

    }
}
