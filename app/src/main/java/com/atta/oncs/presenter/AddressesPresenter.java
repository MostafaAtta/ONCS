package com.atta.oncs.presenter;

import android.content.Context;

import com.atta.oncs.contracts.AddressesContract;
import com.atta.oncs.model.APIService;
import com.atta.oncs.model.APIUrl;
import com.atta.oncs.model.Address;
import com.atta.oncs.model.Result;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddressesPresenter implements AddressesContract.Presenter {

    private AddressesContract.View mView;

    private Context mContext;

    public AddressesPresenter(AddressesContract.View mView, Context mContext) {
        this.mView = mView;
        this.mContext = mContext;
    }

    @Override
    public void getAddresses(int userId) {

        //building retrofit object
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIUrl.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //Defining retrofit api service
        APIService service = retrofit.create(APIService.class);

        //Defining the user object as we need to pass it with the call
        //User user = new User(name, email, password, phone, birthdayString, locationSting);

        //defining the call
        Call<ArrayList<Address>> call = service.getAddresses(APIUrl.ACTION_GET_ADDRESSES, userId);

        //calling the api
        call.enqueue(new Callback<ArrayList<Address>>() {
            @Override
            public void onResponse(Call<ArrayList<Address>> call, Response<ArrayList<Address>> response) {

                if (response.body() != null){

                        ArrayList<Address> addresses = response.body();

                        if (addresses.size() > 0){

                            mView.showRecyclerView(addresses);
                        }else {

                            mView.updateText();
                        }

                }


            }

            @Override
            public void onFailure(Call<ArrayList<Address>> call, Throwable t) {

                mView.showMessage(t.getMessage());
            }
        });
    }

    @Override
    public void removeAddresses(int id) {

        //building retrofit object
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIUrl.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //Defining retrofit api service
        APIService service = retrofit.create(APIService.class);


        //defining the call
        Call<Result> call = service.removeAddress(id);

        //calling the api
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                //hiding progress dialog


                //displaying the message from the response as toast
                /*if (response.body() != null) {

                    mView.showMessage(response.body().getMessage());

                }*/
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {

                    mView.showMessage(t.getMessage());

            }
        });
    }

}
