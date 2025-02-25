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
import com.example.foodplannertest.data.models.Meal;
import com.example.foodplannertest.data.local.AppDatabase;
import com.example.foodplannertest.data.local.MealDao;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class FavouriteMealAdapter extends RecyclerView.Adapter<FavouriteMealAdapter.FavouriteMealViewHolder> {
    private List<Meal> favouriteMeals;
    private OnFavouriteMealClickListener listener;
    private OnDeleteClickListener deleteListener;
    private Context context;
    private MealDao mealDao;

    private Executor executor;

    public FavouriteMealAdapter(List<Meal> favouriteMeals, OnFavouriteMealClickListener listener, OnDeleteClickListener deleteListener, Context context) {
        this.favouriteMeals = favouriteMeals;
        this.listener = listener;
        this.deleteListener = deleteListener;
        this.context = context;
        this.mealDao = AppDatabase.getInstance(context).mealDao();
        this.executor = Executors.newSingleThreadExecutor();
    }

    @NonNull
    @Override
    public FavouriteMealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favourite_meal, parent, false);
        return new FavouriteMealViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavouriteMealViewHolder holder, int position) {
        Meal meal = favouriteMeals.get(position);
        holder.bind(meal);
        holder.deleteIcon.setOnClickListener(v -> {
            executor.execute(() -> {
                mealDao.deleteByMealIdAndType(meal.getIdMeal(), "favourite");
                ((MainActivity) context).runOnUiThread(() -> {
                    favouriteMeals.remove(position);
                    notifyItemRemoved(position);
                    Toast.makeText(context, "Meal removed from favorites!", Toast.LENGTH_SHORT).show();
                });
            });
        });


        holder.itemView.setOnClickListener(v -> listener.onFavouriteMealClick(meal));
    }

    @Override
    public int getItemCount() {
        return favouriteMeals.size();
    }

    static class FavouriteMealViewHolder extends RecyclerView.ViewHolder {
        private TextView mealName, mealCategory;
        private ImageView mealImage, deleteIcon;

        public FavouriteMealViewHolder(@NonNull View itemView) {
            super(itemView);
            mealName = itemView.findViewById(R.id.mealName);
            mealCategory = itemView.findViewById(R.id.mealCategory);
            mealImage = itemView.findViewById(R.id.mealImage);
            deleteIcon = itemView.findViewById(R.id.deleteImage);
        }

        public void bind(Meal meal) {
            mealName.setText(meal.getStrMeal());
            mealCategory.setText(meal.getStrCategory());
            Glide.with(itemView.getContext()).load(meal.getStrMealThumb()).into(mealImage);
        }
    }

    public interface OnFavouriteMealClickListener {
        void onFavouriteMealClick(Meal meal);
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(Meal meal);
    }
}
