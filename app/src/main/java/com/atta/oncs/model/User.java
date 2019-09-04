package com.atta.oncs.model;

import com.google.gson.annotations.SerializedName;

public class User {


    private int id;
    @SerializedName("region")
    private int regionId;
    @SerializedName("fullname")
    private String username;
    private String email;
    @SerializedName("mobileno")
    private String  phone;
    @SerializedName("profile_image")
    private String  profileImage;

    public User(int id, int regionId, String username, String email, String phone) {
        this.id = id;
        this.regionId = regionId;
        this.username = username;
        this.email = email;
        this.phone = phone;
    }

    public User(int id, int regionId, String username, String email, String phone, String profileImage) {
        this.id = id;
        this.regionId = regionId;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.profileImage = profileImage;
    }

    public User() {
    }

    public int getId() {
        return id;
    }

    public int getRegionId() {
        return regionId;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getProfileImage() {
        return profileImage;
    }
}
