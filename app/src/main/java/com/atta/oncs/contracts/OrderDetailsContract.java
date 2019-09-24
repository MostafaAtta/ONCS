package com.atta.oncs.contracts;

import com.atta.oncs.model.Order;

public interface OrderDetailsContract {

    interface View{

        void showMessage(String error);

        void showProgress();

        void hideProgress();

        void showOrder(Order order);

        void navigateToHome();

        void updateOrder();
    }

    interface Presenter{

        void getOrder(final int id);

        void updateOrder(int id, int status, String note, String price);
    }
}
