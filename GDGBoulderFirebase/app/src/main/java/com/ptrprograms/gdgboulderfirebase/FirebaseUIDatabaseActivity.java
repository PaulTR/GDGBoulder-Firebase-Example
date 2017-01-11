package com.ptrprograms.gdgboulderfirebase;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseUIDatabaseActivity extends AppCompatActivity {

    private static final String URL = "https://gdgboulder-firebase.firebaseio.com/jokes";

    private RecyclerView recyclerView;
    private FirebaseRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_ui_database);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReferenceFromUrl(URL);

        adapter = new FirebaseRecyclerAdapter<Joke, DataHolder>(Joke.class, android.R.layout.two_line_list_item, DataHolder.class, databaseRef) {
            @Override
            public void populateViewHolder(DataHolder viewHolder, Joke joke, int position) {
                viewHolder.setAnswer(joke.getAnswer());
                viewHolder.setQuestion(joke.getQuestion());
            }
        };

        recyclerView.setAdapter(adapter);
    }

    public static class DataHolder extends RecyclerView.ViewHolder {

        private TextView mQuestionTextView;
        private TextView mAnswerTextView;

        public DataHolder(View itemView) {
            super(itemView);

            mQuestionTextView = (TextView) itemView.findViewById(android.R.id.text1);
            mAnswerTextView = (TextView) itemView.findViewById(android.R.id.text2);
        }

        public void setQuestion(String question) {
            mQuestionTextView.setText(question);
        }

        public void setAnswer(String answer) {
            mAnswerTextView.setText(answer);
        }
    }
}
