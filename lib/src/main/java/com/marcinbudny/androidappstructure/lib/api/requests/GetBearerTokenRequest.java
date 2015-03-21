package com.marcinbudny.androidappstructure.lib.api.requests;

import com.marcinbudny.androidappstructure.lib.api.TwitterApi;
import com.marcinbudny.androidappstructure.lib.api.contract.BearerToken;

import net.iharder.Base64;


public class GetBearerTokenRequest extends BaseRequest<BearerToken, TwitterApi> {

    private static final String GRANT_TYPE_CLIENT_CREDENTIALS = "client_credentials";

    private String consumerKey;

    private String consumerSecret;

    public String getConsumerKey() {
        return consumerKey;
    }

    public String getConsumerSecret() {
        return consumerSecret;
    }

    public GetBearerTokenRequest(String consumerKey, String consumerSecret) {
        super(BearerToken.class, TwitterApi.class);
        this.consumerKey = consumerKey;
        this.consumerSecret = consumerSecret;
    }


    @Override
    public BearerToken loadDataFromNetwork() throws Exception {

        return getService().getBearerToken(GRANT_TYPE_CLIENT_CREDENTIALS, formatBasicAuthString());
    }

    private String formatBasicAuthString() {
        String credentials = consumerKey + ":" + consumerSecret;
        String encodedCredentials = Base64.encodeBytes(credentials.getBytes());
        return "Basic " + encodedCredentials;
    }
}
