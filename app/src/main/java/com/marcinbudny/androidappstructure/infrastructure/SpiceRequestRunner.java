package com.marcinbudny.androidappstructure.infrastructure;

import com.marcinbudny.androidappstructure.lib.infrastructure.RequestRunner;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.request.SpiceRequest;
import com.octo.android.robospice.request.listener.RequestListener;

public class SpiceRequestRunner implements RequestRunner {

    private SpiceManager spiceManager;

    public SpiceRequestRunner(SpiceManager spiceManager) {
        this.spiceManager = spiceManager;
    }

    @Override
    public <T> void run(SpiceRequest<T> request, RequestListener<T> requestListener) {
        spiceManager.execute(request, requestListener);
    }
}