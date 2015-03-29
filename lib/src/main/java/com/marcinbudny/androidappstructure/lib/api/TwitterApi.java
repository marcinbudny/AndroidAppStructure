package com.marcinbudny.androidappstructure.lib.api;

import com.marcinbudny.androidappstructure.lib.api.contract.BearerToken;
import com.marcinbudny.androidappstructure.lib.api.contract.StatusQueryResponse;
import com.marcinbudny.androidappstructure.lib.api.contract.TrendQueryResult;

import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.Query;

public interface TwitterApi {


    @POST("/oauth2/token")
    @FormUrlEncoded
    BearerToken getBearerToken(@Field("grant_type") String grantType, @Header("Authorization") String basicAuthString);

    @GET("/1.1/trends/place.json")
    TrendQueryResult.List getTrends(@Query("id") int locationId);

    @GET("/1.1/search/tweets.json")
    StatusQueryResponse searchStatuses(@Query("q") String query);
}
