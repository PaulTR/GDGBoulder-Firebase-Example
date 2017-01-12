package com.ptrprograms.gdgboulderfirebase;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends ListActivity {

    private String examples[] = {
            "FirebaseDatabaseActivity",
            "FirebaseUIDatabaseActivity",
            "FirebaseAuthenticationActivity",
            "FirebaseStorageActivity",
            "FirebaseUIStorageActivity",
            "FirebaseAnalyticsActivity"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setListAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, examples));
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
