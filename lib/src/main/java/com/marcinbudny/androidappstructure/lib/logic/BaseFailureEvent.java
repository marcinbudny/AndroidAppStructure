package com.marcinbudny.androidappstructure.lib.logic;

import com.octo.android.robospice.persistence.exception.SpiceException;

public class BaseFailureEvent {

    private SpiceException spiceException;

    public BaseFailureEvent(SpiceException ex) {

        if(ex == null)
            throw new IllegalArgumentException("ex cannot be null");

        this.spiceException = ex;
    }

    public SpiceException getSpiceException() {
        return spiceException;
    }

}
