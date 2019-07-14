package bruno.ellerbach.tmdbmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Movie implements Parcelable {

    private final String POSTER_BASE_URL = "https://image.tmdb.org/t/p/w185";

    private int id;
    private String title;
    private String date;
    private String posterUrl;
    private String overview;
    private ArrayList<Integer> genre;


    public Movie(int id, String title, String date, String posterUrl, String overview, ArrayList<Integer> genre) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.posterUrl = POSTER_BASE_URL + posterUrl;
        this.overview = overview;
        this.genre = genre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public ArrayList<Integer> getGenre() {
        return genre;
    }

    public void setGenre(ArrayList<Integer> genre) {
        this.genre = genre;
    }

    protected Movie(Parcel in) {
        title = in.readString();
        date = in.readString();
        posterUrl = in.readString();
        overview = in.readString();
        if (in.readByte() == 0x01) {
            genre = new ArrayList<>();
            in.readList(genre, String.class.getClassLoader());
        } else {
            genre = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(date);
        dest.writeString(posterUrl);
        dest.writeString(overview);
        if (genre == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(genre);
        }
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
