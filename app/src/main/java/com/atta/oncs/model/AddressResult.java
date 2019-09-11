package com.atta.oncs.model;

import com.google.gson.annotations.SerializedName;

public class AddressResult {
    @SerializedName("status")
    private int status;

    @SerializedName("address")
    private Address address;

    public AddressResult(int status, Address address) {
        this.status = status;
        this.address = address;
    }

    public int getStatus() {
        return status;
    }

    public Address getAddress() {
        return address;
    }
}
