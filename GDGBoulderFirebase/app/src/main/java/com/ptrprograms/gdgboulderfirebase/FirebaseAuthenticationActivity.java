package com.ptrprograms.gdgboulderfirebase;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.common.Scopes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

public class FirebaseAuthenticationActivity extends Activity {

    private final int SIGN_IN_REQUEST_CODE = 42;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_auth);
        handleAuthenticationThroughProvider();
    }

    private void handleEmailOnlyAuthentication() {
        FirebaseAuth auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            displayUser();
        } else {

            startActivityForResult(
                    AuthUI.getInstance().createSignInIntentBuilder().build(),
                    SIGN_IN_REQUEST_CODE);
        }
    }

    private void handleAuthenticationThroughProvider() {
        FirebaseAuth auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            displayUser();
        } else {

            AuthUI.IdpConfig googleIdp = new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER)
                    .setPermissions(Arrays.asList(Scopes.PROFILE))
                    .build();


            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setProviders(Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                    googleIdp))
                            .build(),
                    SIGN_IN_REQUEST_CODE);
        }
    }

    private void displayUser() {
        FirebaseAuth auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            FirebaseUser user = auth.getCurrentUser();
            if( user.getPhotoUrl() != null ) {
                Glide.with(this).load(user.getPhotoUrl().toString()).into((ImageView) findViewById(R.id.image));
            }

            ((TextView) findViewById(R.id.name)).setText(user.getDisplayName());
            ((TextView) findViewById(R.id.id)).setText(user.getUid());
            ((TextView) findViewById(R.id.email)).setText(user.getEmail());
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SIGN_IN_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "User signed in!", Toast.LENGTH_SHORT).show();
                displayUser();
            } else {
                // Something didn't work out. Let the user know and wait for them to sign in again
            }
        }
    }
}
