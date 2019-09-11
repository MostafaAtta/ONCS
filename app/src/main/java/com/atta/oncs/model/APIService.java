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

    @POST("request")
    Call<OrderResult[]> addOrder(
            @Query("action") String action,
            @Query("uid") int id,
            @Query("spid") int providerId,
            @Query("request") String request

    );

    @Multipart
    @POST("request")
    Call<OrderResult[]> addOrderImages(
            @Query("action") String action,
            @Query("id") int id,
            @Query("filevoicename") String fileName1,
            @Part MultipartBody.Part filevoicename1,
            @Part("name") RequestBody name1,
            @Query("filevoicename") String fileName2,
            @Part MultipartBody.Part filevoicename2,
            @Part("name") RequestBody nam2,
            @Query("filevoicename") String fileName3,
            @Part MultipartBody.Part filevoicename3,
            @Part("name") RequestBody name3
    );

    @Multipart
    @POST("request")
    Call<OrderResult[]> addOrderVoice(
            @Query("action") String action,
            @Query("uid") int id,
            @Query("filevoicename") String fileName,
            @Part MultipartBody.Part filevoicename,
            @Part("name") RequestBody name
    );

    @POST("user")
    Call<ArrayList<Address>> getAddresses(
            @Query("action") String action,
            @Query("uid") int userId
    );

    @DELETE("remove_address/{id}")
    Call<Result> removeAddress(
            @Path("id") int id
    );

    @POST("user")
    Call<AddressResult[]> addAddress(
            @Query("action") String action,
            @Query("uid") int userId,
            @Query("floor") String floor,
            @Query("appartmentNumber") String apartmentNumber,
            @Query("buildingNumber") String buildingNumber,
            @Query("area") String area,
            @Query("addressname") String addressName,
            @Query("fulladdress") String fullAddress,
            @Query("street") String street,
            @Query("landmark") String landMark,
            @Query("latitude") float latitude,
            @Query("longitude") float longitude

    );


    @POST("user")
    Call<Result> editAddress(
            @Query("action") String action,
            @Query("id") int id,
            @Query("floor") String floor,
            @Query("appartmentNumber") String apartmentNumber,
            @Query("buildingNumber") String buildingNumber,
            @Query("area") String area,
            @Query("addressname") String addressName,
            @Query("fulladdress") String fullAddress,
            @Query("street") String street,
            @Query("landmark") String landMark,
            @Query("latitude") float latitude,
            @Query("longitude") float longitude

    );

    @DELETE("user")
    Call<Result> deleteAddress(
            @Query("action") String action,
            @Query("id") int id
    );



}
