package bruno.ellerbach.tmdbmovies.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import bruno.ellerbach.tmdbmovies.R;
import bruno.ellerbach.tmdbmovies.model.Genre;

public class GenreRecyclerAdapter extends
        RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context context;
    private ArrayList<Genre> genreList;

    public GenreRecyclerAdapter(Context context, ArrayList<Genre> genreList) {
        this.context = context;
        this.genreList = genreList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_genre_list_item, parent, false);

        return new RecyclerListHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        RecyclerListHolder recyclerListHolder = (RecyclerListHolder) holder;

        Genre genre = genreList.get(position);

        recyclerListHolder.name.setText(genre.getName());
    }

    @Override
    public int getItemCount() {
        return (null != genreList ? genreList.size() : 0);
    }

    public void setGenreList(ArrayList<Genre> genreList) {
        this.genreList = genreList;
        notifyDataSetChanged();
    }

    private class RecyclerListHolder extends RecyclerView.ViewHolder{

        private final TextView name;

        private RecyclerListHolder(View view) {
            super(view);

            name = view.findViewById(R.id.genre_name);
        }
    }
}