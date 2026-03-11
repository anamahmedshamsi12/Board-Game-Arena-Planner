package student;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Implementation of {@link IGameList} that stores a user's personal list of board games.
 *
 * <p>I use a TreeSet with a case-insensitive comparator so the set is always
 * sorted alphabetically by name with no extra sorting step needed.
 * Duplicates are automatically ignored since it is a Set.
 *
 * @author CS5004 Student
 * @version 1.0
 */
public class GameList implements IGameList {

    /**
     * Internal set of games the user has added.
     * TreeSet keeps them sorted case-insensitively by name automatically.
     */
    private final Set<BoardGame> games;

    /**
     * Constructs an empty GameList.
     */
    public GameList() {
        // String.CASE_INSENSITIVE_ORDER compares names ignoring case
        this.games = new TreeSet<>(
                (a, b) -> String.CASE_INSENSITIVE_ORDER.compare(a.getName(), b.getName()));
    }

    /**
     * Returns all game names in the list in ascending case-insensitive alphabetical order.
     *
     * <p>Since the TreeSet already maintains sorted order, we just stream the names out.
     *
     * @return a List of game names sorted A-Z case-insensitively
     */
    @Override
    public List<String> getGameNames() {
        // TreeSet is already sorted so no extra sorting needed here
        return games.stream()
                .map(BoardGame::getName)
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
        // try-with-resources ensures the writer is closed even if an error occurs
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
     * <p>The str parameter can be:
     * "all" -- adds every game in the stream.
     * A game name -- adds the single matching game.
     * A number like "3" -- adds the 3rd game in the stream (1-based).
     * A range like "1-5" -- adds games 1 through 5 from the stream.
     *
     * @param str      the command string describing what to add
     * @param filtered the stream of currently filtered games to select from
     * @throws IllegalArgumentException if the string is not valid
     */
    @Override
    public void addToList(String str, Stream<BoardGame> filtered) throws IllegalArgumentException {
        // collect the stream into a sorted list so we can index into it
        List<BoardGame> filteredList = filtered
                .sorted((a, b) -> String.CASE_INSENSITIVE_ORDER.compare(a.getName(), b.getName()))
                .collect(Collectors.toList());

        String trimmed = str.trim();

        // case 1: "all" -- add every game in the filtered list
        if (trimmed.equalsIgnoreCase(ADD_ALL)) {
            games.addAll(filteredList);
            return;
        }

        // case 2: range like "1-5" -- contains a dash but does not start with one
        if (trimmed.contains("-") && !trimmed.startsWith("-")) {
            addRange(trimmed, filteredList);
            return;
        }

        // case 3: single integer index
        try {
            int index = Integer.parseInt(trimmed);
            addByIndex(index, filteredList);
            return;
        } catch (NumberFormatException e) {
            // not a number, fall through to name search
        }

        // case 4: game name
        addByName(trimmed, filteredList);
    }

    /**
     * Adds a game by its 1-based index in the filtered list.
     *
     * @param index        the 1-based position of the game to add
     * @param filteredList the sorted list of filtered games
     * @throws IllegalArgumentException if the index is out of range
     */
    private void addByIndex(int index, List<BoardGame> filteredList) {
        // valid indices are 1 through size inclusive
        if (index < 1 || index > filteredList.size()) {
            throw new IllegalArgumentException(
                    "Index " + index + " is out of range. Valid range: 1 to "
                            + filteredList.size());
        }
        // convert from 1-based to 0-based before accessing the list
        games.add(filteredList.get(index - 1));
    }

    /**
     * Adds a range of games from the filtered list.
     *
     * <p>Format is "start-end" e.g. "1-5". If end exceeds the list size,
     * it is capped at the last available index.
     *
     * @param range        the range string in "start-end" format
     * @param filteredList the sorted list of filtered games
     * @throws IllegalArgumentException if the format is invalid or bounds are out of range
     */
    private void addRange(String range, List<BoardGame> filteredList) {
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

        // validate start bound
        if (start < 1 || start > filteredList.size()) {
            throw new IllegalArgumentException("Start index " + start + " is out of range.");
        }

        // cap end at list size per the spec
        if (end > filteredList.size()) {
            end = filteredList.size();
        }

        if (end < start) {
            throw new IllegalArgumentException(
                    "End index " + end + " must be >= start index " + start);
        }

        // add all games from start to end inclusive, converting 1-based to 0-based
        for (int i = start; i <= end; i++) {
            games.add(filteredList.get(i - 1));
        }
    }

    /**
     * Adds a game by searching for its name in the filtered list.
     *
     * <p>Search is case-insensitive so "chess" finds "Chess".
     *
     * @param name         the name to search for
     * @param filteredList the list of filtered games to search within
     * @throws IllegalArgumentException if no game with that name is found
     */
    private void addByName(String name, List<BoardGame> filteredList) {
        for (BoardGame game : filteredList) {
            if (game.getName().equalsIgnoreCase(name)) {
                games.add(game);
                return;
            }
        }
        throw new IllegalArgumentException("No game found with name: " + name);
    }

    /**
     * Removes one or more games from the list.
     *
     * <p>The str parameter can be:
     * "all" -- clears the entire list.
     * A game name -- removes the matching game.
     * A number like "2" -- removes the 2nd game in the current list.
     * A range like "1-3" -- removes games 1 through 3.
     *
     * @param str the command string describing what to remove
     * @throws IllegalArgumentException if the string is not valid
     */
    @Override
    public void removeFromList(String str) throws IllegalArgumentException {
        String trimmed = str.trim();

        // case 1: "all" -- clear everything
        if (trimmed.equalsIgnoreCase(ADD_ALL)) {
            clear();
            return;
        }

        // get the current sorted list for index-based removal
        List<BoardGame> currentList = new ArrayList<>(games);

        // case 2: range like "1-3"
        if (trimmed.contains("-") && !trimmed.startsWith("-")) {
            removeRange(trimmed, currentList);
            return;
        }

        // case 3: single integer index
        try {
            int index = Integer.parseInt(trimmed);
            removeByIndex(index, currentList);
            return;
        } catch (NumberFormatException e) {
            // not a number, fall through to name removal
        }

        // case 4: game name
        removeByName(trimmed);
    }

    /**
     * Removes a game by its 1-based index in the current sorted list.
     *
     * @param index       the 1-based position of the game to remove
     * @param currentList the current sorted list of games
     * @throws IllegalArgumentException if the index is out of range
     */
    private void removeByIndex(int index, List<BoardGame> currentList) {
        if (index < 1 || index > currentList.size()) {
            throw new IllegalArgumentException(
                    "Index " + index + " is out of range. Valid range: 1 to "
                            + currentList.size());
        }
        // convert from 1-based to 0-based before accessing the list
        games.remove(currentList.get(index - 1));
    }

    /**
     * Removes a range of games from the list.
     *
     * <p>Games to remove are collected first to avoid modifying the set
     * while iterating over it.
     *
     * @param range       the range string in "start-end" format
     * @param currentList the current sorted list of games
     * @throws IllegalArgumentException if the format is invalid or bounds are out of range
     */
    private void removeRange(String range, List<BoardGame> currentList) {
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

        if (start < 1 || start > currentList.size()) {
            throw new IllegalArgumentException("Start index " + start + " is out of range.");
        }
        if (end < start || end > currentList.size()) {
            throw new IllegalArgumentException("End index " + end + " is out of range.");
        }

        // collect games to remove first to avoid ConcurrentModificationException
        List<BoardGame> toRemove = new ArrayList<>();
        for (int i = start; i <= end; i++) {
            toRemove.add(currentList.get(i - 1));
        }
        games.removeAll(toRemove);
    }

    /**
     * Removes a game by name (case-insensitive).
     *
     * @param name the name of the game to remove
     * @throws IllegalArgumentException if no game with that name is found
     */
    private void removeByName(String name) {
        BoardGame toRemove = null;
        for (BoardGame game : games) {
            if (game.getName().equalsIgnoreCase(name)) {
                toRemove = game;
                break;
            }
        }
        if (toRemove == null) {
            throw new IllegalArgumentException("No game found with name: " + name);
        }
        games.remove(toRemove);
    }
}
