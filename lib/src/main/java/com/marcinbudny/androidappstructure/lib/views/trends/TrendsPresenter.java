package com.marcinbudny.androidappstructure.lib.views.trends;

import com.marcinbudny.androidappstructure.lib.Settings;
import com.marcinbudny.androidappstructure.lib.api.contract.Trend;
import com.marcinbudny.androidappstructure.lib.logic.AccessTokenStorage;
import com.marcinbudny.androidappstructure.lib.logic.Authenticator;
import com.marcinbudny.androidappstructure.lib.logic.TrendsDownloader;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

public class TrendsPresenter {

    private Bus bus;
    private final Authenticator authenticator;
    private final AccessTokenStorage accessTokenStorage;
    private TrendsDownloader trendsDownloader;
    private TrendsView view;
    private Trend.List trends;

    public TrendsPresenter(
            Bus bus,
            Authenticator authenticator,
            AccessTokenStorage accessTokenStorage,
            TrendsDownloader trendsDownloader) {
        this.bus = bus;
        this.authenticator = authenticator;
        this.accessTokenStorage = accessTokenStorage;
        this.trendsDownloader = trendsDownloader;
    }

    public void setView(TrendsView view) {
        this.view = view;
    }

    public void onViewStarted() {
        bus.register(this);
        getTrendingTags();
    }

    public void onViewStopped() {
        bus.unregister(this);
    }

    private void getTrendingTags() {
        if(!accessTokenStorage.hasAccessToken())
            authenticate();
        else
            downloadTrends();
    }

    private void authenticate() {
        authenticator.authenticateAndStoreToken(Settings.CONSUMER_KEY, Settings.CONSUMER_SECRET);
    }

    @Subscribe
    public void onAuthenticationSuccess(Authenticator.Success success) {
        downloadTrends();
    }

    private void downloadTrends() {
        trendsDownloader.start();
    }

    @Subscribe
    public void onAuthenticationFailure(Authenticator.Failure failure) {
        view.displayAuthenticationError();
    }

    @Subscribe
    public void onTrendsDownloadSuccess(TrendsDownloader.Success success) {
        trends = success.getTrends();
        view.displayTrends(trends);
    }

    @Subscribe
    public void onTrendsDownloadFailure(TrendsDownloader.Failure failure) {
        view.displayTrendsDownloadError();
    }

    public void onTrendSelected(int trendIndex) {
        Trend trend = trends.get(trendIndex);
        view.navigateToStatusesWithQuery(trend.query);
    }
}
