package student;


/**
 * Main entry point for the program.
 */
public final class BGArenaPlanner {
    /** default location of collection - relative to the resources directory. */
    private static final String DEFAULT_COLLECTION = "/collection.csv";

    /** private constructor as static class. */
    private BGArenaPlanner() {

    }

    /**
     * Main entry point for the program.
     * 
     * @param args command line arguments - not used at this time.
     */
    public static void main(String[] args) {
        IPlanner planner = new Planner(GamesLoader.loadGamesFile(DEFAULT_COLLECTION));
        IGameList list = new GameList();
        ConsoleApp app = new ConsoleApp(list, planner);
        app.start();
    }



}
