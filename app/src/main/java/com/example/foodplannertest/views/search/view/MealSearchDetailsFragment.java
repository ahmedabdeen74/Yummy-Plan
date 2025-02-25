package com.example.foodplannertest.views.search.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.example.foodplannertest.views.search.presenter.MealSearchDetailsPresenter;
import com.example.foodplannertest.views.search.presenter.MealSearchDetailsView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

public class MealSearchDetailsFragment extends Fragment implements MealSearchDetailsView {

    private String mealId;
    private TextView mealName, mealCategory, mealInstructions;
    private ImageView mealImage;
    private WebView youtubeWebView;
    private TextView country;
    private RecyclerView ingredientsRecyclerView;
    private ImageView backButton;
    private MealSearchDetailsPresenter presenter;
    private Executor executor;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mealId = getArguments().getString("mealId");
        }



        MealServices mealServices = MealRemoteDataSource.getApi();
        MealDao mealDao = AppDatabase.getInstance(requireContext()).mealDao();


        MealRepository repository = new MealRepository(mealServices, mealDao);

        presenter = new MealSearchDetailsPresenter(this, repository);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_meal_detail, container, false);

        mealName = view.findViewById(R.id.mealName);
        mealCategory = view.findViewById(R.id.mealCategory);
        mealInstructions = view.findViewById(R.id.mealInstructions);
        mealImage = view.findViewById(R.id.mealImage);
        ingredientsRecyclerView = view.findViewById(R.id.ingredientsRecyclerView);
        youtubeWebView = view.findViewById(R.id.youtubeWebView);
        country = view.findViewById(R.id.countryName);
        backButton = view.findViewById(R.id.btn_back);

        backButton.setOnClickListener(v -> requireActivity().onBackPressed());

        presenter.loadMealDetails(mealId);

        return view;
    }


    @Override
    public void showMealDetails(Meal meal) {
        mealName.setText(meal.getStrMeal());
        mealCategory.setText(meal.getStrCategory());
        mealInstructions.setText(meal.getStrInstructions());
        country.setText(meal.getStrArea());


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


        Glide.with(this).load(meal.getStrMealThumb()).into(mealImage);
    }

    @Override
    public void showError(String message) {
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