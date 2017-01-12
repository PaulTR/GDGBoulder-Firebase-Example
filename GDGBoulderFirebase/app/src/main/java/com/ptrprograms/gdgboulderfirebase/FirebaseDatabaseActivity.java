package com.ptrprograms.gdgboulderfirebase;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class FirebaseDatabaseActivity extends Activity {

    private static final String URL = "https://gdgboulder-firebase.firebaseio.com/jokes";

    private ListView listView;
    private ArrayAdapter<Joke> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_database);

        listView = (ListView) findViewById(R.id.list);
        adapter = new JokeListAdapter(this);
        listView.setAdapter(adapter);

        //Use one or the other to test functionality between the two types of listeners
        loadDataThroughValueEventListener();
        //loadDataThroughChildEventListener();


    }

    private void loadDataThroughValueEventListener() {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReferenceFromUrl(URL);

        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override

            public void onDataChange(DataSnapshot dataSnapshot) {

                adapter.clear();
                for( DataSnapshot snapshot : dataSnapshot.getChildren() ) {
                    adapter.add(snapshot.getValue(Joke.class));
                }
                adapter.notifyDataSetChanged();

            }

            @Override

            public void onCancelled(DatabaseError databaseError) {

            }

        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Transaction updates
                DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReferenceFromUrl(URL).child("/" + i);

                databaseRef.runTransaction(new Transaction.Handler() {
                    @Override
                    public Transaction.Result doTransaction(MutableData mutableData) {
                        Joke joke = mutableData.getValue(Joke.class);
                        if( joke == null ) {
                            return Transaction.success(mutableData);
                        }
                        joke.plusOned++;
                        mutableData.setValue(joke);
                        return Transaction.success(mutableData);
                    }
                    @Override
                    public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {}
                });

            }
        });
    }

    private void loadDataThroughChildEventListener() {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReferenceFromUrl(URL);

        Query queryOrderByChild = databaseRef.orderByChild("question");
        queryOrderByChild.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                for( DataSnapshot snapshot : dataSnapshot.getChildren() ) {
                    Log.d("Firebase Database", snapshot.getValue().toString());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d("Firebase Database", "onChildChanged");
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d("Firebase Database", "onChildRemoved");
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.d("Firebase Database", "onChildMoved");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("Firebase Database", "onCancelled");
            }
        });
    }

    public void addJoke(View v) {
        Joke joke = new Joke();
        joke.setQuestion("What do you use to fix a broken tuba?");
        joke.setAnswer("a tuba glue!");

        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReferenceFromUrl(URL);
        databaseRef.child("99").setValue(joke);
    }

    public void updateJoke(View v) {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReferenceFromUrl(URL);

        Joke joke = new Joke("Did you hear about the zoo with only one animal?", "It was a shi-tzu");
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/2", joke.toMap());
        databaseRef.updateChildren(childUpdates);
    }

    public void addUniqueKeyJoke(View v) {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReferenceFromUrl(URL);
        Joke joke = new Joke("Did you hear about the zoo with only one animal?", "It was a shi-tzu");

        String key = databaseRef.push().getKey();
        databaseRef.child(key).setValue(joke);
    }
}
