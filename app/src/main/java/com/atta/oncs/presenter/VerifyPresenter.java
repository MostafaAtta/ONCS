package com.atta.oncs.presenter;

import com.atta.oncs.contracts.VerifyContract;
import com.atta.oncs.model.APIService;
import com.atta.oncs.model.APIUrl;
import com.atta.oncs.model.Result;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class VerifyPresenter implements VerifyContract.Presenter{

    private VerifyContract.View mView;

    public VerifyPresenter(VerifyContract.View view) {

        mView = view;
    }


    @Override
    public void verifyMobile(String mobileNumber, int region, String token) {

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
        Call<Result[]> call = service.verifyMobile(APIUrl.ACTION_INSERT, mobileNumber, region, token);

        //calling the api
        call.enqueue(new Callback<Result[]>() {
            @Override
            public void onResponse(Call<Result[]> call, Response<Result[]> response) {


                if (response.body() != null){

                    mView.saveUserDate(response.body()[0]);

                    /*if (response.body().getStatus() == 2){
                        mView.navigateToMain();
                    }else {
                        mView.navigateToVraficationFragment();
                    }*/

                }else {
                    mView.showMessage("An error");
                }

            }

            @Override
            public void onFailure(Call<Result[]> call, Throwable t) {

                mView.showMessage(t.getMessage());
            }
        });
    }

}
