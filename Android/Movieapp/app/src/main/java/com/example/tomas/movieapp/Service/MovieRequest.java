package com.example.tomas.movieapp.Service;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.tomas.movieapp.Domain.Movie;
import com.example.tomas.movieapp.Domain.MovieMapper;
import com.example.tomas.movieapp.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tomas on 20-6-2017.
 */

public class MovieRequest {

    private Context context;
    public final String TAG = this.getClass().getSimpleName();

    // De aanroepende class implementeert deze interface.
    private MovieRequest.MovieListener listener;

    /**
     * Constructor
     *
     * @param context
     * @param listener
     */
    public MovieRequest(Context context, MovieRequest.MovieListener listener) {
        this.context = context;
        this.listener = listener;
    }

    /**
     * Verstuur een GET request om alle Movies
     *op te halen.
     */
    public void handleGetAllMovies() {

        Log.i(TAG, "handleGetAllMovies");

        // Haal het token uit de prefs
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        final String token = sharedPref.getString(context.getString(R.string.saved_token), "dummy default token");
        if(token != null && !token.equals("dummy default token")) {

            Log.i(TAG, "Token gevonden, we gaan het request uitvoeren");
            final JsonObjectRequest jsObjRequest = new JsonObjectRequest
                    (Request.Method.GET, Config.URL_FILMS, null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            // Succesvol response
                            Log.i(TAG, response.toString());

                            ArrayList<Movie> result = MovieMapper.movieArrayList(response);
                            listener.onMoviesAvailable(result);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //handleErrorResponse(error);
                            //Log.i(TAG, response.toString());

                            error.printStackTrace();
                        }
                    }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json");
                    headers.put("Authorization", "Bearer " + token);
                    return headers;
                }
            };

            // Access the RequestQueue through your singleton class.
            VolleyRequestQueue.getInstance(context).addToRequestQueue(jsObjRequest);
        }
    }
    // Callback interface - implemented by the calling class
    //
    public interface MovieListener {
        // Callback function to return a fresh list of Movies
        void onMoviesAvailable(ArrayList<Movie> mMovieArray);

        // Callback function to handle a single added Movie.
        void onMovieAvailable(Movie movie);

        //Callback to handle serverside API errors
        //void onMoviesError(String message);
    }

}


