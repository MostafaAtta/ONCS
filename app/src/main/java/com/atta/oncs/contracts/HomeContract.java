package com.atta.oncs.contracts;

import com.atta.oncs.model.Category;
import com.atta.oncs.model.Region;

import java.util.ArrayList;

public interface HomeContract {

    interface View{

        void showMessage(String error);

        void showRecyclerView(ArrayList<Category> categories);

        void setSpinner(ArrayList<Region> regions);

        void updateUserRegion(String regionName);

        void showProgress();

        void hideProgress();

        void setFlipperView();

    }

    interface Presenter{

        void getRegions(int regionId, int type);

        void getUserRegion(int id);

        void getCategories(int regionId, int type);
    }
}
