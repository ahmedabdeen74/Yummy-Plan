package com.example.foodplannertest.utils.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodplannertest.R;
import com.example.foodplannertest.data.models.Area;

import java.util.HashMap;
import java.util.List;

public class AreaAdapter extends RecyclerView.Adapter<AreaAdapter.ViewHolder> {
    private List<Area> areas;
    private OnItemClickListener listener;
    private HashMap<String, Integer> areaImagesMap;

    private List<Area> areasFull;
    public AreaAdapter(List<Area> areas, OnItemClickListener listener) {
        this.areas = areas;
        this.listener = listener;
        this.areasFull=areasFull;
        initializeAreaImagesMap();
    }
    private void initializeAreaImagesMap() {
        areaImagesMap = new HashMap<>();
        areaImagesMap.put("American", R.drawable.unitedstates);
        areaImagesMap.put("British", R.drawable.unitedkingdom);
        areaImagesMap.put("Canadian", R.drawable.canada);
        areaImagesMap.put("Chinese", R.drawable.china);
        areaImagesMap.put("Croatian", R.drawable.croatia);
        areaImagesMap.put("Dutch", R.drawable.holanda);
        areaImagesMap.put("Egyptian", R.drawable.egypt);
        areaImagesMap.put("Filipino", R.drawable.philippines);
        areaImagesMap.put("French", R.drawable.france);
        areaImagesMap.put("Greek", R.drawable.greek);
        areaImagesMap.put("Indian", R.drawable.india);
        areaImagesMap.put("Irish", R.drawable.irlanda);
        areaImagesMap.put("Italian", R.drawable.italy);
        areaImagesMap.put("Jamaican", R.drawable.jamaica);
        areaImagesMap.put("Japanese", R.drawable.japan);
        areaImagesMap.put("Kenyan", R.drawable.kenya);
        areaImagesMap.put("Malaysian", R.drawable.malaysia);
        areaImagesMap.put("Mexican", R.drawable.mexico);
        areaImagesMap.put("Moroccan", R.drawable.morocco);
        areaImagesMap.put("Norwegian", R.drawable.norway);
        areaImagesMap.put("Polish", R.drawable.poland);
        areaImagesMap.put("Portuguese", R.drawable.portugal);
        areaImagesMap.put("Russian", R.drawable.russia);
        areaImagesMap.put("Spanish", R.drawable.spain);
        areaImagesMap.put("Thai", R.drawable.thailand);
        areaImagesMap.put("Tunisian", R.drawable.tunisia);
        areaImagesMap.put("Turkish", R.drawable.turkey);
        areaImagesMap.put("Ukrainian", R.drawable.location);
        areaImagesMap.put("Uruguayan", R.drawable.uruguay);
        areaImagesMap.put("Vietnamese", R.drawable.vietnam);
    }


    public void filterList(List<Area> filteredList) {
        areas = filteredList;
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
        Area area = areas.get(position);
        holder.textView.setText(area.getStrArea());

        Integer imageRes = areaImagesMap.get(area.getStrArea());
        if (imageRes != null) {
            holder.imageView.setImageResource(imageRes);
        } else {
            holder.imageView.setImageResource(R.drawable.ic_launcher_background);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(area.getStrArea(), "area");
            }
        });
    }

    @Override
    public int getItemCount() {
        return areas.size();
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