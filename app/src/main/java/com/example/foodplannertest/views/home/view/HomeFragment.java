package com.example.foodplannertest.views.home.view;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodplannertest.R;
import com.example.foodplannertest.data.local.AppDatabase;
import com.example.foodplannertest.data.models.Meal;

import com.example.foodplannertest.data.remote.MealRemoteDataSource;
import com.example.foodplannertest.data.repo.MealRepository;
import com.example.foodplannertest.helper.GridSpacingItemDecoration;
import com.example.foodplannertest.utils.Adapter.MealAdapter;
import com.example.foodplannertest.views.home.presenter.HomePresenter;
import com.example.foodplannertest.views.home.presenter.HomeView;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements HomeView {

    private RecyclerView recyclerView;
    private MealAdapter mealAdapter;
    private RecyclerView rvPopular;
    private MealAdapter mealAdapter1;
    private HomePresenter presenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_home, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        MealRepository repository = new MealRepository(MealRemoteDataSource.getApi(), AppDatabase.getInstance(getContext()).mealDao());
        presenter = new HomePresenter(this, repository);

        presenter.getRandomMeals();
        presenter.getRandomMeals2();

        TextView animatedText = view.findViewById(R.id.animatedText);
        ObjectAnimator animator = ObjectAnimator.ofFloat(animatedText, "translationX", -10f, 10f);
        animator.setDuration(500);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setRepeatMode(ValueAnimator.REVERSE);
        animator.start();

        rvPopular = view.findViewById(R.id.rv_popular);
        rvPopular.setNestedScrollingEnabled(false);
        rvPopular.setLayoutManager(new GridLayoutManager(getContext(), 2) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        int spanCount = 2;
        int spacing = 30;
        boolean includeEdge = false;

        rvPopular.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                requireActivity().finish();
            }
        });

        return view;
    }

    @Override
    public void showRandomMeals(List<Meal> meals) {
        Log.d("HomeFragment", "showRandomMeals called with " + meals.size() + " meals");
        mealAdapter = new MealAdapter(meals, meal -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("meal", meal);
            Navigation.findNavController(requireView()).navigate(R.id.action_homeFragment_to_mealDetailFragment, bundle);
        }, getContext(),this.presenter);
        recyclerView.setAdapter(mealAdapter);


    }

    @Override
    public void showError(String message) {
    }

    @Override
    public void showRandomMeals2(List<Meal> meals) {
        mealAdapter1 = new MealAdapter(meals, meal -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("meal", meal);
            Navigation.findNavController(requireView()).navigate(R.id.action_homeFragment_to_mealDetailFragment, bundle);
        }, getContext(),this.presenter);
        rvPopular.setAdapter(mealAdapter1);
    }

    @Override
    public void showMealSavedToFavorites() {

    }

    @Override
    public void showMealAddedToCalendar() {

    }
}