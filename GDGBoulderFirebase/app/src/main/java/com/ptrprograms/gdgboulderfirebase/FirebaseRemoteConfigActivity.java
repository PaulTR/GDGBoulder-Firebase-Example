package com.ptrprograms.gdgboulderfirebase;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

public class FirebaseRemoteConfigActivity extends Activity {

    TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote_config);

        //Allow frequent refreshes
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();
        FirebaseRemoteConfig.getInstance().setConfigSettings(configSettings);

        FirebaseRemoteConfig.getInstance().setDefaults(R.xml.remote_config);

        textView = (TextView) findViewById(R.id.text);
        FirebaseRemoteConfig.getInstance().fetch().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if( task.isSuccessful() ) {
                    FirebaseRemoteConfig.getInstance().activateFetched();
                }

                Toast.makeText(getApplicationContext(), FirebaseRemoteConfig.getInstance().getString("gdg_boulder_example"), Toast.LENGTH_SHORT).show();
                textView.setText(FirebaseRemoteConfig.getInstance().getString("gdg_boulder_example"));
            }
        });

    }
}
