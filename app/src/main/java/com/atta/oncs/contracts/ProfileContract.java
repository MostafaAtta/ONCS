package com.atta.oncs.contracts;

import com.atta.oncs.model.User;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public interface ProfileContract {

    interface View{

        void showMessage(String error);

        void showProgress();

        void hideProgress();

        void showProfile(User user);
    }

    interface Presenter{

        void getProfile(final int id);

        void sendImage(final String mobileNumber, final int userId, final int region, MultipartBody.Part fileupload, RequestBody filename, final String username, final String email);

        void updateProfile(String mobileNumber, int userId, int region, String username, String email);
    }
}
