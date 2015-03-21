package com.marcinbudny.androidappstructure.lib.infrastructure;

public interface SharedPreferencesOperations {

    void putString(String key, String value);

    String getString(String key);

    boolean contains(String key);
}
