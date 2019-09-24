package com.atta.oncs.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Order implements Serializable {

    @SerializedName("id")
    private int id;
    @SerializedName("uid")
    private int userId;
    @SerializedName("spid")
    private int providerId;

    @SerializedName("request")
    private String requestText;
    @SerializedName("request_date")
    private String orderDate;


    @SerializedName("request_voice")
    private String voiceUrl;

    @SerializedName("request_image1")
    private String imageUrl1;
    @SerializedName("request_image2")
    private String imageUrl2;
    @SerializedName("request_image3")
    private String imageUrl3;

    public Order(int id, int userId, int providerId, String requestText, String orderDate, String voiceUrl, String imageUrl1, String imageUrl2, String imageUrl3) {
        this.id = id;
        this.userId = userId;
        this.providerId = providerId;
        this.requestText = requestText;
        this.orderDate = orderDate;
        this.voiceUrl = voiceUrl;
        this.imageUrl1 = imageUrl1;
        this.imageUrl2 = imageUrl2;
        this.imageUrl3 = imageUrl3;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public int getProviderId() {
        return providerId;
    }

    public String getRequestText() {
        return requestText;
    }

    public String getVoiceUrl() {
        return voiceUrl;
    }

    public String getImageUrl1() {
        return imageUrl1;
    }

    public String getImageUrl2() {
        return imageUrl2;
    }

    public String getImageUrl3() {
        return imageUrl3;
    }

    public String getOrderDate() {
        return orderDate;
    }



    public String getImageUrl(int i) {
        String url = null;
        switch (i){
            case 1:
                url = imageUrl1;
                break;
            case 2:
                url = imageUrl2;
                break;
            case 3:
                url = imageUrl3;
                break;

        }

        return url;
    }
}
