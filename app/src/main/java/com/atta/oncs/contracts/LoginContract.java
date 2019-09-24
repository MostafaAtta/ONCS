package com.atta.oncs.contracts;

import com.atta.oncs.model.Region;

import java.util.ArrayList;

public interface LoginContract {

    interface View {

        void showMessage(String error);

        boolean validate(String mobileNumber);

        void setSpinner(ArrayList<Region> regions);
    }

    interface Presenter{

        void getRegions();
    }
}
