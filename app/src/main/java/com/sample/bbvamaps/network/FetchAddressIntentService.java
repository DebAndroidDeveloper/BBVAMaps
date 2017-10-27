package com.sample.bbvamaps.network;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.text.TextUtils;

import com.sample.bbvamaps.R;
import com.sample.bbvamaps.util.BBVAMapsLog;
import com.sample.bbvamaps.util.CommonUtils;
import com.sample.bbvamaps.util.Constants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Exchanger;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class FetchAddressIntentService extends IntentService {
    private static final String TAG = FetchAddressIntentService.class.getCanonicalName();
    private static Context mContext;

    public FetchAddressIntentService() {
        super("FetchAddressIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();

            // Get the location passed to this service through an extra.
            Location location = intent.getParcelableExtra(
                    Constants.LOCATION_DATA_EXTRA);
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());


            List<Address> addresses = null;

            try {
                addresses = geocoder.getFromLocation(
                        location.getLatitude(),
                        location.getLongitude(),
                        // get just a single address.
                        1);
                if(addresses != null && addresses.size() > 0){
                    Address address = addresses.get(0);
                    List<String> fullAddressList = new ArrayList<>();

                    // Fetch the address lines using getAddressLine,
                    // join them, and send them to the thread.
                    for(int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                        fullAddressList.add(address.getAddressLine(i));
                    }

                    String fullyFormattedAddress = TextUtils.join(System.getProperty("line.separator"),fullAddressList);
                    BBVAMapsLog.i(TAG, "Address found "+fullyFormattedAddress);
                    deliverAddressToReceiver(Constants.IntentActions.ACTION_SUCCESS,fullyFormattedAddress);
                }
            } catch (IOException ioException) {
                // Catch network or other I/O problems.
                BBVAMapsLog.e(TAG, ioException.getMessage());
            } catch (IllegalArgumentException illegalArgumentException) {
                // Catch invalid latitude or longitude values.
                BBVAMapsLog.e(TAG, illegalArgumentException.getMessage() + ". " +
                        "Latitude = " + location.getLatitude() +
                        ", Longitude = " +
                        location.getLongitude());
            }catch (Exception ex){
                BBVAMapsLog.e(TAG,ex.getMessage());
            }
        }
    }

    private void deliverAddressToReceiver(String action,String fullyFormattedAddress){
        Intent intent = new Intent();
        intent.setAction(action);
        intent.putExtra(Constants.IntentExtras.MESSAGE,fullyFormattedAddress);
        mContext.sendBroadcast(intent);
    }

    public static void startFetchAddressIntentService(Context context, Location currentLocation) {
        mContext = context;
        if (CommonUtils.isNetworkAvailable(context)) {
            Intent intent = new Intent(context,FetchAddressIntentService.class);
            intent.setAction(Constants.IntentActions.ACTION_GET_FULL_ADDRESS);
            intent.putExtra(Constants.LOCATION_DATA_EXTRA, currentLocation);
            context.startService(intent);
        } else {
            Intent errIntent = new Intent();
            errIntent.setAction(Constants.IntentActions.ACTION_ERROR);
            errIntent.putExtra(Constants.IntentExtras.MESSAGE, Constants.IntentExtras.ERROR_NO_NETWORK);
            context.sendBroadcast(errIntent);
        }
    }
}
