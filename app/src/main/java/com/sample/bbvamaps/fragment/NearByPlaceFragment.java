package com.sample.bbvamaps.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.maps.DirectionsApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.TravelMode;
import com.sample.bbvamaps.R;
import com.sample.bbvamaps.adapter.NearbyPlaceListAdapter;
import com.sample.bbvamaps.callback.GetNearbyPlaceDataCallBack;
import com.sample.bbvamaps.callback.LocationUpdateCallBack;
import com.sample.bbvamaps.callback.LocationUpdateReceiver;
import com.sample.bbvamaps.model.PlaceDetails;
import com.sample.bbvamaps.network.GetNearByPlacesTask;
import com.sample.bbvamaps.util.CommonUtils;
import com.sample.bbvamaps.util.LocationProvider;

import org.joda.time.DateTime;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class NearByPlaceFragment extends BaseFragment implements LocationUpdateCallBack,
        GetNearbyPlaceDataCallBack,NearbyPlaceListAdapter.Listener {

    private List<PlaceDetails> nearbyPlaceList;
    private NearbyPlaceListAdapter nearbyPlaceListAdapter;
    private double currentLatitude;
    private double currentLongitude;
    private ProgressDialog mProgressDialog;
    private LocationUpdateReceiver locationUpdateReceiver;
    private RecyclerView nearbyPlaceListView;
    private IntentFilter mFilter;

    public NearByPlaceFragment(){

    }

    @Override
    public String getTagName() {
        return NearByPlaceFragment.class.getCanonicalName();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage("One moment please...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        this.nearbyPlaceList = new ArrayList<>();
        //this.nearbyPlaceListAdapter = new NearbyPlaceListAdapter(getActivity(),this.nearbyPlaceList,this);
        this.locationUpdateReceiver = new LocationUpdateReceiver(this);
        mFilter = new IntentFilter();
        mFilter.addAction("com.sample.bbvamaps.ACTION_LOCATION_UPDATE");
        /*GetNearByPlacesTask getNearByPlacesTask = new GetNearByPlacesTask(this);
        getNearByPlacesTask.execute(CommonUtils.setUpNearbyPlaceUrl(getActivity(),this.currentLatitude,this.currentLongitude));*/
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View nearbyListView = inflater.inflate(R.layout.layout_fragment_nearby_place,container,false);
        nearbyPlaceListView = (RecyclerView)nearbyListView.findViewById(R.id.nearby_place_listView) ;
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        nearbyPlaceListView.setLayoutManager(layoutManager);
        //nearbyPlaceListView.setAdapter(this.nearbyPlaceListAdapter);
        return nearbyListView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(locationUpdateReceiver,mFilter);
        final GetNearByPlacesTask getNearByPlacesTask = new GetNearByPlacesTask(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getNearByPlacesTask.execute(CommonUtils.setUpNearbyPlaceUrl(getActivity(),currentLatitude,currentLongitude));
            }
        },1500);
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(locationUpdateReceiver);
    }

    @Override
    public void onHttpResponseError(Throwable exception) {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
        showErrorDialog(exception.getMessage());
    }

    @Override
    public void onHttpRequestComplete(List<PlaceDetails> placeDetailsList) {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
        //this.nearbyPlaceList = placeDetailsList;
        populatePlaceListWithDistance(placeDetailsList);
        //sort the locations according to distance
        Collections.sort(this.nearbyPlaceList,new SortNearByPlaceList());
        this.nearbyPlaceListAdapter = new NearbyPlaceListAdapter(getActivity(),this.nearbyPlaceList,this);
        nearbyPlaceListView.setAdapter(this.nearbyPlaceListAdapter);
        this.nearbyPlaceListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRowItemClicked(@NonNull PlaceDetails placeDetails) {
        //TODO:Show details and directions
    }

    @Override
    public void onLocationAvailable(Intent intent) {
        currentLatitude = intent.getDoubleExtra("com.sample.bbvamaps.CURRENT_LAT",0.0);
        currentLongitude = intent.getDoubleExtra("com.sample.bbvamaps.CURRENT_LON",0.0);
    }

    /**
     *
     * @param destLat
     * @param destLong
     * @return distance between 2 locations in miles
     */
    private double getDistanceBetweenLocations(double destLat,double destLong){
        Location loc1 = new Location("");
        loc1.setLatitude(currentLatitude);
        loc1.setLongitude(currentLongitude);

        Location loc2 = new Location("");
        loc2.setLatitude(destLat);
        loc2.setLongitude(destLong);
        float distanceInMeters = loc1.distanceTo(loc2);
        double distance = distanceInMeters * 0.000621371;
        return distance;
    }

    private void populatePlaceListWithDistance(List<PlaceDetails> placeList){
        if(!this.nearbyPlaceList.isEmpty())
            this.nearbyPlaceList.clear();

        for(PlaceDetails placeDetails : placeList){
            double distance = getDistanceBetweenLocations(placeDetails.getLat(),placeDetails.getLon());
            placeDetails.setVicinity(distance);
            this.nearbyPlaceList.add(placeDetails);
        }
    }

    class SortNearByPlaceList implements Comparator<PlaceDetails>{

        @Override
        public int compare(PlaceDetails placeDetails1, PlaceDetails placeDetails2) {
            double diff = placeDetails1.getVicinity() - placeDetails2.getVicinity();
            return Double.valueOf(diff).intValue();
        }
    }
}