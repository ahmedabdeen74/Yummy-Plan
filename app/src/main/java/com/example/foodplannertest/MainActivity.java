package com.example.foodplannertest;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.foodplannertest.data.auth.FirebaseAuthHelper;
import com.example.foodplannertest.helper.NetworkConnectivityManager;
import com.example.foodplannertest.data.pref.UserPreferences;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private NavController navController;
    private BottomNavigationView bottomNavigationView;
    private FirebaseAuthHelper authHelper;
    private UserPreferences userPreferences;
    private NetworkConnectivityManager networkManager;
    private View networkStatusView;
    private boolean isNetworkAvailable = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View rootView = findViewById(android.R.id.content);
        rootView.setBackgroundColor(Color.parseColor("#FAF3E0"));

        // Initialize network status view
        networkStatusView = getLayoutInflater().inflate(R.layout.network_status_view, null);
        ((ViewGroup) findViewById(android.R.id.content)).addView(networkStatusView);

        // Initialize network manager
        networkManager = new NetworkConnectivityManager(this);
        networkManager.getNetworkAvailability().observe(this, available -> {
            isNetworkAvailable = available;
            networkStatusView.setVisibility(available ? View.GONE : View.VISIBLE);
        });

        authHelper = new FirebaseAuthHelper(this);
        userPreferences = new UserPreferences(this);

        //  NavHostFragment
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();

        //  BottomNavigationView link with NavController
        bottomNavigationView = findViewById(R.id.bottom_nav_view);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);


        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            boolean requiresNetwork = (id == R.id.homeFragment || id == R.id.searchFragment || id == R.id.profileFragment);

            if (requiresNetwork && !isNetworkAvailable) {
                Toast.makeText(this, "No internet connection available. Please check your connection and try again.", Toast.LENGTH_SHORT).show();
                return false;
            }


            if (!userPreferences.isFirstSignup() && !userPreferences.isLoggedIn() &&
                    (id == R.id.favoriteFragment || id == R.id.calendarFragment || id == R.id.profileFragment)) {
                showLoginRequiredDialog();
                return false;
            }

            try {
                if (id == R.id.homeFragment) {
                    navController.navigate(R.id.homeFragment);
                } else if (id == R.id.searchFragment) {
                    navController.navigate(R.id.searchFragment);
                } else if (id == R.id.favoriteFragment) {
                    navController.navigate(R.id.favoriteFragment);
                } else if (id == R.id.calendarFragment) {
                    navController.navigate(R.id.calendarFragment);
                } else if (id == R.id.profileFragment) {
                    navController.navigate(R.id.profileFragment);
                }
                return true;
            } catch (Exception e) {

                Toast.makeText(this, "error" + e.getMessage(), Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.homeFragment ||
                    destination.getId() == R.id.favoriteFragment ||
                    destination.getId() == R.id.calendarFragment ||
                    destination.getId() == R.id.profileFragment ||
                    destination.getId() == R.id.mealDetailFragment ||
                    destination.getId() == R.id.mealSearchDetailsFragment2) {

                bottomNavigationView.setVisibility(View.VISIBLE);
            } else {
                bottomNavigationView.setVisibility(View.GONE);
            }
        });
    }

    private void showLoginRequiredDialog() {

        androidx.appcompat.app.AlertDialog dialog = new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Login Required")
                .setMessage("You need to login to access this feature.")
                .setPositiveButton("Login", (dialogInterface, which) -> {
                    navController.navigate(R.id.loginFragment);
                })
                .setNegativeButton("Cancel", (dialogInterface, which) -> {
                    dialogInterface.dismiss();
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .create();


        dialog.show();


        if (dialog.getWindow() != null) {
            GradientDrawable drawable = new GradientDrawable();
            drawable.setColor(Color.parseColor("#FAF3E0"));
            drawable.setCornerRadius(16f);
            dialog.getWindow().setBackgroundDrawable(drawable);
        }
    }

    public void navigateToLoginFragment() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        navController.navigate(R.id.loginFragment);
    }

    @Override
    protected void onResume() {
        super.onResume();
        networkManager.startNetworkCallback();
    }

    @Override
    protected void onPause() {
        super.onPause();
        networkManager.stopNetworkCallback();
    }
}