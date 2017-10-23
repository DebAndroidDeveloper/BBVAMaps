package com.sample.bbvamaps.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;

/**
 * Created by Debasis on 10/19/2017.
 */
public abstract class BaseActivity extends AppCompatActivity {

    /**
     * @return class name
     */
    public abstract String getTag();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logMethodName("OnCreate()");
    }

    @Override
    protected void onStart() {
        super.onStart();
        logMethodName("onStart()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        logMethodName("OnRestart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        logMethodName("OnResume()");
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        logMethodName("OnPostResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        logMethodName("OnPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        logMethodName("OnStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        logMethodName("OnDestroy()");
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        logMethodName("onLowMemory()");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        logMethodName("OnBackPressed()");
    }

    private void logMethodName(String methodName) {
        Log.d(getTag(), ">>>>>>>>>> " + methodName + " in " + getTag() + " <<<<<<<<<<");
    }

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
}
