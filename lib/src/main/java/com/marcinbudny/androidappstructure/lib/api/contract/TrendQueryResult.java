package com.marcinbudny.androidappstructure.lib.api.contract;

import java.util.ArrayList;
import java.util.Collection;

public class TrendQueryResult {
    public Trend.List trends;

    public static class List extends ArrayList<TrendQueryResult> {
        public List(int capacity) {
            super(capacity);
        }

        public List() {
        }

        public List(Collection<? extends TrendQueryResult> collection) {
            super(collection);
        }
    }
}
