package com.marcinbudny.androidappstructure.testutils;

import com.marcinbudny.androidappstructure.lib.api.contract.Status;
import com.marcinbudny.androidappstructure.lib.api.contract.StatusQueryResponse;
import com.marcinbudny.androidappstructure.lib.api.contract.User;

public class StatusQueryResponseBuilder {

    private Status.List statuses = new Status.List();

    public StatusQueryResponseBuilder withStatus(String userName, String text) {
        Status status = new Status();
        status.text = text;
        status.user = new User();
        status.user.name = userName;

        statuses.add(status);

        return this;
    }

    public StatusQueryResponse build() {
        StatusQueryResponse result = new StatusQueryResponse();
        result.statuses = statuses;
        return result;
    }
}
