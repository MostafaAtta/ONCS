package com.atta.oncs.model;

import com.google.gson.annotations.SerializedName;

public class Provider {


    @SerializedName("id")
    private int id;

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
/*

    @SerializedName("provider_id")
    private long providerId;
*/

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

    @SerializedName("deliary")
    private int deliary;

    @SerializedName("opening_time")
    private String openingTime;

    @SerializedName("closed_time")
    private String closedTime;

    @SerializedName("offline")
    private int offline;

    @SerializedName("non_active")
    private int nonActive;

    public Provider(int id, String username, String email, String phone, String profileImage, int provider, String providerName, String providerMobile, String commercialRegister, long providerId, String taxDocNo, String website, int providerRegion, String providerProfileImage, int cid, int sid, int deliary, String openingTime, String closedTime, int offline, int nonActive) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.profileImage = profileImage;
        this.provider = provider;
        this.providerName = providerName;
        this.providerMobile = providerMobile;
        this.commercialRegister = commercialRegister;
        //this.providerId = providerId;
        this.taxDocNo = taxDocNo;
        this.website = website;
        this.providerRegion = providerRegion;
        this.providerProfileImage = providerProfileImage;
        this.cid = cid;
        this.sid = sid;
        this.deliary = deliary;
        this.openingTime = openingTime;
        this.closedTime = closedTime;
        this.offline = offline;
        this.nonActive = nonActive;
    }

    public int getId() {
        return id;
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
/*

    public long getProviderId() {
        return providerId;
    }
*/

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

    public int getDeliary() {
        return deliary;
    }

    public String getOpeningTime() {
        return openingTime;
    }

    public String getClosedTime() {
        return closedTime;
    }

    public int getOffline() {
        return offline;
    }

    public int getNonActive() {
        return nonActive;
    }
}
