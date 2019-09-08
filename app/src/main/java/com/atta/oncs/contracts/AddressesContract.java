package com.atta.oncs.contracts;

import com.atta.oncs.model.Address;

import java.util.ArrayList;

public interface AddressesContract {

    interface View{

        void showMessage(String message);

        void showRecyclerView(ArrayList<Address> addresses);

        void updateText();

        void addNewAddress();
    }

    interface Presenter{
        void getAddresses(int userId) ;

        void removeAddresses(int id);

    }
}
