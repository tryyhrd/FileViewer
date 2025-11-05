package com.example.fileviewer.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Category implements Serializable {
    @SerializedName("id")
    public int id;

    @SerializedName("name")
    public String name;

    @SerializedName("is_deleted")
    public boolean is_deleted;

    @SerializedName("level_id")
    public int level_id;

    public Category() {
    }

    public Category(int id, String name, boolean is_deleted, int level_id) {
        this.id = id;
        this.name = name;
        this.is_deleted = is_deleted;
        this.level_id = level_id;
    }
}