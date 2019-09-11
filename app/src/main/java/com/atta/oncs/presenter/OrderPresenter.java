package com.atta.oncs.presenter;

import androidx.annotation.Nullable;

import com.atta.oncs.contracts.OrderContract;
import com.atta.oncs.model.APIService;
import com.atta.oncs.model.APIUrl;
import com.atta.oncs.model.ImageResponse;
import com.atta.oncs.model.OrderResult;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class OrderPresenter implements OrderContract.Presenter{

    private OrderContract.View mView;

    public OrderPresenter(OrderContract.View view) {

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
                    if (response.body()[0].getMobilNumber().equals(mobileNumber))
                        mView.showMessage("Success");
                    //updateProfile(mobileNumber, userId, region, username, email);
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
    public void sendImages(int id, ArrayList<MultipartBody.Part> fileUpload, ArrayList<RequestBody> filename) {

        mView.showProgress();
        //Defining retrofit api service
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIUrl.BASE_URL)
                .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
                .build();

        //Defining retrofit api service
        APIService service = retrofit.create(APIService.class);
        Call<OrderResult[]> call = service.addOrderImages(APIUrl.ACTION_ADD_ORDER_IMAGE , id, "image", fileUpload.get(0), filename.get(0)
                , "image", fileUpload.get(1), filename.get(1), "image", fileUpload.get(2), filename.get(2));
        //calling the api
        call.enqueue(new Callback<OrderResult[]>() {
            @Override
            public void onResponse(Call<OrderResult[]> call, Response<OrderResult[]> response) {
                //hiding progress dialog
                mView.hideProgress();
                if(response.isSuccessful()){
                    if (response.body()[0].getStatus() == 1)

                            mView.showMessage("Done");
                            mView.showMessage(String.valueOf(response.body()[0].getOrderId()));
                            mView.navigateToMain();

                    //updateProfile(mobileNumber, userId, region, username, email);
                }
            }

            @Override
            public void onFailure(Call<OrderResult[]> call, Throwable t) {
                mView.hideProgress();
                mView.showMessage(t.getMessage());
            }
        });
    }

    @Override
    public void sendVoice(int id, MultipartBody.Part fileUpload, RequestBody filename, boolean images,
                          ArrayList<MultipartBody.Part> imageFileUpload, ArrayList<RequestBody> imageFilename) {

        //Defining retrofit api service
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIUrl.BASE_URL)
                .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
                .build();

        //Defining retrofit api service
        APIService service = retrofit.create(APIService.class);
        Call<OrderResult[]> call = service.addOrderVoice(APIUrl.ACTION_ADD_ORDER_RECORED , id, "voice", fileUpload, filename);
        //calling the api
        call.enqueue(new Callback<OrderResult[]>() {
            @Override
            public void onResponse(Call<OrderResult[]> call, Response<OrderResult[]> response) {
                //hiding progress dialog
                mView.hideProgress();
                if(response.isSuccessful()){
                    if (response.body()[0].getStatus() == 1)

                        if (images){
                            sendImages(id, imageFileUpload, imageFilename);
                        }else {
                            mView.showMessage("Done");
                            mView.showMessage(String.valueOf(response.body()[0].getOrderId()));
                            mView.navigateToMain();
                        }
                    //updateProfile(mobileNumber, userId, region, username, email);
                }
            }

            @Override
            public void onFailure(Call<OrderResult[]> call, Throwable t) {
                mView.hideProgress();
                mView.showMessage(t.getMessage());
            }
        });
    }

    @Override
    public void addOrder(int userId, int providerId, String request, boolean images, boolean voice,
                         @Nullable MultipartBody.Part voiceFileUpload, @Nullable RequestBody voiceFilename,
                         @Nullable ArrayList<MultipartBody.Part> fileUpload, @Nullable ArrayList<RequestBody> filename) {

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
        Call<OrderResult[]> call = service.addOrder(APIUrl.ACTION_ADD_ORDER, userId, providerId, request);

        //calling the api
        call.enqueue(new Callback<OrderResult[]>() {
            @Override
            public void onResponse(Call<OrderResult[]> call, Response<OrderResult[]> response) {


                if (response.body() != null) {


                    if (response.body()[0].getStatus() == 1) {


                        if (voice){
                            sendVoice(response.body()[0].getOrderId(), voiceFileUpload, voiceFilename, images,
                                    fileUpload, filename);
                        }else if(images){
                            sendImages(response.body()[0].getOrderId(),
                                    fileUpload, filename);
                        }else {
                            mView.showMessage("Done");
                            mView.showMessage(String.valueOf(response.body()[0].getOrderId()));
                            mView.navigateToMain();
                        }

                        //mView.navigateToMain(response.body()[0].getUser());

                    }else {
                        mView.showMessage("An error");
                    }

                }else {
                    mView.showMessage("An error");
                }

            }

            @Override
            public void onFailure(Call<OrderResult[]> call, Throwable t) {

                mView.showMessage(t.getMessage());
                //mView.showMessage(t.getMessage());
            }
        });
    }
}
