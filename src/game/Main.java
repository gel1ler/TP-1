package game;

import DB.Saves.GameSave;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static DB.Records.Records.insertRecords;
import static DB.Saves.GameSave.writeSave;
import static DB.Users.register;

import game.Map.MapEditor;
import game.Town.NpcManager;
import game.Town.Town;
import game.Utils.GameTime;
import game.Utils.InputHandler;
import game.Utils.Menu.GameMenu;
import game.Utils.Menu.MainMenu;
import game.Utils.Menu.Menu;

public class Main {
    private static MainGame game;
    private static String name;
    private static final Map<String, Long> stats = new HashMap<>();

    public static boolean saveGame(boolean auto) {
        if (game == null) {
            return false;
        }
        writeSave(game, auto);
        return true;
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


    public static void main(String[] args) throws IOException, InterruptedException {
//        GameTime.init();
//        while (true) {
//            Menu.println("" + GameTime.getMinutes());
//            Thread.sleep(1000);
//        }

        do {
            MainMenu.registrationForm();
            //TEST
//            name = InputHandler.getStringInput();
            name = "asd";
            //TEST
        } while (!register(name));

        while (game == null) {
            MainMenu.showStartMenu();
//TEST
//            switch (InputHandler.getIntInput()) {
            switch (1) {
                //TEST
                case 1:
                    MainMenu.printFormattedMessage("новая игра началась");
                    game = new MainGame(5, 5);
                    break;
                case 2:
                    game = GameSave.readSave();
                    break;
                case 3:
                    MapEditor mapEditor = new MapEditor();
                    mapEditor.start();
                    break;
            }
        }

        stats.put("kills", (long) 0);
        stats.put("steps", (long) 0);

        GameTime.init();
        Town.startLife();

        boolean isWinner = game.start();

        NpcManager.stop();
        stats.put("time", GameTime.getMinutes());

        if (isWinner) {
            insertRecords(name, stats);
        }
    }
}