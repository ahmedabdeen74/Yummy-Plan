package com.example.foodplannertest.views.profile.presenter;

import android.content.Context;
import android.util.Log;

import com.example.foodplannertest.data.auth.FirebaseAuthHelper;
import com.example.foodplannertest.data.auth.UserData;
import com.example.foodplannertest.data.models.Meal;
import com.example.foodplannertest.data.repo.MealRepository;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ProfilePresenter {
    private final ProfileView view;
    private final MealRepository repository;
    private final FirebaseAuthHelper authHelper;
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final FirebaseFirestore firestore;

    public ProfilePresenter(ProfileView view, MealRepository repository, FirebaseAuthHelper authHelper) {
        this.view = view;
        this.repository = repository;
        this.authHelper = authHelper;
        this.firestore = FirebaseFirestore.getInstance();
    }

    public void getUserEmail() {
        String email = authHelper.getCurrentUserEmail();
        if (email != null) {
            view.showEmail(email);
        }
    }

    public void logout() {
        view.showLoading();

        String userEmail = authHelper.getCurrentUserEmail();
        if (userEmail == null) {
            handleLogout();
            return;
        }

        disposables.add(
                Single.zip(
                                getFavoriteMeals(),
                                getCalendarMeals(),
                                (favoriteMeals, calendarMeals) -> new UserData(favoriteMeals, calendarMeals)
                        )
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                userData -> saveDataToFirebase(userEmail, userData),
                                error -> {
                                    Log.e("ProfilePresenter", "Error getting meals", error);
                                    view.showError("Failed to save data: " + error.getMessage());
                                    handleLogout();
                                }
                        )
        );
    }

    private Single<List<Meal>> getFavoriteMeals() {
        return Single.fromCallable(() -> repository.getAllFavoriteMeals())
                .subscribeOn(Schedulers.io());
    }

    private Single<List<Meal>> getCalendarMeals() {
        return Single.fromCallable(() -> repository.getAllCalendarMeals())
                .subscribeOn(Schedulers.io());
    }

    private void saveDataToFirebase(String userEmail, UserData userData) {
        disposables.add(
                Completable.fromAction(() ->
                                firestore.collection("users").document(userEmail)
                                        .set(userData)
                        )
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                () -> {
                                    view.showDataSavedSuccessfully();
                                    clearLocalData();
                                },
                                error -> {
                                    Log.e("ProfilePresenter", "Error saving to Firebase", error);
                                    view.showError("Failed to save data to Firebase: " + error.getMessage());
                                    handleLogout();
                                }
                        )
        );
    }

    private void clearLocalData() {
        disposables.add(
                Completable.mergeArray(
                                repository.deleteAllFavorites(),
                                repository.deleteAllCalendarMeals()
                        )
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                this::handleLogout,
                                error -> {
                                    Log.e("ProfilePresenter", "Error clearing local data", error);
                                    view.showError("Failed to clear local data: " + error.getMessage());
                                    handleLogout();
                                }
                        )
        );
    }

    private void handleLogout() {
        authHelper.logout();
        view.hideLoading();
        view.navigateToLogin();
    }

    public void onDestroy() {
        disposables.clear();
    }
}