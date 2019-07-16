package bruno.ellerbach.tmdbmovies.model;

public class Genre {

    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Genre(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
