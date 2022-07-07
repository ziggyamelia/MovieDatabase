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

    /**
     * This method takes in the name of a movie that is not currently in the database along with a list of actors for that movie.
     * If the movie is already in the database, your code ignores this request (this specification is an oversimplification).
     * If the movie is a new movie, a movie object is created and added to the movieList.
     * If any of the actors happen to be new, they will be added to the actorList.
     * @param name name of movie
     * @param actors array of actors starring in the film
     */
    public void addMovie(String name, String[] actors) {
    }

    /**
     * Add a rating for this movie.
     * Assume that the name argument will definitely be a name that is currently in the database.
     * @param name name of film to add rating to
     * @param rating rating of film out of 100
     */
    public void addRating(String name, double rating) {
    }

    /**
     * Overwrite the current rating of a movie with this new rating. Again, assume that the name argument will
     * definitely be a name that is currently in the database.
     * @param name name of film to update rating for
     * @param newRating rating of film out of 100
     */
    public void updateRating(String name, double newRating) {
    }

    /**
     * Returns the name of the actor that has the best average rating for their movies.
     * @return actors name as a string
     */
    public String getBestActor() {
        return "Actor Name";
    }

    /**
     * Returns the name of the movie with the highest rating.
     * @return returns movie name as a string
     */
    public String getBestMovie() {
        return "Movie Name";
    }
}