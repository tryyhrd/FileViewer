package com.example.fileviewer.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fileviewer.Models.Document;
import com.example.fileviewer.R;

import java.util.List;

public class DocumentAdapter extends RecyclerView.Adapter<DocumentAdapter.DocumentViewHolder> {
    private List<Document> documentList;
    private OnItemClickListener itemClickListener;

    public interface OnItemClickListener {
        void onItemClick(Document document);
        void onItemLongClick(Document document);
    }

    static class DocumentViewHolder extends RecyclerView.ViewHolder {
        TextView tvDocumentTitle;

        DocumentViewHolder(View itemView) {
            super(itemView);
            tvDocumentTitle = itemView.findViewById(R.id.tvDocumentTitle);
        }
    }

    public DocumentAdapter(List<Document> documentList, OnItemClickListener listener) {
        this.documentList = documentList;
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

        holder.tvDocumentTitle.setText(document.title);

//        if (document.publication_date != null) {
//            holder.tvDocumentDate.setText("Дата: " + document.publication_date.toString());
//        } else {
//            holder.tvDocumentDate.setText("Дата не указана");
//        }
//
//        if (document.status != null) {
//            holder.tvDocumentStatus.setText("Статус: " + document.status);
//        } else {
//            holder.tvDocumentStatus.setText("Статус не указан");
//        }
//
//        if (document.sections != null) {
//            holder.tvSectionsCount.setText("Разделов: " + document.sections.size());
//        } else {
//            holder.tvSectionsCount.setText("Разделов: 0");
//        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(document);
                }
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (itemClickListener != null) {
                    itemClickListener.onItemLongClick(document);
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return documentList.size();
    }

    public void updateData(List<Document> newDocumentList) {
        this.documentList = newDocumentList;
        notifyDataSetChanged();
    }
}
