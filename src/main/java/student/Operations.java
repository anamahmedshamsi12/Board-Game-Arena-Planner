package student;

/**
 * Enum for the different operations that can be performed on a filter.
 * 
 * This is useful, as you can do the following in your code to easily get the parts
 * of a filter
 * 
 * <pre>
 *  private Stream<BoardGame> filterSingle(String filter, Stream<BoardGame> filteredGames) {
        Operations operator = Operations.getOperatorFromStr(filter);
        if (operator == null) {
            return filteredGames;
        }
        // remove spaces
        filter = filter.replaceAll(" ", "");

        String[] parts = filter.split(operator.getOperator());
        if (parts.length != 2) {
            return filteredGames;
        }
        GameData column;
        try {
            column = GameData.fromString(parts[0]);
        } catch (IllegalArgumentException e) {
            return filteredGames;
        }
        // more work here to filter the games
        // we found creating a String filter and a Number filter to be useful.
        // both of the them take in both the GameData enum, Operator Enum, and the value to parse and filter on.
    }
 * </pre>
 * 
 * It is technically OPTIONAL for you to use this file, but
 * we included it as it was very useful in our solution.
 */
public enum Operations {

    /** Operations to use. */
    EQUALS("=="), NOT_EQUALS("!="), GREATER_THAN(">"), LESS_THAN("<"), GREATER_THAN_EQUALS(
            ">="),
    /** Operations to use. */
    LESS_THAN_EQUALS("<="), CONTAINS("~=");

    /** The operator. */
    private final String operator;

    /**
     * Constructor for the operations.
     * 
     * @param operator The operator.
     */
    Operations(String operator) {
        this.operator = operator;
    }

    /**
     * Get the operator.
     * 
     * @return The operator.
     */
    public String getOperator() {
        return operator;
    }

    /**
     * Get the operation from the operator.
     * 
     * @param operator The operator.
     * @return The operation.
     */
    public static Operations fromOperator(String operator) {
        for (Operations op : Operations.values()) {
            if (op.getOperator().equals(operator)) {
                return op;
            }
        }
        throw new IllegalArgumentException("No operator with name " + operator);
    }

    /**
     * Get the operator from a string that contains it.
     * 
     * @param str The string.
     * @return The operator.
     */
    public static Operations getOperatorFromStr(String str) {
        if (str.contains(">=")) {
            return Operations.GREATER_THAN_EQUALS;
        } else if (str.contains("<=")) {
            return Operations.LESS_THAN_EQUALS;
        } else if (str.contains(">")) {
            return Operations.GREATER_THAN;
        } else if (str.contains("<")) {
            return Operations.LESS_THAN;
        } else if (str.contains("==")) {
            return Operations.EQUALS;
        } else if (str.contains("!=")) {
            return Operations.NOT_EQUALS;
        } else if (str.contains("~=")) {
            return Operations.CONTAINS;
        } else {
            return null;
        }
    }
}
