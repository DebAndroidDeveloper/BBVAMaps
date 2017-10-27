package com.sample.bbvamaps.activity;

import android.Manifest;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.TravelMode;
import com.sample.bbvamaps.R;
import com.sample.bbvamaps.callback.AddressCallBack;
import com.sample.bbvamaps.callback.AddressReceiver;
import com.sample.bbvamaps.network.FetchAddressIntentService;
import com.sample.bbvamaps.util.CommonUtils;
import com.sample.bbvamaps.util.Constants;
import com.sample.bbvamaps.util.DirectionProvider;

import java.util.List;

public class DirectionActivity extends BaseActivity implements OnMapReadyCallback,AddressCallBack {

    private GoogleMap mMap;
    private MapFragment mapFragment;
    private ProgressDialog mProgressDialog;
    private String destinationAddress;
    private DirectionProvider directionProvider;
    private AddressReceiver addressReceiver;
    private IntentFilter mFilter;
    private int overview = 0;

    @Override
    public String getTag() {
        return DirectionActivity.class.getCanonicalName();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direction);

        this.mapFragment = new MapFragment();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.
                beginTransaction().
                replace(R.id.activity_direction, mapFragment, "mapView").
                addToBackStack(null).
                commit();
        mapFragment.getMapAsync(this);
        Intent intent = getIntent();
        Location currentLocation = new Location("");
        currentLocation.setLatitude(intent.getDoubleExtra("current_lat",0.0));
        currentLocation.setLongitude(intent.getDoubleExtra("current_lon",0.0));
        FetchAddressIntentService.startFetchAddressIntentService(this,currentLocation);
        this.addressReceiver = new AddressReceiver(this);
        mFilter = new IntentFilter();
        mFilter.addAction(Constants.IntentActions.ACTION_SUCCESS);
        mFilter.addAction(Constants.IntentActions.ACTION_ERROR);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("One moment please...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        this.destinationAddress = intent.getStringExtra("dest_address");
        this.directionProvider = new DirectionProvider(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!CommonUtils.isNetworkAvailable(this)) {
            showErrorDialog("Please make sure you have proper internet connection!!");
        }

        this.registerReceiver(addressReceiver,mFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.unregisterReceiver(addressReceiver);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        setupGoogleMapScreenSettings();
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);
            }
        } else {
            mMap.setMyLocationEnabled(true);
        }
        mMap.clear();
    }

    private void setupGoogleMapScreenSettings() {
        mMap.setBuildingsEnabled(true);
        mMap.setIndoorEnabled(true);
        mMap.setTrafficEnabled(true);
        UiSettings mUiSettings = mMap.getUiSettings();
        mUiSettings.setZoomControlsEnabled(true);
        mUiSettings.setCompassEnabled(true);
        mUiSettings.setMyLocationButtonEnabled(true);
        mUiSettings.setScrollGesturesEnabled(true);
        mUiSettings.setZoomGesturesEnabled(true);
        mUiSettings.setTiltGesturesEnabled(true);
        mUiSettings.setRotateGesturesEnabled(true);
    }

    @Override
    public void onAddressDataAvailable(Intent intent) {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }

        if(intent.getAction().equals(Constants.IntentActions.ACTION_SUCCESS)){
            String currentFullAddress = intent.getStringExtra(Constants.IntentExtras.MESSAGE);
            DirectionsResult results = this.directionProvider.getDirectionsDetails(currentFullAddress,this.destinationAddress, TravelMode.DRIVING);
            if (results != null) {
                addPolyline(results, mMap);
                positionCamera(results.routes[overview], mMap);
                addMarkersToMap(results, mMap);
            }
        }else if(intent.getAction().equals(Constants.IntentActions.ACTION_ERROR)){
            showErrorDialog(intent.getStringExtra(Constants.IntentExtras.MESSAGE));
        }
    }

    private void addMarkersToMap(DirectionsResult results, GoogleMap mMap) {
        mMap.addMarker(new MarkerOptions().position(new LatLng(results.routes[overview].legs[overview].startLocation.lat,results.routes[overview].legs[overview].startLocation.lng)).title(results.routes[overview].legs[overview].startAddress));
        mMap.addMarker(new MarkerOptions().position(new LatLng(results.routes[overview].legs[overview].endLocation.lat,results.routes[overview].legs[overview].endLocation.lng)).title(results.routes[overview].legs[overview].startAddress).snippet(getEndLocationTitle(results)));
    }

    private void positionCamera(DirectionsRoute route, GoogleMap mMap) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(route.legs[overview].startLocation.lat, route.legs[overview].startLocation.lng), 12));
    }

    private void addPolyline(DirectionsResult results, GoogleMap mMap) {
        List<LatLng> decodedPath = PolyUtil.decode(results.routes[overview].overviewPolyline.getEncodedPath());
        mMap.addPolyline(new PolylineOptions().addAll(decodedPath));
    }

    private String getEndLocationTitle(DirectionsResult results){
        return  "Time :"+ results.routes[overview].legs[overview].duration.humanReadable + " Distance :" + results.routes[overview].legs[overview].distance.humanReadable;
    }

}
