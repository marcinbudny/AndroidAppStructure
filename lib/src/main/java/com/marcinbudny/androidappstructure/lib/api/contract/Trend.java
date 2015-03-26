package com.marcinbudny.androidappstructure.lib.api.contract;

import java.util.ArrayList;
import java.util.Collection;

public class Trend  {
    public String name;
    public String query;

    public static class List extends ArrayList<Trend> {
        public List(int capacity) {
            super(capacity);
        }

        public List() {
        }

        public List(Collection<? extends Trend> collection) {
            super(collection);
        }
    }

    @Override
    public String toString() {
        return name;
    }
}
