package com.marcinbudny.androidappstructure.lib.infrastructure;

import com.octo.android.robospice.request.SpiceRequest;
import com.octo.android.robospice.request.listener.RequestListener;

public interface RequestRunner {
    <T> void run(SpiceRequest<T> request, RequestListener<T> requestListener);
}
