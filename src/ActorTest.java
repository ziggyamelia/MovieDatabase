import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ActorTest {
    Actor actor;
    final static String ACTOR_NAME = "Alice";
    final static Movie movie1 = new Movie("Alien", new ArrayList<Actor>(), 9); // keeping actors as empty for tests
    final static Movie movie2 = new Movie("Casino", new ArrayList<Actor>(), 8); // keeping actors as empty for tests
    ArrayList<Movie> movies;


    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        movies = new ArrayList<Movie>(); // empty movie list
        movies.add(this.movie1); // start by adding movie1 to the actor's CV
        actor = new Actor(this.ACTOR_NAME, movies);
    }

    @org.junit.jupiter.api.Test
    void getName() {
        assertEquals(actor.getName(), this.ACTOR_NAME);
    }

    @org.junit.jupiter.api.Test
    void setName() {
        String newName = "Bob";
        actor.setName(newName);
        assertEquals(actor.getName(), newName);
    }

    @org.junit.jupiter.api.Test
    void getMovies() {
        // Don't want this test to be checking movie methods, as this will be covered in MovieTest
        // Will keep is pretty simple and check if names align.
        ArrayList<Movie> testMovieList = actor.getMovies();
        Movie testMovie1 = testMovieList.get(0); // get first and only movie from list
        assertEquals(testMovie1.getName(), this.movie1.getName());
    }

    @org.junit.jupiter.api.Test
    void setMovies() {
        // Change movie and check
        ArrayList<Movie> testMovies = new ArrayList<Movie>(); // empty movie list
        testMovies.add(this.movie2); // start by adding movie1 to the actor's CV
        actor.setMovies(testMovies);
        assertEquals(actor.getMovies().get(0).getName(), this.movie2.getName());
    }
}