package main;

import View.GameFrame;
import View.WelcomeView;

import javax.swing.*;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    static int DIMENSION = 1000;
    static WelcomeView welcomePage;
    static GameFrame gameBoard;

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

    public static void gameStarted() {
        gameBoard = new GameFrame(DIMENSION);
    }

    public static void closeWelcomePage() {
        if (welcomePage != null) {
            welcomePage.dispose();
        }
    }
}