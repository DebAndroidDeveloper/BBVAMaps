package com.sample.bbvamaps.callback;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.sample.bbvamaps.util.Constants;

public class AddressReceiver extends BroadcastReceiver {
    private AddressCallBack mAddressCallBack;

    public AddressReceiver(AddressCallBack addressCallBack){
        this.mAddressCallBack = addressCallBack;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent != null){
            this.mAddressCallBack.onAddressDataAvailable(intent);
        }
    }
}
