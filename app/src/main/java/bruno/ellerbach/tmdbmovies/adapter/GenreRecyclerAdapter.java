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

    // The activity context
    private Context context;
    // The ArrayList of genres in the RecyclerView
    private ArrayList<Genre> genreList;

    /**
     * Constructor.
     *
     * @param context   Activity context
     * @param genreList List with genres to show
     */
    public GenreRecyclerAdapter(Context context, ArrayList<Genre> genreList) {
        this.context = context;
        this.genreList = genreList;
    }

    /**
     * Initiating ViewHolder with layout.
     *
     * @return RecyclerImageViewHolder
     * @see RecyclerListHolder( View )
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_genre_list_item, parent, false);

        return new RecyclerListHolder(view);
    }

    /**
     * Setting content in Views in the ViewHolder.
     *
     * @param holder   ViewHolder
     * @param position position in adapter
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        RecyclerListHolder recyclerListHolder = (RecyclerListHolder) holder;

        Genre genre = genreList.get(position);

        recyclerListHolder.name.setText(genre.getName());
    }

    /**
     * @return int number of objects in adapter.
     */
    @Override
    public int getItemCount() {
        return (null != genreList ? genreList.size() : 0);
    }

    /**
     * Method to set a new movie list to be shown
     *
     * @param genreList movieList to show
     */
    public void setGenreList(ArrayList<Genre> genreList) {
        this.genreList = genreList;
        notifyDataSetChanged();
    }

    /**
     * Inner class, ViewHolder for the elements in the RecyclerView
     */
    private class RecyclerListHolder extends RecyclerView.ViewHolder{

        private TextView name;

        /**
         * @param view Root
         */
        private RecyclerListHolder(View view) {
            super(view);

            name = view.findViewById(R.id.genre_name);

        }
    }


}