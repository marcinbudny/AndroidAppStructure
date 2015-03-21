package com.marcinbudny.androidappstructure.lib.api;

import com.marcinbudny.androidappstructure.lib.api.contract.BearerToken;

import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.Header;
import retrofit.http.POST;

public interface TwitterApi {


    @POST("/oauth2/token")
    @FormUrlEncoded
    BearerToken getBearerToken(@Field("grant_type") String grantType, @Header("Authorization") String basicAuthString);

}
