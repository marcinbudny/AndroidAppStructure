package com.marcinbudny.androidappstructure.lib.logic;

import com.marcinbudny.androidappstructure.lib.api.contract.Trend;
import com.octo.android.robospice.persistence.exception.SpiceException;

public interface TrendsDownloader {

    void start();

    public class Success {
        private final Trend.List trends;

        public Success(Trend.List trends) {

            this.trends = trends;
        }

        public Trend.List getTrends() {
            return trends;
        }
    }

    public class Failure extends BaseFailureEvent {

        public Failure(SpiceException ex) {
            super(ex);
        }
    }
}
