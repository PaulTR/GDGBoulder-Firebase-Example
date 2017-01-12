package com.ptrprograms.gdgboulderfirebase;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.analytics.FirebaseAnalytics;

public class FirebaseAnalyticsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_analytics);
    }

    public void setUserProperties(View v) {
        FirebaseAnalytics.getInstance(this).setUserProperty("custom_property", "some custom value");
    }

    public void logEvent(View v) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Analytics Event Button Click");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "button_click");
        FirebaseAnalytics.getInstance(this).logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

    }
}
