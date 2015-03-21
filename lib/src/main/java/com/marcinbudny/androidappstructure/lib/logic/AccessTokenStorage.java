package com.marcinbudny.androidappstructure.lib.logic;

public interface AccessTokenStorage {
    void setAccessToken(String accessToken);

    String getAccessToken();

    boolean hasAccessToken();
}
