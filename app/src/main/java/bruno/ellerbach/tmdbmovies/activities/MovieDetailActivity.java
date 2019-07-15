package bruno.ellerbach.tmdbmovies.activities;

import android.content.Intent;
import android.os.Bundle;

import com.squareup.picasso.Picasso;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import bruno.ellerbach.tmdbmovies.R;
import bruno.ellerbach.tmdbmovies.adapter.GenreRecyclerAdapter;
import bruno.ellerbach.tmdbmovies.loader.GenreLoader;
import bruno.ellerbach.tmdbmovies.model.Genre;
import bruno.ellerbach.tmdbmovies.model.Movie;

public class MovieDetailActivity extends AppCompatActivity {

    Movie mMovie;
    TextView title, date, overview;
    ImageView poster;
    RecyclerView rvGenres;
    GenreRecyclerAdapter genreRecyclerAdapter;
    ArrayList<Integer> genresListDetail;
    ArrayList<Genre> genresListDownloaded;
    ArrayList<Genre> finalGenreListDisplayed;
    private GenreLoader genreLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        Intent intent = getIntent();
        this.mMovie = intent.getParcelableExtra("movie");

        title = findViewById(R.id.title_detail);
        date = findViewById(R.id.date_detail);
        overview = findViewById(R.id.overview_detail);
        poster = findViewById(R.id.poster_detail);

        genreLoader = GenreLoader.getInstance(this);
        genresListDownloaded = new ArrayList<>();
        finalGenreListDisplayed = new ArrayList<>();

        this.title.setText(mMovie.getTitle());
        this.date.setText(mMovie.getDate());
        this.overview.setText(mMovie.getOverview());
        this.genresListDetail = mMovie.getGenre();

        Picasso.with(getApplicationContext()).load(mMovie.getPosterUrl())
                .error(android.R.drawable.alert_dark_frame)
                .placeholder(android.R.drawable.alert_dark_frame)
                .into(poster);

        setupGenres();
        downloadGenres();

    }

    private void setupGenres() {

        rvGenres = findViewById(R.id.recyclerview_genres);
        genreRecyclerAdapter = new GenreRecyclerAdapter(this, finalGenreListDisplayed);
        rvGenres.setAdapter(genreRecyclerAdapter);

        LinearLayoutManager layout = new LinearLayoutManager(
                this, RecyclerView.HORIZONTAL, false);
        rvGenres.setLayoutManager(layout);
    }

    private void downloadGenres() {
        genreLoader.getGenres(new GenreLoader.GenresListener<ArrayList<Genre>>() {
            @Override
            public void onGenresDownloaded(ArrayList<Genre> result) {
                genresListDownloaded = result;

                for (int i = 0; i < genresListDetail.size(); i++) {
                    for (int j = 0; j < genresListDownloaded.size(); j++) {
                        if (genresListDetail.get(i).equals(genresListDownloaded.get(j).getId())) {
                            finalGenreListDisplayed.add(genresListDownloaded.get(j));
                            break;
                        }
                    }
                }

                genreRecyclerAdapter.setGenreList(finalGenreListDisplayed);
            }

            @Override
            public void onErrorDownloading(String errorMessage) {

            }
        });
    }
}
