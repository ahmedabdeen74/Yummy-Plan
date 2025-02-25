package com.example.foodplannertest.views.welcome.view;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.foodplannertest.R;
import com.example.foodplannertest.views.welcome.presenter.Splash2Contract;
import com.example.foodplannertest.views.welcome.presenter.Splash2Presenter;


public class SplashFragment2 extends Fragment implements Splash2Contract.View {
    private Splash2Presenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_splash2, container, false);

        presenter = new Splash2Presenter(this);

        TextView textView1 = view.findViewById(R.id.textView1);
        TextView textView2 = view.findViewById(R.id.textView2);
        Button btnGetStarted = view.findViewById(R.id.getStartedButton);
        ImageView backButton = view.findViewById(R.id.btn_back);

        btnGetStarted.setOnClickListener(v -> presenter.onGetStartedClicked());
        backButton.setOnClickListener(v -> requireActivity().onBackPressed());

        ObjectAnimator anim1 = ObjectAnimator.ofFloat(textView1, "translationX", -500f, 0f);
        anim1.setDuration(1500);
        anim1.start();

        ObjectAnimator anim2 = ObjectAnimator.ofFloat(textView2, "alpha", 0f, 1f);
        anim2.setDuration(2000);
        anim2.start();

        return view;
    }

    @Override
    public void navigateToHome() {
        Navigation.findNavController(requireView()).navigate(R.id.action_splash2Fragment_to_homeFragment);
    }
}