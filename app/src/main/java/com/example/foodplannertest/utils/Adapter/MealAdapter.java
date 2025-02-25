package com.example.foodplannertest.utils.Adapter;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodplannertest.MainActivity;
import com.example.foodplannertest.R;
import com.example.foodplannertest.data.local.AppDatabase;
import com.example.foodplannertest.data.models.Meal;
import com.example.foodplannertest.data.local.MealDao;
import com.example.foodplannertest.data.pref.UserPreferences;
import com.example.foodplannertest.helper.CardAnimationHelper;
import com.example.foodplannertest.views.home.presenter.HomePresenter;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MealAdapter extends RecyclerView.Adapter<MealAdapter.MealViewHolder> {
    private List<Meal> meals;
    private OnMealClickListener listener;
    private Context context;
    private UserPreferences userPreferences;
    private HomePresenter presenter;
    private boolean isSaved = false;

    public MealAdapter(List<Meal> meals, OnMealClickListener listener, Context context, HomePresenter presenter) {
        this.meals = meals;
        this.listener = listener;
        this.context = context;
        this.userPreferences = new UserPreferences(context);
        this.presenter = presenter;
    }

    @NonNull
    @Override
    public MealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meal, parent, false);
        return new MealViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MealViewHolder holder, int position) {
        Meal meal = meals.get(position);
        holder.bind(meal);
    }

    @Override
    public int getItemCount() {
        return meals.size();
    }

    class MealViewHolder extends RecyclerView.ViewHolder {
        private TextView mealName, mealCategory;
        private ImageView mealImage, saveIcon, calendarIcon;

        public MealViewHolder(@NonNull View itemView) {
            super(itemView);
            mealName = itemView.findViewById(R.id.mealName);
            mealCategory = itemView.findViewById(R.id.mealCategory);
            mealImage = itemView.findViewById(R.id.mealImage);
            saveIcon = itemView.findViewById(R.id.bookmarkIcon);
            calendarIcon = itemView.findViewById(R.id.calendarIcon);

            saveIcon.setOnClickListener(v -> {
                if (!userPreferences.isLoggedIn()) {
                    showLoginRequiredDialog();
                } else {
                    Meal meal = meals.get(getAdapterPosition());
                    presenter.saveMealToFavorites(meal);
                    isSaved = !isSaved;
                    saveIcon.setImageResource(isSaved ? R.drawable.bookmark2 : R.drawable.bookmark);
                    Toast.makeText(context, "Meal saved to favorites!", Toast.LENGTH_SHORT).show();

                }
            });

            calendarIcon.setOnClickListener(v -> {
                if (!userPreferences.isLoggedIn()) {
                    showLoginRequiredDialog();
                } else {
                    Meal meal = meals.get(getAdapterPosition());
                    showDatePickerDialog(meal);
                }
            });
        }

        public void bind(Meal meal) {
            mealName.setText(meal.getStrMeal());
            mealCategory.setText(meal.getStrCategory());
            Glide.with(itemView.getContext()).load(meal.getStrMealThumb()).into(mealImage);

            itemView.setOnClickListener(v -> listener.onMealClick(meal));
        }
    }

    private void showLoginRequiredDialog() {

        androidx.appcompat.app.AlertDialog dialog = new androidx.appcompat.app.AlertDialog.Builder(context)
                .setTitle("Login Required")
                .setMessage("You need to login to access this feature.")
                .setPositiveButton("Login", (dialogInterface, which) -> {

                    if (context instanceof MainActivity) {
                        ((MainActivity) context).navigateToLoginFragment();
                    }
                })
                .setNegativeButton("Cancel", (dialogInterface, which) -> {
                    dialogInterface.dismiss();
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .create();


        dialog.show();


        if (dialog.getWindow() != null) {

            GradientDrawable drawable = new GradientDrawable();
            drawable.setColor(Color.parseColor("#FAF3E0"));
            drawable.setCornerRadius(16f);


            dialog.getWindow().setBackgroundDrawable(drawable);
        }
    }

    private void showDatePickerDialog(Meal meal) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(context, (view, selectedYear, selectedMonth, selectedDay) -> {
            String selectedDate = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDay;
            presenter.saveMealToCalendar(meal, selectedDate);
            Toast.makeText(context, "Meal added to calendar!", Toast.LENGTH_SHORT).show();
        }, year, month, day);

        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        datePickerDialog.show();
    }

    public interface OnMealClickListener {
        void onMealClick(Meal meal);
    }
}