package com.example.tomas.movieapp.Presentation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.example.tomas.movieapp.Domain.Movie;
import com.example.tomas.movieapp.Domain.MovieAdapter;
import com.example.tomas.movieapp.R;
import com.example.tomas.movieapp.Service.MovieRequest;

import java.util.ArrayList;

/**
 * Created by Tomas on 17-6-2017.
 */

public class MovieList extends AppCompatActivity implements AdapterView.OnItemClickListener {

    public final String TAG = this.getClass().getSimpleName();

    // The name for communicating Intents extras
    public final static String FILM = "Filmnaam";

    public static final int MY_REQUEST_CODE = 1234;

    private ListView listViewFilms;
    private ArrayAdapter<Movie> movieAdapter;
    private ArrayList<Movie> movies = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movielist);

        listViewFilms = (ListView) findViewById(R.id.MovieListView);
        listViewFilms.setOnItemClickListener(this);
        movieAdapter = new MovieAdapter(this, getLayoutInflater(),movies);
        listViewFilms.setAdapter(movieAdapter);

        getMovies();


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
       // Log.i(TAG, "Position " + position + " is geselecteerd");

        Movie  movie = movies.get(position);
        Intent intent = new Intent(getApplicationContext(), MovieDetail.class);
        intent.putExtra(FILM, movie);
        startActivity(intent);
    }

    @Override
    public void onMoviesAvailable(ArrayList<Movie> movieArrayList) {

        Log.i(TAG, "We hebben " + movieArrayList.size() + " items in de lijst");

        movies.clear();
        for(int i = 0; i < movieArrayList.size(); i++) {
            movies.add(movieArrayList.get(i));
        }
        movieAdapter.notifyDataSetChanged();
    }

    /**
     * Callback function - handle a single ToDo
     *

     */
    @Override
    public void onMovieAvailable(Movie movie) {
        movies.add(movie);
        movieAdapter.notifyDataSetChanged();
    }


    private void getMovies(){
        MovieRequest request = new MovieRequest(getApplicationContext(), (MovieRequest.MovieListener) this);
        request.handleGetAllMovies();
    }

}
