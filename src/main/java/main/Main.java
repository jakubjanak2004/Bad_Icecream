package main;

import View.GameFrame;
import View.WelcomeView;

public class Main {
    static int DIMENSION = 1000;
    static WelcomeView welcomePage;
    static GameFrame gameBoard;

    public static void main(String[] args) {
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