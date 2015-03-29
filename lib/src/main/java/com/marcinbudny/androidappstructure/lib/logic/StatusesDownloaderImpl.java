package com.marcinbudny.androidappstructure.lib.logic;

import com.marcinbudny.androidappstructure.lib.api.contract.StatusQueryResponse;
import com.marcinbudny.androidappstructure.lib.api.requests.SearchStatusesRequest;
import com.marcinbudny.androidappstructure.lib.infrastructure.RequestRunner;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.squareup.otto.Bus;

public class StatusesDownloaderImpl implements StatusesDownloader {

    private RequestRunner requestRunner;
    private Bus bus;

    public StatusesDownloaderImpl(RequestRunner requestRunner, Bus bus) {
        this.requestRunner = requestRunner;
        this.bus = bus;
    }

    @Override
    public void start(String query) {

        SearchStatusesRequest request = new SearchStatusesRequest(query);
        requestRunner.run(request, new RequestListener<StatusQueryResponse>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                bus.post(new Failure(spiceException));
            }

            @Override
            public void onRequestSuccess(StatusQueryResponse statusQueryResonse) {
                bus.post(new Success(statusQueryResonse.statuses));
            }
        });
    }
}
