package student;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Implementation of {@link IPlanner} that filters and sorts a collection of board games.
 *
 * <p>The Planner is given the full set of games once at construction time. After that,
 * each call to a {@code filter()} method narrows the working set further -- filters are
 * cumulative (they stack on top of each other) until {@link #reset()} is called.
 *
 * @author CS5004 Student
 * @version 1.0
 */
public class Planner implements IPlanner {

    /**
     * The original, unmodified set of all board games passed in at construction.
     * This is never changed so that reset() can always restore the full collection.
     */
    private final Set<BoardGame> allGames;

    /**
     * The currently filtered subset of games.
     * Starts as a copy of allGames and shrinks with each successive filter call.
     * Reset restores it back to a copy of allGames.
     */
    private Set<BoardGame> filteredGames;

    /**
     * Constructs a new Planner with the given collection of board games.
     *
     * <p>A defensive copy of the set is stored internally so that external changes
     * to the caller's set do not affect this Planner.
     *
     * @param games the full collection of board games to plan with; must not be null
     */
    public Planner(Set<BoardGame> games) {
        // store the original set so reset() can always go back to the full collection
        this.allGames = new HashSet<>(games);
        // filteredGames starts as a full copy -- no filters applied yet
        this.filteredGames = new HashSet<>(games);
    }

    /**
     * Filters the current game collection using the given filter string, then returns
     * the results sorted by name (GameData.NAME) in ascending order.
     *
     * <p>Delegates to the three-argument version with default sort column and direction.
     *
     * @param filter the filter expression, e.g. "minPlayers >= 2" or "name ~= go"
     * @return a Stream of matching games sorted by name ascending
     */
    @Override
    public Stream<BoardGame> filter(String filter) {
        return filter(filter, GameData.NAME, true);
    }

    /**
     * Filters the current game collection using the given filter string, then returns
     * the results sorted by the specified column in ascending order.
     *
     * <p>Delegates to the three-argument version with ascending as the default direction.
     *
     * @param filter the filter expression string
     * @param sortOn the GameData column to sort results on
     * @return a Stream of matching games sorted by sortOn ascending
     */
    @Override
    public Stream<BoardGame> filter(String filter, GameData sortOn) {
        return filter(filter, sortOn, true);
    }

    /**
     * Filters the current game collection using the given filter string, then sorts and
     * returns the results.
     *
     * <p>This is the core filter method that all other overloads delegate to.
     *
     * <p>How it works:
     * <ol>
     *   <li>If the filter string is empty, skip filtering and return current games.</li>
     *   <li>Split on commas -- each piece is one filter condition (ANDed together).</li>
     *   <li>Call Filter.apply() for each piece to narrow the stream.</li>
     *   <li>Collect back into filteredGames so the next call continues from here.</li>
     * </ol>
     *
     * @param filter    the filter expression, possibly comma-separated for multiple filters
     * @param sortOn    the column to sort on
     * @param ascending true for ascending order, false for descending
     * @return a Stream of matching games sorted as requested
     */
    @Override
    public Stream<BoardGame> filter(String filter, GameData sortOn, boolean ascending) {
        // if the filter string is empty, skip filtering and just return what we currently have
        if (filter == null || filter.trim().isEmpty()) {
            return GameSorter.sort(filteredGames.stream(), sortOn, ascending);
        }

        // split on commas so "minPlayers>2,maxPlayers<6" becomes two separate filter pieces
        // each piece is ANDed together -- each one narrows the results further
        String[] filterParts = filter.split(",");

        // start with a stream of whatever is currently in filteredGames
        Stream<BoardGame> stream = filteredGames.stream();

        // apply each individual filter piece one at a time
        for (String part : filterParts) {
            String trimmed = part.trim();
            if (!trimmed.isEmpty()) {
                // Filter.apply() handles parsing the operator, column, and value
                stream = Filter.apply(trimmed, stream);
            }
        }

        // collect the result back into filteredGames so the NEXT filter() call
        // continues narrowing from here instead of starting over
        // we must collect first because a Stream can only be consumed once
        Set<BoardGame> result = new HashSet<>();
        stream.forEach(result::add);
        filteredGames = result;

        // TODO Step 6: replace this with GameSorter.sort() once sort logic is ready
        return filteredGames.stream();
    }

    /**
     * Resets the planner back to the full original game collection with no filters applied.
     *
     * <p>After calling reset(), the next filter() call will search across all games again.
     * This is done by replacing filteredGames with a fresh copy of allGames.
     */
    @Override
    public void reset() {
        // replace the current filtered set with a fresh copy of the complete original set
        filteredGames = new HashSet<>(allGames);
    }
}


