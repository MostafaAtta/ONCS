package com.atta.oncs.presenter;

import com.atta.oncs.contracts.RegisterContract;
import com.atta.oncs.model.APIService;
import com.atta.oncs.model.APIUrl;
import com.atta.oncs.model.ImageResponse;
import com.atta.oncs.model.Result;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RegisterPresenter implements RegisterContract.Presenter{

    private RegisterContract.View mView;

    public RegisterPresenter(RegisterContract.View view) {

        mView = view;
    }


    public void sendImage(final String mobileNumber, final int userId, final int region, MultipartBody.Part fileupload, RequestBody filename, final String username, final String email){

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
                    if (response.body()[0].getMobilNumber().equals(mobileNumber)) {
                        mView.showMessage("Success");
                        updateProfile(mobileNumber, userId, region, username, email);
                    }
                }
            }

            @Override
            public void onFailure(Call<ImageResponse[]> call, Throwable t) {
                mView.hideProgress();
                mView.showMessage(t.getMessage());
            }
        });
    }

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

}
