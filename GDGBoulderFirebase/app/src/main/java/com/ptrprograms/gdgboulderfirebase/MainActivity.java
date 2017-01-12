package com.ptrprograms.gdgboulderfirebase;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.appinvite.AppInvite;
import com.google.android.gms.appinvite.AppInviteInvitationResult;
import com.google.android.gms.appinvite.AppInviteReferral;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;

public class MainActivity extends ListActivity {

    private String examples[] = {
            "FirebaseDatabaseActivity",
            "FirebaseUIDatabaseActivity",
            "FirebaseAuthenticationActivity",
            "FirebaseStorageActivity",
            "FirebaseAnalyticsActivity",
            "FirebaseRemoteConfigActivity",
            "FirebaseAppInvitesActivity"
    };

    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setListAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, examples));

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(AppInvite.API)
                .build();

        boolean autodeeplink = false;

        AppInvite.AppInviteApi.getInvitation(mGoogleApiClient, this, autodeeplink)
                .setResultCallback(
                        new ResultCallback<AppInviteInvitationResult>() {
                            @Override
                            public void onResult(AppInviteInvitationResult result) {
                                if (result.getStatus().isSuccess()) {
                                    Intent intent = result.getInvitationIntent();
                                    String deepLink = AppInviteReferral.getDeepLink(intent);
                                    String invitationId = AppInviteReferral.getInvitationId(intent);

                                    //if autodeeplink is false, can handle manually here
                                    //else the app restarts itself and handles it
                                }
                            }
                        }
                );
    }

    @Override
    protected void onResume() {
        super.onResume();
        if( mGoogleApiClient != null ) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if( mGoogleApiClient != null && mGoogleApiClient.isConnected() ) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    protected void onListItemClick(ListView list, View view, int position,
                                   long id) {
        super.onListItemClick(list, view, position, id);
        String testName = examples[position];
        try {
            Class clazz = Class
                    .forName("com.ptrprograms.gdgboulderfirebase." + testName);
            Intent intent = new Intent(this, clazz);
            startActivity(intent);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
