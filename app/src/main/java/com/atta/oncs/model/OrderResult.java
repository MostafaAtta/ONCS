package com.atta.oncs.model;

import com.google.gson.annotations.SerializedName;

public class OrderResult {

    @SerializedName("id")
    private int status;

    @SerializedName("request_id")
    private int orderId;

    public OrderResult(int status, int orderId) {
        this.status = status;
        this.orderId = orderId;
    }

    public int getStatus() {
        return status;
    }

    public int getOrderId() {
        return orderId;
    }
}
