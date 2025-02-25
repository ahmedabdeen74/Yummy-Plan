package com.example.foodplannertest.views.login.presenter;

public interface LoginContract {
    interface View {
        void showError(String message);
        void navigateToHome();
        void navigateToSignup();
        void showPasswordVisibility(boolean isVisible);
        void setEmailError(String message);
        void setPasswordError(String message);
        void clearEmailError();
        void clearPasswordError();
    }

    interface Presenter {
        void navigateToSignup();
        void loginUser(String email, String password);
        void signInWithGoogle();
        void togglePasswordVisibility(boolean isVisible);
    }
}