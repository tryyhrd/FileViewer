package com.example.fileviewer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class GOSTAdapter extends RecyclerView.Adapter<GOSTAdapter.GostViewHolder>{

    private List<GOST> gostList;
    private OnItemClickListener itemClickListener;

    interface OnItemClickListener {
        void onItemClick(GOST gost);
        void onItemLongClick(GOST gost);
    }

    static class GostViewHolder extends RecyclerView.ViewHolder {
        TextView tvGostCode;
        TextView tvGostTitle;
        TextView tvGostDescription;

        GostViewHolder(View itemView) {
            super(itemView);
            tvGostCode = itemView.findViewById(R.id.tvGostCode);
            tvGostTitle = itemView.findViewById(R.id.tvGostTitle);
            tvGostDescription = itemView.findViewById(R.id.tvGostDescription);
        }
    }

    GOSTAdapter(List<GOST> gostList, OnItemClickListener listener) {
        this.gostList = gostList;
        this.itemClickListener = listener;
    }

    @Override
    public GostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_gost, parent, false);
        return new GostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GostViewHolder holder, int position) {
        final GOST gost = gostList.get(position);

        holder.tvGostCode.setText(gost.number + ". " + gost.code);
        holder.tvGostTitle.setText(gost.title);
        holder.tvGostDescription.setText(gost.description);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(gost);
                }
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (itemClickListener != null) {
                    itemClickListener.onItemLongClick(gost);
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return gostList.size();
    }

    void updateData(List<GOST> newGostList) {
        this.gostList = newGostList;
        notifyDataSetChanged();
    }
}
