package com.atta.oncs.model;

import com.google.gson.annotations.SerializedName;

public class Result {
    @SerializedName("status")
    private int status;

    @SerializedName("verify_code")
    private String verifyCode;

    @SerializedName("lastid")
    private int id;

    @SerializedName("user")
    private User user;

    public Result(int status, String verifyCode, User user) {
        this.status = status;
        this.verifyCode = verifyCode;
        this.user = user;
    }

    public int getStatus() {
        return status;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public User getUser() {
        return user;
    }

    public int getId() {
        return id;
    }
}
