package com.example.foodplannertest.utils.Adapter;

import android.content.Context;
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

import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import de.hdodenhof.circleimageview.CircleImageView;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.MealViewHolder> {
        private List<Meal> meals;
        private OnMealClickListener listener;
        private Context context;

        private MealDao mealDao;

        private Executor executor;

        public CalendarAdapter(List<Meal> meals, OnMealClickListener listener, Context context) {
            this.meals = meals;
            this.listener = listener;
            this.context = context;
            this.mealDao = AppDatabase.getInstance(context).mealDao();
            this.executor = Executors.newSingleThreadExecutor();
        }

        @NonNull
        @Override
        public MealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_calendar_meal, parent, false);
            return new MealViewHolder(view);
        }

    @Override
    public void onBindViewHolder(@NonNull MealViewHolder holder, int position) {
        Meal meal = meals.get(position);
        holder.bind(meal);
        holder.mealDelete.setOnClickListener(v -> {
            executor.execute(() -> {
                mealDao.deleteByMealIdAndType(meal.getIdMeal(), "calendar");
                ((MainActivity) context).runOnUiThread(() -> {
                    ListIterator<Meal> iterator = meals.listIterator();
                    while (iterator.hasNext()) {
                        Meal currentMeal = iterator.next();
                        if (currentMeal.getIdMeal() == meal.getIdMeal()) {
                            iterator.remove();
                            break;
                        }
                    }
                    notifyDataSetChanged();
                    Toast.makeText(context, "Meal removed from Calendar!", Toast.LENGTH_SHORT).show();
                    if (meals.isEmpty()) {
                        Toast.makeText(context, "The calendar is empty for this day!", Toast.LENGTH_SHORT).show();
                    }
                });
            });
        });
    }

        @Override
        public int getItemCount() {
            return meals.size();
        }

        class MealViewHolder extends RecyclerView.ViewHolder {
            private TextView mealName, mealCategory;
            private CircleImageView mealImage;
            private ImageView mealDelete;

            public MealViewHolder(@NonNull View itemView) {
                super(itemView);
                mealName = itemView.findViewById(R.id.mealName);
                mealCategory = itemView.findViewById(R.id.mealCategory);
                mealImage = itemView.findViewById(R.id.mealImage);
                mealDelete = itemView.findViewById(R.id.deleteImage);
            }

            public void bind(Meal meal) {
                mealName.setText(meal.getStrMeal());
                mealCategory.setText(meal.getStrCategory());
                Glide.with(itemView.getContext()).load(meal.getStrMealThumb()).into(mealImage);

                itemView.setOnClickListener(v -> listener.onMealClick(meal));
            }
        }

        public interface OnMealClickListener {
            void onMealClick(Meal meal);
        }
    }

