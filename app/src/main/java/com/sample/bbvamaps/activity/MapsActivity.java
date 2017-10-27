package com.sample.bbvamaps.activity;

import android.Manifest;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sample.bbvamaps.R;
import com.sample.bbvamaps.callback.GetNearbyPlaceDataCallBack;
import com.sample.bbvamaps.callback.LocationUpdateCallBack;
import com.sample.bbvamaps.fragment.NearByPlaceFragment;
import com.sample.bbvamaps.model.PlaceDetails;
import com.sample.bbvamaps.network.GetNearByPlacesTask;
import com.sample.bbvamaps.util.BBVAMapsLog;
import com.sample.bbvamaps.util.CommonUtils;
import com.sample.bbvamaps.util.LocationProvider;

import java.util.List;

public class MapsActivity extends BaseActivity implements OnMapReadyCallback,
        LocationProvider.LocationCallback, GetNearbyPlaceDataCallBack, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private LocationProvider mLocationProvider;
    private double currentLatitude;
    private double currentLongitude;
    private MapFragment mapFragment;
    FragmentManager fragmentManager;
    private ProgressDialog mProgressDialog;
    public static final int PERMISSIONS_REQUEST_LOCATION = 100;

    @Override
    public String getTag() {
        return MapsActivity.class.getCanonicalName();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_maps);
        if (!CheckGooglePlayServices()) {
            showErrorDialog("Google Play not installed in your device!!");
        }
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("One moment please...");
        mProgressDialog.setCancelable(false);
        this.mLocationProvider = new LocationProvider(this, MapsActivity.this, this);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }

        //mProgressDialog.show();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);

        this.mapFragment = new MapFragment();
        this.fragmentManager = getFragmentManager();
        this.fragmentManager.
                beginTransaction().
                replace(R.id.activity_maps, mapFragment, "mapView").
                addToBackStack(null).
                commit();
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!CommonUtils.isNetworkAvailable(this)) {
            showErrorDialog("Please make sure you have proper internet connection!!");
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the MapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
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
        mMap.setOnMarkerClickListener(this);
        mMap.clear();
    }

    @Override
    public void handleNewLocation(Location location) {
        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();
        broadCast("com.sample.bbvamaps.ACTION_LOCATION_UPDATE");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_map:
                final GetNearByPlacesTask getNearByPlacesTask = new GetNearByPlacesTask(this);
                getNearByPlacesTask.execute(CommonUtils.setUpNearbyPlaceUrl(getApplicationContext(), currentLatitude, currentLongitude));
                this.fragmentManager.
                        beginTransaction().
                        replace(R.id.activity_maps, mapFragment, "mapView").
                        addToBackStack(null).
                        commit();
                mapFragment.getMapAsync(this);
                return true;
            case R.id.menu_list:
                this.fragmentManager.
                        beginTransaction().
                        replace(R.id.activity_maps, new NearByPlaceFragment(), "placeList").
                        addToBackStack(null).
                        commit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull String[] permissions, final @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSIONS_REQUEST_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //start location service
                    permissionGranted();
                } else {
                    showErrorDialog("BBVA app requires location permission.Please restart application and allow permission in order to continue.");
                }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopLocationTracking();
    }

    private void startLocationTracking() {
        if (mLocationProvider != null) {
            mLocationProvider.connect();
        }
    }

    private void stopLocationTracking() {
        if (mLocationProvider != null) {
            mLocationProvider.disconnect();
        }
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            //Prompt the user once explanation has been shown
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_LOCATION);


        } else {
            permissionGranted();
        }

    }

    private void permissionGranted() {
        startLocationTracking();
        final GetNearByPlacesTask getNearByPlacesTask = new GetNearByPlacesTask(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getNearByPlacesTask.execute(CommonUtils.setUpNearbyPlaceUrl(getApplicationContext(), currentLatitude, currentLongitude));
            }
        }, 1500);
        mProgressDialog.show();
    }

    private boolean CheckGooglePlayServices() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);
        if (result != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this, result,
                        0).show();
            }
            return false;
        }
        return true;
    }

    private void broadCast(String action) {
        Intent intent = new Intent();
        intent.setAction(action);
        intent.putExtra("com.sample.bbvamaps.CURRENT_LAT", currentLatitude);
        intent.putExtra("com.sample.bbvamaps.CURRENT_LON", currentLongitude);
        sendBroadcast(intent);
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
        for (PlaceDetails placeDetails : placeDetailsList) {
            MarkerOptions markerOptions = new MarkerOptions();
            LatLng latLng = new LatLng(placeDetails.getLat(), placeDetails.getLon());
            markerOptions.position(latLng);
            //displaying rating instead of distance,as The Place API does not return distance or vicinity
            markerOptions.title(placeDetails.getPlaceName() + " : " + placeDetails.getRating());
            mMap.addMarker(markerOptions);
            //Bug in the Marker class,not able display custom icon in place of default icon
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.bbva_bank_map_icon)));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Bundle bundle = new Bundle();
        bundle.putString("place_name", marker.getTitle());
        bundle.putDouble("lat", marker.getPosition().latitude);
        bundle.putDouble("lon", marker.getPosition().longitude);
        Intent intent = new Intent(this, PlaceDetailsActivity.class);
        intent.putExtra("place_details", bundle);
        startActivity(intent);
        return false;
    }
}
