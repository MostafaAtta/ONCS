package com.atta.oncs.model;

import com.google.gson.annotations.SerializedName;

public class Region {

    @SerializedName("id")
    int id;
    @SerializedName("name")
    String name;
    @SerializedName("name_en")
    String nameEnglish;
    @SerializedName("non_active")
    int active;

    public Region(int id, String name, String nameEnglish, int active) {
        this.id = id;
        this.name = name;
        this.nameEnglish = nameEnglish;
        this.active = active;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getNameEnglish() {
        return nameEnglish;
    }

    public int getActive() {
        return active;
    }

    @Override
    public String toString() {
        return name ;
    }
}
