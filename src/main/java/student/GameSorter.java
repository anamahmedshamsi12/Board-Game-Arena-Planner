package student;

import java.util.Comparator;
import java.util.stream.Stream;

/**
 * Utility class that provides sorting strategies for streams of BoardGame objects.
 *
 * <p>I separated sorting into its own class so that Planner.java stays focused on
 * filtering logic. The sort strategy pattern is implemented here -- one entry-point
 * method (sort) builds the right comparator based on which column and direction are
 * requested, then applies it to the stream.
 *
 * <p>Tie-breaking: when two games have the same value on the chosen column, a secondary
 * sort by name (ascending, case-insensitive) is always applied to guarantee a
 * deterministic, consistent order.
 *
 * @author CS5004 Student
 * @version 1.0
 */
public final class GameSorter {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private GameSorter() {
    }

    /**
     * Sorts the provided stream by the given column in the specified direction.
     *
     * <p>A comparator is built for the primary sort column, optionally reversed
     * for descending order, then a tie-breaker on name is chained so results
     * are always deterministic regardless of the original Set iteration order.
     *
     * @param stream    the stream of games to sort
     * @param column    the GameData column to sort on
     * @param ascending true for ascending order, false for descending
     * @return a new stream with all elements sorted according to the criteria
     */
    public static Stream<BoardGame> sort(Stream<BoardGame> stream, GameData column,
                                         boolean ascending) {
        // build the primary comparator for the requested column
        Comparator<BoardGame> comparator = buildComparator(column);

        // flip direction if descending was requested
        if (!ascending) {
            comparator = comparator.reversed();
        }

        // chain a tie-breaker by name so order is always consistent
        // when two games have the same value on the primary column
        comparator = comparator.thenComparing(
                Comparator.comparing(g -> g.getName().toLowerCase()));

        return stream.sorted(comparator);
    }

    /**
     * Builds a Comparator for the given GameData column.
     *
     * <p>For the NAME column, comparison is case-insensitive. For numeric columns,
     * natural numeric ordering is used. A switch is used here so adding a new column
     * only requires one new case rather than changing multiple places in the code.
     *
     * @param column the column to build a comparator for
     * @return a Comparator that compares two BoardGame objects by the specified column
     */
    private static Comparator<BoardGame> buildComparator(GameData column) {
        switch (column) {
            case NAME:
                return Comparator.comparing(g -> g.getName().toLowerCase());
            case RATING:
                return Comparator.comparingDouble(BoardGame::getRating);
            case DIFFICULTY:
                return Comparator.comparingDouble(BoardGame::getDifficulty);
            case RANK:
                return Comparator.comparingInt(BoardGame::getRank);
            case MIN_PLAYERS:
                return Comparator.comparingInt(BoardGame::getMinPlayers);
            case MAX_PLAYERS:
                return Comparator.comparingInt(BoardGame::getMaxPlayers);
            case MIN_TIME:
                return Comparator.comparingInt(BoardGame::getMinPlayTime);
            case MAX_TIME:
                return Comparator.comparingInt(BoardGame::getMaxPlayTime);
            case YEAR:
                return Comparator.comparingInt(BoardGame::getYearPublished);
            default:
                return Comparator.comparing(g -> g.getName().toLowerCase());
        }
    }
}

