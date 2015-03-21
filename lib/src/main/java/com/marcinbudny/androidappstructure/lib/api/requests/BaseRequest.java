package com.marcinbudny.androidappstructure.lib.api.requests;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

public abstract class BaseRequest<T, R> extends RetrofitSpiceRequest<T, R> {

    public BaseRequest(Class<T> clazz, Class<R> retrofitedInterfaceClass) {
        super(clazz, retrofitedInterfaceClass);

        setRetryPolicy(null);
    }


}