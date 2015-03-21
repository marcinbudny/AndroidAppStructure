package com.marcinbudny.androidappstructure.infrastructure;

import com.marcinbudny.androidappstructure.lib.logic.AccessTokenStorage;

import retrofit.RequestInterceptor;

public class AuthenticatingRequestInterceptor implements RequestInterceptor {

    private AccessTokenStorage accessTokenStorage;

    public AuthenticatingRequestInterceptor(AccessTokenStorage accessTokenStorage) {
        this.accessTokenStorage = accessTokenStorage;
    }


    @Override
    public void intercept(RequestFacade request) {
        if(accessTokenStorage.hasAccessToken())
            request.addHeader("Authorization", "Bearer " + accessTokenStorage.getAccessToken());
    }
}
