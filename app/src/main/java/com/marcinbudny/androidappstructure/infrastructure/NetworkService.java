package com.marcinbudny.androidappstructure.infrastructure;

import android.app.Notification;

import com.octo.android.robospice.retrofit.RetrofitGsonSpiceService;

import javax.inject.Inject;

import retrofit.RestAdapter;

public class NetworkService extends RetrofitGsonSpiceService {

    @Inject
    RestAdapter.Builder builder;

    @Override
    public void onCreate() {
        ThisApplication.inject(this);

        super.onCreate();
    }

    @Override
    protected RestAdapter.Builder createRestAdapterBuilder() {
        return builder;
    }

    @Override
    protected String getServerUrl() {
        return null;
    }

    @Override
    public Notification createDefaultNotification() {
        return null;
    }
}