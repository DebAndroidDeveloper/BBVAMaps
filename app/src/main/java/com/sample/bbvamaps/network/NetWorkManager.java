package com.sample.bbvamaps.network;

import com.sample.bbvamaps.util.BBVAMapsLog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class NetWorkManager {
    private static final String TAG = NetWorkManager.class.getCanonicalName();

    public String httpGet(String strUrl) {
        String data = "";
        InputStream iStream = null;
        HttpsURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpsURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            if (urlConnection.getResponseCode() == HttpsURLConnection.HTTP_OK) {
                iStream = urlConnection.getInputStream();

                BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

                StringBuffer sb = new StringBuffer();

                String line = "";
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }

                data = sb.toString();
                BBVAMapsLog.d(TAG, data);
                br.close();
            }else{
                BBVAMapsLog.e(TAG, "HTTP Response returned error code "+ urlConnection.getResponseCode());
            }

        } catch (Exception e) {
            BBVAMapsLog.d(TAG, e.getMessage());
        } finally {
            try {
                if (iStream != null)
                    iStream.close();
            } catch (IOException e) {
                BBVAMapsLog.d(TAG, e.getMessage());
            }
            if (urlConnection != null)
                urlConnection.disconnect();
        }
        return data;
    }
}