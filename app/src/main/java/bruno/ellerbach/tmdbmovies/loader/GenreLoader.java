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

import bruno.ellerbach.tmdbmovies.model.Genre;

public class GenreLoader {

    public interface GenresListener<AnyType> {
        void onGenresDownloaded(AnyType a);

        void onErrorDownloading(String errorMessage);
    }

    private static GenreLoader genreLoader = null;

    private RequestQueue queue;

    private GenreLoader(Context context) {
        queue = Volley.newRequestQueue(context);
    }

    public static synchronized GenreLoader getInstance(Context context) {
        if (genreLoader == null)
            genreLoader = new GenreLoader(context);

        return genreLoader;
    }

    public void getGenres(final GenresListener<ArrayList<Genre>> listener) {

        String url = "https://api.themoviedb.org/3/genre/movie/list";
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
                        ArrayList<Genre> genres = parseGenres(response);

                        listener.onGenresDownloaded(genres);
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

    private ArrayList<Genre> parseGenres(JSONObject jsonObject) {
        try {
            ArrayList<Genre> genres = new ArrayList<>();

            JSONArray array = jsonObject.getJSONArray("genres");

            for (int i = 0; i < array.length(); i++) {

                JSONObject jo = array.getJSONObject(i);

                int id = jo.getInt("id");
                String name = jo.getString("name");


                Genre genre = new Genre(id, name);

                genres.add(genre);
            }

            return genres;
        } catch (JSONException err) {
            return null;
        }
    }
}
