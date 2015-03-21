package com.marcinbudny.androidappstructure.lib.logic;

import com.marcinbudny.androidappstructure.lib.infrastructure.SharedPreferencesOperations;

public class AccessTokenStorageImpl implements AccessTokenStorage {

    public static final String ACCESS_TOKEN = "ACCESS_TOKEN";
    private SharedPreferencesOperations sharedPreferencesOperations;

    public AccessTokenStorageImpl(SharedPreferencesOperations sharedPreferencesOperations) {

        this.sharedPreferencesOperations = sharedPreferencesOperations;
    }

    @Override
    public void setAccessToken(String accessToken) {
        sharedPreferencesOperations.putString(ACCESS_TOKEN, accessToken);
    }

    @Override
    public String getAccessToken() {
        return sharedPreferencesOperations.getString(ACCESS_TOKEN);
    }

    @Override
    public boolean hasAccessToken() {
        return sharedPreferencesOperations.contains(ACCESS_TOKEN);
    }
}
