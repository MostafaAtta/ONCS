package com.atta.oncs.presenter;

import com.atta.oncs.contracts.ProvidersContract;
import com.atta.oncs.model.APIService;
import com.atta.oncs.model.APIUrl;
import com.atta.oncs.model.Facility;
import com.atta.oncs.model.Provider;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProvidersPresenter implements ProvidersContract.Presenter{

    private ProvidersContract.View mView;

    public ProvidersPresenter(ProvidersContract.View view) {

        mView = view;
    }


    @Override
    public void getProviders(int cId, int rId) {

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
        Call<ArrayList<Provider>> call = service.getProviders(APIUrl.ACTION_GET_PROVIDERS, cId, rId);

        //calling the api
        call.enqueue(new Callback<ArrayList<Provider>>() {
            @Override
            public void onResponse(Call<ArrayList<Provider>> call, Response<ArrayList<Provider>> response) {


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
            public void onFailure(Call<ArrayList<Provider>> call, Throwable t) {

                mView.showMessage(t.getMessage());
                mView.hideProgress();
            }
        });
    }




    @Override
    public void getFacilities(int cId, int rId) {

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
        Call<ArrayList<Facility>> call = service.getFacilities(APIUrl.ACTION_GET_BY_REGION, rId, cId);

        //calling the api
        call.enqueue(new Callback<ArrayList<Facility>>() {
            @Override
            public void onResponse(Call<ArrayList<Facility>> call, Response<ArrayList<Facility>> response) {


                if (response.body() != null){


                    mView.showFacilityRecyclerView(response.body());


                }else {
                    mView.showMessage("An error");
                }
                //mView.hideProgress();

            }

            @Override
            public void onFailure(Call<ArrayList<Facility>> call, Throwable t) {

                mView.showMessage(t.getMessage());
                mView.hideProgress();
            }
        });
    }


}
