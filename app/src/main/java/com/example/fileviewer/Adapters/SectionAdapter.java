package com.example.fileviewer.Adapters;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fileviewer.Models.Section;
import com.example.fileviewer.R;

import java.util.List;

public class SectionAdapter extends RecyclerView.Adapter<SectionAdapter.SectionViewHolder> {
    private List<Section> sectionList;

    public SectionAdapter(List<Section> sectionList) {
        this.sectionList = sectionList;
    }

    public void updateData(List<Section> newSections) {
        sectionList.clear();
        sectionList.addAll(newSections);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_section, parent, false);
        return new SectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SectionViewHolder holder, int position) {
        Section section = sectionList.get(position);
        holder.bind(section);
    }

    @Override
    public int getItemCount() {
        return sectionList.size();
    }

    static class SectionViewHolder extends RecyclerView.ViewHolder {
        private TextView tvSectionTitle;
        private TextView tvSectionContent;

        public SectionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSectionTitle = itemView.findViewById(R.id.tvSectionTitle);
            tvSectionContent = itemView.findViewById(R.id.tvSectionContent);
        }

        public void bind(Section section) {
            if (!TextUtils.isEmpty(section.title)) {
                tvSectionTitle.setText(section.getFormattedTitle());
                tvSectionTitle.setVisibility(View.VISIBLE);
            } else {
                tvSectionTitle.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(section.content)) {
                tvSectionContent.setText(section.getFormattedContent());
                tvSectionContent.setVisibility(View.VISIBLE);
            } else {
                tvSectionContent.setVisibility(View.GONE);
            }
        }
    }
}
