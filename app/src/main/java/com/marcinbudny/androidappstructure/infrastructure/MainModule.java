package com.marcinbudny.androidappstructure.infrastructure;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.marcinbudny.androidappstructure.lib.Settings;
import com.marcinbudny.androidappstructure.lib.infrastructure.RequestRunner;
import com.marcinbudny.androidappstructure.lib.infrastructure.SharedPreferencesOperations;
import com.marcinbudny.androidappstructure.lib.logic.AccessTokenStorage;
import com.marcinbudny.androidappstructure.lib.logic.AccessTokenStorageImpl;
import com.marcinbudny.androidappstructure.lib.logic.Authenticator;
import com.marcinbudny.androidappstructure.lib.logic.AuthenticatorImpl;
import com.marcinbudny.androidappstructure.lib.logic.StatusesDownloader;
import com.marcinbudny.androidappstructure.lib.logic.StatusesDownloaderImpl;
import com.marcinbudny.androidappstructure.lib.logic.TrendsDownloader;
import com.marcinbudny.androidappstructure.lib.logic.TrendsDownloaderImpl;
import com.marcinbudny.androidappstructure.lib.views.statuses.StatusesPresenter;
import com.marcinbudny.androidappstructure.lib.views.trends.TrendsPresenter;
import com.marcinbudny.androidappstructure.views.statuses.StatusesActivity;
import com.marcinbudny.androidappstructure.views.trends.TrendsActivity;
import com.octo.android.robospice.SpiceManager;
import com.squareup.otto.Bus;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

@Module(injects={
        NetworkService.class,
        TrendsActivity.class,
        StatusesActivity.class
})
public class MainModule {

    @Provides
    @Named("application")
    public Context provideApplicationContext() {
        return ThisApplication.getInstance();
    }

    @Provides
    @Singleton
    public SpiceManager provideSpiceManager(@Named("application") Context applicationContext) {

        // SpiceManager bound to application context
        SpiceManager spiceManager = new SpiceManager(NetworkService.class);
        spiceManager.start(applicationContext);
        return spiceManager;
    }

    @Provides
    @Singleton
    public RestAdapter.Builder provideRestAdapterBuilder(AccessTokenStorage accessTokenStorage) {


        return new RestAdapter.Builder()
                .setEndpoint(Settings.API_URL)
                .setRequestInterceptor(new AuthenticatingRequestInterceptor(accessTokenStorage))
                .setConverter(new GsonConverter(createGson()))
                .setLogLevel(RestAdapter.LogLevel.FULL);
    }

    private Gson createGson() {
        return new GsonBuilder().create();
    }

    @Provides
    @Singleton
    public Bus provideBus() {
        return new Bus();
    }

    @Provides
    @Singleton
    public SharedPreferencesOperations provideSharedPreferencesOperations(@Named("application") Context applicationContext) {
        return new SharedPreferencesOperationsImpl(applicationContext);
    }

    @Provides
    @Singleton
    public RequestRunner provideRequestRunner(SpiceManager spiceManager) {
        return new SpiceRequestRunner(spiceManager);
    }

    @Provides
    @Singleton
    public AccessTokenStorage accessTokenStorage(SharedPreferencesOperations sharedPreferencesOperations) {
        return new AccessTokenStorageImpl(sharedPreferencesOperations);
    }

    @Provides
    @Singleton
    public Authenticator provideAuthenticator(RequestRunner requestRunner, Bus bus, AccessTokenStorage accessTokenStorage) {
        return new AuthenticatorImpl(requestRunner, bus, accessTokenStorage);
    }

    @Provides
    @Singleton
    public TrendsDownloader provideTrendsDownloader(RequestRunner requestRunner, Bus bus) {
        return new TrendsDownloaderImpl(requestRunner, bus);
    }

    @Provides
    public TrendsPresenter provideTrendsPresenter(
            Bus bus,
            Authenticator authenticator,
            AccessTokenStorage accessTokenStorage,
            TrendsDownloader trendsDownloader) {

        return new TrendsPresenter(bus, authenticator, accessTokenStorage, trendsDownloader);
    }

    @Provides
    @Singleton
    public StatusesDownloader provideStatusesDownloader(RequestRunner requestRunner, Bus bus) {
        return new StatusesDownloaderImpl(requestRunner, bus);
    }

    @Provides
    public StatusesPresenter provideStatusesPresenter(
            Bus bus,
            StatusesDownloader statusesDownloader) {
        return new StatusesPresenter(bus, statusesDownloader);
    }
}
