package com.example.tomas.movieapp.Domain;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Tomas on 20-6-2017.
 */

public class MovieMapper {

    public static final String MOVIE_TITLE = "title";
    public static final String MOVIE_DESCRIPTION = "description";
    public static final String MOVIE_RELEASEYEAR = "release_year";
    public static final String MOVIE_RENTALRATE= "rental_rate";
    public static final String MOVIE_RATING = "rating";
    public static final String MOVIE_LENGTH = "length";
    public static final String MOVIE_RENTALDURATION = "rental_duration";

    /**
     * Map het JSON response op een arraylist en retourneer deze.
     */
    public static ArrayList<Movie> movieArrayList (JSONObject response){

        ArrayList<Movie> result = new ArrayList<>();

        try{
            JSONArray jsonArray = response.getJSONArray("");

            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonProduct = jsonArray.getJSONObject(i);


                Movie movie = new Movie();

                movie.setMovietitle(jsonProduct.getString(MOVIE_TITLE));
                movie.setDescription(jsonProduct.getString(MOVIE_DESCRIPTION));
                movie.setReleaseYear(jsonProduct.getString(MOVIE_RELEASEYEAR));
                movie.setRentalRate(jsonProduct.getDouble(MOVIE_RENTALRATE));
                movie.setRating(jsonProduct.getString(MOVIE_RATING));
                movie.setLength(jsonProduct.getInt(MOVIE_LENGTH));
                movie.setRentalDuration(jsonProduct.getInt(MOVIE_RENTALDURATION));
                Log.i("MovieMapper", "Movie: " + movie.getMovietitle());

                result.add(movie);
            }
        } catch( JSONException ex) {
            Log.e("MovieMapperr", "onPostExecute JSONException " + ex.getLocalizedMessage());
        }
        return result;
    }
}

