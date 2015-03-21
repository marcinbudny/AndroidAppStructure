package com.marcinbudny.androidappstructure.testutils;

import com.marcinbudny.androidappstructure.lib.infrastructure.SharedPreferencesOperations;

import java.util.HashMap;

public class MockSharedPreferencesOperations implements SharedPreferencesOperations {

    private HashMap<String, String> store = new HashMap<>();

    @Override
    public void putString(String key, String value) {
        store.put(key, value);
    }

    @Override
    public String getString(String key) {
        if(!store.containsKey(key))
            return null;

        return store.get(key);
    }

    @Override
    public boolean contains(String key) {
        return store.containsKey(key);
    }
}
