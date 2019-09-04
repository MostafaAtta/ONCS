package com.atta.oncs.model;

import com.google.gson.annotations.SerializedName;

public class ImageResponse {

    @SerializedName("mobileno")
    String mobilNumber;
    @SerializedName("profile_image")
    String url;

    public ImageResponse(String mobilNumber, String url) {
        this.mobilNumber = mobilNumber;
        this.url = url;
    }

    public String getMobilNumber() {
        return mobilNumber;
    }

    public String getUrl() {
        return url;
    }
}
