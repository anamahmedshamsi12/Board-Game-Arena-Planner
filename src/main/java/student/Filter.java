package student;

import java.util.stream.Stream;

/**
 * Utility class for applying a single filter expression to a stream of BoardGame objects.
 */
public class Filter {

    private Filter() { }

    /**
     * Applies a single filter string to the given stream of games.
     *
     * @param filter the filter expression e.g. "minPlayers>=2" or "name~=catan"
     * @param filteredGames the stream to filter
     * @return filtered stream
     */
    public static Stream<BoardGame> apply(String filter, Stream<BoardGame> filteredGames) {
        Operations operator = Operations.getOperatorFromStr(filter);
        if (operator == null) {
            return filteredGames;
        }

        // remove spaces
        filter = filter.replaceAll(" ", "");

        String[] parts = filter.split(java.util.regex.Pattern.quote(operator.getOperator()));
        if (parts.length != 2) {
            return filteredGames;
        }

        GameData column;
        try {
            column = GameData.fromString(parts[0]);
        } catch (IllegalArgumentException e) {
            return filteredGames;
        }

        String value = parts[1].trim();

        if (column == GameData.NAME) {
            return applyStringFilter(filteredGames, operator, value);
        }
        return applyNumberFilter(filteredGames, operator, value, column);
    }

    private static Stream<BoardGame> applyStringFilter(Stream<BoardGame> stream,
                                                       Operations op, String value) {
        return stream.filter(game -> {
            String name = game.getName().toLowerCase();
            String val = value.toLowerCase();
            switch (op) {
                case EQUALS: return name.equals(val);
                case NOT_EQUALS: return !name.equals(val);
                case CONTAINS: return name.contains(val);
                default: return false;
            }
        });
    }

    private static Stream<BoardGame> applyNumberFilter(Stream<BoardGame> stream,
                                                       Operations op, String value,
                                                       GameData column) {
        double val;
        try {
            val = Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return stream;
        }

        return stream.filter(game -> {
            double gameVal = getNumericValue(game, column);
            int cmp = Double.compare(gameVal, val);
            switch (op) {
                case EQUALS: return cmp == 0;
                case NOT_EQUALS: return cmp != 0;
                case GREATER_THAN: return cmp > 0;
                case LESS_THAN: return cmp < 0;
                case GREATER_THAN_EQUALS: return cmp >= 0;
                case LESS_THAN_EQUALS: return cmp <= 0;
                default: return false;
            }
        });
    }

    static double getNumericValue(BoardGame game, GameData column) {
        switch (column) {
            case MIN_PLAYERS: return game.getMinPlayers();
            case MAX_PLAYERS: return game.getMaxPlayers();
            case MIN_TIME: return game.getMinPlayTime();
            case MAX_TIME: return game.getMaxPlayTime();
            case DIFFICULTY: return game.getDifficulty();
            case RANK: return game.getRank();
            case RATING: return game.getRating();
            case YEAR: return game.getYearPublished();
            case ID: return game.getId();
            default: return 0;
        }
    }
}
