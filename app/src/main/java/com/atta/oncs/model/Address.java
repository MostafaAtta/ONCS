package com.atta.oncs.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Address implements Serializable {

    @SerializedName("id")
    private int id;
    @SerializedName("uid")
    private int userId;

    @SerializedName("fulladdress")
    private String fullAddress;
    @SerializedName("buildingNumber")
    private String buildingNumber;
    @SerializedName("area")
    private String area;
    @SerializedName("addressname")
    private String addressName;
    @SerializedName("street")
    private String street;
    @SerializedName("landmark")
    private String landMark;
    @SerializedName("floor")
    private String floor;
    @SerializedName("appartmentNumber")
    private String apartmentNumber;

    @SerializedName("latitude")
    private float latitude;
    @SerializedName("longitude")
    private float longitude;


    public Address(int id, int userId, String floor, String apartmentNumber, String buildingNumber, String area,
                   String addressName, String fullAddress, String street, String landMark, float latitude, float longitude) {
        this.id = id;
        this.userId = userId;
        this.floor = floor;
        this.apartmentNumber = apartmentNumber;
        this.buildingNumber = buildingNumber;
        this.area = area;
        this.addressName = addressName;
        this.fullAddress = fullAddress;
        this.street = street;
        this.landMark = landMark;
        this.latitude = latitude;
        this.longitude = longitude;
    }


    public Address(int userId, String floor, String apartmentNumber, String buildingNumber, String area,
                   String addressName, String fullAddress, String street, String landMark, float latitude, float longitude) {

        this.userId = userId;
        this.floor = floor;
        this.apartmentNumber = apartmentNumber;
        this.buildingNumber = buildingNumber;
        this.area = area;
        this.addressName = addressName;
        this.fullAddress = fullAddress;
        this.street = street;
        this.landMark = landMark;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public String getFloor() {
        return floor;
    }

    public String getApartmentNumber() {
        return apartmentNumber;
    }

    public String getBuildingNumber() {
        return buildingNumber;
    }

    public String getArea() {
        return area;
    }

    public String getAddressName() {
        return addressName;
    }

    public String getStreet() {
        return street;
    }

    public String getLandMark() {
        return landMark;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public String getFullAddress() {
        return fullAddress;
    }
}
