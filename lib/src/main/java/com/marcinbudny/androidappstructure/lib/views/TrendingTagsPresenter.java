package com.marcinbudny.androidappstructure.lib.views;

import com.marcinbudny.androidappstructure.lib.Settings;
import com.marcinbudny.androidappstructure.lib.logic.AccessTokenStorage;
import com.marcinbudny.androidappstructure.lib.logic.Authenticator;

public class TrendingTagsPresenter {

    private final Authenticator authenticator;
    private final AccessTokenStorage accessTokenStorage;

    public TrendingTagsPresenter(Authenticator authenticator, AccessTokenStorage accessTokenStorage) {
        this.authenticator = authenticator;
        this.accessTokenStorage = accessTokenStorage;
    }

    public void onViewStarted() {
        getTrendingTags();
    }

    private void getTrendingTags() {

        if(!accessTokenStorage.hasAccessToken())
            authenticate();
    }

    private void authenticate() {
        authenticator.authenticateAndStoreToken(Settings.CONSUMER_KEY, Settings.CONSUMER_SECRET);
    }

}
