package com.sample.bbvamaps.callback;

import android.content.Intent;

public interface LocationUpdateCallBack {
    void onLocationAvailable(Intent intent);
}