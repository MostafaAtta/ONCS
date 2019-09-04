package com.atta.oncs.contracts;

import com.atta.oncs.model.Category;
import com.atta.oncs.model.Region;

import java.util.ArrayList;

public interface HomeContract {

    interface View{

        void showMessage(String error);

        void showRecyclerView(ArrayList<Category> categories);

        void setSpinner(ArrayList<Region> regions);
    }

    interface Presenter{

        void getRegions();

        void getCategories();
    }
}
