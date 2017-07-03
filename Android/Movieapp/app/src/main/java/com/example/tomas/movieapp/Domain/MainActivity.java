package com.example.tomas.movieapp.Domain;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.tomas.movieapp.Presentation.MovieDetail;
import com.example.tomas.movieapp.R;
import com.example.tomas.movieapp.Service.MovieRequest;

import java.util.ArrayList;

/**
 * Created by Tomas on 20-6-2017.
 */

public class MainActivity extends AppCompatActivity implements ListView.OnItemClickListener, MovieRequest.MovieListener{

    private final String TAG = this.getClass().getSimpleName();
    private ArrayList<Movie> mMovieArray = new ArrayList<Movie>();
    private ListView mMovieListView;
    private MovieAdapter mMovieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movielist);

        mMovieListView = (ListView) findViewById(R.id.MovieListView);

        mMovieAdapter = new MovieAdapter(this, getLayoutInflater(),mMovieArray);

        mMovieListView.setAdapter(mMovieAdapter);

        mMovieAdapter.notifyDataSetChanged();

        mMovieListView.setOnItemClickListener(this);

        getMovies();



    }
   
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int i, long l) {

        Intent intent = new Intent(getApplicationContext(), MovieDetail.class);
        Movie movie = (Movie) this.mMovieArray.get(i);
        intent.putExtra("MovieItem",movie);
        startActivity(intent);

    }
    

    public void onMoviesAvailable(ArrayList<Movie> movieArrayList) {

        Log.i(TAG, "We hebben " + movieArrayList.size() + " items in de lijst");

        mMovieArray.clear();
        for(int i = 0; i < movieArrayList.size(); i++) {
            mMovieArray.add(movieArrayList.get(i));
        }
        mMovieAdapter.notifyDataSetChanged();
    }

    public void onMovieAvailable(Movie movie) {
        mMovieArray.add(movie);
        mMovieAdapter.notifyDataSetChanged();
    }

    private void getMovies(){
        MovieRequest request = new MovieRequest(getApplicationContext(), this);
        request.handleGetAllMovies();
    }

}
