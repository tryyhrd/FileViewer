package com.example.fileviewer.Models;

import android.text.Html;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Section implements Serializable {
    @SerializedName("id")
    public int id;

    @SerializedName("document_id")
    public int document_id;

    @SerializedName("title")
    public String title;

    @SerializedName("content")
    public String content;

    @SerializedName("order")
    public int order;

    @SerializedName("is_deleted")
    public boolean is_deleted;

    public Section() {
    }

    public Section(int id, int document_id, String title, String content, int order, boolean is_deleted) {
        this.id = id;
        this.document_id = document_id;
        this.title = title;
        this.content = content;
        this.order = order;
        this.is_deleted = is_deleted;
    }

    public CharSequence getFormattedContent() {
        if (content == null) return "";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            return Html.fromHtml(content, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(content);
        }
    }

    public CharSequence getFormattedTitle() {
        if (title == null || title.isEmpty()) {
            return "";
        }

        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                return Html.fromHtml(title, Html.FROM_HTML_MODE_LEGACY);
            } else {
                return Html.fromHtml(title);
            }
        } catch (Exception e) {
            return title;
        }
    }
}