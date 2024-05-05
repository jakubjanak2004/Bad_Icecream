package main;

import View.GameFrame;
import View.WelcomeView;

import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main class is the class containing the main method that runs at first and will execute the application
 */
public class Main {
    static int DIMENSION = 1000;
    static WelcomeView welcomePage;
    static GameFrame gameBoard;
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    /**
     * Main method will be run as first when app is being run
     *
     * @param args arguments received
     */
    public static void main(String[] args) {
        boolean verbose = JOptionPane.showConfirmDialog(null, "Do you want to log?", "LOGGING", JOptionPane.YES_NO_OPTION)
                == JOptionPane.YES_OPTION;

        // set logging setting as if logging will be op/off
        // and the formatting of the logs to be aesthetic
        setLogsForApp(verbose);

        logger.config("Opening the Welcome Window");
        welcomePage = new WelcomeView(DIMENSION);
    }

    /**
     * When Game page is started this method will create window on which the game runs
     */
    public static void gameStarted() {
        logger.warning("GameFrame is being Opened");

        gameBoard = new GameFrame(DIMENSION);
    }

    /**
     * This method closes the welcome page if it exists;
     */
    public static void closeWelcomePage() {
        if (welcomePage != null) {
            welcomePage.dispose();
        }

        logger.warning("Welcome Window is closed");
    }

    private static void setLogsForApp(boolean verbose) {
        // set the logging level
        Logger RootLogger = Logger.getLogger("");
        if (verbose) {
            RootLogger.setLevel(Level.CONFIG); // all logs are on
        } else {
            RootLogger.setLevel(Level.SEVERE); // default state
        }

        // Remove all handlers
        Handler[] handlers = RootLogger.getHandlers();
        for (Handler handler : handlers) {
            RootLogger.removeHandler(handler);
        }

        // Create a console handler
        ConsoleHandler consoleHandler = new ConsoleHandler();

        // Set a custom formatter with colors
        consoleHandler.setFormatter(new java.util.logging.Formatter() {
            // ANSI color codes
            private static final String ANSI_RESET = "\u001B[0m";
            private static final String ANSI_RED = "\u001B[31m";
            private static final String ANSI_YELLOW = "\u001B[33m";
            private static final String ANSI_GREEN = "\u001B[32m";
            private static final String ANSI_BLUE = "\u001B[34m";
            private static final String ANSI_CYAN = "\u001B[36m";

            // Date format
            private SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss:SSS");

            @Override
            public String format(java.util.logging.LogRecord record) {
                String level = record.getLevel().getName();
                String message = record.getMessage();
                String color = switch (level) {
                    case "SEVERE" -> ANSI_RED;
                    case "WARNING" -> ANSI_YELLOW;
                    case "INFO" -> ANSI_GREEN;
                    case "CONFIG" -> ANSI_BLUE;
                    case "FINE" -> ANSI_CYAN;
                    default -> "";
                };

                // Format the current date
                String formattedDate = dateFormat.format(new Date());
                // Append microseconds manually
                long microSeconds = (System.nanoTime() / 1000) % 1000;
                // Format microseconds with leading zeros
                String formattedMicroSeconds = String.format("%03d", microSeconds);

                // Format the log message with color
                return color + formattedDate + ":" + formattedMicroSeconds + " [" + level + "] " + message + ANSI_RESET + "\n";
            }
        });

        // setting the ConsoleHandler
        consoleHandler.setLevel(Level.CONFIG);
        RootLogger.addHandler(consoleHandler);
    }
}