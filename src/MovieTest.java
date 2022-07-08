import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class MovieTest {
    private Movie testMovie;
    private static final String MOVIE_NAME = "Alien";
    private static final double MOVIE_RATING = 9;
    private Actor actor1;
    private Actor actor2;

    @BeforeEach
    void setUp() {
        actor1 = new Actor("Alice", new ArrayList<>());
        actor2 = new Actor("Bob", new ArrayList<>());
        ArrayList<Actor> actorList = new ArrayList<>();
        actorList.add(actor1);
        testMovie = new Movie(MOVIE_NAME, actorList);
        testMovie.setRating(MOVIE_RATING);
    }

    @Test
    void getName() {
        assertEquals(testMovie.getName(), MOVIE_NAME);
    }

    @Test
    void setName() {
        String newMovieName = "Casino";
        testMovie.setName(newMovieName);
        assertEquals(testMovie.getName(), newMovieName);
    }

    @Test
    void getActors() {
        Actor testActor = testMovie.getActors().get(0);
        assertEquals(testActor.getName(), actor1.getName());
    }

    @Test
    void setActors() {
        ArrayList<Actor> setActorList = new ArrayList<>();
        setActorList.add(actor2);
        testMovie.setActors(setActorList);
        Actor testActor = testMovie.getActors().get(0);
        assertEquals(testActor.getName(), actor2.getName());
    }

    @Test
    void getRating() {
        assertEquals(testMovie.getRating(), MOVIE_RATING);
    }

    @Test
    void setRating() {
        double newRating = 1;
        testMovie.setRating(newRating);
        assertEquals(testMovie.getRating(), newRating);
    }
}