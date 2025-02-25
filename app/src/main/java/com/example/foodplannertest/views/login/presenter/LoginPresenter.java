package com.example.foodplannertest.views.login.presenter;

import android.content.Intent;
import android.util.Log;

import androidx.fragment.app.Fragment;

import com.example.foodplannertest.data.auth.FirebaseAuthHelper;
import com.example.foodplannertest.data.auth.UserData;
import com.example.foodplannertest.data.local.AppDatabase;
import com.example.foodplannertest.data.local.MealDao;
import com.example.foodplannertest.views.login.presenter.LoginContract;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class LoginPresenter implements LoginContract.Presenter {
    private LoginContract.View view;
    private FirebaseAuthHelper authHelper;

    public LoginPresenter(LoginContract.View view, FirebaseAuthHelper authHelper) {
        this.view = view;
        this.authHelper = authHelper;
    }

    @Override
    public void loginUser(String email, String password) {
        boolean isEmailEmpty = email.isEmpty();
        boolean isPasswordEmpty = password.isEmpty();
        if (isEmailEmpty && isPasswordEmpty) {
            view.setEmailError("Email is required");
            view.setPasswordError("Password is required");
            return;
        }
        if (email.isEmpty()) {
            view.setEmailError("Email is required");
            return;
        } else {
            view.clearEmailError();
        }

        if (password.isEmpty()) {
            view.setPasswordError("Password is required");
            return;
        } else {
            view.clearPasswordError();
        }
        authHelper.loginUser(email, password, new FirebaseAuthHelper.AuthCallback() {
            @Override
            public void onSuccess() {
                retrieveDataFromFirebase(email);
                view.navigateToHome();
            }

            @Override
            public void onFailure(String errorMessage) {
                view.showError("Error: " + errorMessage);
            }
        });
    }

    @Override
    public void signInWithGoogle() {
        authHelper.getGoogleSignInClient().signOut().addOnCompleteListener(task -> {
            Intent signInIntent = authHelper.getGoogleSignInIntent();
            ((Fragment) view).startActivityForResult(signInIntent, 9001);
        });
    }

    @Override
    public void togglePasswordVisibility(boolean isVisible) {
        view.showPasswordVisibility(isVisible);
    }

    public void retrieveDataFromFirebase(String userEmail) {
        FirebaseFirestore dbFirestore = FirebaseFirestore.getInstance();
        dbFirestore.collection("users").document(userEmail)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        UserData userData = documentSnapshot.toObject(UserData.class);
                        if (userData != null) {
                            saveDataToRoom(userData);
                        }
                    }
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Error retrieving data", e));
    }

    private void saveDataToRoom(UserData userData) {
        AppDatabase db = AppDatabase.getInstance(((Fragment) view).requireContext());
        MealDao mealDao = db.mealDao();

        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            mealDao.insertAll(userData.getFavouriteMeals());
            mealDao.insertAll(userData.getCalendarMeals());
        });
    }
    @Override
    public void navigateToSignup() {
        view.navigateToSignup();
    }

}