package student;

import java.util.stream.Stream;

/**
 * Utility class that handles applying a single filter expression to a stream of BoardGame objects.
 *
 * <p>I created this as a separate helper class so that Planner.java does not get too long
 * and messy. The idea is that Planner handles splitting the filter string by commas,
 * and then calls Filter.apply() for each individual piece.
 *
 * <p>A filter expression looks like: {@code column OPERATOR value}
 * For example: {@code minPlayers >= 2} or {@code name ~= go}
 *
 * <p>This class is "final" and has a private constructor because it only contains
 * static methods -- there is no reason to ever create a Filter object.
 *
 * @author CS5004 Student
 * @version 1.0
 */
public final class Filter {

    /**
     * Private constructor to prevent instantiation.
     * This is a static utility class -- you should never do: new Filter().
     */
    private Filter() {
    }

    /**
     * Applies a single filter expression to the given stream of board games.
     *
     * <p>The steps taken here:
     * <ol>
     *   <li>Use Operations.getOperatorFromStr() to figure out which operator is in the string.
     *       If none is found, return the stream unchanged.</li>
     *   <li>Strip spaces so "minPlayers >= 2" becomes "minPlayers>=2" for easier splitting.</li>
     *   <li>Split on the operator to get [columnName, value].</li>
     *   <li>Look up which GameData column the left side refers to.</li>
     *   <li>If the column is NAME, delegate to the string filter; otherwise delegate
     *       to the numeric filter.</li>
     * </ol>
     *
     * @param filter        the raw filter expression, e.g. "minPlayers >= 2"
     * @param filteredGames the current stream of games to filter further
     * @return a stream containing only the games that match the filter expression,
     *         or the original stream unchanged if the expression could not be parsed
     */
    public static Stream<BoardGame> apply(String filter, Stream<BoardGame> filteredGames) {
        // Step 1: figure out which operator symbol appears in this filter string
        Operations operator = Operations.getOperatorFromStr(filter);
        if (operator == null) {
            // no recognizable operator means we cannot do anything useful, return as-is
            return filteredGames;
        }

        // Step 2: remove all spaces so splitting on the operator is reliable
        String cleaned = filter.replaceAll(" ", "");

        // Step 3: split on the operator to get the column name (left) and value (right)
        // We expect exactly 2 parts: e.g. ["minPlayers", "2"]
        String[] parts = cleaned.split(operator.getOperator());
        if (parts.length != 2) {
            return filteredGames;
        }

        // Step 4: convert the left-side string into a GameData enum value
        GameData column;
        try {
            column = GameData.fromString(parts[0]);
        } catch (IllegalArgumentException e) {
            // column name was not recognized, skip this filter
            return filteredGames;
        }

        String value = parts[1];

        // Step 5: route to the correct filter type based on the column
        // NAME is the only String column; everything else is numeric
        if (column == GameData.NAME) {
            return applyStringFilter(operator, value, filteredGames);
        } else {
            return applyNumberFilter(column, operator, value, filteredGames);
        }
    }

    /**
     * Applies a string-based filter to the stream.
     *
     * <p>The comparison is done case-insensitively by converting both sides to lowercase,
     * which matches the requirement that string filters are case-insensitive.
     *
     * @param operator the comparison operator to use
     * @param value    the string value to compare against
     * @param stream   the stream of games to filter
     * @return a filtered stream where only matching games remain
     */
    private static Stream<BoardGame> applyStringFilter(Operations operator,
                                                       String value, Stream<BoardGame> stream) {
        // lowercase the target value once outside the lambda for efficiency
        String lowerVal = value.toLowerCase();

        return stream.filter(game -> {
            // compare in lowercase so "Go" == "go" == "GO"
            String fieldVal = game.getName().toLowerCase();

            switch (operator) {
                case EQUALS:
                    return fieldVal.equals(lowerVal);
                case NOT_EQUALS:
                    return !fieldVal.equals(lowerVal);
                case CONTAINS:
                    return fieldVal.contains(lowerVal);
                default:
                    // operators like > or < do not apply to strings, keep the game
                    return true;
            }
        });
    }

    /**
     * Applies a numeric filter to the stream.
     *
     * <p>The right-hand side value is converted to a double so it works for both
     * integer columns (like minPlayers) and decimal columns (like rating).
     *
     * @param column   the GameData column whose values will be compared
     * @param operator the comparison operator to apply
     * @param value    the numeric value as a string, e.g. "4" or "7.5"
     * @param stream   the stream of games to filter
     * @return a filtered stream where only matching games remain
     */
    private static Stream<BoardGame> applyNumberFilter(GameData column, Operations operator,
                                                       String value, Stream<BoardGame> stream) {
        // try to parse the right-hand side as a number before entering the stream pipeline
        double numVal;
        try {
            numVal = Double.parseDouble(value);
        } catch (NumberFormatException e) {
            // value is not a valid number, skip this filter entirely
            return stream;
        }

        return stream.filter(game -> {
            // get the actual numeric value for this game's field
            double fieldVal = getNumericValue(game, column);

            switch (operator) {
                case EQUALS:
                    // use Double.compare to avoid floating-point equality pitfalls
                    return Double.compare(fieldVal, numVal) == 0;
                case NOT_EQUALS:
                    return Double.compare(fieldVal, numVal) != 0;
                case GREATER_THAN:
                    return fieldVal > numVal;
                case LESS_THAN:
                    return fieldVal < numVal;
                case GREATER_THAN_EQUALS:
                    return fieldVal >= numVal;
                case LESS_THAN_EQUALS:
                    return fieldVal <= numVal;
                default:
                    // CONTAINS does not make sense for numbers, keep the game
                    return true;
            }
        });
    }

    /**
     * Reads the numeric value of a specific field from a BoardGame object.
     *
     * <p>A switch on the GameData enum picks the correct getter to call.
     * This keeps all getter-routing logic in one place.
     *
     * @param game   the board game to read the value from
     * @param column the GameData enum specifying which field to read
     * @return the field value as a double
     */
    static double getNumericValue(BoardGame game, GameData column) {
        switch (column) {
            case RATING:
                return game.getRating();
            case DIFFICULTY:
                return game.getDifficulty();
            case RANK:
                return game.getRank();
            case MIN_PLAYERS:
                return game.getMinPlayers();
            case MAX_PLAYERS:
                return game.getMaxPlayers();
            case MIN_TIME:
                return game.getMinPlayTime();
            case MAX_TIME:
                return game.getMaxPlayTime();
            case YEAR:
                return game.getYearPublished();
            default:
                // NAME and ID are not numeric, return 0 as a safe fallback
                return 0;
        }
    }
}
