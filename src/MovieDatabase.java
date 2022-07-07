import java.util.ArrayList;

public class MovieDatabase {
    private ArrayList<Movie> movieList;
    private ArrayList<Actor> actorList;

    public MovieDatabase() {
        this.movieList = new ArrayList<Movie>();
        this.actorList = new ArrayList<Actor>();
    }

    public static void main(String[] args) {
        MovieDatabase movieDatabase = new MovieDatabase();
        // add all movies in movies.txt
        // add all ratings for ratings.txt
        System.out.println(movieDatabase.getBestActor());
        System.out.println(movieDatabase.getBestMovie());
    }

    public ArrayList<Movie> getMovieList() {
        return movieList;
    }

    public ArrayList<Actor> getActorList() {
        return actorList;
    }

    public void addMovie(String name, String[] actors) {
    }

    public void addRating(String name, double rating) {
    }

    public void updateRating(String name, double newRating) {
    }

    public String getBestActor() {
        return "Actor Name";
    }

    public String getBestMovie() {
        return "Movie Name";
    }
}