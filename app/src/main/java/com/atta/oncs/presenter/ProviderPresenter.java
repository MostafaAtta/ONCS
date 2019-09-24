package com.atta.oncs.presenter;

import com.atta.oncs.contracts.ProviderContract;
import com.atta.oncs.model.APIService;
import com.atta.oncs.model.APIUrl;
import com.atta.oncs.model.Order;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProviderPresenter implements ProviderContract.Presenter{

    private ProviderContract.View mView;

    public ProviderPresenter(ProviderContract.View view) {

        mView = view;
    }


    @Override
    public void getOrders(int pId) {

        //building retrofit object
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIUrl.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //Defining retrofit api service
        APIService service = retrofit.create(APIService.class);

        //Defining the user object as we need to pass it with the call
        //User user = new User(name, email, password, password, birthdayString, locationSting);

        //defining the call
        Call<ArrayList<Order>> call = service.getProviderOrders(APIUrl.ACTION_GET_PROVIDER_ORDERS, pId);

        //calling the api
        call.enqueue(new Callback<ArrayList<Order>>() {
            @Override
            public void onResponse(Call<ArrayList<Order>> call, Response<ArrayList<Order>> response) {


                if (response.body() != null){


                    mView.showRecyclerView(response.body());

                    /*if (response.body().getStatus() == 2){
                        mView.navigateToMain();
                    }else {
                        mView.navigateToVraficationFragment();
                    }*/

                }else {
                    mView.showMessage("An error");
                }
                //mView.hideProgress();

            }

            @Override
            public void onFailure(Call<ArrayList<Order>> call, Throwable t) {

                mView.showMessage(t.getMessage());
                mView.hideProgress();
            }
        });
    }



}
