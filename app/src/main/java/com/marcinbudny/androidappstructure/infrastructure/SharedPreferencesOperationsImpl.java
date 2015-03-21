package com.marcinbudny.androidappstructure.infrastructure;

import android.content.Context;
import android.content.SharedPreferences;

import com.marcinbudny.androidappstructure.lib.infrastructure.SharedPreferencesOperations;

public class SharedPreferencesOperationsImpl implements SharedPreferencesOperations {

    public static final String PREFERENCES_KEY = "PREFERENCES_KEY";

    private Context applicationContext;

    public SharedPreferencesOperationsImpl(Context applicationContext) {
        this.applicationContext = applicationContext;
    }


    @Override
    public void putString(String key, String value) {
        SharedPreferences.Editor editor = getEditor();
        editor.putString(key, value);
        editor.commit();
    }

    @Override
    public String getString(String key) {
        return getPreferences().getString(key, null);
    }

    @Override
    public boolean contains(String key) {
        return getPreferences().contains(key);
    }

    private SharedPreferences.Editor getEditor() {
        return getPreferences().edit();
    }

    private SharedPreferences getPreferences() {
        return applicationContext.getSharedPreferences(PREFERENCES_KEY, 0);
    }
}