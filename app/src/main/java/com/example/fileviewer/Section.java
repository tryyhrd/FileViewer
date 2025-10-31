package com.example.fileviewer;

import com.google.gson.annotations.SerializedName;

public class Section {
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
}