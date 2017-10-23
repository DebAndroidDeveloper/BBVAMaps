package com.sample.bbvamaps.network;

import com.sample.bbvamaps.util.BBVAMapsLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JSONParser {
    private static final String TAG = JSONParser.class.getCanonicalName();
    public List<HashMap<String, String>> parseJson(String jsonData) {
        JSONArray jsonArray = null;
        JSONObject jsonObject;

        try {
            BBVAMapsLog.d(TAG, "parse");
            jsonObject = new JSONObject(jsonData);
            jsonArray = jsonObject.getJSONArray("results");
        } catch (JSONException e) {
            BBVAMapsLog.e(TAG, e.getMessage());
            e.printStackTrace();
        }
        return getPlaces(jsonArray);
    }

    private List<HashMap<String, String>> getPlaces(JSONArray jsonArray) {
        int placesCount = jsonArray.length();
        List<HashMap<String, String>> placesList = new ArrayList<>();
        HashMap<String, String> placeMap;
        BBVAMapsLog.d(TAG, "getPlaces");

        for (int i = 0; i < placesCount; i++) {
            try {
                placeMap = getPlace((JSONObject) jsonArray.get(i));
                placesList.add(placeMap);
                BBVAMapsLog.d(TAG, "Adding places");

            } catch (JSONException e) {
                BBVAMapsLog.e(TAG, "Error in Adding places "+ e.getMessage());
                e.printStackTrace();
            }
        }
        return placesList;
    }

    private HashMap<String, String> getPlace(JSONObject googlePlaceJson) {
        HashMap<String, String> googlePlaceMap = new HashMap<String, String>();
        String placeName = "-NA-";
        String vicinity = "-NA-";
        String latitude = "";
        String longitude = "";
        String reference = "";
        String rating = "";
        BBVAMapsLog.d(TAG, "Entered");

        try {
            if (!googlePlaceJson.isNull("formatted_address")) {
                placeName = googlePlaceJson.getString("formatted_address");
            }
            if (!googlePlaceJson.isNull("vicinity")) {
                vicinity = googlePlaceJson.getString("vicinity");
            }
            if(!googlePlaceJson.isNull("rating")){
                rating = googlePlaceJson.getString("rating");
            }
            latitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lat");
            longitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lng");
            reference = googlePlaceJson.getString("reference");
            googlePlaceMap.put("place_name", placeName);
            googlePlaceMap.put("vicinity", vicinity);
            googlePlaceMap.put("lat", latitude);
            googlePlaceMap.put("lng", longitude);
            googlePlaceMap.put("reference", reference);
            googlePlaceMap.put("rating",rating);
            BBVAMapsLog.d(TAG, "Putting Places");
        } catch (JSONException e) {
            BBVAMapsLog.e(TAG, e.getMessage());
            e.printStackTrace();
        }
        return googlePlaceMap;
    }
}