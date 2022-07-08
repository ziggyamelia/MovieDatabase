import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;

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

        // Add all movies and actors to database
        movieDatabase.etlMovieFile(); // adds all movies and
//        for (Movie movie : movieDatabase.getMovieList()) {
//            System.out.println(movie.getName() + movie.getRating());
//        }
//        for (Actor actor : movieDatabase.getActorList()) {
//            System.out.println(actor.getName());
//        }

        // add all ratings for ratings.txt
        movieDatabase.etlRatingsFile();

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
     * Add movie and actors to database
     *
     * From assignment:
     * This method takes in the name of a movie that is not currently in the database along with a list of actors for that movie.
     * If the movie is already in the database, your code ignores this request (this specification is an oversimplification).
     * If the movie is a new movie, a movie object is created and added to the movieList.
     * If any of the actors happen to be new, they will be added to the actorList.
     *
     *
     * There is a circular dependency between movie and actor objects. Ideally, this would be resolved by having the
     * movieList and actorList reference each other, but given the requirements for this assignment it doesn't look like
     * it can be set up that way. Therefore, will cut this dependency by only providing a single movie for the actors
     * contained in a movie object.
     *
     * Method name is required to be addMovie for grading, but should be addMovieAndActors.
     *
     * @param name name of movie
     * @param actors array of actors starring in the film
     */
    public void addMovie(String name, String[] actors) {
        ArrayList<Actor> movieActorList = generateActorListFromStringArray(actors, name); // recursive dependency is broken here
        Movie newMovieRecord = new Movie(name, movieActorList); // setting default rating of null

        // Add movie record if it doesn't exist
        if (!movieRecordExists(name)) {
            this.movieList.add(newMovieRecord);
        }

        // Add or update actor record
        addActorRecord(actors, newMovieRecord);
    }

    /**
     * Check if actor in list, if so then get actor and update record, otherwise add new record
     *
     * @param actors
     * @param newMovieRecord
     */
    public void addActorRecord(String[] actors, Movie newMovieRecord) {
        String actorName;
        Actor actorRecord;
        Actor newActorRecord;
        ArrayList<Movie> newMovieRecords; // movie list is added to actor record

        for (int actorIdx = 0; actorIdx < actors.length; actorIdx++) {

            actorName = actors[actorIdx];
            actorRecord = getActorRecord(actorName);

            if (actorRecord != null) {
                actorRecord.getMovies().add(newMovieRecord); // Add movie to actors record
                newActorRecord = new Actor(actorRecord.getName(), actorRecord.getMovies());
            } else {
                newMovieRecords = new ArrayList<>();
                newMovieRecords.add(newMovieRecord);
                newActorRecord = new Actor(actorName, newMovieRecords); // Add movie
            }

            this.actorList.add(newActorRecord);
        }
    }

    /**
     * Get an actors record by their name
     *
     * Definitely a better way to do this, but keeping the structure consistent with requirements of the assignment.
     * Should be able to have a structure that is queriable by actor name rather than looping through the whole
     * thing every time. This is very inefficient.
     *
     * @param name actors name
     * @return actor record object or null if doesn't exist
     */
    public Actor getActorRecord(String name) {
        String actorRecordName;

        for (int actorIdx = 0; actorIdx < this.actorList.size(); actorIdx++) {
            actorRecordName = this.actorList.get(actorIdx).getName();
            if (actorRecordName.equals(name)) {
                return this.actorList.get(actorIdx);
            }
        }
        return null;
    }

    /**
     * If movie record exists in db, return true, otherwise false.
     *
     * @param name
     * @return true or false depenending on existance of record
     */
    public boolean movieRecordExists(String name) {
        String movieRecordName;

        for (Movie movie : this.movieList) {
            movieRecordName = movie.getName();
            if (movieRecordName != null && movieRecordName.equals(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get a movie record by name
     *
     * @param name
     * @return Movie object
     */
    public Movie getMovieRecord(String name) {
        String movieRecordName;

        for (Movie movie : this.movieList) {
            movieRecordName = movie.getName();
            if (movieRecordName != null && movieRecordName.equals(name)) {
                return movie;
            }
        }
        return null;
    }

    /**
     * Update a movie record by removing and replacing with the passed in record.
     *
     * This is not efficient. Checking if record exists before running scans the entire DB, then does it again to
     * replace the record.
     *
     * @param movieRecord
     * @throws RuntimeException
     */
    public void updateMovieRecord(Movie movieRecord) throws RuntimeException {
        Movie movie;
        if (!movieRecordExists(movieRecord.getName())) {
            throw new RuntimeException("Attempted to update movie record by name, but it does not exist in database.");
        }

        for (int movieIdx = 0; movieIdx < this.movieList.size(); movieIdx++) {
            movie = this.movieList.get(movieIdx);
            if (movieRecord.getName() != null && movieRecord.getName().equals(movie.getName())) {
                this.movieList.remove(movie);
                this.movieList.add(movieRecord);
                break; // once record is updated, no need to continue to scan DB, assumes
            }
        }
    }

    /**
     * Updates all the movie records in the Actor objects stored in the actor database
     *
     * Purpose of this is to ensure ratings are up-to-date.
     *
     */
    public void refreshActorMovieRecords() {
        Actor actor;
        Movie movieFromActorDB;
        Movie movieFromMovieDB;
        for (int actorIdx = 0; actorIdx < this.actorList.size(); actorIdx++) {
            actor = this.actorList.get(actorIdx);
            ArrayList<Movie> updatedMovies = new ArrayList<>();
            for (int movieIdx = 0; movieIdx < actor.getMovies().size(); movieIdx++) {
                // Gets the movie record from the Movie DB to update the movie records in the Actor DB.
                movieFromActorDB = actor.getMovies().get(movieIdx);
                movieFromMovieDB = getMovieRecord(movieFromActorDB.getName());
                updatedMovies.add(movieFromMovieDB);
            }
            actor.setMovies(updatedMovies); // refreshes the movies for that actor record
            this.actorList.set(actorIdx, actor);
        }
    }

    /**
     * Generates an ArrayList of Actor objects
     *
     * This method deliberately breaks the recursion from the Movie-Actor relationship.
     * The resulting output is:
     * - ArrayList:
     * -- Actor: name and a single movie
     * --- Movie with no actors and a rating of -1
     *
     * @param actors
     * @param movieName
     * @return
     */
    public ArrayList<Actor> generateActorListFromStringArray(String[] actors, String movieName) {
        ArrayList<Actor> outputActorList = new ArrayList<>();
        ArrayList<Actor> emptyActorList = new ArrayList<>();
        ArrayList<Movie> movieList = new ArrayList<>();
        movieList.add(new Movie(movieName, emptyActorList));
        Actor actor;

        for (int actorIdx = 0; actorIdx < actors.length; actorIdx++) {
            actor = new Actor(actors[actorIdx], movieList);
            outputActorList.add(actor);
        }
        return outputActorList;
    }

    /**
     * Add a rating for this movie.
     * Assume that the name argument will definitely be a name that is currently in the database.
     * @param name name of film to add rating to
     * @param rating rating of film out of 100
     */
    public void addRating(String name, double rating) {
        // Do not understand why it was required to have this method as well as updateRating. They do the same thing.
        updateRating(name, rating);
    }

    /**
     * Overwrite the current rating of a movie with this new rating. Again, assume that the name argument will
     * definitely be a name that is currently in the database.
     * @param name name of film to update rating for
     * @param newRating rating of film out of 100
     */
    public void updateRating(String name, double newRating) {
        Movie movieRecord;
        movieRecord = getMovieRecord(name);
        if (movieRecord != null) {
            movieRecord.setRating(newRating);
            updateMovieRecord(movieRecord);
        }
    }

    /**
     * Returns the name of the actor that has the best average rating for their movies.
     * @return actors name as a string
     */
    public String getBestActor() {
        // Start with first movie then scan list and update until list is done
        Actor topRatedActor = this.actorList.get(0);

        for (Actor actor : this.actorList) {
            if (actor.getAverageMovieRating() > topRatedActor.getAverageMovieRating()) {
                topRatedActor = actor;
                System.out.println("New top actor " + topRatedActor.getName() + topRatedActor.getAverageMovieRating());
            }
        }

        System.out.println(topRatedActor.getName() + ": " + topRatedActor.getAverageMovieRating());
        return topRatedActor.getName();
    }

    /**
     * Returns the name of the movie with the highest rating.
     * @return returns movie name as a string
     */
    public String getBestMovie() {
        // Start with first movie then scan list and update until list is done
        Movie topRatedMovie = this.movieList.get(0);

        for (Movie movie : this.movieList) {
            if (movie.getRating() > topRatedMovie.getRating()) {
                System.out.println("New top movie " + topRatedMovie.getName() + topRatedMovie.getRating());
                topRatedMovie = movie;
            }
        }
        System.out.println(topRatedMovie.getName() + ": " + topRatedMovie.getRating());
        return topRatedMovie.getName();
    }

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
     * when loading, and allow for sorting of this dataset before loading into the database. In an ideal world an
     * updateMovie or addActorsToMovie method that allows movies to be updated once loaded.
     *
     * Method process is:
     * 1. Prepare file path
     * 2. Read in file and load into a long ArrayList
     * 3. Define comparator for sorting
     * 4. Sort table by movie, then actor
     * 5. Loop through sorted table and load each movie into the database
     *
     */
    private void etlMovieFile() {

        // Prepare long data structure
        MovieLongRecord movieLongRecord; // this is a record of the generated dataset
        ArrayList<MovieLongRecord> actorMovieLong = new ArrayList<>();

        // Prepare file path to be read from
        Path path = Paths.get(movieFilePath).normalize();
        System.out.println("Reading file: " + path);

        try {
            // BufferedReader and Scanner are both viable options in this case. Have chosen BufferedReader as we do
            // parsing later on and don't need Scanner for this.
            String line; // line of file
            String[] movieWideRecord; // this if a record of the ingested dataset
            String actorName; // name of actor

            // Prepare buffered reader instance
            Reader reader = new FileReader(path.toString());
            BufferedReader br = new BufferedReader(reader);

            // Read the file and convert it to a long dataset
            // Make dataset long, that way we capture all the actors in each movie
            while((line = br.readLine()) != null) {
                // Get each line and split by comma.
                // This allows for the actor name and movie names to be extracted individually
                movieWideRecord = line.split(", ");
                actorName = movieWideRecord[0]; // EDA shows actor is always first column, movies are the rest of the columns

                // Loop through each of these movies and create a long dataset. Movies are columns 2 to n
                for(int movieIdx = 1; movieIdx < movieWideRecord.length; movieIdx++) {
                    movieLongRecord = new MovieLongRecord(actorName, movieWideRecord[movieIdx]); // create record
                    actorMovieLong.add(movieLongRecord); // add record to dataset
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Prepare comparator which is used for the sorting below.
        Comparator<MovieLongRecord> movieLongRecordComparator = new Comparator<MovieLongRecord>() {
            @Override
            public int compare(MovieLongRecord o1, MovieLongRecord o2) {
                int movieCompare = o1.movie.compareTo(o2.movie);
                int actorCompare = o1.actor.compareTo(o2.movie);
                return (movieCompare == 0) ? actorCompare : movieCompare; // compare movie, if movie the same compare actor
            }
        };

        // Sort table by movie-actor
        // Mainly want it ordered by movie, so it's easy to loop through each movie one at a time.
        actorMovieLong.sort(movieLongRecordComparator);

        // Read long dataset and load into DB
        // There would be a better way to do this, being able to query the array list by movie and load that way would
        // make more sense. But this simple solution is fine.
        String currMovie;
        String prevMovie = null;
        ArrayList<String> actorsInMovie = new ArrayList<>();
        for (int row = 0; row < actorMovieLong.size(); row++) {

            currMovie = actorMovieLong.get(row).movie;

            // If movie has changed from last row, then load the previous movie to the DB and reset the actor
            // list.
            if (!currMovie.equals(prevMovie) && prevMovie != null) {
                addMovie(prevMovie, actorsInMovie.toArray(new String[0])); // load previous movie to db with all actors
                actorsInMovie = new ArrayList<>();
            }

            // Append actor to actors list
            actorsInMovie.add(actorMovieLong.get(row).actor);

            // Update previous movie to current movie
            prevMovie = currMovie;

            if (row == actorMovieLong.size() - 1) { // if end of dataset load movie to db
                addMovie(prevMovie, actorsInMovie.toArray(new String[0]));
            }
        }
    }

    /**
     * A single record for the long movie-actor dataset
     */
    class MovieLongRecord {
        public String actor;
        public String movie;

        public MovieLongRecord(String actor, String movie) {
            this.actor = actor;
            this.movie = movie;
        }
    }

    /**
     * Ingests and loads the ratings file
     */
    private void etlRatingsFile() {
        // Prepare file path to be read from
        Path path = Paths.get(ratingsFilePath).normalize();
        System.out.println("Reading file: " + path);

        try {
            String line; // line of file
            String[] ratingRecord;
            String movieName;
            double movieRating;
            Movie movieRecord;

            // Prepare buffered reader instance
            Reader reader = new FileReader(path.toString());
            BufferedReader br = new BufferedReader(reader);

            // Read in file (Skip first line since it is a header)
            br.readLine();
            while((line = br.readLine()) != null) {
                // parse the information to movie name and rating value
                ratingRecord = line.split("\t");
                movieName = ratingRecord[0];
                movieRating = Double.parseDouble(ratingRecord[1]);

                // updates movie record in DB
                addRating(movieName, movieRating);
            }

            // After all the ratings are updated in the movie DB need to refresh all the Actor objects that contain
            // movies
            refreshActorMovieRecords();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


