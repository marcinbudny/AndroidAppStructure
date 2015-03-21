package com.marcinbudny.androidappstructure.lib.api.contract;

import com.google.gson.annotations.SerializedName;

public class BearerToken {

    @SerializedName("access_token")
    public String accessToken;
}
