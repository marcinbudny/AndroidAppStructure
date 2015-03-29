package com.marcinbudny.androidappstructure.lib.api.contract;

import java.util.ArrayList;
import java.util.Collection;

public class Status {
    public String text;
    public User user;

    @Override
    public String toString() {
        return String.format("%s: %s", user.name, text);
    }

    public static class List extends ArrayList<Status> {
        public List(int capacity) {
            super(capacity);
        }

        public List() {
        }

        public List(Collection<? extends Status> collection) {
            super(collection);
        }
    }

}
