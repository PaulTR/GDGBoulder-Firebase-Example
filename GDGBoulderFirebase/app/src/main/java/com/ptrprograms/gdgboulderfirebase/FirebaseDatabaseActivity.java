package com.ptrprograms.gdgboulderfirebase;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
    private Button addJokeButton;
    private Button updateJokeButton;
    private Button addUniqueKeyJokeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_database);

        listView = (ListView) findViewById(R.id.list);
        addJokeButton = (Button) findViewById(R.id.add_joke);
        updateJokeButton = (Button) findViewById(R.id.update_joke);
        addUniqueKeyJokeButton = (Button) findViewById(R.id.add_unique_key);

        addJokeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Joke joke = new Joke();
                joke.setQuestion("What do you use to fix a broken tuba?");
                joke.setAnswer("a tuba glue!");

                DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReferenceFromUrl(URL);
                databaseRef.child("99").setValue(joke);

            }
        });

        updateJokeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReferenceFromUrl(URL);

                Joke joke = new Joke("Did you hear about the zoo with only one animal?", "It was a shi-tzu");
                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("/2", joke.toMap());
                databaseRef.updateChildren(childUpdates);
            }
        });

        addUniqueKeyJokeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReferenceFromUrl(URL);
                Joke joke = new Joke("Did you hear about the zoo with only one animal?", "It was a shi-tzu");

                String key = databaseRef.push().getKey();
                databaseRef.child(key).setValue(joke);
            }
        });

        updateJokeButton = (Button) findViewById(R.id.update_joke);

        adapter = new JokeListAdapter(this);
        listView.setAdapter(adapter);

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
                    Log.e("Example", snapshot.getValue().toString());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });


    }
}
