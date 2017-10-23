package com.sample.bbvamaps.callback;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class LocationUpdateReceiver extends BroadcastReceiver {
    private LocationUpdateCallBack mLocationUpdateCallBack;

    public LocationUpdateReceiver(LocationUpdateCallBack locationUpdateCallBack){
        this.mLocationUpdateCallBack = locationUpdateCallBack;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals("com.sample.bbvamaps.ACTION_LOCATION_UPDATE"))
            this.mLocationUpdateCallBack.onLocationAvailable(intent);
    }
}
