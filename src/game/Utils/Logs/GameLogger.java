package game.Utils.Logs;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.*;


public class GameLogger {
    private static final Logger logger = Logger.getLogger("GameLogger");

    static {
        try {
            String timestamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("dd-MM-yyyy (HH-mm)"));
            String filename = "logs/game_" + timestamp + ".log";

            FileHandler fileHandler = new FileHandler(filename);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);

            logger.setUseParentHandlers(false);
        } catch (IOException e) {
            System.err.println("Failed to setup logger: " + e.getMessage());
        }
    }

    public static void info(String message) {
        logger.info(message);
    }

    public static void warn(String message) {
        logger.warning(message);
    }

    public static void error(String message) {
        logger.severe(message);
    }
}