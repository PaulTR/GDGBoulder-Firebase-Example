package com.ptrprograms.gdgboulderfirebase;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class JokeListAdapter extends ArrayAdapter<Joke> {

    public JokeListAdapter(Context context) {
        this(context, 0);
    }

    public JokeListAdapter(Context context, int resource) {
        super(context, resource);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if( convertView == null ) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from( getContext() ).inflate( android.R.layout.simple_list_item_2, parent, false );
            holder.questionTextView = (TextView) convertView.findViewById( android.R.id.text1 );
            holder.answerTextView = (TextView) convertView.findViewById( android.R.id.text2 );
            convertView.setTag( holder );
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.questionTextView.setText( getItem( position ).getQuestion() );
        holder.answerTextView.setText( getItem( position ).getAnswer() );

        return convertView;
    }

    public class ViewHolder {
        public TextView questionTextView;
        public TextView answerTextView;
    }
}
