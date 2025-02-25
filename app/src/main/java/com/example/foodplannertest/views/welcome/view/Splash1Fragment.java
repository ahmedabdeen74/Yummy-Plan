package com.example.foodplannertest.views.welcome.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.foodplannertest.R;
import com.example.foodplannertest.views.welcome.presenter.Splash1Contract;
import com.example.foodplannertest.views.welcome.presenter.Splash1Presenter;


public class Splash1Fragment extends Fragment implements Splash1Contract.View {
    private Splash1Presenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_splash1, container, false);

        presenter = new Splash1Presenter(this);

        Button btnGetStarted = view.findViewById(R.id.getStartedButton);
        btnGetStarted.setOnClickListener(v -> presenter.onGetStartedClicked());

        ImageView backButton = view.findViewById(R.id.btn_back);
        backButton.setOnClickListener(v -> requireActivity().onBackPressed());

        return view;
    }

    @Override
    public void navigateToSplash2() {
        Navigation.findNavController(requireView()).navigate(R.id.action_splash1Fragment_to_splash2Fragment);
    }
}