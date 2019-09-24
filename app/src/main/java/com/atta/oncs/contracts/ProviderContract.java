package com.atta.oncs.contracts;

import com.atta.oncs.model.Order;

import java.util.ArrayList;

public interface ProviderContract {

    interface View{

        void showMessage(String error);

        void showRecyclerView(ArrayList<Order> orders);

        void showProgress();

        void hideProgress();

    }

    interface Presenter{

        void getOrders(int pId);
    }
}
