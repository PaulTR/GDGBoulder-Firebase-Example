package com.ptrprograms.gdgboulderfirebase;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.appinvite.AppInviteInvitation;

public class FirebaseAppInvitesActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_app_invite);
    }

    public void sendAppInvite(View v) {
        Intent intent = new AppInviteInvitation.IntentBuilder("Title")
                .setMessage("Invitation Message")
                .setCustomImage(Uri.parse(getString(R.string.invitation_custom_image)))
                .setCallToActionText("Call to Action")
                .setDeepLink(Uri.parse("deep link URL here"))
                .setOtherPlatformsTargetApplication(
                        AppInviteInvitation.IntentBuilder.PlatformMode.PROJECT_PLATFORM_IOS,
                        "iOS Bundle ID" )
                .build();
        startActivityForResult(intent, 42);
    }
}
