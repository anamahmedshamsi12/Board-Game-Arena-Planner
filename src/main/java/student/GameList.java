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

    /**
     * Internal set of games the user has added.
     */
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
     * @param str      the command string describing what to add
     * @param filtered the stream of currently filtered games to select from
     * @throws IllegalArgumentException if the string is not valid
     */
    @Override
    public void addToList(String str, Stream<BoardGame> filtered) throws IllegalArgumentException {
        List<BoardGame> filteredList = filtered
                .sorted((a, b) -> a.getName().compareToIgnoreCase(b.getName()))
                .collect(Collectors.toList());

        String trimmed = str.trim();

        if (trimmed.equalsIgnoreCase(ADD_ALL)) {
            games.addAll(filteredList);
            return;
        }

        if (trimmed.contains("-") && !trimmed.startsWith("-")) {
            addRange(trimmed, filteredList);
            return;
        }

        try {
            int index = Integer.parseInt(trimmed);
            addByIndex(index, filteredList);
            return;
        } catch (NumberFormatException e) {
            // not a number, fall through to name search
        }

        addByName(trimmed, filteredList);
    }

    private void addByIndex(int index, List<BoardGame> filteredList) {
        if (index < 1 || index > filteredList.size()) {
            throw new IllegalArgumentException(
                    "Index " + index + " is out of range. Valid range: 1 to "
                            + filteredList.size());
        }
        games.add(filteredList.get(index - 1));
    }

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

        if (start < 1 || start > filteredList.size()) {
            throw new IllegalArgumentException("Start index " + start + " is out of range.");
        }

        if (end > filteredList.size()) {
            end = filteredList.size();
        }

        if (end < start) {
            throw new IllegalArgumentException(
                    "End index " + end + " must be >= start index " + start);
        }

        for (int i = start; i <= end; i++) {
            games.add(filteredList.get(i - 1));
        }
    }

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

        List<BoardGame> currentList = games.stream()
                .sorted((a, b) -> a.getName().compareToIgnoreCase(b.getName()))
                .collect(Collectors.toList());

        if (trimmed.contains("-") && !trimmed.startsWith("-")) {
            removeRange(trimmed, currentList);
            return;
        }

        try {
            int index = Integer.parseInt(trimmed);
            removeByIndex(index, currentList);
            return;
        } catch (NumberFormatException e) {
            // not a number, fall through to name removal
        }

        removeByName(trimmed);
    }

    private void removeByIndex(int index, List<BoardGame> currentList) {
        if (index < 1 || index > currentList.size()) {
            throw new IllegalArgumentException(
                    "Index " + index + " is out of range. Valid range: 1 to "
                            + currentList.size());
        }
        games.remove(currentList.get(index - 1));
    }

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

        List<BoardGame> toRemove = new ArrayList<>();
        for (int i = start; i <= end; i++) {
            toRemove.add(currentList.get(i - 1));
        }
        games.removeAll(toRemove);
    }

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
