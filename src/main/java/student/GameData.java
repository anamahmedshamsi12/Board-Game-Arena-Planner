package student;

/**
 * Enum to represent the columns in the game data.
 * 
 * This is to make it easier to access the column names
 * from the CSV file, without knowing
 * the names of the specific columns anywhere else in the program.
 * 
 * Throughout your program, you will use GameData when parsing anything
 * that is associated with column names (filter) and sorting.
 */
public enum GameData {
    /**
     * Enums matching CODE(cvsname) pattern.
     * 
     * name and id are used for game uniqueness.
     */
    NAME("objectname"), ID("objectid"),
    /** Enums that are based on double values in the csv file. */
    RATING("average"), DIFFICULTY("avgweight"),
    /** Enums based on whole int values in the csv file. */
    RANK("rank"), MIN_PLAYERS("minplayers"), MAX_PLAYERS("maxplayers"),
    /** More int based columns. */
    MIN_TIME("minplaytime"), MAX_TIME("maxplaytime"), YEAR("yearpublished");

    /** stores the original csv name in the enum. */
    private final String columnName;

    /**
     * Constructor for the enum.
     * 
     * @param columnName the name of the column in the CSV file.
     */
    GameData(String columnName) {
        this.columnName = columnName;
    }

    /**
     * Getter for the column name.
     * 
     * @return the name of the column in the CSV file.
     */
    public String getColumnName() {
        return columnName;
    }

    /**
     * Get the enum from the column name.
     * 
     * @param columnName the name of the column in the CSV file.
     * @return the enum that matches the column name.
     */
    public static GameData fromColumnName(String columnName) {
        for (GameData col : GameData.values()) {
            if (col.getColumnName().equals(columnName)) {
                return col;
            }
        }
        throw new IllegalArgumentException("No column with name " + columnName);
    }

    /**
     * Get the enum from the enum name.
     * 
     * Can use the enum name or the column name. Useful for filters and sorts
     * as they can use both.
     * 
     * @param name the name of the enum.
     * @return the enum that matches the name.
     */
    public static GameData fromString(String name) {
        for (GameData col : GameData.values()) {
            if (col.name().equalsIgnoreCase(name) || col.getColumnName().equalsIgnoreCase(name)) {
                return col;
            }
        }
        throw new IllegalArgumentException("No column with name " + name);
    }

}
