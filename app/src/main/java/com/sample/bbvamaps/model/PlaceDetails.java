package com.sample.bbvamaps.model;

import android.support.annotation.NonNull;

public class PlaceDetails{
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

    public double getVicinity() {
        return vicinity;
    }

    public void setVicinity(double vicinity) {
        this.vicinity = vicinity;
    }

    private double lat;
    private double lon;
    private String placeName;
    private double vicinity;

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    private String rating;
}