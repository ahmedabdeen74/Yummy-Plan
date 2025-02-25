package com.example.foodplannertest.data.auth;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.foodplannertest.R;
import com.example.foodplannertest.data.pref.UserPreferences;
import com.example.foodplannertest.data.local.MealDao;
import com.example.foodplannertest.data.models.Meal;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class FirebaseAuthHelper {
    private final FirebaseAuth mAuth;
    private final UserPreferences userPreferences;
    private final GoogleSignInClient googleSignInClient;
    private MealDao mealDao;


    public FirebaseAuthHelper(Context context) {
        FirebaseApp.initializeApp(context);
        mAuth = FirebaseAuth.getInstance();
        userPreferences = new UserPreferences(context);

        // Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(context, gso);
    }
    public GoogleSignInClient getGoogleSignInClient() {
        return googleSignInClient;
    }
    public String getCurrentUserEmail() {
        if (mAuth.getCurrentUser() != null) {
            return mAuth.getCurrentUser().getEmail();
        }
        return null;
    }

    public Intent getGoogleSignInIntent() {
        return googleSignInClient.getSignInIntent();
    }


    public void handleGoogleSignInResult(Intent data, AuthCallback callback) {
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            if (account != null) {
                firebaseAuthWithGoogle(account.getIdToken(), callback);
            }
        } catch (ApiException e) {
            callback.onFailure(e.getMessage());
        }
    }


    private void firebaseAuthWithGoogle(String idToken, AuthCallback callback) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        userPreferences.saveUser(mAuth.getCurrentUser().getEmail());
                        callback.onSuccess();
                    } else {
                        callback.onFailure(task.getException().getMessage());
                    }
                });
    }

    public void loginUser(String email, String password, AuthCallback callback) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        userPreferences.saveUser(email);
                        callback.onSuccess();
                    } else {
                        callback.onFailure(task.getException().getMessage());
                    }
                });
    }

    public void registerUser(String email, String password, AuthCallback callback) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess();
                    } else {
                        callback.onFailure(task.getException().getMessage());
                    }
                });
    }
    private void retrieveDataFromFirestore(String email) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();


        db.collection("users").document(email)
                .collection("favourites").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Meal meal = document.toObject(Meal.class);
                            mealDao.insert(meal);
                        }
                    } else {
                        Log.w("Firestore", "Error getting documents.", task.getException());
                    }
                });

        db.collection("users").document(email)
                .collection("calendar").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Meal meal = document.toObject(Meal.class);
                            mealDao.insert(meal);
                        }
                    } else {
                        Log.w("Firestore", "Error getting documents.", task.getException());
                    }
                });
    }

    public void logout() {
        mAuth.signOut();
        userPreferences.logout();
    }
    public boolean isLoggedInBefore() {
        return userPreferences.isLoggedInBefore();
    }


    public interface AuthCallback {
        void onSuccess();

        void onFailure(String errorMessage);
    }
}