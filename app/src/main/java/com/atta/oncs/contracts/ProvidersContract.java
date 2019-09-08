package com.atta.oncs.contracts;

import com.atta.oncs.model.Provider;

import java.util.ArrayList;

public interface ProvidersContract {

    interface View{

        void showMessage(String error);

        void showRecyclerView(ArrayList<Provider> providers);

        void showProgress();

        void hideProgress();

    }

    interface Presenter{

        void getProviders(int cId, int rId);
    }
}
