package student;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Implementation of {@link IGameList} that stores a user's personal list of board games.
 *
 * <p>Games are stored in a HashSet and sorted explicitly when needed.
 * Duplicates are automatically ignored since it is a Set.
 *
 * @author CS5004 Student
 * @version 1.0
 */
public class GameList implements IGameList {

    /** Internal set of games the user has added. */
    private final Set<BoardGame> games;

    /**
     * Constructs an empty GameList.
     */
    public GameList() {
        this.games = new HashSet<>();
    }

    /**
     * Returns all game names in the list in ascending case-insensitive alphabetical order.
     *
     * @return a List of game names sorted A-Z case-insensitively
     */
    @Override
    public List<String> getGameNames() {
        return games.stream()
                .map(BoardGame::getName)
                .sorted((a, b) -> a.compareToIgnoreCase(b))
                .collect(Collectors.toList());
    }

    /**
     * Removes all games from the list.
     */
    @Override
    public void clear() {
        games.clear();
    }

    /**
     * Returns the number of games currently in the list.
     *
     * @return the number of games in the list
     */
    @Override
    public int count() {
        return games.size();
    }

    /**
     * Saves all game names to a text file, one name per line, in sorted order.
     *
     * <p>If the file already exists it will be overwritten.
     * If an IO error occurs it is printed to stderr.
     *
     * @param filename the path/name of the file to write to
     */
    @Override
    public void saveGame(String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (String name : getGameNames()) {
                writer.println(name);
            }
        } catch (IOException e) {
            System.err.println("Error saving game list: " + e.getMessage());
        }
    }

    /**
     * Adds one or more games to the list from the filtered stream.
     *
     * <p>The str parameter can be "all", a game name, a 1-based index, or a range like "1-5".
     *
     * @param str      the command string describing what to add
     * @param filtered the stream of currently filtered games to select from
     * @throws IllegalArgumentException if the string is not valid
     */
    @Override
    public void addToList(String str, Stream<BoardGame> filtered) throws IllegalArgumentException {
        List<BoardGame> filteredList = sortedList(filtered.collect(Collectors.toList()));
        String trimmed = str.trim();

        if (trimmed.equalsIgnoreCase(ADD_ALL)) {
            games.addAll(filteredList);
            return;
        }

        if (isRange(trimmed)) {
            int[] bounds = parseRange(trimmed, filteredList.size());
            for (int i = bounds[0]; i <= bounds[1]; i++) {
                games.add(filteredList.get(i - 1));
            }
            return;
        }

        try {
            int index = Integer.parseInt(trimmed);
            games.add(getByIndex(index, filteredList));
            return;
        } catch (NumberFormatException e) {
            // not a number, fall through to name search
        }

        games.add(findByName(trimmed, filteredList));
    }

    /**
     * Removes one or more games from the list.
     *
     * <p>The str parameter can be "all", a game name, a 1-based index, or a range like "1-3".
     *
     * @param str the command string describing what to remove
     * @throws IllegalArgumentException if the string is not valid
     */
    @Override
    public void removeFromList(String str) throws IllegalArgumentException {
        String trimmed = str.trim();

        if (trimmed.equalsIgnoreCase(ADD_ALL)) {
            clear();
            return;
        }

        List<BoardGame> currentList = sortedList(new ArrayList<>(games));

        if (isRange(trimmed)) {
            int[] bounds = parseRange(trimmed, currentList.size());
            List<BoardGame> toRemove = new ArrayList<>();
            for (int i = bounds[0]; i <= bounds[1]; i++) {
                toRemove.add(currentList.get(i - 1));
            }
            games.removeAll(toRemove);
            return;
        }

        try {
            int index = Integer.parseInt(trimmed);
            games.remove(getByIndex(index, currentList));
            return;
        } catch (NumberFormatException e) {
            // not a number, fall through to name removal
        }

        games.remove(findByName(trimmed, currentList));
    }

    /**
     * Returns a copy of the given list sorted case-insensitively by name.
     *
     * <p>This is used by both {@code addToList} and {@code removeFromList} to ensure
     * consistent ordering for index-based operations.
     *
     * @param list the list to sort
     * @return a new sorted list
     */
    private List<BoardGame> sortedList(List<BoardGame> list) {
        return list.stream()
                .sorted((a, b) -> a.getName().compareToIgnoreCase(b.getName()))
                .collect(Collectors.toList());
    }

    /**
     * Returns true if the string looks like a range (contains a dash not at the start).
     *
     * @param str the string to check
     * @return true if the string is a range expression
     */
    private boolean isRange(String str) {
        return str.contains("-") && !str.startsWith("-");
    }

    /**
     * Parses a range string like "1-5" into a validated int array {start, end}.
     *
     * <p>The end value is capped at maxSize if it exceeds the list size during add operations.
     * For remove operations the caller should validate the end bound separately if needed.
     *
     * @param range   the range string in "start-end" format
     * @param maxSize the size of the list being indexed into
     * @return an int array where index 0 is start and index 1 is end (both 1-based)
     * @throws IllegalArgumentException if the format is invalid or bounds are out of range
     */
    private int[] parseRange(String range, int maxSize) {
        String[] parts = range.split("-");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid range format: " + range);
        }

        int start;
        int end;
        try {
            start = Integer.parseInt(parts[0].trim());
            end = Integer.parseInt(parts[1].trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Range values must be integers: " + range);
        }

        if (start < 1 || start > maxSize) {
            throw new IllegalArgumentException("Start index " + start + " is out of range.");
        }

        if (end > maxSize) {
            end = maxSize;
        }

        if (end < start) {
            throw new IllegalArgumentException(
                    "End index " + end + " must be >= start index " + start);
        }

        return new int[]{start, end};
    }

    /**
     * Returns the game at the given 1-based index in the list.
     *
     * @param index the 1-based position of the game
     * @param list  the sorted list to index into
     * @return the BoardGame at that position
     * @throws IllegalArgumentException if the index is out of range
     */
    private BoardGame getByIndex(int index, List<BoardGame> list) {
        if (index < 1 || index > list.size()) {
            throw new IllegalArgumentException(
                    "Index " + index + " is out of range. Valid range: 1 to " + list.size());
        }
        return list.get(index - 1);
    }

    /**
     * Finds and returns a game by name (case-insensitive) from the given list.
     *
     * @param name the name to search for
     * @param list the list to search within
     * @return the matching BoardGame
     * @throws IllegalArgumentException if no game with that name is found
     */
    private BoardGame findByName(String name, List<BoardGame> list) {
        for (BoardGame game : list) {
            if (game.getName().equalsIgnoreCase(name)) {
                return game;
            }
        }
        throw new IllegalArgumentException("No game found with name: " + name);
    }
}
