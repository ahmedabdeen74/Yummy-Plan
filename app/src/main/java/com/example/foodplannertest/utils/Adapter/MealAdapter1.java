package com.example.foodplannertest.utils.Adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodplannertest.R;
import com.example.foodplannertest.data.models.Meal;

import java.util.List;

public class MealAdapter1 extends RecyclerView.Adapter<MealAdapter1.MealViewHolder> {
    private List<Meal> meals;
    private OnMealClickListener listener;

    public MealAdapter1(List<Meal> meals, OnMealClickListener listener) {
        this.meals = meals;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meal2, parent, false);
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
        private ImageView mealImage;

        public MealViewHolder(@NonNull View itemView) {
            super(itemView);
            mealName = itemView.findViewById(R.id.txt_name);
            mealCategory = itemView.findViewById(R.id.txt_desc);
            mealImage = itemView.findViewById(R.id.iv_image);
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
