package com.atta.oncs.model;

import com.google.gson.annotations.SerializedName;

public class User {


    @SerializedName("id")
    private int id;
    @SerializedName("region")
    private int regionId;
    @SerializedName("fullname")
    private String username;
    @SerializedName("email")
    private String email;
    @SerializedName("mobileno")
    private String  phone;
    @SerializedName("profile_image")
    private String  profileImage;
    @SerializedName("provider")
    private int provider;

    @SerializedName("provider_name")
    private String providerName;

    @SerializedName("provider_mobile")
    private String providerMobile;

    @SerializedName("commercial_register")
    private String commercialRegister;

    @SerializedName("tax_doc_no")
    private String taxDocNo;

    @SerializedName("website")
    private String website;

    @SerializedName("provider_region")
    private int providerRegion;

    @SerializedName("provider_profile_image")
    private String providerProfileImage;

    @SerializedName("cid")
    private int cid;

    @SerializedName("sid")
    private int sid;

    @SerializedName("opening_time")
    private int openingTime;

    @SerializedName("closed_time")
    private int closedTime;

    @SerializedName("offline")
    private int offline;

    @SerializedName("non_active")
    private int nonActive;


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

    public int getProvider() {
        return provider;
    }

    public String getProviderName() {
        return providerName;
    }

    public String getProviderMobile() {
        return providerMobile;
    }

    public String getCommercialRegister() {
        return commercialRegister;
    }

    public String getTaxDocNo() {
        return taxDocNo;
    }

    public String getWebsite() {
        return website;
    }

    public int getProviderRegion() {
        return providerRegion;
    }

    public String getProviderProfileImage() {
        return providerProfileImage;
    }

    public int getCid() {
        return cid;
    }

    public int getSid() {
        return sid;
    }

    public int getOpeningTime() {
        return openingTime;
    }

    public int getClosedTime() {
        return closedTime;
    }

    public int getOffline() {
        return offline;
    }

    public int getNonActive() {
        return nonActive;
    }
}
