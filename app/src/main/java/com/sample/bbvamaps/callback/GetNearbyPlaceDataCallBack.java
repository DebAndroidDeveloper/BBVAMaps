package com.sample.bbvamaps.callback;

import com.sample.bbvamaps.model.PlaceDetails;

import java.util.List;

public interface GetNearbyPlaceDataCallBack {
    void onHttpResponseError(Throwable exception);

    void onHttpRequestComplete(List<PlaceDetails> placeDetailsList);
}