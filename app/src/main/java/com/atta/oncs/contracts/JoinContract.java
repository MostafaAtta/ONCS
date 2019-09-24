package com.atta.oncs.contracts;

import com.atta.oncs.model.Category;
import com.atta.oncs.model.Region;

import java.util.ArrayList;

public interface JoinContract {

    interface View{

        void showMessage(String error);

        boolean validate(String mobileNumber);

        void setRegionsSpinner(ArrayList<Region> regions);

        void setCategoriesSpinner(ArrayList<Category> categories);
    }

    interface Presenter{

        void getRegions();

        void getCategorie(int type);
    }
}
