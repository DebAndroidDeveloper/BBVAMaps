package com.sample.bbvamaps.network;

import android.os.AsyncTask;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sample.bbvamaps.callback.GetNearbyPlaceDataCallBack;
import com.sample.bbvamaps.model.PlaceDetails;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static com.sample.bbvamaps.util.BBVAMapsLog.d;

public class GetNearByPlacesTask extends AsyncTask<Object,String ,String > {
    private static final String TAG = GetNearByPlacesTask.class.getCanonicalName();
    private NetWorkManager netWorkManager;
    //private GoogleMap mMap;
    private GetNearbyPlaceDataCallBack mGetNearbyPlaceDataCallBack;
    List<PlaceDetails> placeDetailsList;

    public GetNearByPlacesTask(GetNearbyPlaceDataCallBack getNearbyPlaceDataCallBack){
        this.netWorkManager = new NetWorkManager();
        this.placeDetailsList = new ArrayList<>();
        this.mGetNearbyPlaceDataCallBack = getNearbyPlaceDataCallBack;
    }

    @Override
    protected String doInBackground(Object... params) {
        String googlePlacesData = null;
        try {
            d(TAG, "doInBackground entered");
            //mMap = (GoogleMap) params[0];
            String url = (String) params[0];
            googlePlacesData = netWorkManager.httpGet(url);
            d(TAG, "doInBackground Exit");
        } catch (Exception e) {
            d(TAG, e.getMessage());
            mGetNearbyPlaceDataCallBack.onHttpResponseError(e);
        }
        return googlePlacesData;
    }

    @Override
    protected void onPostExecute(String result) {
        if(result != null && result.length() > 0) {
            d(TAG, "onPostExecute Entered");
            JSONParser jsonParser = new JSONParser();
            List<HashMap<String, String>> nearbyPlacesList = jsonParser.parseJson(result);
            displayNearbyPlaces(nearbyPlacesList);
            d(TAG, "onPostExecute Exit");
        }
    }

    private void displayNearbyPlaces(List<HashMap<String, String>> nearbyPlacesList) {
        if(!this.placeDetailsList.isEmpty())
            this.placeDetailsList.clear();

        for (int i = 0; i < nearbyPlacesList.size(); i++) {
            d(TAG,"Entered into showing locations");
            HashMap<String, String> googlePlace = nearbyPlacesList.get(i);
            double lat = Double.parseDouble(googlePlace.get("lat"));
            double lng = Double.parseDouble(googlePlace.get("lng"));
            String placeName = googlePlace.get("place_name");
            String vicinity = googlePlace.get("vicinity");
            String rating  = googlePlace.get("rating");
            PlaceDetails placeDetails = new PlaceDetails();
            placeDetails.setPlaceName(placeName);
            //placeDetails.setVicinity(vicinity);
            placeDetails.setLat(lat);
            placeDetails.setLon(lng);
            placeDetails.setRating(rating);
            placeDetailsList.add(placeDetails);
        }
        //avoding sorting according to distance,as the Place API does not return vicinity or distance
        //Collections.sort(this.placeDetailsList);
        mGetNearbyPlaceDataCallBack.onHttpRequestComplete(this.placeDetailsList);
    }

}