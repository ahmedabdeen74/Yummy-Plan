package com.example.foodplannertest.views.signup.view;

import android.animation.ObjectAnimator;
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
import com.example.foodplannertest.views.signup.presenter.SignupContract;
import com.example.foodplannertest.views.signup.presenter.SignupPresenter;


public class SignupFragment extends Fragment implements SignupContract.View {
    private EditText editTextEmail, editTextPassword;
    private SignupPresenter presenter;
    private FirebaseAuthHelper authHelper;
    LinearLayout emailContainer;
    LinearLayout passwordContainer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_signup, container, false);

        authHelper = new FirebaseAuthHelper(requireContext());
        presenter = new SignupPresenter(this, authHelper);

        editTextEmail = view.findViewById(R.id.btn_email);
        editTextPassword = view.findViewById(R.id.btn_password);
        Button btnSignup = view.findViewById(R.id.registerButton);
        TextView signInText = view.findViewById(R.id.signInText);
        TextView animatedText = view.findViewById(R.id.animaText);
        ImageView btnTogglePasswordVisibility = view.findViewById(R.id.btnTogglePasswordVisibility);
        emailContainer = view.findViewById(R.id.emailContainer);
        passwordContainer = view.findViewById(R.id.passwordContainer);

        ObjectAnimator animator = ObjectAnimator.ofFloat(animatedText, "translationX", -500f, 0f);
        animator.setDuration(1500);
        animator.start();

        btnSignup.setOnClickListener(v -> {
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();
            registerUser();
        });

        signInText.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_signupFragment_to_loginFragment));

        setupPasswordVisibilityToggle(btnTogglePasswordVisibility, editTextPassword);

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
        Toast.makeText(requireContext(), "Error: " + message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void navigateToSplash1() {
        Navigation.findNavController(requireView()).navigate(R.id.action_signupFragment_to_splash1Fragment);
    }

    private void registerUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        boolean hasError = false;


        if (email.isEmpty()) {
            emailContainer.setBackgroundResource(R.drawable.edittext_error_background);
            editTextEmail.setHint("Email is required");
            editTextEmail.setHintTextColor(getResources().getColor(android.R.color.holo_red_light));
            hasError = true;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Invalid email format");
            hasError = true;
        } else {
            emailContainer.setBackgroundResource(R.drawable.edittext_background);
        }


        if (password.isEmpty()) {
            passwordContainer.setBackgroundResource(R.drawable.edittext_error_background);
            editTextPassword.setHint("Password is required");
            editTextPassword.setHintTextColor(getResources().getColor(android.R.color.holo_red_light));
            hasError = true;
        } else if (password.length() < 6) {
            editTextPassword.setError("Password must be at least 6 characters");
            hasError = true;
        } else {
            passwordContainer.setBackgroundResource(R.drawable.edittext_background);
        }

        if (hasError) {
            Toast.makeText(requireContext(), "Please fill all fields correctly", Toast.LENGTH_SHORT).show();
            return;
        }

        presenter.registerUser(email, password);
    }


    private void setupPasswordVisibilityToggle(ImageView btnTogglePasswordVisibility, EditText editTextPassword) {
        btnTogglePasswordVisibility.setImageResource(R.drawable.visibilityoff);

        btnTogglePasswordVisibility.setOnClickListener(v -> {
            if (editTextPassword.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                editTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                btnTogglePasswordVisibility.setImageResource(R.drawable.visibility);
            } else {
                editTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                btnTogglePasswordVisibility.setImageResource(R.drawable.visibilityoff);
            }
            editTextPassword.setSelection(editTextPassword.getText().length());
        });
    }

}