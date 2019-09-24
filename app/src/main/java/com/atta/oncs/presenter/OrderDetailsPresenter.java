package com.atta.oncs.presenter;

import com.atta.oncs.contracts.OrderDetailsContract;
import com.atta.oncs.model.APIService;
import com.atta.oncs.model.APIUrl;
import com.atta.oncs.model.Order;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OrderDetailsPresenter implements OrderDetailsContract.Presenter{

    private OrderDetailsContract.View mView;

    public OrderDetailsPresenter(OrderDetailsContract.View view) {

        mView = view;
    }



    @Override
    public void getOrder(int id) {

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
        Call<Order> call = service.getOrder(APIUrl.ACTION_GET_BY_ID, id);

        //calling the api
        call.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {


                if (response.body() != null) {




                    //mView.showMessage("Done");
                    mView.showOrder(response.body());



                }else {
                    //mView.showMessage("An error");
                }


            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {

                mView.showMessage(t.getMessage());

                //mView.showMessage(t.getMessage());
            }
        });
    }

    @Override
    public void updateOrder(int id, int status, String note, String price) {

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
        Call<int[]> call = service.updateOrder(APIUrl.ACTION_UPDATE_ORDER, id, status, note, price);

        //calling the api
        call.enqueue(new Callback<int[]>() {
            @Override
            public void onResponse(Call<int[]> call, Response<int[]> response) {


                if (response.body() != null) {


                    if (response.body()[0] == 1) {


                        //mView.showMessage("Done");
                        mView.navigateToHome();

                    }

                }else {
                    //mView.showMessage("An error");
                }

            }

            @Override
            public void onFailure(Call<int[]> call, Throwable t) {

                mView.showMessage(t.getMessage());
                //mView.showMessage(t.getMessage());
            }
        });
    }



}
