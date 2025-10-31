package com.example.fileviewer;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
        TextView tvDocumentDate;
        TextView tvDocumentStatus;
        TextView tvSectionsCount;

        DocumentViewHolder(View itemView) {
            super(itemView);
            tvDocumentTitle = itemView.findViewById(R.id.tvDocumentTitle);
            tvDocumentDate = itemView.findViewById(R.id.tvDocumentDate);
            tvDocumentStatus = itemView.findViewById(R.id.tvDocumentStatus);
            tvSectionsCount = itemView.findViewById(R.id.tvSectionsCount);
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

        // Устанавливаем данные в View
        holder.tvDocumentTitle.setText(document.title);

        if (document.publication_date != null) {
            holder.tvDocumentDate.setText("Дата: " + document.publication_date.toString());
        } else {
            holder.tvDocumentDate.setText("Дата не указана");
        }

        if (document.status != null) {
            holder.tvDocumentStatus.setText("Статус: " + document.status);
        } else {
            holder.tvDocumentStatus.setText("Статус не указан");
        }

        // Показываем количество разделов
        if (document.sections != null) {
            holder.tvSectionsCount.setText("Разделов: " + document.sections.size());
        } else {
            holder.tvSectionsCount.setText("Разделов: 0");
        }

        // Обработчики кликов
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
