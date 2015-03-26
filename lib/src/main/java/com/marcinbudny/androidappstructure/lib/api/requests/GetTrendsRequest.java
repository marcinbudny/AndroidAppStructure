package com.marcinbudny.androidappstructure.lib.api.requests;

import com.marcinbudny.androidappstructure.lib.api.TwitterApi;
import com.marcinbudny.androidappstructure.lib.api.contract.TrendQueryResult;

public class GetTrendsRequest extends BaseRequest<TrendQueryResult.List, TwitterApi> {


    private int locationId;

    public int getLocationId() {
        return locationId;
    }

    public GetTrendsRequest(int locationId) {
        super(TrendQueryResult.List.class, TwitterApi.class);
        this.locationId = locationId;
    }

    @Override
    public TrendQueryResult.List loadDataFromNetwork() throws Exception {
        return getService().getTrends(locationId);
    }
}
