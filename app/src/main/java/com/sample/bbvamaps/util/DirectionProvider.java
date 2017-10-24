package com.sample.bbvamaps.util;

import android.content.Context;

import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.TravelMode;
import com.sample.bbvamaps.R;

import org.joda.time.DateTime;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class DirectionProvider {
    private static final String TAG = DirectionProvider.class.getCanonicalName();
    private Context mContext;

    public DirectionProvider(Context context){
        this.mContext = context;
    }


    public DirectionsResult getDirectionsDetails(String origin, String destination, TravelMode mode) {
        DateTime now = new DateTime();
        try {
            return DirectionsApi.newRequest(getGeoContext())
                    .mode(mode)
                    .origin(origin)
                    .destination(destination)
                    .departureTime(now)
                    .await();
        } catch (ApiException e) {
            e.printStackTrace();
            return null;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private GeoApiContext getGeoContext() {
        GeoApiContext geoApiContext = new GeoApiContext();
        return geoApiContext
                .setQueryRateLimit(3)
                .setApiKey(this.mContext.getString(R.string.google_maps_key))
                .setConnectTimeout(1, TimeUnit.SECONDS)
                .setReadTimeout(1, TimeUnit.SECONDS)
                .setWriteTimeout(1, TimeUnit.SECONDS);
    }
}