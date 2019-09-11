package com.atta.oncs.presenter;

import com.atta.oncs.contracts.HomeContract;
import com.atta.oncs.model.APIService;
import com.atta.oncs.model.APIUrl;
import com.atta.oncs.model.Category;
import com.atta.oncs.model.Region;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomePresenter implements HomeContract.Presenter{

    private HomeContract.View mView;

    public HomePresenter(HomeContract.View view) {

        mView = view;
    }


    @Override
    public void getUserRegion(int id) {

        //building retrofit object
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIUrl.BASE_URL)
                .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
                .build();

        //Defining retrofit api service
        APIService service = retrofit.create(APIService.class);

        //Defining the user object as we need to pass it with the call
        //User user = new User(name, email, password, password, birthdayString, locationSting);

        //defining the call
        Call<Region> call = service.getUserRegion(APIUrl.ACTION_GET_BY_ID, id);

        //calling the api
        call.enqueue(new Callback<Region>() {
            @Override
            public void onResponse(Call<Region> call, Response<Region> response) {


                assert response.body() != null;

                mView.updateUserRegion(response.body().getName());

                getCategories();

            }

            @Override
            public void onFailure(Call<Region> call, Throwable t) {

                mView.showMessage(t.getMessage());
            }
        });
    }

    @Override
    public void getRegions() {

        //building retrofit object
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIUrl.BASE_URL)
                .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
                .build();

        //Defining retrofit api service
        APIService service = retrofit.create(APIService.class);

        //Defining the user object as we need to pass it with the call
        //User user = new User(name, email, password, password, birthdayString, locationSting);

        //defining the call
        Call<ArrayList<Region>> call = service.getRegions(APIUrl.ACTION_GET_ALL);

        //calling the api
        call.enqueue(new Callback<ArrayList<Region>>() {
            @Override
            public void onResponse(Call<ArrayList<Region>> call, Response<ArrayList<Region>> response) {


                assert response.body() != null;
                if (response.body() != null){

                    mView.setSpinner(response.body());

                }else {
                    mView.showMessage("An error");
                }

                getCategories();

            }

            @Override
            public void onFailure(Call<ArrayList<Region>> call, Throwable t) {

                mView.showMessage(t.getMessage());

                getCategories();
            }
        });
    }

    @Override
    public void getCategories() {

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
        Call<ArrayList<Category>> call = service.getCategories(APIUrl.ACTION_GET_ALL);

        //calling the api
        call.enqueue(new Callback<ArrayList<Category>>() {
            @Override
            public void onResponse(Call<ArrayList<Category>> call, Response<ArrayList<Category>> response) {


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
            public void onFailure(Call<ArrayList<Category>> call, Throwable t) {

                mView.showMessage(t.getMessage());
                mView.hideProgress();
            }
        });
    }



}
