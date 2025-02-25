package com.example.foodplannertest.views.splash.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.foodplannertest.R;
import com.example.foodplannertest.data.pref.UserPreferences;
import com.example.foodplannertest.views.splash.presenter.SplashContract;
import com.example.foodplannertest.views.splash.presenter.SplashPresenter;


import android.os.Handler;

public class SplashFragment extends Fragment implements SplashContract.View {
    private static final int SPLASH_DELAY = 2000;
    private SplashPresenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_splash, container, false);

        UserPreferences userPreferences = new UserPreferences(requireContext());
        presenter = new SplashPresenter(this, userPreferences);

        new Handler().postDelayed(() -> presenter.checkUserLoggedIn(), SPLASH_DELAY);

        TextView textView = view.findViewById(R.id.textView2);
        Animation animation = AnimationUtils.loadAnimation(requireContext(), R.anim.translate_up);
        textView.startAnimation(animation);

        return view;
    }

    @Override
    public void navigateToHome() {
        Navigation.findNavController(requireView()).navigate(R.id.action_splashFragment_to_homeFragment);
    }

    @Override
    public void navigateToLogin() {
        Navigation.findNavController(requireView()).navigate(R.id.action_splashFragment_to_loginFragment);
    }
}