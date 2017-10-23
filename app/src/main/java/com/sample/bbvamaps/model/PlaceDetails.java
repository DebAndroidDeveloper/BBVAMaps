package com.sample.bbvamaps.model;

import android.support.annotation.NonNull;

public class PlaceDetails implements Comparable<PlaceDetails>{
    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    private double lat;
    private double lon;
    private String placeName;
    private String vicinity;

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    private String rating;

    @Override
    public int compareTo(@NonNull PlaceDetails placeDetails) {
        //return Integer.parseInt(this.vicinity) - Integer.parseInt(placeDetails.getVicinity());
        return 0;
    }
}