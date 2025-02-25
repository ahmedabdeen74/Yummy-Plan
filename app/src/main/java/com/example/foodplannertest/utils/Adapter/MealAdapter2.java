package com.example.foodplannertest.utils.Adapter;


import android.app.DatePickerDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.foodplannertest.MainActivity;
import com.example.foodplannertest.R;
import com.example.foodplannertest.data.local.AppDatabase;
import com.example.foodplannertest.data.models.Meal;
import com.example.foodplannertest.data.remote.MealServices;
import com.example.foodplannertest.data.local.MealDao;
import com.example.foodplannertest.data.remote.MealResponse;
import com.example.foodplannertest.data.remote.MealRemoteDataSource;
import com.example.foodplannertest.data.pref.UserPreferences;
import com.example.foodplannertest.views.home.presenter.HomePresenter;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MealAdapter2 extends RecyclerView.Adapter<MealAdapter2.MealViewHolder> {

    private List<Meal> meals;
    private Context context;
    private OnItemClickListener listener;
    private UserPreferences userPreferences;
    private MealDao mealDao;
    private Executor executor;

    private HomePresenter presenter;

    public MealAdapter2(List<Meal> meals, OnItemClickListener listener, Context context) {
        this.meals = meals;
        this.listener = listener;
        this.context = context;
        this.userPreferences = new UserPreferences(context);
        this.mealDao = AppDatabase.getInstance(context).mealDao();
        this.executor = Executors.newSingleThreadExecutor();
        this.presenter = presenter;

    }

    @NonNull
    @Override
    public MealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meal1, parent, false);
        return new MealViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MealViewHolder holder, int position) {
        Meal meal = meals.get(position);
        holder.bind(meal, listener);
    }

    @Override
    public int getItemCount() {
        return meals.size();
    }
    public void filterList(List<Meal> filteredList) {
        meals = filteredList;
        notifyDataSetChanged();
    }

    public  class MealViewHolder extends RecyclerView.ViewHolder {
        private ImageView mealImage, saveIcon, calendarIcon;
        private TextView mealName;
        private boolean isSaved = false;



        public MealViewHolder(@NonNull View itemView) {
            super(itemView);
            mealImage = itemView.findViewById(R.id.mealImage);
            mealName = itemView.findViewById(R.id.mealName);
            saveIcon = itemView.findViewById(R.id.bookmarkIcon);
            calendarIcon = itemView.findViewById(R.id.calendarIcon);



        }

        public void bind(final Meal meal, final OnItemClickListener listener) {
            Glide.with(itemView.getContext())
                    .load(meal.getStrMealThumb())
                    .centerCrop()
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background)
                    .into(mealImage);

            mealName.setText(meal.getStrMeal());
            if (meal.getType() != null && meal.getType().equals("favourite")) {
                saveIcon.setImageResource(R.drawable.bookmark2);
                isSaved = true;
            } else {
                saveIcon.setImageResource(R.drawable.bookmark);
                isSaved = false;
            }
            saveIcon.setOnClickListener(v -> {
                if (!userPreferences.isLoggedIn()) {
                    showLoginRequiredDialog();
                } else {
                    if (isSaved) {
                        meal.setType("none");
                        saveIcon.setImageResource(R.drawable.bookmark);
                        isSaved = false;
                    } else {
                        meal.setType("favourite");
                        saveIcon.setImageResource(R.drawable.bookmark2);
                        isSaved = true;
                    }
                    MealServices api = MealRemoteDataSource.getApi();
                    Call<MealResponse> call = api.getMealDetails2(meal.getIdMeal());
                    call.enqueue(new Callback<MealResponse>() {
                        @Override
                        public void onResponse(Call<MealResponse> call, Response<MealResponse> response) {
                            if (response.isSuccessful() && response.body() != null &&
                                    response.body().getMeals() != null &&
                                    !response.body().getMeals().isEmpty()) {

                                Meal completeMeal = response.body().getMeals().get(0);
                                completeMeal.setType("favourite");

                                executor.execute(() -> {
                                    mealDao.insert(completeMeal);
                                    ((MainActivity) context).runOnUiThread(() -> {
                                        Toast.makeText(context, "Meal saved to favorites!", Toast.LENGTH_SHORT).show();
                                    });
                                });
                            }
                        }

                        @Override
                        public void onFailure(Call<MealResponse> call, Throwable t) {
                            Toast.makeText(context, "Failed to save meal", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });


            calendarIcon.setOnClickListener(v -> {
                if (!userPreferences.isLoggedIn()) {
                    showLoginRequiredDialog();
                } else {


                    Meal meal1 = meals.get(getAdapterPosition());
                    meal1.setType("calendar");
                    showDatePickerDialog(meal1);
                }

            });

            itemView.setOnClickListener(v -> listener.onItemClick(meal));
        }

        private void showLoginRequiredDialog() {
            new AlertDialog.Builder(context)
                    .setTitle("Login Required")
                    .setMessage("You need to login to access this feature.")
                    .setPositiveButton("Login", (dialog, which) -> {
                        if (context instanceof MainActivity) {
                            ((MainActivity) context).navigateToLoginFragment();
                        }
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }
    private void showDatePickerDialog(Meal meal) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(context, (view, selectedYear, selectedMonth, selectedDay) -> {
            String selectedDate = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDay;
            meal.setDate(selectedDate);
            executor.execute(() -> {
                mealDao.insert(meal);
                ((MainActivity) context).runOnUiThread(() -> {
                    Toast.makeText(context, "Meal added to calendar!", Toast.LENGTH_SHORT).show();
                });
            });
        }, year, month, day);

        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());

        datePickerDialog.show();
    }

    public interface OnItemClickListener {
        void onItemClick(Meal meal);
    }
}








