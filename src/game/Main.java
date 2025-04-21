package game;

import game.Castle.Buildings.Tavern.Checkers.Checkers;

import java.util.HashMap;
import java.util.Map;

import static DB.Saves.GameSave.writeSave;

import game.Utils.Menu.Menu;

public class Main {
    private static final Checkers checkers = new Checkers(6);

    public static void main(String[] args) {
        checkers.start();
    }

    private static MainGame game;
    private static String name;
    private static final Map<String, Long> stats = new HashMap<>();

    public static void saveGame(boolean auto) {
        writeSave(game, auto);
    }

    public static String getUserName() {
        return name;
    }

    public static void incrementStats(String stat) {
        try {
            stats.put(stat, stats.get(stat) + 1);
        } catch (NullPointerException e) {
            Menu.errorMessage(e.getMessage(), true);
        }
    }
//
//    public static void main(String[] args) throws IOException {
//        MainMenu.registrationForm();
//        name = InputHandler.getStringInput();
//        while (!register(name)) {
//            MainMenu.registrationForm();
//            name = InputHandler.getStringInput();
//        }
//
//        boolean isRunning = true;
//        while (isRunning) {
//            MainMenu.showStartMenu();
//            int selected = InputHandler.getIntInput();
//
//            switch (selected) {
//                case 1:
//                    MainMenu.printFormattedMessage("новая игра началась");
//                    game = new MainGame(5, 5);
//                    isRunning = false;
//                    break;
//                case 2:
//                    game = GameSave.readSave();
//                    isRunning = false;
//                    break;
//                case 3:
//                    MapEditor mapEditor = new MapEditor();
//                    mapEditor.start();
//                    break;
//            }
//        }
//
//        assert game != null;
//        stats.put("kills", (long) 0);
//        stats.put("steps", (long) 0);
//        long startTime = System.currentTimeMillis();
//
//        boolean isWinner = game.start();
//
//        long endTime = System.currentTimeMillis();
//        long executionTime = endTime - startTime;
//        stats.put("time", executionTime / 1000);
//
//        if (isWinner) {
//            insertRecords(name, stats);
//        }
//    }
}