package com.sample.bbvamaps.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.ParcelUuid;
import android.util.Log;

import com.sample.bbvamaps.BuildConfig;
import com.sample.bbvamaps.R;

public class CommonUtils {
    private static final String TAG = CommonUtils.class.getCanonicalName();
    private static final String SEARCH_PARAMETER = "BBVA+Compass";

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            Log.d(TAG, " isOnline : True");
            return true;
        } else {
            Log.d(TAG, " isOnline : False");
            return false;
        }
    }

    public static String setUpNearbyPlaceUrl(Context context, double latitude, double longitude){
        StringBuilder googlePlacesUrl = new StringBuilder(BuildConfig.GOOGLE_PLACES_API_BASE_URL);
        googlePlacesUrl.append("=");
        //googlePlacesUrl.append(BuildConfig.SEARCH_QUERY);
        //googlePlacesUrl.append("+");
        googlePlacesUrl.append(SEARCH_PARAMETER);
        googlePlacesUrl.append("&location=" + latitude + "," + longitude);
        googlePlacesUrl.append("&radius=" + BuildConfig.PROXIMITY_RADIUS);
        googlePlacesUrl.append("&key=" + context.getResources().getString(R.string.google_maps_key));
        BBVAMapsLog.d(TAG, googlePlacesUrl.toString());
        return (googlePlacesUrl.toString());
    }
}