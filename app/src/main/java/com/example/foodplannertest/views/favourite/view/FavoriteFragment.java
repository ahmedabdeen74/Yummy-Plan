package com.example.foodplannertest.views.favourite.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.foodplannertest.R;
import com.example.foodplannertest.data.local.AppDatabase;
import com.example.foodplannertest.data.models.Meal;
import com.example.foodplannertest.data.local.MealDao;
import com.example.foodplannertest.data.remote.MealRemoteDataSource;
import com.example.foodplannertest.data.remote.MealServices;
import com.example.foodplannertest.utils.Adapter.FavouriteMealAdapter;

import java.util.List;


import android.widget.Toast;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


import androidx.navigation.Navigation;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.foodplannertest.R;
import com.example.foodplannertest.data.models.Meal;
import com.example.foodplannertest.data.repo.MealRepository;
import com.example.foodplannertest.utils.Adapter.FavouriteMealAdapter;
import com.example.foodplannertest.views.favourite.presenter.FavoriteContract;
import com.example.foodplannertest.views.favourite.presenter.FavoritePresenter;

public class FavoriteFragment extends Fragment implements FavoriteContract.View {

    private RecyclerView recyclerView;
    private FavouriteMealAdapter favouriteMealAdapter;
    private LottieAnimationView lottieAnimationView;
    private FavoritePresenter presenter;
    private MealRepository mealRepository;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        lottieAnimationView = view.findViewById(R.id.lottieAnimationView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        MealServices mealServices = MealRemoteDataSource.getApi();
        MealDao mealDao = AppDatabase.getInstance(requireContext()).mealDao();

        mealRepository= new MealRepository(mealServices, mealDao);

        presenter = new FavoritePresenter(this, mealRepository);

        presenter.loadFavouriteMeals();

        return view;
    }

    @Override
    public void showFavouriteMeals(List<Meal> meals) {
        lottieAnimationView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);

        favouriteMealAdapter = new FavouriteMealAdapter(
                meals,
                meal -> {
                    Bundle bundle = new Bundle();
                    bundle.putString("mealId", meal.getIdMeal());
                    Navigation.findNavController(requireView()).navigate(R.id.action_favoriteFragment_to_favoriteDetailFragment, bundle);
                },
                meal -> {
                    presenter.deleteMealFromFavorites(meal);
                },
                requireContext()
        );
        recyclerView.setAdapter(favouriteMealAdapter);
    }

    @Override
    public void showEmptyView() {
        lottieAnimationView.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        Toast.makeText(requireContext(), "Your Favourites is empty, add your meal now!", Toast.LENGTH_SHORT).show();
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
}












































