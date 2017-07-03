package com.example.tomas.movieapp.Domain;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.tomas.movieapp.R;

import java.util.ArrayList;

/**
 * Created by Tomas on 19-6-2017.
 */

public class MovieAdapter extends ArrayAdapter<Movie> {
    private Context context;
    private LayoutInflater mInflator;

    public MovieAdapter(Context context, LayoutInflater layoutInflater, ArrayList<Movie> Movies) {
        super(context, 0, Movies);

        this.context = context;
        this.mInflator = layoutInflater;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

       Movie movie = getItem(position);

        if( convertView == null ) {
            convertView = mInflator.inflate(R.layout.movielistrow, parent, false);
        }


        TextView moviename = (TextView) convertView.findViewById(R.id.NameTextView);
        moviename.setText(movie.getMovietitle());




        return convertView;
    }
}
