package com.example.fileviewer;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class GOST extends RecyclerView.Adapter {
    Long id;
    String number;
    String code;
    String title;
    String description;

    GOST(Long id, String number, String code, String title, String description) {
        this.id = id;
        this.number = number;
        this.code = code;
        this.title = title;
        this.description = description;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}

