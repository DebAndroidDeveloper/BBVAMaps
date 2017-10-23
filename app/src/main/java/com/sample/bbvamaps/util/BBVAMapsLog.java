package com.sample.bbvamaps.util;

import android.util.Log;

import com.sample.bbvamaps.BuildConfig;

public class BBVAMapsLog {
    public static void e(String TAG, String MESSAGE) {
        //if (BuildConfig.LOGGING_ENABLED)
        Log.e(TAG,MESSAGE);
    }

    public static void d(String TAG, String MESSAGE) {
        if (BuildConfig.LOGGING_ENABLED)
            Log.d(TAG,MESSAGE);
    }

    public static void i(String TAG, String MESSAGE) {
        if (BuildConfig.LOGGING_ENABLED)
            Log.i(TAG,MESSAGE);
    }

    public static void v(String TAG, String MESSAGE) {
        if (BuildConfig.LOGGING_ENABLED)
            Log.v(TAG,MESSAGE);
    }

    public static void w(String TAG, String MESSAGE) {
        if (BuildConfig.LOGGING_ENABLED)
            Log.w(TAG,MESSAGE);
    }
} 