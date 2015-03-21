package com.marcinbudny.androidappstructure.lib.logic;

import com.octo.android.robospice.persistence.exception.SpiceException;

public interface Authenticator {
    void authenticateAndStoreToken(String consumerKey, String consumerSecret);

    public static class Success { }
    public static class Failure extends BaseFailureEvent {
        public Failure(SpiceException ex) {
            super(ex);
        }
    }
}
