package bruno.ellerbach.tmdbmovies.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import bruno.ellerbach.tmdbmovies.R;
import bruno.ellerbach.tmdbmovies.adapter.MovieRecyclerAdapter;
import bruno.ellerbach.tmdbmovies.loader.MovieLoader;
import bruno.ellerbach.tmdbmovies.model.Movie;

import java.util.ArrayList;

public class MovieListActivity extends AppCompatActivity implements MovieRecyclerAdapter.OnMovieListener {

    // The loader class
    private MovieLoader movieLoader;

    // The RecyclerView
    private RecyclerView moviesRecyclerView;

    // Our custom adapter for the RecyclerView
    private MovieRecyclerAdapter movieRecyclerAdapter;

    // The list downloaded from the API
    private ArrayList<Movie> movieList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        // Instantiating the loader and the list
        movieLoader = MovieLoader.getInstance(this);
        movieList = new ArrayList<>();

        setUpRecycler();
        downloadPopularMovies();
    }

    /**
     * Setting up the recyclerview
     */
    private void setUpRecycler() {
        // Connecting the recyclerview to the view in the layout
        moviesRecyclerView = findViewById(R.id.recycler_main_movie);

        // Creating our custom adapter
        movieRecyclerAdapter = new MovieRecyclerAdapter(this, movieList, this);

        // Setting the adapter to our recyclerview
        moviesRecyclerView.setAdapter(movieRecyclerAdapter);

        // Creating and setting a layout manager.
        // Note that the manager is VERTICAL, thus a vertical list
        LinearLayoutManager layout = new LinearLayoutManager(
                this, RecyclerView.VERTICAL, false);
        moviesRecyclerView.setLayoutManager(layout);

    }

    /**
     * Downloading popular movies, and notifying the adapter when the list is downloaded.
     */
    private void downloadPopularMovies() {
        movieLoader.getPopularMovies(new MovieLoader.TmdbListener<ArrayList<Movie>>() {
            @Override
            public void onMovieDownloaded(ArrayList<Movie> result) {
                movieList = result;

                // Setting the list to the adapter.
                // This will cause the list to be presented in the layout!
                movieRecyclerAdapter.setMovieList(result);
            }

            @Override
            public void onErrorDownloading(String errorMessage) {

            }
        });
    }

    @Override
    public void onMovieClick(int position) {
        Intent movieDetailIntent = new Intent(getBaseContext(), MovieDetailActivity.class);
        movieDetailIntent.putExtra("movie", movieList.get(position));
        startActivity(movieDetailIntent);
    }
}
