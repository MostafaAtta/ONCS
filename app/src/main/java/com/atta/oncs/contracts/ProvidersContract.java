package com.atta.oncs.contracts;

import com.atta.oncs.model.Facility;
import com.atta.oncs.model.Provider;

import java.util.ArrayList;

public interface ProvidersContract {

    interface View{

        void showMessage(String error);

        void showRecyclerView(ArrayList<Provider> providers);

        void showFacilityRecyclerView(ArrayList<Facility> facilities);

        void showProgress();

        void hideProgress();

    }

    interface Presenter{

        void getProviders(int cId, int rId);

        void getFacilities(int cId, int rId);
    }
}
