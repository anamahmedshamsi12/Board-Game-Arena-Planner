import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import student.BoardGame;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import student.Planner;
import student.IPlanner;
import student.GameData;


/**
 * JUnit test for the Planner class.
 *
 * Just a sample test to get you started, also using
 * setup to help out.
 *
 * I am following TDD here -- I write one test at a time, run it,
 * implement just enough code to make it pass, then move on to the next.
 * Tests are organized by what they are testing: name filters first,
 * then numeric filters, and more to come as the program grows.
 *
 * @author CS5004 Student
 * @version 1.0
 */
public class TestPlanner {

    /**
     * Shared set of board games used across all tests.
     * Created once before any tests run to avoid repeating setup in every test.
     * I chose games with varied names, player counts, ratings, and years so I
     * can meaningfully test every filter column.
     */
    static Set<BoardGame> games;

    /**
     * Sets up the shared game dataset before any tests run.
     * Uses @BeforeAll so this only runs once, not before every single test.
     *
     * BoardGame constructor order:
     * name, id, minPlayers, maxPlayers, minPlayTime, maxPlayTime,
     * difficulty, rank, averageRating, yearPublished
     */
    @BeforeAll
    public static void setup() {
        games = new HashSet<>();
        games.add(new BoardGame("17 days", 6, 1, 8, 70, 70, 9.0, 600, 9.0, 2005));
        games.add(new BoardGame("Chess", 7, 2, 2, 10, 20, 10.0, 700, 10.0, 2006));
        games.add(new BoardGame("Go", 1, 2, 5, 30, 30, 8.0, 100, 7.5, 2000));
        games.add(new BoardGame("Go Fish", 2, 2, 10, 20, 120, 3.0, 200, 6.5, 2001));
        games.add(new BoardGame("golang", 4, 2, 7, 50, 55, 7.0, 400, 9.5, 2003));
        games.add(new BoardGame("GoRami", 3, 6, 6, 40, 42, 5.0, 300, 8.5, 2002));
        games.add(new BoardGame("Monopoly", 8, 6, 10, 20, 1000, 1.0, 800, 5.0, 2007));
        games.add(new BoardGame("Tucano", 5, 10, 20, 60, 90, 6.0, 500, 8.0, 2004));
    }

    /**
     * Test: filter by exact name match using ==.
     *
     * "name == Go" should return exactly 1 game and that game's name should be "Go".
     * This tests that the == operator works for string columns and that the match
     * is exact -- it should NOT return "Go Fish", "golang", or "GoRami".
     */
    @Test
    public void testFilterName() {
        IPlanner planner = new Planner(games);
        List<BoardGame> filtered = planner.filter("name == Go").toList();
        assertEquals(1, filtered.size());
        assertEquals("Go", filtered.get(0).getName());
    }

    /**
     * Test: filter by minPlayers greater than 5 using >.
     *
     * Only GoRami (minPlayers=6), Monopoly (minPlayers=6), and Tucano (minPlayers=10)
     * have more than 5 minimum players, so we expect exactly 3 results.
     * This tests that numeric filtering works with the > operator.
     */
    @Test
    public void testFilterMinPlayersGreaterThan() {
        IPlanner planner = new Planner(games);
        List<BoardGame> filtered = planner.filter("minPlayers > 5").toList();
        assertEquals(3, filtered.size());
    }

    /**
     * Test: filter by rating greater than or equal to 9.0 using >=.
     *
     * "17 days" (rating=9.0), "Chess" (rating=10.0), and "golang" (rating=9.5)
     * all have ratings >= 9.0, so we expect exactly 3 results.
     * This tests that numeric filtering works with the >= operator and
     * that decimal values are handled correctly (9.0 should be included, not just > 9.0).
     */
    @Test
    public void testFilterRatingGreaterThanOrEqual() {
        IPlanner planner = new Planner(games);
        List<BoardGame> filtered = planner.filter("rating >= 9.0").toList();
        assertEquals(3, filtered.size());
    }

    @Test
    public void testFilterMultipleFiltersAnd() {
        IPlanner planner = new Planner(games);
        List<BoardGame> filtered = planner.filter("minPlayers > 1, maxPlayers < 6").toList();
        assertEquals(2, filtered.size());
    }

    @Test
    public void testFilterSortByRatingDescending() {
        IPlanner planner = new Planner(games);
        List<BoardGame> filtered = planner.filter("", GameData.RATING, false).toList();
        assertEquals("Chess", filtered.get(0).getName());
    }

    @Test
    public void testResetRestoresAllGames() {
        IPlanner planner = new Planner(games);
        planner.filter("name == Go");
        planner.reset();
        List<BoardGame> result = planner.filter("").toList();
        assertEquals(8, result.size());
    }
}