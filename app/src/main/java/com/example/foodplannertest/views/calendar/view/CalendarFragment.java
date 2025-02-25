package com.example.foodplannertest.views.calendar.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.foodplannertest.R;
import com.example.foodplannertest.data.local.AppDatabase;
import com.example.foodplannertest.data.local.MealDao;
import com.example.foodplannertest.data.models.Meal;
import com.example.foodplannertest.data.remote.MealRemoteDataSource;
import com.example.foodplannertest.data.remote.MealServices;
import com.example.foodplannertest.data.repo.MealRepository;

import com.example.foodplannertest.utils.Adapter.CalendarAdapter;
import com.example.foodplannertest.views.calendar.presenter.CalendarContract;
import com.example.foodplannertest.views.calendar.presenter.CalendarPresenter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CalendarFragment extends Fragment implements CalendarContract.View {

    private CalendarView calendarView;
    private RecyclerView recyclerView;
    private CalendarAdapter mealAdapter;
    private LottieAnimationView lottieAnimationView;
    private CalendarPresenter presenter;
    private MealRepository mealRepository;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        calendarView = view.findViewById(R.id.calendarView);
        recyclerView = view.findViewById(R.id.recyclerView);
        lottieAnimationView = view.findViewById(R.id.lottieAnimationView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        MealServices mealServices = MealRemoteDataSource.getApi();
        MealDao mealDao = AppDatabase.getInstance(requireContext()).mealDao();

        // Create the repository
        MealRepository repository = new MealRepository(mealServices, mealDao);
        presenter = new CalendarPresenter(this, repository);

        calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
            String selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
            presenter.loadMealsForDate(selectedDate);
        });

        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        presenter.loadMealsForDate(today);
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                requireActivity().finish();
            }
        });

        return view;
    }

    @Override
    public void showMeals(List<Meal> meals) {
        lottieAnimationView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);

        mealAdapter = new CalendarAdapter(meals, meal -> {
        }, getContext());
        recyclerView.setAdapter(mealAdapter);
    }

    @Override
    public void showEmptyCalendar() {
        lottieAnimationView.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        Toast.makeText(requireContext(), "Your Calendar is empty, add your meal now!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showEmptyDay() {
        lottieAnimationView.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        Toast.makeText(requireContext(), "No meals planned for this day!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showError(String message) {
        Toast.makeText(requireContext(), "Your Calendar is empty, add your meal now!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}