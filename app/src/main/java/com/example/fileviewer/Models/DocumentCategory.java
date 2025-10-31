package com.example.fileviewer.Models;

import com.google.gson.annotations.SerializedName;

public class DocumentCategory {
    @SerializedName("id")
    public int id;

    @SerializedName("document_id")
    public int document_id;

    @SerializedName("category_id")
    public int category_id;

    public DocumentCategory() {
    }

    public DocumentCategory(int id, int document_id, int category_id) {
        this.id = id;
        this.document_id = document_id;
        this.category_id = category_id;
    }
}