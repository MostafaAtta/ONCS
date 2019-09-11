package com.atta.oncs.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Addresses {

    @SerializedName("addresses")
    private ArrayList<Address> addresses;

    public Addresses() {
    }

    public ArrayList<Address> getAddresses() {
        return addresses;
    }
}
