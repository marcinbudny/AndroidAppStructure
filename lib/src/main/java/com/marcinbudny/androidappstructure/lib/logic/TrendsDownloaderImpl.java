package com.marcinbudny.androidappstructure.lib.logic;

import com.marcinbudny.androidappstructure.lib.api.contract.Trend;
import com.marcinbudny.androidappstructure.lib.api.contract.TrendQueryResult;
import com.marcinbudny.androidappstructure.lib.api.requests.GetTrendsRequest;
import com.marcinbudny.androidappstructure.lib.infrastructure.RequestRunner;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.squareup.otto.Bus;

public class TrendsDownloaderImpl implements TrendsDownloader {

    private final static int LOCATION_ID = 1;

    private final RequestRunner requestRunner;
    private final Bus bus;

    public TrendsDownloaderImpl(RequestRunner requestRunner, Bus bus) {
        this.requestRunner = requestRunner;
        this.bus = bus;
    }

    @Override
    public void start() {
        GetTrendsRequest request = new GetTrendsRequest(LOCATION_ID);

        requestRunner.run(request, new RequestListener<TrendQueryResult.List>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                bus.post(new Failure(spiceException));
            }

            @Override
            public void onRequestSuccess(TrendQueryResult.List trendQueryResults) {
                Trend.List trends = extractFirstTrends(trendQueryResults);
                bus.post(new Success(trends));
            }
        });
    }

    private Trend.List extractFirstTrends(TrendQueryResult.List trendQueryResults) {
        if(trendQueryResults.size() > 0) {
            return trendQueryResults.get(0).trends;
        }
        return new Trend.List();
    }
}
