package com.atta.oncs.presenter;

import com.atta.oncs.contracts.JoinContract;
import com.atta.oncs.model.APIService;
import com.atta.oncs.model.APIUrl;
import com.atta.oncs.model.Category;
import com.atta.oncs.model.Region;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class JoinPresenter implements JoinContract.Presenter{

    private JoinContract.View mView;

    public JoinPresenter(JoinContract.View view) {

        mView = view;
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

                    mView.setRegionsSpinner(response.body());

                }else {
                    mView.showMessage("An error");
                }

            }

            @Override
            public void onFailure(Call<ArrayList<Region>> call, Throwable t) {

                mView.showMessage(t.getMessage());
            }
        });
    }

    @Override
    public void getCategorie(int type) {
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
        Call<ArrayList<Category>> call = service.getCategories(APIUrl.ACTION_GET_CATEGORIES, 0, type);

        //calling the api
        call.enqueue(new Callback<ArrayList<Category>>() {
            @Override
            public void onResponse(Call<ArrayList<Category>> call, Response<ArrayList<Category>> response) {


                assert response.body() != null;
                if (response.body() != null){

                    mView.setCategoriesSpinner(response.body());

                }else {
                    mView.showMessage("An error");
                }

            }

            @Override
            public void onFailure(Call<ArrayList<Category>> call, Throwable t) {

                mView.showMessage(t.getMessage());
            }
        });
    }
}
