package com.example.fileviewer;

import com.google.gson.annotations.SerializedName;

public class Category {
    @SerializedName("id")
    public int id;

    @SerializedName("name")
    public String name;

    @SerializedName("is_deleted")
    public boolean is_deleted;

    @SerializedName("level_id")
    public int level_id;

    @SerializedName("CountDocumentCategory")
    public int CountDocumentCategory;

    public Category() {
    }

    public Category(int id, String name, boolean is_deleted, int level_id, int countDocumentCategory) {
        this.id = id;
        this.name = name;
        this.is_deleted = is_deleted;
        this.level_id = level_id;
        this.CountDocumentCategory = countDocumentCategory;
    }
}