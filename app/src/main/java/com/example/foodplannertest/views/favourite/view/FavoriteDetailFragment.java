package com.example.foodplannertest.views.favourite.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodplannertest.R;
import com.example.foodplannertest.data.local.AppDatabase;
import com.example.foodplannertest.data.local.MealDao;
import com.example.foodplannertest.data.models.Meal;
import com.example.foodplannertest.data.remote.MealRemoteDataSource;
import com.example.foodplannertest.data.remote.MealServices;
import com.example.foodplannertest.data.repo.MealRepository;

import com.example.foodplannertest.utils.Adapter.IngredientsAdapter;
import com.example.foodplannertest.views.favourite.presenter.FavoriteDetailContract;
import com.example.foodplannertest.views.favourite.presenter.FavoriteDetailPresenter;

import java.util.ArrayList;
import java.util.List;

public class FavoriteDetailFragment extends Fragment implements FavoriteDetailContract.View {

    private FavoriteDetailPresenter presenter;
    private MealRepository mealRepository;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MealServices mealServices = MealRemoteDataSource.getApi();
        MealDao mealDao = AppDatabase.getInstance(requireContext()).mealDao();

        mealRepository= new MealRepository(mealServices, mealDao);

        presenter = new FavoriteDetailPresenter(this, mealRepository);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_meal_detail, container, false);

        String mealId = getArguments().getString("mealId");
        if (mealId == null) {
            requireActivity().onBackPressed();
            return view;
        }

        presenter.loadMealDetails(mealId);

        return view;
    }

    @Override
    public void showMealDetails(Meal meal) {
        View view = getView();
        if (view == null) return;

        ImageView mealImage = view.findViewById(R.id.mealImage);
        TextView mealName = view.findViewById(R.id.mealName);
        TextView mealCategory = view.findViewById(R.id.mealCategory);
        TextView mealInstructions = view.findViewById(R.id.mealInstructions);
        RecyclerView ingredientsRecyclerView = view.findViewById(R.id.ingredientsRecyclerView);
        WebView youtubeWebView = view.findViewById(R.id.youtubeWebView);
        TextView countryName = view.findViewById(R.id.countryName);
        ImageView arrowButton = view.findViewById(R.id.btn_back);
        arrowButton.setOnClickListener(v -> requireActivity().onBackPressed());

        Glide.with(requireContext()).load(meal.getStrMealThumb()).into(mealImage);
        mealName.setText(meal.getStrMeal());
        mealCategory.setText(meal.getStrCategory());
        mealInstructions.setText(meal.getStrInstructions());
        countryName.setText(meal.getStrArea());

        List<String> ingredients = getIngredientsList(meal);
        IngredientsAdapter ingredientsAdapter = new IngredientsAdapter(ingredients);
        ingredientsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        ingredientsRecyclerView.setAdapter(ingredientsAdapter);

        String youtubeUrl = meal.getStrYoutube();
        if (youtubeUrl != null && !youtubeUrl.isEmpty()) {
            youtubeWebView.setWebViewClient(new WebViewClient());
            youtubeWebView.getSettings().setJavaScriptEnabled(true);
            youtubeWebView.loadUrl(youtubeUrl);
        }
    }

    @Override
    public void showError(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    private List<String> getIngredientsList(Meal meal) {
        List<String> ingredients = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            String ingredient = meal.getIngredient(i);
            String measure = meal.getMeasure(i);
            if (ingredient != null && !ingredient.isEmpty()) {
                ingredients.add(ingredient + " - " + measure);
            }
        }
        return ingredients;
    }
}