package com.atta.oncs.contracts;

import java.util.ArrayList;

import okhttp3.MultipartBody;

public interface OrderContract {

    interface View{

        void showMessage(String error);

        void showProgress();

        void hideProgress();

        void navigateToMain();
    }

    interface Presenter{

        void sendImages(int id, ArrayList<MultipartBody.Part> fileUpload);

        void sendVoice(int id, MultipartBody.Part fileUpload, boolean images,
                       ArrayList<MultipartBody.Part> imageFileUpload);

        void addOrder(int userId, int providerId, String request, boolean images, boolean voice,
                      MultipartBody.Part voiceFileUpload,
                      ArrayList<MultipartBody.Part> fileUpload);
    }
}
