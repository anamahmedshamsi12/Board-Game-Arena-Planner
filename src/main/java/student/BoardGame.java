package student;


import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Data Class for the Board Game Object.
 * 
 * This class is considered immutable, as every value is both final, and can only be accessed
 * through getters.
 */
public class BoardGame {
    /** Name of the board game. */
    private final String name;
    /** Unique identifier of the board game. */
    private final int id;
    /** Minimum number of players. */
    private final int minPlayers;
    /** Maximum number of players. */
    private final int maxPlayers;
    /** Maximum play time in minutes. */
    private final int maxPlayTime;
    /** Minimum play time in minutes. */
    private final int minPlayTime;
    /** Average difficulty of the game. */
    private final double difficulty; // avgweight
    /** Rank of the game. */
    private final int rank;
    /** Average rating of the game. */
    private final double averageRating;
    /** Year the game was published. */
    private final int yearPublished;

    /**
     * Constructor for the BoardGame object.
     * 
     * @param name game name
     * @param id unique identifier
     * @param minPlayers minimum number of players
     * @param maxPlayers maximum number of players
     * @param minPlayTime minimum play time in minutes
     * @param maxPlayTime maximum play time in minutes
     * @param difficulty average difficulty of the game
     * @param rank rank of the game
     * @param averageRating average rating of the game
     * @param yearPublished year the game was published
     */
    public BoardGame(String name, int id, int minPlayers, int maxPlayers, int minPlayTime,
            int maxPlayTime, double difficulty, int rank, double averageRating, int yearPublished) {
        this.name = name;
        this.id = id; // purposefully kept hidden, so not used in filters or sorting
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
        this.maxPlayTime = maxPlayTime;
        this.minPlayTime = minPlayTime;
        this.difficulty = difficulty;
        this.rank = rank;
        this.averageRating = averageRating;
        this.yearPublished = yearPublished;
    }

    /**
     * Get the name of the game.
     * 
     * @return name of the game
     */
    public String getName() {
        return name;
    }

    /**
     * Get the unique identifier of the game.
     * 
     * @return unique identifier
     */
    public int getId() {
        return id;
    }

    /**
     * Get the minimum number of players.
     * 
     * @return minimum number of players
     */
    public int getMinPlayers() {
        return minPlayers;
    }

    /**
     * Get the maximum number of players.
     * 
     * @return maximum number of players
     */
    public int getMaxPlayers() {
        return maxPlayers;
    }

    /**
     * Get the maximum play time in minutes.
     * 
     * @return maximum play time in minutes
     */
    public int getMaxPlayTime() {
        return maxPlayTime;
    }


    /**
     * Get the minimum play time in minutes.
     * 
     * @return minimum play time in minutes
     */
    public int getMinPlayTime() {
        return minPlayTime;
    }


    /**
     * Get the average difficulty of the game.
     * 
     * @return average difficulty of the game
     */
    public double getDifficulty() {
        return difficulty;
    }


    /**
     * Get the rank of the game. The rank is defined on its rankings in BGGeek.
     * 
     * @return rank of the game
     */
    public int getRank() {
        return rank;
    }


    /**
     * Get the average rating of the game.
     * 
     * @return average rating of the game
     */
    public double getRating() {
        return averageRating;
    }


    /**
     * Get the year the game was published.
     * 
     * @return year the game was published
     */
    public int getYearPublished() {
        return yearPublished;
    }

    /**
     * Get the Name (value) pair based on the GameData enum.
     * 
     * In the case of Name, it will return only the name of the game.
     * 
     * @param col GameData enum value
     * @return value of Name (value) pair
     */
    public String toStringWithInfo(GameData col) {
        switch (col) {
            case NAME:
                return name;
            case RATING:
                return String.format("%s (%.2f)", name, averageRating);
            case DIFFICULTY:
                return String.format("%s (%.2f)", name, difficulty);
            case RANK:
                return String.format("%s (%d)", name, rank);
            case MIN_PLAYERS:
                return String.format("%s (%d)", name, minPlayers);
            case MAX_PLAYERS:
                return String.format("%s (%d)", name, maxPlayers);
            case MIN_TIME:
                return String.format("%s (%d)", name, minPlayTime);
            case MAX_TIME:
                return String.format("%s (%d)", name, maxPlayTime);
            case YEAR:
                return String.format("%s (%d)", name, yearPublished);
            default:
                return name;
        }
    }

    /**
     * Get a toString that shows all values of the object.
     * 
     * @return string representation of the object
     */
    @Override
    public String toString() {
        return "BoardGame{" + "name='" + name + '\'' + ", id=" + id + ", minPlayers=" + minPlayers
                + ", maxPlayers=" + maxPlayers + ", maxPlayTime=" + maxPlayTime + ", minPlayTime="
                + minPlayTime + ", difficulty=" + difficulty + ", rank=" + rank + ", averageRating="
                + averageRating + ", yearPublished=" + yearPublished + '}';
    }

    /**
     * Check if two BoardGame objects are equal.
     * 
     * Two BoardGame objects are considered equal if all fields are equal, except for the following:
     * - minPlayers - maxPlayers - maxPlayTime - minPlayTime - difficulty - rank - averageRating -
     * yearPublished
     * 
     * @param obj object to compare
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj, // exclude the following fields
                List.of("minPlayers", "maxPlayers", "maxPlayTime", "minPlayTime", "difficulty",
                        "rank", "averageRating", "yearPublished"));
    }

    /**
     * Get the hash code of the object.
     * 
     * The hash code is based on all fields, except for the following: - minPlayers - maxPlayers -
     * maxPlayTime - minPlayTime - difficulty - rank - averageRating - yearPublished
     * 
     * @return hash code of the object
     */
    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this, // exclude the following fields
                List.of("minPlayers", "maxPlayers", "maxPlayTime", "minPlayTime", "difficulty",
                        "rank", "averageRating", "yearPublished"));
    }


    /**
     * Simple main we used for testing.
     * 
     * It is possible to include small mains in each class as you develop to test/practice different
     * things you are working on. We left this in to demonstrate that it is possible to have a main
     * across multiple classes.
     * 
     * @param args command line arguments
     */
    public static void main(String[] args) { // used for local quick tests
        BoardGame bg = new BoardGame("Catan", 1, 3, 4, 60, 30, 2.5, 1, 4.5, 1995);
        BoardGame bg2 = new BoardGame("Catan", 1, 3, 4, 60, 30, 2.5, 1, 4.5, 1995);
        BoardGame bg3 = new BoardGame("Catan", 2, 3, 4, 60, 30, 2.5, 1, 4.5, 1995);
        System.out.println(bg);

        System.out.println(bg.equals(bg) + " " + String.valueOf(bg.hashCode() == bg2.hashCode()));
        System.out.println(bg.equals(bg2) + " " + String.valueOf(bg.hashCode() == bg2.hashCode()));
        System.out.println(bg.equals(bg3) + " " + String.valueOf(bg.hashCode() == bg3.hashCode()));

    }

}
