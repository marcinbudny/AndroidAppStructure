package com.marcinbudny.androidappstructure.lib.api.requests;

import com.marcinbudny.androidappstructure.lib.api.TwitterApi;
import com.marcinbudny.androidappstructure.lib.api.contract.StatusQueryResponse;

public class SearchStatusesRequest extends BaseRequest<StatusQueryResponse, TwitterApi> {


    private String query;

    public String getQuery() {
        return query;
    }

    public SearchStatusesRequest(String query) {
        super(StatusQueryResponse.class, TwitterApi.class);
        this.query = query;
    }

    @Override
    public StatusQueryResponse loadDataFromNetwork() throws Exception {
        return getService().searchStatuses(query);
    }
}
