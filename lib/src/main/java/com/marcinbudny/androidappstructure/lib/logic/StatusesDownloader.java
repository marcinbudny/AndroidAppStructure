package com.marcinbudny.androidappstructure.lib.logic;

import com.marcinbudny.androidappstructure.lib.api.contract.Status;
import com.octo.android.robospice.persistence.exception.SpiceException;

public interface StatusesDownloader {

    void start(String query);

    public class Success {
        private final Status.List statuses;

        public Success(Status.List statuses) {

            this.statuses = statuses;
        }

        public Status.List getStatuses() {
            return statuses;
        }
    }

    public class Failure extends BaseFailureEvent {

        public Failure(SpiceException ex) {
            super(ex);
        }
    }
}
