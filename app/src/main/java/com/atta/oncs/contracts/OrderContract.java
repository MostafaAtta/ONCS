package com.atta.oncs.contracts;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public interface OrderContract {

    interface View{

        void showMessage(String error);

        void showProgress();

        void hideProgress();

        void navigateToMain();
    }

    interface Presenter{

        void sendImages(int id, ArrayList<MultipartBody.Part> fileUpload, ArrayList<RequestBody> filename);

        void sendVoice(int id, MultipartBody.Part fileUpload, RequestBody filename, boolean images,
                       ArrayList<MultipartBody.Part> imageFileUpload, ArrayList<RequestBody> imageFilename);

        void addOrder(int userId, int providerId, String request, boolean images, boolean voice,
                      MultipartBody.Part voiceFileUpload, RequestBody voiceFilename,
                      ArrayList<MultipartBody.Part> fileUpload, ArrayList<RequestBody> filename);
    }
}
