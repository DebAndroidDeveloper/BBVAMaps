package com.sample.bbvamaps.activity;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.util.Log;

/**
 * Created by Debasis on 10/19/2017.
 */
public abstract class BaseFragmentActivity extends FragmentActivity {

    /**
     * @return class name
     */
    public abstract String getTag();


    public void showErrorDialog(String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Error");
        alertDialog.setMessage(Html.fromHtml(message));
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logMethodName("onCreate()");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        logMethodName("onBackPressed()");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        logMethodName("OnConfigurationChanged()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        logMethodName("onDestroy()");
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        logMethodName("onLowMemory()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        logMethodName("onPause()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        logMethodName("OnResume()");
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        logMethodName("onPostResume()");
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        logMethodName("onResumeFragments()");
    }

    @Override
    protected void onStart() {
        super.onStart();
        logMethodName("onStart()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        logMethodName("onStop()");
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        logMethodName("onAttachFragment()");
    }

    private void logMethodName(String methodName) {
        Log.d(getTag(), ">>>>>>>>>> " + methodName + " in " + getTag() + " <<<<<<<<<<");
    }
}
