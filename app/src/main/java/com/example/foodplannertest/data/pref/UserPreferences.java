package com.example.foodplannertest.data.pref;



import android.content.Context;
import android.content.SharedPreferences;

public class UserPreferences {
    private static final String PREF_NAME = "user_prefs";
    private static final String KEY_USER_EMAIL = "user_email";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";
    private static final String KEY_FIRST_SIGNUP = "first_signup";
    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    public UserPreferences(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("Context cannot be null");
        }
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveUser(String email) {
        editor.putString(KEY_USER_EMAIL, email);
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putBoolean(KEY_FIRST_SIGNUP, true);
        editor.apply();
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }
    public boolean isFirstSignup() {
        return sharedPreferences.getBoolean(KEY_FIRST_SIGNUP, false);
    }

    public boolean isLoggedInBefore() {
        return sharedPreferences.contains(KEY_USER_EMAIL);
    }
    public void logout() {
        editor.clear();
        editor.apply();
    }
}
