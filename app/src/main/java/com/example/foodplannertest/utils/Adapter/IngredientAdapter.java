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
import com.example.foodplannertest.data.models.Ingredient;

import java.util.List;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.ViewHolder> {
    private List<Ingredient> ingredients;
    private OnItemClickListener listener;
    private List<Ingredient> ingredientsFull;

    public IngredientAdapter(List<Ingredient> ingredients, OnItemClickListener listener) {
        this.ingredients = ingredients;
        this.listener = listener;
        this.ingredientsFull=ingredientsFull;
    }
    public void filterList(List<Ingredient> filteredList) {
        this.ingredients = filteredList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Ingredient ingredient = ingredients.get(position);
        holder.textView.setText(ingredient.getStrIngredient());


        String imageUrl = "https://www.themealdb.com/images/ingredients/" + ingredient.getStrIngredient() + ".png";
        Glide.with(holder.itemView.getContext())
                .load(imageUrl)
                .into(holder.imageView);


        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(ingredient.getStrIngredient(), "ingredient");
            }
        });
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textView = itemView.findViewById(R.id.textView);
        }
    }
}