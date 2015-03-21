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
import com.marcinbudny.androidappstructure.lib.views.TrendingTagsPresenter;
import com.marcinbudny.androidappstructure.views.trending.TrendingTagsActivity;
import com.octo.android.robospice.SpiceManager;
import com.squareup.otto.Bus;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

@Module(library=true, // todo: remove
        injects={
        NetworkService.class,
        TrendingTagsActivity.class
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
    public TrendingTagsPresenter provideTrendingTagsPresenter(Authenticator authenticator, AccessTokenStorage accessTokenStorage) {
        return new TrendingTagsPresenter(authenticator, accessTokenStorage);
    }
}
