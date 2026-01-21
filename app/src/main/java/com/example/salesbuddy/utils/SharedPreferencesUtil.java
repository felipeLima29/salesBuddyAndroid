package com.example.salesbuddy.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtil {
    private static volatile SharedPreferencesUtil _instance;

    Context context;
    SharedPreferences sharedPref;


    public static SharedPreferencesUtil instance(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("Context não pode ser nulo em SharedPreferencesUtil.instance()");
        }

        if (_instance == null) {
            _instance = new SharedPreferencesUtil();
            _instance.configSessionUtils(context.getApplicationContext());
        }
        return _instance;
    }

    public void configSessionUtils(Context context) {
        this.context = context;
        sharedPref = context.getSharedPreferences("AppPreferences", Activity.MODE_PRIVATE);
    }

    public void storeValueString(String key, String value) {
        SharedPreferences.Editor sharedPrefEditor = sharedPref.edit();
        sharedPrefEditor.putString(key, value);
        sharedPrefEditor.commit();
    }

    public void storeValueBoolean(String key, boolean value) {
        SharedPreferences.Editor sharedPrefEditor = sharedPref.edit();
        sharedPrefEditor.putBoolean(key, value);
        sharedPrefEditor.commit();
    }

    public void storeValueInt(String key, int value) {
        SharedPreferences.Editor sharedPrefEditor = sharedPref.edit();
        sharedPrefEditor.putInt(key, value);
        sharedPrefEditor.commit();
    }

    public void storeValueLong(String key, long value) {
        SharedPreferences.Editor sharedPrefEditor = sharedPref.edit();
        sharedPrefEditor.putLong(key, value);
        sharedPrefEditor.commit();
    }

    public long fetchValueLong(String key) {
        return sharedPref.getLong(key, 0);
    }

    public int fetchValueInt(String key) {
        return sharedPref.getInt(key, 0);
    }

    public String fetchValueString(String key) {
        return sharedPref.getString(key, "");
    }


    public String fetchValueString(String key, String valueInitial) {
        return sharedPref.getString(key, valueInitial);
    }

    public boolean fetchValueBoolean(String key, boolean valueInitial) {
        return sharedPref.getBoolean(key, valueInitial);
    }

    public boolean deleteValue(String key) {
        try {
            SharedPreferences.Editor sharedPrefEditor = sharedPref.edit();
            sharedPrefEditor.remove(key);
            sharedPrefEditor.commit();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
