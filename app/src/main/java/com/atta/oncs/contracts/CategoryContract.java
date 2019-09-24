package com.atta.oncs.contracts;

import com.atta.oncs.model.Category;

import java.util.ArrayList;

public interface CategoryContract {

    interface View{

        void showMessage(String error);

        void showRecyclerView(ArrayList<Category> categories);

        void showProgress();

        void hideProgress();

    }

    interface Presenter{

        void getCategories(int regionId, int type);
    }
}
