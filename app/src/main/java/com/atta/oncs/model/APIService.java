package com.atta.oncs.model;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIService {


    //The register call
    @FormUrlEncoded
    @POST("register")
    Call<Result> createUser(
            @Field("name") String name,
            @Field("email") String email,
            @Field("job") String job,
            @Field("password") String password,
            @Field("phone") String phone,
            @Field("birthday") String birthday,
            @Field("location") String location
    );



    @FormUrlEncoded
    @POST("save_token")
    Call<Result> saveToken(
            @Field("token") String token,
            @Field("user_id") int userId
    );

    @GET("user")
    Call<Result[]> verifyMobile(
            @Query("action") String action,
            @Query("mno") String mobile,
            @Query("rid") int regionId
    );


    @FormUrlEncoded
    @POST("user")
    Call<User> getProfile(
            @Query("action") String action,
            @Field("id") int userId
    );

    @GET("region")
    Call<ArrayList<Region>> getRegions(
            @Query("action") String action
    );


    @GET("region")
    Call<Region> getUserRegion(
            @Query("action") String action,
            @Query("id") int id
    );



    @GET("user")
    Call<ArrayList<Provider>> getProviders(
            @Query("action") String action,
            @Query("cid") int cId,
            @Query("rid") int rId
    );


    @GET("category")
    Call<ArrayList<Category>> getCategories(
            @Query("action") String action
    );

    @POST("user")
    Call<Result[]> updateProfile(
            @Query("action") String action,
            @Query("id") int id,
            @Query("mno") String mobile,
            @Query("fullname") String fullName,
            @Query("email") String email,
            @Query("rid") int regionId

    );

    @Multipart
    @POST("user")
    Call<ImageResponse[]> postImage(
            @Query("action") String action,
            @Query("mno") String mobile,
            @Query("filename") String fileName,
            @Part MultipartBody.Part filename,
            @Part("name") RequestBody name
    );

    @FormUrlEncoded
    @POST("get_addresses")
    Call<Addresses> getAddresses(
            @Field("user_id") int userId
    );

    @DELETE("remove_address/{id}")
    Call<Result> removeAddress(
            @Path("id") int id
    );

    @FormUrlEncoded
    @POST("add_address")
    Call<Result> addAddress(
            @Field("user_id") int userId,
            @Field("floor") String floor,
            @Field("apartmentNumber") String apartmentNumber,
            @Field("buildingNumber") String buildingNumber,
            @Field("area") String area,
            @Field("addressName") String addressName,
            @Field("fullAddress") String fullAddress,
            @Field("street") String street,
            @Field("landMark") String landMark,
            @Field("latitude") float latitude,
            @Field("longitude") float longitude

    );


    @PUT("edit_address/{id}/{user_id}/{floor}/{apartmentNumber}/{buildingNumber}/{area}/{addressName}/{fullAddress}/{street}/{landMark}/{latitude}/{longitude}")
    Call<Result> editAddress(
            @Path("id") int id,
            @Path("user_id") int userId,
            @Path("floor") String floor,
            @Path("apartmentNumber") String apartmentNumber,
            @Path("buildingNumber") String buildingNumber,
            @Path("area") String area,
            @Path("addressName") String addressName,
            @Path("fullAddress") String fullAddress,
            @Path("street") String street,
            @Path("landMark") String landMark,
            @Path("latitude") float latitude,
            @Path("longitude") float longitude

    );

    @DELETE("remove_address/{id}")
    Call<Result> deleteAddress(
            @Path("id") int id
    );



}
