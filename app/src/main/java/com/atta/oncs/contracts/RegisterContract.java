package com.atta.oncs.contracts;

import com.atta.oncs.model.User;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public interface RegisterContract {

    interface View{

        void showMessage(String error);

        void showProgress();

        void hideProgress();

        void navigateToMain(User user);
    }

    interface Presenter{

        void sendImage(final String mobileNumber, int userId, final int region, MultipartBody.Part fileupload, RequestBody filename, String username, String email);

        void updateProfile(String mobileNumber, int userId, int region, String username, String email);
    }
}
