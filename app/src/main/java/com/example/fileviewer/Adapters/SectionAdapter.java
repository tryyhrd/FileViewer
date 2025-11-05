package com.example.fileviewer.Adapters;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fileviewer.Models.Section;
import com.example.fileviewer.R;

import java.util.List;

public class SectionAdapter extends RecyclerView.Adapter<SectionAdapter.WebViewSectionViewHolder> {
    private List<Section> sectionList;

    public SectionAdapter(List<Section> sectionList) {
        this.sectionList = sectionList;
    }

    public void updateData(List<Section> newSections) {
        this.sectionList.clear();
        this.sectionList.addAll(newSections);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public WebViewSectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_section, parent, false);
        return new WebViewSectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WebViewSectionViewHolder holder, int position) {
        Section section = sectionList.get(position);
        holder.bind(section);
    }

    @Override
    public int getItemCount() {
        return sectionList.size();
    }

    static class WebViewSectionViewHolder extends RecyclerView.ViewHolder {
        private TextView tvSectionTitle;
        private WebView webViewSectionContent;

        public WebViewSectionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSectionTitle = itemView.findViewById(R.id.tvSectionTitle);
            webViewSectionContent = itemView.findViewById(R.id.webViewSectionContent);

            setupWebView();
        }

        private void setupWebView() {
            webViewSectionContent.setBackgroundColor(0x00000000);
            webViewSectionContent.getSettings().setJavaScriptEnabled(false);
            webViewSectionContent.setVerticalScrollBarEnabled(false);
            webViewSectionContent.setHorizontalScrollBarEnabled(false);
            webViewSectionContent.getSettings().setSupportZoom(true);
            webViewSectionContent.getSettings().setBuiltInZoomControls(true);
            webViewSectionContent.getSettings().setDisplayZoomControls(false);
        }

        public void bind(Section section) {
            if (!TextUtils.isEmpty(section.title)) {
                tvSectionTitle.setText(section.title);
                tvSectionTitle.setVisibility(View.VISIBLE);
            } else {
                tvSectionTitle.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(section.content)) {
                String htmlContent = section.getHtmlFormattedContent();
                webViewSectionContent.loadDataWithBaseURL(
                        null,
                        htmlContent,
                        "text/html; charset=utf-8",
                        "UTF-8",
                        null
                );
                webViewSectionContent.setVisibility(View.VISIBLE);
            } else {
                webViewSectionContent.setVisibility(View.GONE);
            }
        }
    }
}
