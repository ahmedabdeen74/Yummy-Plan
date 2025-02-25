package com.example.foodplannertest.views.search.view;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodplannertest.R;
import com.example.foodplannertest.data.local.AppDatabase;
import com.example.foodplannertest.data.local.MealDao;
import com.example.foodplannertest.data.models.Meal;
import com.example.foodplannertest.data.remote.MealRemoteDataSource;
import com.example.foodplannertest.data.remote.MealServices;
import com.example.foodplannertest.data.repo.MealRepository;
import com.example.foodplannertest.utils.Adapter.MealAdapter2;
import com.example.foodplannertest.views.search.presenter.SearchResultsPresenter;
import com.example.foodplannertest.views.search.presenter.SearchResultsView;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class SearchResultsFragment extends Fragment implements SearchResultsView, MealAdapter2.OnItemClickListener {

    private RecyclerView recyclerView;
    private MealAdapter2 adapter;
    private ImageView arrowBack;
    private TextInputEditText searchEditText;
    private TextView noResultsTextView;
    private List<Meal> originalMeals = new ArrayList<>();
    private List<Meal> filteredMeals = new ArrayList<>();
    private SearchResultsPresenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_results, container, false);


        arrowBack = view.findViewById(R.id.btn_back);
        recyclerView = view.findViewById(R.id.recyclerView);
        searchEditText = view.findViewById(R.id.searchEditText);
        noResultsTextView = view.findViewById(R.id.noResultsTextView);


        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        arrowBack.setOnClickListener(v -> requireActivity().onBackPressed());


        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                presenter.filterResults(originalMeals, s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });


        MealServices mealServices = MealRemoteDataSource.getApi();
        MealDao mealDao = AppDatabase.getInstance(requireContext()).mealDao();
        MealRepository repository = new MealRepository(mealServices, mealDao);
        presenter = new SearchResultsPresenter(this, repository);

        Bundle bundle = getArguments();
        if (bundle != null) {
            String itemName = bundle.getString("itemName");
            String type = bundle.getString("type");
            presenter.loadSearchResults(itemName, type);
        }

        return view;
    }

    @Override
    public void showSearchResults(List<Meal> meals) {
        originalMeals = meals;
        filteredMeals = new ArrayList<>(originalMeals);
        adapter = new MealAdapter2(filteredMeals, this, requireContext());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void showFilteredResults(List<Meal> meals) {
        filteredMeals.clear();
        filteredMeals.addAll(meals);
        adapter.notifyDataSetChanged();

        if (filteredMeals.isEmpty()) {
            noResultsTextView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            noResultsTextView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showError(String message) {
    }

    @Override
    public void onItemClick(Meal meal) {
        Bundle bundle = new Bundle();
        bundle.putString("mealId", meal.getIdMeal());

        NavController navController = Navigation.findNavController(requireView());
        navController.navigate(R.id.action_searchResultsFragment_to_mealSearchDetailsFragment2, bundle);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.dispose();
    }
}