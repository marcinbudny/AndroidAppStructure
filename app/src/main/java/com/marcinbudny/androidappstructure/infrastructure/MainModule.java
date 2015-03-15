package com.marcinbudny.androidappstructure.infrastructure;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.marcinbudny.androidappstructure.Settings;
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
    public RestAdapter.Builder provideRestAdapterBuilder() {


        return new RestAdapter.Builder()
                .setEndpoint(Settings.API_URL)
                .setConverter(new GsonConverter(createGson()));
    }

    private Gson createGson() {
        return new GsonBuilder().create();
    }

    @Provides
    @Singleton
    public Bus provideBus() {
        return new Bus();
    }

}
