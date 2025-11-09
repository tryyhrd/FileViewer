package com.example.fileviewer.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fileviewer.Models.Category;
import com.example.fileviewer.Models.Document;
import com.example.fileviewer.Models.DocumentCategory;
import com.example.fileviewer.Models.Level;
import com.example.fileviewer.Models.Section;
import com.example.fileviewer.R;

import java.util.ArrayList;
import java.util.List;

public class DocumentAdapter extends RecyclerView.Adapter<DocumentAdapter.DocumentViewHolder> {
    private List<Document> documentList;
    private List<Document> allDocuments;
    private List<Category> allCategories;
    private List<Level> allLevels;
    private OnItemClickListener itemClickListener;

    public interface OnItemClickListener {
        void onItemClick(Document document);
    }

    static class DocumentViewHolder extends RecyclerView.ViewHolder {
        TextView tvDocumentTitle, tvLevel;

        DocumentViewHolder(View itemView) {
            super(itemView);
            tvDocumentTitle = itemView.findViewById(R.id.tvDocumentTitle);
            tvLevel = itemView.findViewById(R.id.tvLevel);
        }
    }

    public DocumentAdapter(List<Document> documentList, List<Category> allCategories,
                           List<Level> allLevels, OnItemClickListener listener) {
        this.allDocuments = documentList != null ? documentList : new ArrayList<>();
        this.documentList = filterDocumentsWithSections(allDocuments);
        this.allCategories = allCategories != null ? allCategories : new ArrayList<>();
        this.allLevels = allLevels != null ? allLevels : new ArrayList<>();
        this.itemClickListener = listener;
    }

    @NonNull
    @Override
    public DocumentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_document, parent, false);
        return new DocumentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DocumentViewHolder holder, int position) {
        final Document document = documentList.get(position);
        if (!hasValidSections(document)) {
            holder.itemView.setVisibility(View.GONE);
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
            return;
        }

        holder.itemView.setVisibility(View.VISIBLE);
        holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        holder.tvDocumentTitle.setText(document.title);

        String levelName = "Неизвестный уровень";

        if (document.document_categories != null && !document.document_categories.isEmpty()) {
            DocumentCategory docCategory = document.document_categories.get(0);
            Category category = findCategoryById(docCategory.category_id);
            if (category != null) {
                Level level = findLevelById(category.level_id);
                if (level != null) {
                    levelName = level.name;
                }
            }
        }

        holder.tvLevel.setText(levelName);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null && hasValidSections(document)) {
                    itemClickListener.onItemClick(document);
                }
            }
        });
    }
    private List<Document> filterDocumentsWithSections(List<Document> documents) {
        List<Document> filtered = new ArrayList<>();
        if (documents != null) {
            for (Document document : documents) {
                if (hasValidSections(document)) {
                    filtered.add(document);
                }
            }
        }
        return filtered;
    }
    private boolean hasValidSections(Document document) {
        return !document.sections.isEmpty();
    }

    private Category findCategoryById(int categoryId) {
        if (allCategories == null) return null;
        for (Category category : allCategories) {
            if (category.id == categoryId) {
                return category;
            }
        }
        return null;
    }

    private Level findLevelById(int levelId) {
        if (allLevels == null) return null;
        for (Level level : allLevels) {
            if (level.id == levelId) {
                return level;
            }
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return documentList.size();
    }
    public void updateData(List<Document> newDocumentList) {
        this.allDocuments = newDocumentList != null ? newDocumentList : new ArrayList<>();
        this.documentList = filterDocumentsWithSections(allDocuments);
        notifyDataSetChanged();
    }
}