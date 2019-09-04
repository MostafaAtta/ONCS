package com.atta.oncs.model;

import com.google.gson.annotations.SerializedName;

public class Category {

    @SerializedName("id")
    int id;

    @SerializedName("name")
    String name;

    @SerializedName("name_en")
    String name_en;

    @SerializedName("delivary")
    int delivary;


    @SerializedName("non_active")
    int non_active;


    public Category(int id, String name, String name_en, int delivary, int non_active) {
        this.id = id;
        this.name = name;
        this.name_en = name_en;
        this.delivary = delivary;
        this.non_active = non_active;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getName_en() {
        return name_en;
    }

    public int getDelivary() {
        return delivary;
    }

    public int getNon_active() {
        return non_active;
    }
}
