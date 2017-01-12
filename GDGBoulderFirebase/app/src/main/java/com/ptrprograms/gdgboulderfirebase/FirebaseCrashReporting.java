package com.ptrprograms.gdgboulderfirebase;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.crash.FirebaseCrash;

import java.io.IOException;

public class FirebaseCrashReporting extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_crash_reporting);
    }

    public void forceException(View v) {
        try {
            throw new IOException("Bad URL example exception");
        } catch( IOException e ) {
            FirebaseCrash.report(e);
        }
    }

    public void saveLog(View v) {
        FirebaseCrash.log("MainActivity: some important log message");
    }
}
