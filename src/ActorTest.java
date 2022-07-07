import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ActorTest {
    private Actor actor;
    private final static String ACTOR_NAME = "Alice";
    private final static Movie movie1 = new Movie("Alien", new ArrayList<Actor>(), 9); // keeping actors as empty for tests
    private final static Movie movie2 = new Movie("Casino", new ArrayList<Actor>(), 8); // keeping actors as empty for tests
    private ArrayList<Movie> movies;

    @BeforeEach
    void setUp() {
        movies = new ArrayList<Movie>(); // empty movie list
        movies.add(movie1); // start by adding movie1 to the actor's CV
        actor = new Actor(ACTOR_NAME, movies);
    }

    @Test
    void getName() {
        assertEquals(actor.getName(), ACTOR_NAME);
    }

    @Test
    void setName() {
        String newName = "Bob";
        actor.setName(newName);
        assertEquals(actor.getName(), newName);
    }

    @Test
    void getMovies() {
        // Don't want this test to be checking movie methods, as this will be covered in MovieTest
        // Will keep is pretty simple and check if names align.
        ArrayList<Movie> testMovieList = actor.getMovies();
        Movie testMovie1 = testMovieList.get(0); // get first and only movie from list
        assertEquals(testMovie1.getName(), movie1.getName());
    }

    @Test
    void setMovies() {
        // Change movie and check
        ArrayList<Movie> testMovies = new ArrayList<Movie>(); // empty movie list
        testMovies.add(movie2); // start by adding movie1 to the actor's CV
        actor.setMovies(testMovies);
        assertEquals(actor.getMovies().get(0).getName(), movie2.getName());
    }
}