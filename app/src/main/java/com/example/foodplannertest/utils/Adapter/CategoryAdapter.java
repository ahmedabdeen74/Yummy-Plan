package com.example.foodplannertest.utils.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodplannertest.R;
import com.example.foodplannertest.data.models.Category;

import java.util.HashMap;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private List<Category> categories;
    private OnItemClickListener listener;
    private List<Category> categoriesFull;
    private HashMap<String, Integer> categoryImagesMap;


    public CategoryAdapter(List<Category> categories, OnItemClickListener listener) {
        this.categories = categories;
        this.listener = listener;
        this.categoriesFull=categoriesFull;
        initializeCategoryImagesMap();
    }
    private void initializeCategoryImagesMap() {
        categoryImagesMap = new HashMap<>();
        categoryImagesMap.put("Beef", R.drawable.beef);
        categoryImagesMap.put("Breakfast", R.drawable.breakfast);
        categoryImagesMap.put("Chicken", R.drawable.chicken);
        categoryImagesMap.put("Dessert", R.drawable.dessert);
        categoryImagesMap.put("Goat", R.drawable.goat);
        categoryImagesMap.put("Lamb", R.drawable.lamb);
        categoryImagesMap.put("Miscellaneous", R.drawable.miscellaneous);
        categoryImagesMap.put("Pasta", R.drawable.pasta);
        categoryImagesMap.put("Pork", R.drawable.pork);
        categoryImagesMap.put("Seafood", R.drawable.seafood);
        categoryImagesMap.put("Side", R.drawable.side);
        categoryImagesMap.put("Starter", R.drawable.starter);
        categoryImagesMap.put("Vegan", R.drawable.vegan);
        categoryImagesMap.put("Vegetarian", R.drawable.vegetarian);
    }
    public void filterList(List<Category> filteredList) {
        categories = filteredList;
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
        Category category = categories.get(position);
        holder.textView.setText(category.getStrCategory());


        Integer imageRes = categoryImagesMap.get(category.getStrCategory());
        if (imageRes != null) {
            holder.imageView.setImageResource(imageRes);
        } else {

            holder.imageView.setImageResource(R.drawable.ic_launcher_background);
        }


        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(category.getStrCategory(), "category");
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
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
