package com.atta.oncs.presenter;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.atta.oncs.contracts.NewAddressContract;
import com.atta.oncs.model.APIService;
import com.atta.oncs.model.APIUrl;
import com.atta.oncs.model.Address;
import com.atta.oncs.model.AddressResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewAddressPresenter implements NewAddressContract.Presenter {

    private NewAddressContract.View mView;

    private Context mContext;

    public NewAddressPresenter(NewAddressContract.View mView, Context mContext) {
        this.mView = mView;
        this.mContext = mContext;
    }

    @Override
    public void getAddress(String url) {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
            // Create an empty ArrayList that we can start adding mobiles to

            try {
                // Create a JSONObject from the JSON response string
                JSONObject baseJsonResponse = new JSONObject(response);
                JSONArray baseJsonArray, jsonArray;

                baseJsonArray = baseJsonResponse.getJSONArray("results");



                if (!selectAddress(baseJsonArray, "RANGE_INTERPOLATED")){
                    if (!selectAddress(baseJsonArray, "ROOFTOP")){
                        if (!selectAddress(baseJsonArray, "GEOMETRIC_CENTER")){
                            if (!selectAddress(baseJsonArray, "APPROXIMATE")){
                                mView.showMessage("no Address selected");
                            }
                        }
                    }
                }

            } catch (JSONException e) {

                mView.showMessage(e.getMessage());

            }
        }, error -> {
            String message = null;
            if (error instanceof NetworkError) {
                message = "Cannot connect to Internet...Please check your connection!";
            } else if (error instanceof ServerError) {
                message = "The server could not be found. Please try again after some time!!";
            } else if (error instanceof AuthFailureError) {
                message = "Cannot connect to Internet...Please check your connection!";
            } else if (error instanceof ParseError) {
                message = "Parsing error! Please try again after some time!!";
            } else if (error instanceof TimeoutError) {
                message = "Connection TimeOut! Please check your internet connection.";
            }


            mView.showMessage(message);

        });

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        requestQueue.add(stringRequest);
    }

    public boolean selectAddress(JSONArray baseJsonArray, String type) throws JSONException {
        JSONArray jsonArray;
        boolean selected = false;
        for (int j = 0; j < baseJsonArray.length(); j++){

            jsonArray = baseJsonArray.getJSONObject(j).getJSONArray("address_components");

            String locationType = baseJsonArray.getJSONObject(j).getJSONObject("geometry").getString("location_type");


            String formattedAddress = baseJsonArray.getJSONObject(j).getString("formatted_address");


            if (locationType.equals(type)){

                getAddress(jsonArray, formattedAddress);

                selected =  true;
                break;
            }

        }

        return selected;
    }

    public void getAddress(JSONArray jsonArray, String formattedAddress) throws JSONException {


        String route = "", area = "", buildingNumber = "", city = "";
        boolean correctAddress = false;

        for (int i = 0; i < jsonArray.length(); i++){


            JSONArray typeJSONArray = jsonArray.getJSONObject(i).getJSONArray("types");

            switch (typeJSONArray.getString(0)){
                case "country":

                    correctAddress = jsonArray.getJSONObject(i).getString("long_name").equals("Egypt");

                    break;
                case "route":

                    route = jsonArray.getJSONObject(i).getString("long_name");
                    break;
                case "street_number":
                    buildingNumber = jsonArray.getJSONObject(i).getString("long_name");
                    break;
                case "administrative_area_level_2":
                    area = jsonArray.getJSONObject(i).getString("long_name");
                    break;
                case "administrative_area_level_1":
                    city = jsonArray.getJSONObject(i).getString("long_name");
                    break;

            }



        }

        if (correctAddress){


            mView.setStreet(route);

            mView.setBuildingNumber(buildingNumber);

            mView.setArea(area);

            mView.setCity(city);

            mView.setFullAddress(formattedAddress);

        }else {
            mView.showMessage("Select address in Egypt");
        }
    }

    @Override
    public void addAddress(Address address) {

        //building retrofit object
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIUrl.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //Defining retrofit api service
        APIService service = retrofit.create(APIService.class);


        //defining the call
        Call<AddressResult[]> call = service.addAddress(
                APIUrl.ACTION_ADD_ADDRESS,
                address.getUserId(),
                address.getFloor(),
                address.getApartmentNumber(),
                address.getBuildingNumber(),
                address.getArea(),
                address.getAddressName(),
                address.getFullAddress(),
                address.getStreet(),
                address.getLandMark(),
                address.getLatitude(),
                address.getLongitude()
        );

        //calling the api
        call.enqueue(new Callback<AddressResult[]>() {
            @Override
            public void onResponse(Call<AddressResult[]> call, Response<AddressResult[]> response) {
                //hiding progress dialog


                //displaying the message from the response as toast
                if (response.body() != null) {

                    if (response.body()[0].getStatus() == 1) {

                        mView.showMessage("تم اضافة العنوان");


                        mView.moveToAddressesActivity();
                    }

                }
            }

            @Override
            public void onFailure(Call<AddressResult[]> call, Throwable t) {

                mView.showMessage(t.getMessage());

            }
        });
    }


    @Override
    public void editAddress(Address address) {

        //building retrofit object
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIUrl.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //Defining retrofit api service
        APIService service = retrofit.create(APIService.class);


        //defining the call
        Call<AddressResult[]> call = service.editAddress(
                APIUrl.ACTION_EDIT_ADDRESS,
                address.getId(),
                address.getFloor(),
                address.getApartmentNumber(),
                address.getBuildingNumber(),
                address.getArea(),
                address.getAddressName(),
                address.getFullAddress(),
                address.getStreet(),
                address.getLandMark(),
                address.getLatitude(),
                address.getLongitude()
        );

        //calling the api
        call.enqueue(new Callback<AddressResult[]>() {
            @Override
            public void onResponse(Call<AddressResult[]> call, Response<AddressResult[]> response) {
                //hiding progress dialog


                //displaying the message from the response as toast
                if (response.body() != null) {


                    if (response.body()[0].getStatus() == 1) {

                        mView.showMessage("تم تعديل العنوان");


                        mView.moveToAddressesActivity();
                    }

                }
            }

            @Override
            public void onFailure(Call<AddressResult[]> call, Throwable t) {

                mView.showMessage(t.getMessage());

            }
        });
    }

    @Override
    public void deleteAddress(int id) {

        //building retrofit object
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIUrl.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //Defining retrofit api service
        APIService service = retrofit.create(APIService.class);


        //defining the call
        Call<AddressResult[]> call = service.deleteAddress(APIUrl.ACTION_DELETE_ADDRESS, id);

        //calling the api
        call.enqueue(new Callback<AddressResult[]>() {
            @Override
            public void onResponse(Call<AddressResult[]> call, Response<AddressResult[]> response) {
                //hiding progress dialog


                //displaying the message from the response as toast
                if (response.body() != null) {


                    if (response.body()[0].getStatus() == 1) {

                        mView.showMessage("تم مسح العنوان");


                        mView.moveToAddressesActivity();
                    }

                }
            }

            @Override
            public void onFailure(Call<AddressResult[]> call, Throwable t) {

                mView.showMessage(t.getMessage());

            }
        });
    }

}
