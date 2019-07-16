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

    public interface TmdbListener<AnyType> {
        void onMovieDownloaded(AnyType a);

        void onErrorDownloading(String errorMessage);
    }

    private static MovieLoader movieLoader = null;
    private final RequestQueue queue;

    private MovieLoader(Context context) {
        queue = Volley.newRequestQueue(context);
    }

    public static synchronized MovieLoader getInstance(Context context) {
        if (movieLoader == null)
            movieLoader = new MovieLoader(context);

        return movieLoader;
    }

    public void getUpcomingMovies(final TmdbListener<ArrayList<Movie>> listener) {

        String url = "https://api.themoviedb.org/3/movie/upcoming";

        String apiKey = "db7711b0fbefcf85a150d01dc73fb62d";
        Uri uri = Uri.parse(url).buildUpon()
                .appendQueryParameter("api_key", apiKey)
                .appendQueryParameter("language", "pt-BR")
                .build();

        Log.d("loader", uri.toString());


        final JsonObjectRequest json = new JsonObjectRequest(
                Request.Method.GET, uri.toString(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ArrayList<Movie> movies = parseMovies(response);

                        listener.onMovieDownloaded(movies);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onErrorDownloading("Error connecting to the API");
            }
        }
        );

        queue.add(json);
    }

    private ArrayList<Movie> parseMovies(JSONObject jsonObject) {
        try {
            ArrayList<Movie> movies = new ArrayList<>();

            JSONArray array = jsonObject.getJSONArray("results");

            for (int i = 0; i < array.length(); i++) {

                JSONObject jo = array.getJSONObject(i);

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
            return null;
        }
    }
}