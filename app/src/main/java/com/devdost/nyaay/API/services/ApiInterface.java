package com.devdost.nyaay.API.services;

import com.devdost.nyaay.API.models.responses.ReportRes;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("check.json")
    Call<ReportRes> checkReport(@Query("models") String models, @Query("api_user") String api_user, @Query("api_secret") String api_secret, @Query("url") String url);

}