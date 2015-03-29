package com.marcinbudny.androidappstructure.lib.views.statuses;

import com.marcinbudny.androidappstructure.lib.api.contract.Status;

public interface StatusesView {
    void displayStatuses(Status.List statuses);

    void displayDownloadErrorText();
}
