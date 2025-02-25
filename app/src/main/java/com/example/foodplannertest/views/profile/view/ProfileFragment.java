package com.example.foodplannertest.views.profile.view;

import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.example.foodplannertest.R;
import com.example.foodplannertest.data.auth.FirebaseAuthHelper;
import com.example.foodplannertest.data.local.AppDatabase;
import com.example.foodplannertest.data.repo.MealRepository;
import com.example.foodplannertest.views.profile.presenter.ProfilePresenter;
import com.example.foodplannertest.views.profile.presenter.ProfileView;

public class ProfileFragment extends Fragment implements ProfileView {
    private LinearLayout logOutButton;
    private LinearLayout favButton;
    private LinearLayout todoButton;
    private TextView userEmailTextView;
   // private ProgressBar progressBar;
    private FirebaseAuthHelper authHelper;
    private ProfilePresenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Initialize components
        initUI(view);

        // Setup the presenter
        setupPresenter();

        // Get user email through the presenter
        presenter.getUserEmail();

        // Set click listeners
        setupClickListeners();

        // animation for text
        applyTextAnimation(view);
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                requireActivity().finish();
            }
        });

        return view;
    }

    private void initUI(View view) {
        logOutButton = view.findViewById(R.id.btn_logout);
        favButton = view.findViewById(R.id.btn_fav);
        todoButton = view.findViewById(R.id.btn_plan);
        userEmailTextView = view.findViewById(R.id.userEmailTextView);
        TextView animatedText = view.findViewById(R.id.animatedText);


        ObjectAnimator animator = ObjectAnimator.ofFloat(animatedText, "translationX", -500f, 0f);
        animator.setDuration(1500);
        animator.start();


    }

    private void setupPresenter() {
        authHelper = new FirebaseAuthHelper(requireContext());
        AppDatabase db = AppDatabase.getInstance(requireContext());
        MealRepository repository = new MealRepository(
                null,
                db.mealDao()
        );
        presenter = new ProfilePresenter(this, repository, authHelper);
    }

    private void setupClickListeners() {
        favButton.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_profileFragment_to_favoriteFragment);
        });

        todoButton.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_profileFragment_to_calendarFragment2);
        });

        logOutButton.setOnClickListener(v -> showLogoutConfirmationDialog());
    }

    private void applyTextAnimation(View view) {
        TextView animatedText = view.findViewById(R.id.animatedText);
        ObjectAnimator animator = ObjectAnimator.ofFloat(animatedText, "translationX", -500f, 0f);
        animator.setDuration(1500);
        animator.start();
    }

    private void showLogoutConfirmationDialog() {
        androidx.appcompat.app.AlertDialog dialog = new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setTitle("Logout Confirmation")
                .setMessage("Are you sure you want to log out?")
                .setPositiveButton("Yes", (dialogInterface, which) -> {
                    presenter.logout();
                })
                .setNegativeButton("No", (dialogInterface, which) -> dialogInterface.dismiss())
                .create();

        dialog.show();

        if (dialog.getWindow() != null) {
            GradientDrawable drawable = new GradientDrawable();
            drawable.setColor(Color.parseColor("#FAF3E0"));
            drawable.setCornerRadius(16f);
            dialog.getWindow().setBackgroundDrawable(drawable);
        }
    }

    @Override
    public void showEmail(String email) {
        userEmailTextView.setText(email);
    }

    @Override
    public void navigateToLogin() {
        Navigation.findNavController(requireView()).navigate(R.id.action_profileFragment2_to_loginFragment);
    }

    @Override
    public void showError(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoading() {
      //  progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
       // progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showDataSavedSuccessfully() {
        Toast.makeText(requireContext(), "Data saved successfully", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}