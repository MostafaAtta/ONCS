package com.atta.oncs.presenter;

import com.atta.oncs.contracts.ProfileContract;
import com.atta.oncs.model.APIService;
import com.atta.oncs.model.APIUrl;
import com.atta.oncs.model.Address;
import com.atta.oncs.model.ImageResponse;
import com.atta.oncs.model.Region;
import com.atta.oncs.model.Result;
import com.atta.oncs.model.User;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfilePresenter implements ProfileContract.Presenter{

    private ProfileContract.View mView;

    public ProfilePresenter(ProfileContract.View view) {

        mView = view;
    }



    @Override
    public void sendImage(final String mobileNumber, MultipartBody.Part fileupload, RequestBody filename){

        mView.showProgress();
        //Defining retrofit api service
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIUrl.BASE_URL)
                .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
                .build();

        //Defining retrofit api service
        APIService service = retrofit.create(APIService.class);
        Call<ImageResponse[]> call = service.postImage(APIUrl.ACTION_CHANGE_PROFILE_IMAGE , mobileNumber, "image", fileupload, filename);
        //calling the api
        call.enqueue(new Callback<ImageResponse[]>() {
            @Override
            public void onResponse(Call<ImageResponse[]> call, Response<ImageResponse[]> response) {
                //hiding progress dialog
                mView.hideProgress();
                if(response.isSuccessful()){
                    if (response.body()[0].getMobilNumber().equals(mobileNumber))
                        mView.showMessage("Success");
                }
            }

            @Override
            public void onFailure(Call<ImageResponse[]> call, Throwable t) {
                mView.hideProgress();
                mView.showMessage(t.getMessage());
            }
        });
    }

    @Override
    public void getProfile(int id) {

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
        Call<User> call = service.getProfile(APIUrl.ACTION_GET_BY_ID, id);

        //calling the api
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {


                if (response.body() != null) {




                        mView.showMessage("Done");
                        mView.showProfile(response.body());



                }else {
                    //mView.showMessage("An error");
                }

                getRegions(id);

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

                mView.showMessage(t.getMessage());
                getRegions(id);
                //mView.showMessage(t.getMessage());
            }
        });
    }

    @Override
    public void getRegions(int id) {

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
                getAddresses(id);
            }

            @Override
            public void onFailure(Call<ArrayList<Region>> call, Throwable t) {

                mView.showMessage(t.getMessage());

                getAddresses(id);
            }
        });
    }

    @Override
    public void updateProfile(String mobileNumber, int userId, int region, String username, String email) {

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
        Call<Result[]> call = service.updateProfile(APIUrl.ACTION_UPDATE, userId, mobileNumber, username, email, region);

        //calling the api
        call.enqueue(new Callback<Result[]>() {
            @Override
            public void onResponse(Call<Result[]> call, Response<Result[]> response) {


                if (response.body() != null) {


                    if (response.body()[0].getStatus() == 1) {


                        mView.showMessage("Done");
                        mView.navigateToMain(response.body()[0].getUser());

                    }

                }else {
                    //mView.showMessage("An error");
                }

            }

            @Override
            public void onFailure(Call<Result[]> call, Throwable t) {

                mView.showMessage(t.getMessage());
                //mView.showMessage(t.getMessage());
            }
        });
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
                    if (response.body() != null){

                        ArrayList<Address> addresses = response.body();

                        if (addresses.size() > 0){

                            mView.showAddresses(addresses);
                        }else {

                            mView.showAddressesMessage();
                        }

                    }
                }else {
                    mView.showMessage("An error");
                }

            }

            @Override
            public void onFailure(Call<ArrayList<Address>> call, Throwable t) {

                mView.showMessage(t.getMessage());
            }
        });
    }


}
