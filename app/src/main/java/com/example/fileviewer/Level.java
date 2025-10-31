package com.example.fileviewer;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Level {
    @SerializedName("id")
    public int id;

    @SerializedName("name")
    public String name;

    @SerializedName("is_deleted")
    public boolean is_deleted;

    @SerializedName("categories")
    public List<Category> categories;

    public Level() {
    }

    public Level(int id, String name, boolean is_deleted) {
        this.id = id;
        this.name = name;
        this.is_deleted = is_deleted;
    }
}