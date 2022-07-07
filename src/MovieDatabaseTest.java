import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
/*
 * Have chosen to build DB from code rather than data (csvs, txts, etc). This will mean there are less movies and a bit
 * more code to write. But it also means tests can be written before any of the methods involved in ingesting data
 * have been written in the MovieDatabase class.
 **/

class MovieDatabaseTest {
    private MovieDatabase movieDatabase;
    private static final String MOVIE_NAME1 = "Alien";
    private static final String MOVIE_NAME2 = "Casino";
    private static final String MOVIE_NAME3 = "Goodfellas";
    private static final String ACTOR_NAME1 = "Alice";
    private static final String ACTOR_NAME2 = "Bob";
    private static final String ACTOR_NAME3 = "Charlie";

    /**
     * Sets up a movie database with a two movies present
     */
    @BeforeEach
    void setUp() {
        String[] actorsMovie1 = new String[]{ACTOR_NAME1, ACTOR_NAME2};
        String[] actorsMovie2 = new String[]{ACTOR_NAME1, ACTOR_NAME3};
        movieDatabase = new MovieDatabase();
        movieDatabase.addMovie(MOVIE_NAME1, actorsMovie1);
        movieDatabase.addMovie(MOVIE_NAME2, actorsMovie2);
    }

    @Test
    void getMovieList() {
        ArrayList<Movie> testMovieList = movieDatabase.getMovieList();
        assertEquals(testMovieList.get(0).getName(), MOVIE_NAME1);
    }

    @Test
    void getActorList() {
        ArrayList<Actor> testActorList = movieDatabase.getActorList();
        assertEquals(testActorList.get(0).getName(), ACTOR_NAME1);
        assertEquals(testActorList.get(2).getName(), ACTOR_NAME2);
    }

    @Test
    void addMovie() {
        // set up a new movie list with a single movie to add to the db
        ArrayList<Movie> newMovieList;
        Movie newMovie;
        String newMovieName = MOVIE_NAME3;
        String[] newMovieActors = {ACTOR_NAME3};
        movieDatabase.addMovie(newMovieName, newMovieActors);

        // get movie list and check if added movie is present
        newMovieList = movieDatabase.getMovieList();
        newMovie = newMovieList.get(1);
        assertEquals(newMovie.getName(), newMovieName); // checks movie name of new movie
        assertEquals(newMovie.getActors().get(0).getName(), newMovieActors[0]); // checks actors name of new movie
    }

    @Test
    void addRating() {
        // add rating to original movie in db added on setup
        double testRating = 11;
        movieDatabase.addRating(MOVIE_NAME1, testRating);
        // check rating
        assertEquals(movieDatabase.getMovieList().get(0).getRating(), testRating);
    }

    @Test
    void updateRating() {
        // add rating to original movie in db added on setup
        double addedRating = 1;
        double updatedRating = 99;
        movieDatabase.addRating(MOVIE_NAME1, addedRating);
        movieDatabase.updateRating(MOVIE_NAME1, updatedRating);
        // check rating
        assertEquals(movieDatabase.getMovieList().get(0).getRating(), updatedRating);
    }

    @Test
    void getBestActor() {
        // add ratings for the movies
        movieDatabase.addRating(MOVIE_NAME1, 80);
        movieDatabase.addRating(MOVIE_NAME2, 70);

        // get average ratings and pick best actor
        assertEquals(movieDatabase.getBestActor(), ACTOR_NAME2); // Actor 2 in one movie with rating of 80
    }

    @Test
    void getBestMovie() {
        // add ratings for the movies
        movieDatabase.addRating(MOVIE_NAME1, 80);
        movieDatabase.addRating(MOVIE_NAME2, 70);

        // get the highest rated movie
        assertEquals(movieDatabase.getBestMovie(), MOVIE_NAME1); // Movie 1 highest rating of 80
    }
}