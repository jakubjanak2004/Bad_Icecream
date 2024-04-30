package main;

import View.GameFrame;
import View.WelcomeView;

import javax.swing.*;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main class is the class containing the main method that runs at first and will execute the application
 */
public class Main {
    static int DIMENSION = 1000;
    static WelcomeView welcomePage;
    static GameFrame gameBoard;

    /**
     * Main method will be run as first when app is being run
     * @param args arguments received
     */
    public static void main(String[] args) {

        boolean verbose = JOptionPane.showConfirmDialog(null, "Do you want to log?", "LOGGING", JOptionPane.YES_NO_OPTION)
                == JOptionPane.YES_OPTION;

        // Nastavení úrovně logování
        if (verbose) {
            Logger.getLogger("").setLevel(Level.CONFIG); // Zapnutí všech úrovní logování
        } else {
            Logger.getLogger("").setLevel(Level.INFO); // Výchozí úroveň logování
        }

        // Nastavení výstupního handleru pro config logery na standardní výstup
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.CONFIG);
        Logger.getLogger("").addHandler(consoleHandler);

        welcomePage = new WelcomeView(DIMENSION);
    }

    /**
     * When Game page is started this method will create window on which the game runs
     */
    public static void gameStarted() {
        gameBoard = new GameFrame(DIMENSION);
    }

    /**
     * This method closes the welcome page if it exists;
     */
    public static void closeWelcomePage() {
        if (welcomePage != null) {
            welcomePage.dispose();
        }
    }
}