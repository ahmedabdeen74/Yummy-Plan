package com.example.foodplannertest.views.signup.presenter;



import com.example.foodplannertest.data.auth.FirebaseAuthHelper;
import com.example.foodplannertest.data.pref.UserPreferences;

public class SignupPresenter implements SignupContract.Presenter {
    private SignupContract.View view;
    private FirebaseAuthHelper authHelper;
    public SignupPresenter(SignupContract.View view, FirebaseAuthHelper authHelper) {
        this.view = view;
        this.authHelper = authHelper;
    }

    @Override
    public void registerUser(String email, String password) {
        if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            view.showError("Email and password must not be empty.");
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            view.showError("Invalid email format.");
            return;
        }

        if (password.length() < 6) {
            view.showError("Password must be at least 6 characters.");
            return;
        }

        authHelper.registerUser(email, password, new FirebaseAuthHelper.AuthCallback() {
            @Override
            public void onSuccess() {
                UserPreferences userPreferences = new UserPreferences(view.getContext());
                userPreferences.saveUser(email);
                view.navigateToSplash1();
            }

            @Override
            public void onFailure(String errorMessage) {
                view.showError(errorMessage);
            }
        });
    }

}
