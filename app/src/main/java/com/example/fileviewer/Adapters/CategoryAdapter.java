package com.example.fileviewer.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fileviewer.Models.Category;
import com.example.fileviewer.R;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private List<Category> categoryList;
    private OnItemClickListener itemClickListener;

    public interface OnItemClickListener {
        void onItemClick(Category category);
    }

    public CategoryAdapter(List<Category> categoryList, OnItemClickListener listener) {
        this.categoryList = categoryList;
        this.itemClickListener = listener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categoryList.get(position);
        holder.bind(category, position);

        holder.itemView.setOnClickListener(v -> {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(category);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public void updateData(List<Category> newCategoryList) {
        this.categoryList = newCategoryList;
        notifyDataSetChanged();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        ImageView ivCategoryIcon;
        TextView tvCategoryName;

        CategoryViewHolder(View itemView) {
            super(itemView);
            ivCategoryIcon = itemView.findViewById(R.id.ivCategoryIcon);
            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
        }

        void bind(Category category, int position) {
            tvCategoryName.setText(category.name);
            ivCategoryIcon.setImageResource(getIconResource(category.name));
        }

        public static int getIconResource(String categoryName) {
            if (categoryName == null) return R.drawable.ic_uris;
            String name = categoryName.toLowerCase();

            switch (name) {
                case "здравоохранение": return R.drawable.ic_healthing;
                case "образование": return R.drawable.ic_education;
                case "строительство": return R.drawable.ic_building;
                case "общее": return R.drawable.ic_socials;
                case "транспорт": return R.drawable.ic_transport;
                case "финансы": return R.drawable.ic_finance;
                case "промышленность": return R.drawable.ic_industry;
                case "экономика": return R.drawable.ic_economy;
                case "торговля": return R.drawable.ic_trading;
                case "информационные технологии": return R.drawable.ic_itechnology;
                default: return R.drawable.ic_uris;
            }
        }
    }
}
