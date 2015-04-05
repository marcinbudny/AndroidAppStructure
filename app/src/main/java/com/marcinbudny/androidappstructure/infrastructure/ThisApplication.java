package com.marcinbudny.androidappstructure.infrastructure;

import android.app.Application;

import dagger.ObjectGraph;

public class ThisApplication extends Application {

    private static ObjectGraph graph;
    private static ThisApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
        graph = ObjectGraph.create(new MainModule());
    }

    public static void inject(Object target) {
        graph.inject(target);
    }

    public static ThisApplication getInstance() {
        return instance;
    }
}
