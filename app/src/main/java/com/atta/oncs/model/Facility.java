package com.atta.oncs.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Facility implements Serializable {

    @SerializedName("id")
    int id;

    @SerializedName("facility_name")
    String name;

    @SerializedName("facility_type")
    int type;

    @SerializedName("facility_phone")
    String phone;


    @SerializedName("facility_email")
    String email;

    @SerializedName("facility_address")
    String address;

    @SerializedName("facility_website")
    String website;

    @SerializedName("region")
    int region;

    @SerializedName("about")
    String about;

    @SerializedName("facility_logo")
    String logo;

    public Facility(int id, String name, int type, String phone, String email, String address, String website, int region, String about, String logo) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.website = website;
        this.region = region;
        this.about = about;
        this.logo = logo;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getType() {
        return type;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getWebsite() {
        return website;
    }

    public int getRegion() {
        return region;
    }

    public String getAbout() {
        return about;
    }

    public String getLogo() {
        return logo;
    }
}
