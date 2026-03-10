package student;

import java.util.stream.Stream;

/**
 * Sets up filters for the board game data.
 * 
 * This the primary interface for the program. DO NOT MODIFY THIS FILE.
 * 
 * Students, you will need to implement the methods in this interface in a class called
 * Planner.java. You can assume the constructor of Planner.java takes in a Set<BoardGame> as a
 * parameter. This represents the total board game collection.
 * 
 * An important note, while most of methods return streams, each method builds on each other / is
 * progressive. As such if filter by minPlayers, then filter by maxPlayers, the maxPlayers filter
 * should be applied to the results of the minPlayers filter unless reset is called between.
 * 
 */
public interface IPlanner {

    /**
     * 
     * Assumes the results are sorted in ascending order, and that the steam is sorted by the name
     * of the board game (GameData.NAME).
     * 
     * @param filter The filter to apply to the board games.
     * @return A stream of board games that match the filter.
     * @see #filter(String, GameData, boolean)
     */
    Stream<BoardGame> filter(String filter);

    /**
     * Filters the board games by the passed in text filter. Assumes the results are sorted in
     * ascending order.
     * 
     * @param filter The filter to apply to the board games.
     * @param sortOn The column to sort the results on.
     * @return A stream of board games that match the filter.
     * @see #filter(String, GameData, boolean)
     */
    Stream<BoardGame> filter(String filter, GameData sortOn);

    /**
     * Filters the board games by the passed in text filter.
     * 
     * 
     * A text filter can contain the following options:
     * 
     * > : greater than
     * 
     * < : less than
     * 
     * >= : greater than or equal to
     * 
     * <= : less than or equal to
     * 
     * == : equal to
     * 
     * != : not equal to
     * 
     * ~= : contains the text
     * 
     * The left side of the filter describes the column to filter on. The right side of the filter
     * describes the value to filter on.
     * 
     * Fo example:
     * 
     * minPlayers>4
     * 
     * would filter the board games to only those with a minimum number of players greater than 4.
     * 
     * Commas between filters are treated as ANDs. For example:
     * 
     * minPlayers>4,maxPlayers<6
     * 
     * It is possible to filter on the same column multiple times. For example:
     * 
     * minPlayers>4,minPlayers<6
     * 
     * This would filter the board games to only those with a minimum number of players greater than
     * 4 and less than 6.
     * 
     * Spaces should be ignored, but can be included for readability. For example:
     * 
     * minPlayers > 4
     * 
     * is the same as
     * 
     * minPlayers>4
     * 
     * 
     * If filtering on a string column, the filter should be case insensitive. For example:
     * 
     * name~=pandemic
     * 
     * would filter the board games to only those with the word "pandemic" in the name, but could
     * also have Pandemic or PANDEMIC.
     * 
     * Column names will match the values in GameData. As such is it possible to use
     * 
     * GameData.MIN_PLAYERS.getColumnName() or GameData.fromString("minplayers") to get the column
     * name for the minPlayers column.
     * 
     * Note: id is a special column that is not used for filtering or sorting.
     * 
     * if the filter is empty (""), then the results should return the current filter sorted based
     * on the sortOn column and in the defined direction.
     * 
     * 
     * 
     * @param filter The filter to apply to the board games.
     * @param sortOn The column to sort the results on.
     * @param ascending Whether to sort the results in ascending order or descending order.
     * @return A stream of board games that match the filter.
     */
    Stream<BoardGame> filter(String filter, GameData sortOn, boolean ascending);

    /**
     * Resets the collection to have no filters applied.
     */
    void reset();

}
