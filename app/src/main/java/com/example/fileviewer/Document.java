package com.example.fileviewer;

import com.google.gson.annotations.SerializedName;
import java.util.Date;
import java.util.List;

public class Document {
    @SerializedName("id")
    public int id;

    @SerializedName("title")
    public String title;

    @SerializedName("publication_date")
    public Date publication_date;

    @SerializedName("status")
    public String status;

    @SerializedName("source_url")
    public String source_url;

    @SerializedName("created_at")
    public Date created_at;

    @SerializedName("updated_at")
    public Date updated_at;

    @SerializedName("is_deleted")
    public boolean is_deleted;

    @SerializedName("sections")
    public List<Section> sections;

    @SerializedName("document_categories")
    public List<DocumentCategory> document_categories;

    @SerializedName("parse")
    public boolean parse;

    public Document() {
    }
}