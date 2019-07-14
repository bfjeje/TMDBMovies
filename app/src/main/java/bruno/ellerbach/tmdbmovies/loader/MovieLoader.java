package bruno.ellerbach.tmdbmovies.loader;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import bruno.ellerbach.tmdbmovies.model.Movie;

public class MovieLoader {

    /**
     * The interface which will be notified when the movies are downloaded
     */
    public interface TmdbListener<AnyType> {
        void onMovieDownloaded(AnyType a);

        void onErrorDownloading(String errorMessage);
    }

    /**
     * Singleton instance of this class
     */
    private static MovieLoader movieLoader = null;

    /**
     * RequestQueue Volley-library
     */
    protected RequestQueue queue;
    /**
     * Activity context
     */
    protected Context context;
    /**
     * API KEY
     */
    protected String apiKey = "db7711b0fbefcf85a150d01dc73fb62d";

    /**
     * Private constructor. Called from getInstance().
     *
     * @param context Activity-context
     * @see #getInstance(Context)
     */
    private MovieLoader(Context context) {
        this.context = context;
        queue = Volley.newRequestQueue(context);
    }

    /**
     * Initalizing a MovieLoader object if null.
     *
     * @param context Activity-context
     * @return Singleton object
     */
    public static synchronized MovieLoader getInstance(Context context) {
        if (movieLoader == null)
            movieLoader = new MovieLoader(context);

        return movieLoader;
    }

    /**
     * Fetching popular movies
     *
     * @param listener listener, fired when downloaded
     */
    public void getPopularMovies(final TmdbListener<ArrayList<Movie>> listener) {

        // This is the base URL where the movies are downloaded from
        String url = "https://api.themoviedb.org/3/movie/upcoming";
        // Append the api key to the url
        Uri uri = Uri.parse(url).buildUpon()
                .appendQueryParameter("api_key", apiKey)
                .appendQueryParameter("language", "pt-BR")
                .build();

        Log.d("loader", uri.toString());


        // Construction a JsonObjectRequest.
        // This means that we expect to receive a JsonObject from the API.
        final JsonObjectRequest json = new JsonObjectRequest(
                Request.Method.GET, uri.toString(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // This indicates that the call was successfull.

                        // Creating an ArrayList with Movie-objects.
                        ArrayList<Movie> movies = parseMovies(response);

                        // Firing our interface-method, returning the movies to the activity
                        listener.onMovieDownloaded(movies);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // This indicates that the call wasn't successful.
                // We'll return a String to our activity
                listener.onErrorDownloading("Error connecting to the API");
            }
        }
        );

        // Adding the response to our requestqueue
        queue.add(json);
    }

    /**
     * Fetching movies from json provided by the TMDB API.
     *
     * @param jsonObject
     * @return ArrayList of movies
     */
    private ArrayList<Movie> parseMovies(JSONObject jsonObject) {
        try {
            // The arraylist with movies
            ArrayList<Movie> movies = new ArrayList<>();

            // Fetching the array of movie results
            JSONArray array = jsonObject.getJSONArray("results");

            // Looping through and creating the Movie objects
            for (int i = 0; i < array.length(); i++) {

                // Getting the current jsonobject
                JSONObject jo = array.getJSONObject(i);

                // Creating the Movie object
                int id = jo.getInt("id");
                String title = jo.getString("title");
                String date = jo.getString("release_date");
                String posterUrl = jo.getString("poster_path");
                String overview = jo.getString("overview");

                ArrayList<Integer> genresList = new ArrayList<>();
                JSONArray genres = jo.getJSONArray("genre_ids");
                for (int j = 0; j < genres.length(); j++) {
                    genresList.add(genres.getInt(j));
                }

                Movie movie = new Movie(id, title, date, posterUrl, overview, genresList);

                movies.add(movie);
            }

            return movies;
        } catch (JSONException err) {
            // Error occurred!
            return null;
        }
    }
}