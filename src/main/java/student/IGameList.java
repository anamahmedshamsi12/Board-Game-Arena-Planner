package student;

import java.util.stream.Stream;
import java.util.List;

/**
 * Interface for a list of games. DO NOT MODIFY THIS FILE.
 * 
 * You will write GameList.java which will implement this interface. Assume
 * a no parameter constructor is provided for the GameList.
 * (IGameList list = new GameList();)
 * 
 * The list of games is a collection of games that can be added to or removed
 * from. This will
 * be used to store a list of games that the user has selected/wants to play.
 * 
 * A few things to note about the GameList
 * * Values are unique / no duplicates (based on BoardGame.equals/equality)
 * * It will return values in Case Insensitive ascending order of game Name for
 * any method
 * that returns a list of games or strings.
 */
public interface IGameList {
    /**
     * Default key word to use to add or remove an entire filter from/to the list.
     */
    String ADD_ALL = "all";

    /**
     * Gets the contents of a list, as list of names (Strings) in ascending order
     * ignoring case.
     * 
     * @return the list of game names in ascending order ignoring case.
     */
    List<String> getGameNames();

    /**
     * Removes all games in the list (clears it out completely).
     */
    void clear();

    /**
     * Counts/returns the number of games in the list.
     * 
     * @return the number of games in the list.
     */
    int count();

    /**
     * Saves the list of games to a file.
     * 
     * The contents of the file will be each game name on a new line. It will
     * overwrite the file if
     * it already exists.
     * 
     * Saves them in the same order as getGameNames.
     * 
     * @param filename The name of the file to save the list to.
     */
    void saveGame(String filename);

    /**
     * Adds a game or games to the list.
     * 
     * If a single name is specified, that takes priority. However, it could also
     * use a number such
     * as 1 which would indicate game 1 from the current filtered list should be
     * added to the list.
     * (1 being the first game in the list, normal counting).
     * 
     * A range can also be added, so if 1-5 was presented, it is assumed that games
     * 1 through 5
     * should be added to the list - or if the number is larger than the filtered
     * group 1-n (with n
     * being the last game in the filter). 1-1 type formatting
     * is allowed, and treated as just adding a single game.
     * 
     * If "all" is specified, then all games in the filtered collection should be
     * added to the list.
     * 
     * If any part of the string is not valid, an IllegalArgumentException should be
     * thrown. Such as
     * ranges being out of range.
     * 
     * @param str      the string to parse and add games to the list.
     * @param filtered the filtered list to use as a basis for adding.
     * @throws IllegalArgumentException if the string is not valid.
     */
    void addToList(String str, Stream<BoardGame> filtered) throws IllegalArgumentException;

    /**
     * Removes a game or games from the list.
     * 
     * If a single name is specified, that takes priority. However, it could also
     * use a number such
     * as 1 which would indicate game 1 from the current games list should be
     * removed. A range can
     * also be specified to remove multiple games.
     * 
     * If all is provided, then clear should be called.
     * 
     * If any part of the string is not valid, an IllegalArgumentException should be
     * thrown. Such as
     * ranges being out of range, or none of the results doing anything.
     * 
     * @param str The string to parse and remove games from the list.
     * @throws IllegalArgumentException If the string is not valid.
     * 
     */
    void removeFromList(String str) throws IllegalArgumentException;

}
