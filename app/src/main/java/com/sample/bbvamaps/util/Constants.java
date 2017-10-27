package com.sample.bbvamaps.util;

public class Constants {

    public static final String PACKAGE_NAME =
            "com.sample.bbvamaps";
    public static final String LOCATION_DATA_EXTRA = PACKAGE_NAME +
            ".LOCATION_DATA_EXTRA";

    public interface IntentExtras {
        String ERROR_NO_NETWORK = "com.sample.bbvamaps.appIntentExtras.ERROR_NO_NETWORK";
        String MESSAGE = "com.sample.bbvamaps.appIntentExtras.MESSAGE";
        String JSON_RESPONSE = "com.sample.bbvamaps.appIntentExtras.JSON_RESPONSE";
        String REQUEST_ID = "com.sample.bbvamaps.appIntentExtras.ID";
    }

    public interface IntentActions {
        String ACTION_ERROR = "com.sample.bbvamaps.appIntentExtras.ACTION_ERROR";
        String ACTION_SUCCESS = "com.sample.bbvamaps.appIntentExtras.ACTION_SUCCESS";
        String ACTION_GET_FULL_ADDRESS = "com.sample.bbvamaps.appIntentExtras.ACTION_GET_FULL_ADDRESS";
    }
}