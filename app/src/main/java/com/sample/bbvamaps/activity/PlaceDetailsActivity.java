package com.sample.bbvamaps.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.sample.bbvamaps.R;

public class PlaceDetailsActivity extends BaseActivity {
    private TextView placeName;
    private TextView rating;
    private TextView latitude;
    private TextView longitude;

    @Override
    public String getTag() {
        return PlaceDetailsActivity.class.getCanonicalName();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_details);
        this.placeName = (TextView) findViewById(R.id.txtView_place);
        this.rating = (TextView) findViewById(R.id.txtView_rating);
        this.latitude = (TextView) findViewById(R.id.txtView_lat);
        this.longitude = (TextView) findViewById(R.id.txtView_lon);
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("place_details");
        String placeDetails = bundle.getString("place_name");
        String[] strArr = placeDetails.split(":");
        this.placeName.setText("Place : " + strArr[0]);
        this.rating.setText("Rating : "+strArr[1]);
        this.latitude.setText("Latitude : " + bundle.getDouble("lat"));
        this.longitude.setText("Longitude : " + bundle.getDouble("lon"));

    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
