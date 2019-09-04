package com.atta.oncs.contracts;

import com.atta.oncs.model.Result;

public interface VerifyContract {

    interface View{

        void showMessage(String error);

        void startSMSListener();

        void saveUserDate(Result result);
    }

    interface Presenter{

        void verifyMobile(String mobileNumber, int region);
    }
}
