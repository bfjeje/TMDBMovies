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

    private MovieLoader movieLoader;

    private MovieRecyclerAdapter movieRecyclerAdapter;

    private ArrayList<Movie> movieList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        movieLoader = MovieLoader.getInstance(this);
        movieList = new ArrayList<>();

        setUpRecycler();
        downloadUpcomingMovies();
    }

    private void setUpRecycler() {

        RecyclerView moviesRecyclerView = findViewById(R.id.recycler_main_movie);

        movieRecyclerAdapter = new MovieRecyclerAdapter(this, movieList, this);

        moviesRecyclerView.setAdapter(movieRecyclerAdapter);

        LinearLayoutManager layout = new LinearLayoutManager(
                this, RecyclerView.VERTICAL, false);
        moviesRecyclerView.setLayoutManager(layout);

    }


    private void downloadUpcomingMovies() {
        movieLoader.getUpcomingMovies(new MovieLoader.TmdbListener<ArrayList<Movie>>() {
            @Override
            public void onMovieDownloaded(ArrayList<Movie> result) {
                movieList = result;

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
