package com.atta.oncs.contracts;

import com.atta.oncs.model.Address;
import com.atta.oncs.model.Region;
import com.atta.oncs.model.User;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public interface ProfileContract {

    interface View{

        void showMessage(String error);

        void showProgress();

        void hideProgress();

        void showProfile(User user);

        void setSpinner(ArrayList<Region> regions);

        void navigateToMain(User user);

        void showAddressesMessage();

        void showAddresses(List<Address> mAddresses);
    }

    interface Presenter{

        void getProfile(final int id);

        void sendImage(final String mobileNumber, MultipartBody.Part fileupload, RequestBody filename);

        void updateProfile(String mobileNumber, int userId, int region, String username, String email);

        void getRegions(int id);

        void getAddresses(int userId);
    }
}
