package com.example.foodplannertest.views.search.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.foodplannertest.R;
import com.example.foodplannertest.data.local.AppDatabase;
import com.example.foodplannertest.data.local.MealDao;
import com.example.foodplannertest.data.models.Area;
import com.example.foodplannertest.data.models.Category;
import com.example.foodplannertest.data.models.Ingredient;
import com.example.foodplannertest.data.remote.MealRemoteDataSource;
import com.example.foodplannertest.data.remote.MealServices;
import com.example.foodplannertest.data.repo.MealRepository;
import com.example.foodplannertest.utils.Adapter.AreaAdapter;
import com.example.foodplannertest.utils.Adapter.CategoryAdapter;
import com.example.foodplannertest.utils.Adapter.IngredientAdapter;
import com.example.foodplannertest.utils.Adapter.OnItemClickListener;
import com.example.foodplannertest.views.search.presenter.SearchPresenter;
import com.example.foodplannertest.views.search.presenter.SearchView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment implements SearchView, OnItemClickListener {

    private RecyclerView recyclerView;
    private ChipGroup chipGroup;
    private Chip chipIngredients, chipAreas, chipCategories;
    private TextInputEditText searchEditText;
    private TextInputLayout textInputLayout;
    private TextView noResultsTextView;
    private List<Ingredient> ingredientList = new ArrayList<>();
    private List<Area> areaList = new ArrayList<>();
    private List<Category> categoryList = new ArrayList<>();
    private IngredientAdapter ingredientAdapter;
    private AreaAdapter areaAdapter;
    private CategoryAdapter categoryAdapter;
    private ImageView backButton;
    private SearchPresenter presenter;
    private String currentChipType = "ingredients";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        chipGroup = view.findViewById(R.id.chipGroup);
        chipIngredients = view.findViewById(R.id.chipIngredients);
        chipAreas = view.findViewById(R.id.chipAreas);
        chipCategories = view.findViewById(R.id.chipCategories);

        searchEditText = view.findViewById(R.id.searchEditText);
        textInputLayout = view.findViewById(R.id.textInputLayout);
        backButton = view.findViewById(R.id.btn_back);
        noResultsTextView = view.findViewById(R.id.noResultsTextView);

        backButton.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(requireView());
            navController.navigate(R.id.homeFragment);
        });



        MealServices mealServices = MealRemoteDataSource.getApi();
        MealDao mealDao = AppDatabase.getInstance(requireContext()).mealDao();


        MealRepository repository = new MealRepository(mealServices, mealDao);

        presenter = new SearchPresenter(this);
        presenter.setRepository(repository);



        if (savedInstanceState != null) {
            currentChipType = savedInstanceState.getString("currentChipType", "ingredients");
        }


        switch (currentChipType) {
            case "ingredients":
                chipIngredients.setChecked(true);
                textInputLayout.setHint("Search by Ingredients");
                break;
            case "areas":
                chipAreas.setChecked(true);
                textInputLayout.setHint("Search by Areas");
                break;
            case "categories":
                chipCategories.setChecked(true);
                textInputLayout.setHint("Search by Categories");
                break;
        }

        presenter.loadData(currentChipType);


        chipIngredients.setOnClickListener(v -> {
            currentChipType = "ingredients";
            textInputLayout.setHint("Search by Ingredients");
            presenter.loadData(currentChipType);
        });

        chipAreas.setOnClickListener(v -> {
            currentChipType = "areas";
            textInputLayout.setHint("Search by Areas");
            presenter.loadData(currentChipType);
        });

        chipCategories.setOnClickListener(v -> {
            currentChipType = "categories";
            textInputLayout.setHint("Search by Categories");
            presenter.loadData(currentChipType);
        });


        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
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
    public void showIngredients(List<Ingredient> ingredients) {
        ingredientList = ingredients;
        ingredientAdapter = new IngredientAdapter(ingredientList, this);
        recyclerView.setAdapter(ingredientAdapter);
    }

    @Override
    public void showAreas(List<Area> areas) {
        areaList = areas;
        areaAdapter = new AreaAdapter(areaList, this);
        recyclerView.setAdapter(areaAdapter);
    }

    @Override
    public void showCategories(List<Category> categories) {
        categoryList = categories;
        categoryAdapter = new CategoryAdapter(categoryList, this);
        recyclerView.setAdapter(categoryAdapter);
    }

    @Override
    public void showError(String message) {

    }


    public void showNoResults(boolean show) {
        if (show) {
            noResultsTextView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            noResultsTextView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void navigateToSearchResults(String itemName, String type) {
        Bundle bundle = new Bundle();
        bundle.putString("itemName", itemName);
        bundle.putString("type", type);

        NavController navController = Navigation.findNavController(requireView());
        navController.navigate(R.id.action_searchFragment_to_searchResultsFragment, bundle);
    }

    @Override
    public void onItemClick(String itemName, String type) {
        presenter.onItemClicked(itemName, type);
    }
    private void filter(String text) {
        if (chipIngredients.isChecked()) {
            List<Ingredient> filteredList = new ArrayList<>();
            for (Ingredient ingredient : ingredientList) {
                if (ingredient.getStrIngredient().toLowerCase().contains(text.toLowerCase())) {
                    filteredList.add(ingredient);
                }
            }
            ingredientAdapter.filterList(filteredList);
            showNoResults(filteredList.isEmpty());
        } else if (chipAreas.isChecked()) {
            List<Area> filteredList = new ArrayList<>();
            for (Area area : areaList) {
                if (area.getStrArea().toLowerCase().contains(text.toLowerCase())) {
                    filteredList.add(area);
                }
            }
            areaAdapter.filterList(filteredList);
            showNoResults(filteredList.isEmpty());
        } else if (chipCategories.isChecked()) {
            List<Category> filteredList = new ArrayList<>();
            for (Category category : categoryList) {
                if (category.getStrCategory().toLowerCase().contains(text.toLowerCase())) {
                    filteredList.add(category);
                }
            }
            categoryAdapter.filterList(filteredList);
            showNoResults(filteredList.isEmpty());
        }
    }
}