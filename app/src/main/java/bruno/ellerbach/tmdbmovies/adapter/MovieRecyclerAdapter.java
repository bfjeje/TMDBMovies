package bruno.ellerbach.tmdbmovies.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import bruno.ellerbach.tmdbmovies.R;
import bruno.ellerbach.tmdbmovies.model.Movie;

public class MovieRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;

    private ArrayList<Movie> movieList;

    private OnMovieListener mOnMovieListener;

    public MovieRecyclerAdapter(Context context, ArrayList<Movie> movieList, OnMovieListener onMovieListener) {
        this.context = context;
        this.movieList = movieList;
        this.mOnMovieListener = onMovieListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_movie_list_item, parent, false);

        return new RecyclerListHolder(view, mOnMovieListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        RecyclerListHolder recyclerListHolder = (RecyclerListHolder) holder;

        Movie movie = movieList.get(position);

        recyclerListHolder.title.setText(movie.getTitle());
        recyclerListHolder.date.setText(movie.getDate());

        Picasso.with(context).load(movie.getPosterUrl())
                .error(android.R.drawable.alert_dark_frame)
                .placeholder(android.R.drawable.alert_dark_frame)
                .into(recyclerListHolder.poster);
    }

    @Override
    public int getItemCount() {
        return (null != movieList ? movieList.size() : 0);
    }

    public void setMovieList(ArrayList<Movie> movieList) {
        this.movieList = movieList;
        notifyDataSetChanged();
    }

    private class RecyclerListHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView poster;
        private TextView title;
        private TextView date;
        OnMovieListener onMovieListener;

        private RecyclerListHolder(View view, OnMovieListener onMovieListener) {
            super(view);

            poster = view.findViewById(R.id.poster);
            title = view.findViewById(R.id.title);
            date = view.findViewById(R.id.date);
            this.onMovieListener = onMovieListener;

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onMovieListener.onMovieClick(getAdapterPosition());
        }
    }

    public interface OnMovieListener {
        void onMovieClick(int position);
    }
}
