import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class MovieDatabase {
    private ArrayList<Movie> movieList;
    private ArrayList<Actor> actorList;
    private static String movieFilePath = "src/movies.txt";
    private static String ratingsFilePath = "src/ratings.txt";

    public MovieDatabase() {
        this.movieList = new ArrayList<>();
        this.actorList = new ArrayList<>();
    }

    public static void main(String[] args) {
        MovieDatabase movieDatabase = new MovieDatabase();
        // add all movies in movies.txt
        // add all ratings for ratings.txt
//        System.out.println(movieDatabase.getBestActor());
//        System.out.println(movieDatabase.getBestMovie());
        movieDatabase.etlMovieFile();
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
        // TODO
    }

    /**
     * Add a rating for this movie.
     * Assume that the name argument will definitely be a name that is currently in the database.
     * @param name name of film to add rating to
     * @param rating rating of film out of 100
     */
    public void addRating(String name, double rating) {
        // TODO
    }

    /**
     * Overwrite the current rating of a movie with this new rating. Again, assume that the name argument will
     * definitely be a name that is currently in the database.
     * @param name name of film to update rating for
     * @param newRating rating of film out of 100
     */
    public void updateRating(String name, double newRating) {
        // TODO
    }

    /**
     * Returns the name of the actor that has the best average rating for their movies.
     * @return actors name as a string
     */
    public String getBestActor() {
        // TODO
        return "Actor Name";
    }

    /**
     * Returns the name of the movie with the highest rating.
     * @return returns movie name as a string
     */
    public String getBestMovie() {
        // TODO
        return "Movie Name";
    }

//    private String readMovieList() throws IOException {
//        return Files.lines(Paths.get("movies.txt")).forEach();
//    }

    /**
     * Extract, Transform and Load of Movie file
     *
     * Extract: read in txt file
     * Transform: transforms wide data into a more usable long dataset.
     * Load: loops through long dataset and loads to database
     *
     * Transformation of dataset from wide to long is done to ensure that every actor is captured when loading a new
     * movie into the database. Another (more reasonable) solution would be to just add the actor to the actor list of
     * the movie when the movie is seen again, but this project required the addMovie method to ignore a request if it
     * already exists in the database, hence have gone with the transformation to long dataset to get all actors listed
     * when loading. In an ideal world an updateMovie or addActorsToMovie method that allows movies to be updated once
     * loaded.
     *
     */
    private void etlMovieFile() {

        // Prepare long data structure
        ArrayList<String[]> actorMovieLong = new ArrayList<>();
        String[] movieLongRecord; // this is a record of the generated dataset
        actorMovieLong.add(new String[]{"ACTOR", "FILM"});

        // Prepare file path to be read from
        Path path = Paths.get(movieFilePath).normalize();
        System.out.println("Reading file: " + path);

        try {
            // Using BufferedReader as it is
            String line;
            String[] movieWideRecord; // this if a record of the ingested dataset
            String actorName;
            Reader reader = new FileReader(path.toString());
            BufferedReader br = new BufferedReader(reader);
//            System.out.println(actorMovieLong.get(0)[0]);

            // Read the file and convert it to a long dataset
            // Make dataset long, that way we capture all the actors in each movie
            while((line = br.readLine()) != null) {
                movieWideRecord = line.split(", ");
                actorName = movieWideRecord[0]; // EDA shows actor is always first column, movies are the rest of the columns

                // movies are columns 2 to n
                // Loop through each of these movies and create a long dataset
                for(int movieIdx = 1; movieIdx < movieWideRecord.length; movieIdx++) {
                    movieLongRecord = new String[] {actorName, movieWideRecord[movieIdx]}; // create record for long dataset
                    actorMovieLong.add(movieLongRecord); // add record to dataset
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // TODO sort table

        // TODO Read long dataset and parse into DB
        for (int row = 0; row < actorMovieLong.size(); row++) {
            System.out.println(actorMovieLong.get(row)[0] + " --- " + actorMovieLong.get(row)[1]);
        }

    }
}