package com.example.foodplannertest.views.login.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.foodplannertest.R;
import com.example.foodplannertest.data.auth.FirebaseAuthHelper;
import com.example.foodplannertest.views.login.presenter.LoginContract;
import com.example.foodplannertest.views.login.presenter.LoginPresenter;


public class LoginFragment extends Fragment implements LoginContract.View {
    private static final int RC_SIGN_IN = 9001;
    private EditText editTextEmail, editTextPassword;
    private LoginPresenter presenter;
    private FirebaseAuthHelper authHelper;
    private TextView guestText;
    private LinearLayout emailContainer, passwordContainer;
    private ImageView btnTogglePasswordVisibility;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_login, container, false);

        authHelper = new FirebaseAuthHelper(requireContext());
        presenter = new LoginPresenter(this, authHelper);

        editTextEmail = view.findViewById(R.id.btn_email);
        editTextPassword = view.findViewById(R.id.btn_password);
        Button btnLogin = view.findViewById(R.id.registerButton);
        TextView btnSignup = view.findViewById(R.id.signInText);
        ImageView btnGoogleSignIn = view.findViewById(R.id.btnGoogleSignIn);
        guestText = view.findViewById(R.id.guestButton);
        emailContainer = view.findViewById(R.id.emailContainer);
        passwordContainer = view.findViewById(R.id.passwordContainer);
        btnTogglePasswordVisibility = view.findViewById(R.id.btnTogglePasswordVisibility);

        guestText.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_homeFragment));
        btnLogin.setOnClickListener(v -> {
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();
            presenter.loginUser(email, password);
        });
        btnSignup.setOnClickListener(v -> presenter.navigateToSignup());
        btnGoogleSignIn.setOnClickListener(v -> presenter.signInWithGoogle());

        btnTogglePasswordVisibility.setOnClickListener(v -> {
            boolean isVisible = editTextPassword.getTransformationMethod() == null;
            presenter.togglePasswordVisibility(!isVisible);
        });

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                requireActivity().finish();
            }
        });

        return view;
    }

    @Override
    public void showError(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void navigateToHome() {
        Navigation.findNavController(requireView()).navigate(R.id.action_loginFragment_to_homeFragment);
    }

    @Override
    public void navigateToSignup() {
        Navigation.findNavController(requireView()).navigate(R.id.action_loginFragment_to_signupFragment);
    }

    @Override
    public void showPasswordVisibility(boolean isVisible) {
        if (isVisible) {
            editTextPassword.setTransformationMethod(null);
            btnTogglePasswordVisibility.setImageResource(R.drawable.visibility);
        } else {
            editTextPassword.setTransformationMethod(new android.text.method.PasswordTransformationMethod());
            btnTogglePasswordVisibility.setImageResource(R.drawable.visibilityoff);
        }
        editTextPassword.setSelection(editTextPassword.getText().length());
    }

    @Override
    public void setEmailError(String message) {
        editTextEmail.setHint(message);
        editTextEmail.setHintTextColor(getResources().getColor(android.R.color.holo_red_light));
        emailContainer.setBackgroundResource(R.drawable.edittext_error_background);
    }

    @Override
    public void setPasswordError(String message) {
        editTextPassword.setHint(message);
        editTextPassword.setHintTextColor(getResources().getColor(android.R.color.holo_red_light));
        passwordContainer.setBackgroundResource(R.drawable.edittext_error_background);
    }

    @Override
    public void clearEmailError() {
        if (!editTextPassword.getText().toString().trim().isEmpty()) {
            emailContainer.setBackgroundResource(R.drawable.edittext_background);
        }
    }

    @Override
    public void clearPasswordError() {
        if (!editTextEmail.getText().toString().trim().isEmpty()) {
            passwordContainer.setBackgroundResource(R.drawable.edittext_background);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            authHelper.handleGoogleSignInResult(data, new FirebaseAuthHelper.AuthCallback() {
                @Override
                public void onSuccess() {
                    String userEmail = authHelper.getCurrentUserEmail();
                    if (userEmail != null) {
                        presenter.retrieveDataFromFirebase(userEmail);
                    }

                    if (authHelper.isLoggedInBefore()) {
                        navigateToHome();
                    } else {
                        Navigation.findNavController(requireView()).navigate(R.id.action_loginFragment_to_splash1Fragment);
                    }
                }

                @Override
                public void onFailure(String errorMessage) {
                    showError("error: " + errorMessage);
                }
            });
        }
    }
}