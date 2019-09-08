package com.atta.oncs.model;

import com.google.gson.annotations.SerializedName;

public class Category {

    @SerializedName("id")
    int id;

    @SerializedName("name")
    String name;

    @SerializedName("name_en")
    String name_en;

    @SerializedName("delivery")
    int delivery;


    @SerializedName("non_active")
    int non_active;

    @SerializedName("category_icon")
    String categoryIcon;


    public Category(int id, String name, String name_en, int delivery, int non_active) {
        this.id = id;
        this.name = name;
        this.name_en = name_en;
        this.delivery = delivery;
        this.non_active = non_active;
    }

    public Category(int id, String name, String name_en, int delivery, int non_active, String categoryIcon) {
        this.id = id;
        this.name = name;
        this.name_en = name_en;
        this.delivery = delivery;
        this.non_active = non_active;
        this.categoryIcon = categoryIcon;
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

    public int getDelivery() {
        return delivery;
    }

    public int getNon_active() {
        return non_active;
    }

    public String getCategoryIcon() {
        return categoryIcon;
    }
}
