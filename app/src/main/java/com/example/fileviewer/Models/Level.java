package com.example.fileviewer.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Level implements Serializable {
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

    public Level(int id, String name, List<Category> categories) {
        this.id = id;
        this.name = name;
        this.categories = categories;
    }
}